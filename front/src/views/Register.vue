<script lang="ts" setup>
import {useRouter} from 'vue-router'
import {reactive,ref} from 'vue'
let $router = useRouter()
const registerForm = ref()
const formData = reactive({   /* 学号，电话，姓名，身份证号，密码，性别，政治面貌，民族，邮箱，电话*/
  userNumber: '',//学号
  userName: '', //姓名
  idCardNumber: '', //身份证号
  gender:false, //false默认是女性
  ethnic:'', //民族
  politicalAffiliation:'', //政治面貌
  eMail:'', //邮箱
  phone:'',//电话
  password:'',
})
/* 定义校验规则 */
const validateIdCardNumber=(rule, value,callback)=> {  //检验身份证号
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码'));
    } else {
      callback();
    }
  }
}
const validateEMail=(rule, value,callback)=>{  //检验邮箱
  const reg =/^([a-zA-Z0-9] [-_\.]?) @[a-zA-Z0-9] \.[a-z] $/;
  if(value==''||value==undefined||value==null){
    callback();
  }else{
    if (!reg.test(value)){
      callback(new Error('请输入正确的邮箱'));
    } else {
      callback();
    }
  }
}
const validatePhone=(rule,value,callback)=>{
  const reg =/^[1][3-9][0-9]{9}$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
const rules = reactive({
  userNumber:[
  {required:true,message:'请填写您的学号',trigger:'blur'},
  {min:12,max:12,message:'学号格式不正确',trigger:'blur'}
  ],
  userName:[
  {required:true,message:'请填写您的姓名',trigger:'blur'},
  ],
  idCardNumber:[
  { required: true, validator: validateIdCardNumber, trigger: 'blur' }],
  eMail:[
    {required:true,validator:validateEMail,trigger:'blur'}
  ],
  phone:[
    {required:true,validator:validatePhone,trigger:'blur'}
  ],

  password:[{required:true,message:'请输入密码！',trigger:'blur'},
  {min:6,max:16,message:'长度需要在6到16位之间!',trigger:'blur'}],
  ethnic:[
    {required:true,message:'请填写您的民族',trigger:'blur'}
  ],
})
/* const onSubmit = () => {
  console.log('submit!')
} */
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
    <el-form-item label="性别:">
      <el-radio-group v-model="formData.gender">
        <el-radio label="男" />
        <el-radio label="女" />
      </el-radio-group>
    </el-form-item>
    <el-form-item label="政治面貌:">
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
    <el-form-item label="电话:" prop="phone">
      <el-input v-model="formData.phone"/>
    </el-form-item>
    <el-form-item label="密码:" prop="password">
      <el-input v-model="formData.password" type="password"/>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" class="registerPageEl-botton">注册</el-button>
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
  height: 70%;
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
  margin-left: 25px;
  margin-right: 25px;
  line-height: 50px;
  border-radius: 35px;
}
</style>