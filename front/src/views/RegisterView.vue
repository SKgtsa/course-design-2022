<script lang="ts" setup>
import {useRouter} from 'vue-router'
import {reactive,ref} from 'vue'
import service from "@/request";
import { messageError, messageSuccess } from "@/utils/message";
import router from '@/router';
import { Refresh } from '@element-plus/icons-vue';
let $router = useRouter()
const registerForm = ref()
let show = ref(true);
let count = ref();
let timer = reactive(null);

const formData = reactive({   /* 学号，电话，姓名，身份证号，密码，性别，政治面貌，民族，邮箱，电话*/
  phone:'',//电话
  userNumber: '',//学号
  password:'',
  userName: '', //姓名
  idCardNumber: '', //身份证号
  gender:false, //false默认是男性
  ethnic:'', //民族
  politicalAffiliation:'', //政治面貌
  eMail:'', //邮箱
  code:'', //验证码
})
const sendCode =async () =>{
  await service.post('/api/user/registerPhone',{phone:formData.phone}).then(res=>{
    const data = res.data;
    if(data.success){
      show.value = false;
      localStorage.setItem('token',data.token);
      messageSuccess('发送成功！')  //这个还得把发送验证码那个按钮给他禁用了，不然一直发
      const TIME_COUNT = 60; //更改倒计时时间
                if (!timer) {
                    count.value = TIME_COUNT;
                    show.value = false;
                    timer = setInterval(() => {
                      if (count.value > 0 && count.value <= TIME_COUNT) {
                            count.value--;
                        } else {
                            show.value = true;
                            clearInterval(timer); // 清除定时器
                            timer = null;
                        }
                    }, 1000);
                }
    }else{
      messageError(data.message)
    }
  })
}

/* 定义校验规则 */
const validateUserName = (rule,value,callback) =>{  //校验姓名，考虑少数民族
    const reg = /(^[\u4e00-\u9fa5]{1}[\u4e00-\u9fa5\.·.]{0,18}[\u4e00-\u9fa5]{1}$)|(^[a-zA-Z]{1}[a-zA-Z\s]{0,18}[a-zA-Z]{1}$)/;
    if(value==''||value==undefined||value==null){
    callback(new Error('请输入姓名！'));
    }else {
        if ((!reg.test(value)) && value != '') {
            callback(new Error('请输入正确的姓名！'));
        } 
  }
}

const validatepassword = (rule,value,callback) =>{  //校验密码复杂度
    const reg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,32}/;
    if(value==''||value==undefined||value==null){
    callback(new Error('请设置您的密码！'));
    }else {
        if ((!reg.test(value)) && value != '') {
            callback(new Error('您的密码复杂度太低（密码中必须包含字母、数字,长度在8-16位之间），请及时修改密码！'));
        } 
  }
}

const validateIdCardNumber=(rule, value,callback)=> {  //检验身份证号(精确校验)
  const reg =  /^[1-9]\d{5}(19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
  if(value==''||value==undefined||value==null){
    callback(new Error('请输入身份证号'));
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码！'));
    } else {
      callback();
    }
  }
}
const validateEMail=(rule, value,callback)=>{  //检验邮箱
  const reg =/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if(value==''||value==undefined||value==null){
    callback(new Error('请输入邮箱！'));
  }else{
    if (!reg.test(value)){
      callback(new Error('请输入正确的邮箱'));
    } 
  }
}
const validatePhone=(rule,value,callback)=>{ //检验手机号(不能是座机......)
  const reg =/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
  if(value==''||value==undefined||value==null){
    callback(new Error('请输入手机号码！'));
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } 
  }
}
const rules = reactive({
  userName:[{ validator: validateUserName,trigger:'blur'}],
  userNumber:[{required:true,message:'请输入学号',trigger:'blur'}],
  gender:[{required:true,message:'请选择性别',trigger:'blur'}],
  idCardNumber:[{ validator: validateIdCardNumber, trigger: 'blur' }],
  eMail:[{validator:validateEMail,trigger:'blur'}],
  ethnic:[{required:true,message:'请填写您的民族',triggwe:'blur'}], 
  politicalAffiliation:[{required:true,message:'请选择您的政治面貌',triggwe:'blur'}], 
  phone:[{validator:validatePhone,trigger:'blur'}],
  password:[{validator:validatepassword,trigger:'blur'}],
  code:[{required:true,message:'请输入验证码',trigger:'blur'}],
})
/* const onSubmit = () => {
  console.log('submit!')
} */

/* loginForm.value.validate((valid,fields)=>{
    if(!valid){
      isSubmit = false; 
    }
  });
  if(!isSubmit){
    return;
  } */
const register =() =>{
    console.log(formData),     //这个下面这么写不知道对不对，提交表单的时候,出现的问题是直接往后端传的时候发现表单为
    //空的时候也能传过去，所以又写了个整个表单的校验
    registerForm.value.validate((valid)=>{    //registerForm是上面表单ref绑定的值
      if(valid){
        service.post('/api/user/loginCode',{formData}).then(res=>{
        const data = res.data;
        if(data.success){
            messageSuccess('注册成功！')
            router.push('/Login')
        }else{
            messageError(data.message)
        }
    })
      }else{
        messageError('注册失败，请完善您的信息！')
      }
    })
    
}
/* function onSubmit() {
        $Refs.[formData].validate(valid => {
            if (valid) {
                console.log("success submit!!");
            }else{
                console.log("error submit!!");
            }
        });
    }, */
</script>
<template>
    <div class="pageBackground">
        <div class="registerForm">
          <div class="registerFormContent">
          <el-form ref="registerForm" :model="formData" :rules="rules" label-width="auto" label-position="right" status-icon>
    <el-form-item label="学号:" prop="userNumber">
      <el-input v-model="formData.userNumber"  />
    </el-form-item>
    <el-form-item label="姓名:" prop="userName">
      <el-input v-model="formData.userName" />
    </el-form-item>
    <el-form-item label="身份证号:" prop="idCardNumber">
      <el-input v-model="formData.idCardNumber"  />
    </el-form-item>
    <el-form-item label="性别:" prop="gender">
      <el-radio-group v-model="formData.gender">
        <el-radio :label="false" >男</el-radio> <!-- 不确定是不是这么绑定，传false和true -->
        <el-radio :label="true" >女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="政治面貌:" prop="politicalAffiliation">
      <el-select v-model="formData.politicalAffiliation">
        <el-option label="群众" value="people" />
        <el-option label="共青团员" value="leagueMember" />
        <el-option label="共产党员" value="partyMember"/>
      </el-select>
    </el-form-item>
    <el-form-item label="民族:" prop="ethnic">
      <el-input v-model="formData.ethnic"/>
    </el-form-item>
    <el-form-item label="邮箱:" prop="eMail">
      <el-input v-model="formData.eMail"/>
    </el-form-item>
    <el-form-item label="手机号:" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号"/>
    </el-form-item>
    <el-form-item class="loginPageFormText" label="" prop="code">
          <el-row>
            <el-col :span="16">
              <el-input
                v-model="formData.code"
                class="captchaInput"
                id="code"
                placeholder="请输入验证码"
          />
            </el-col>
            <el-col :span="8">
              <el-button type="success" class="captchaButton" @click="sendCode" :disabled="!show">
                <span v-show="show">获取验证码</span>
                <span v-show="!show" class="count">{{count}} s</span>
              </el-button>
            </el-col>
          </el-row>
          </el-form-item>
    <el-form-item label="密码:" prop="password">
      <el-input v-model="formData.password" type="password"  show-password/>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" class="registerPageEl-botton" @click="register">注册</el-button>
    </el-form-item>
    </el-form>
      </div>
    </div>
  </div>
</template>
<style scoped>
.pageBackground{
   background-color: rgba(14, 207, 133, 0.25); 
 /*  background-image: url("../assets/images/哥斯拉.jpg"); */
  background-repeat: no-repeat;
  background-size: 100%;
  position:fixed;
  /* left: 25%; */
  /* top: 0px; */
  height: 100%;
  width: 100%;
  left:0px;
  top:0px;
  justify-content: center;
  justify-items: center;
  vertical-align: middle;
  align-items: center;
  display: flex;
}
.registerForm{
  width: 30%;
  height: 85%;
  /* margin: 25vh 25vh;
  height: 50vh; */
  /* top:25vh; */
  align-items: center;
  position: absolute;
  top: 10%;
  background: #c9c4ce60;
  bottom: 20%; 
}
.registerFormContent{
  width: 80%;
  margin-left:10%;
  margin-right: 10%;
  margin-top: 8%;
  align-items: center;
  display: flex;
}.registerPageEl-botton{
  width: 100%;
  /* text-align: center; */
  margin-top: 30px;
  margin-left: 25px;
  margin-right: 25px;
  line-height: 50px;
  border-radius: 35px;
}
/* .loginPageEl-botton{
  width: 80%;
  margin-left: 25px;
  margin-right: 25px;
  line-height: 25px;
  border-radius: 15px;
  margin-top: 40px;
  height: 40px;
  font-weight: 400px;
  font-style:inherit;
  font-size:medium;
} */
.captchaInput{
  height: 50px;
  width:100% ! important;
  padding-right: 0px;
  border-top-right-radius: 0px!important;
  border-bottom-right-radius: 0px!important;
}
.captchaButton{
  margin-top: 4px;
  margin-left: 5px;
  height: 43px !important;
  width: 90%!important;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 3px!important;
}
.buttonLogin{
  margin-top: 40px;
}
</style>