<template>
<div class="loginContainer">
  <div class="topText">
    <a>大学</a>
  </div>
  <div class="middleWindow">
    <div class="loginWindow">  <!-- 登录界面哪个窗口 -->
      <div class="switchButtonDiv">
        <el-button 
        class="switchButton switchButtonLeft" 
        color="#333399"
        type="success"
        @click="toUserNumber"
        >
        学号登录</el-button>
        <el-button 
        class="switchButton switchButtonRight" 
        color="#333399" 
        type="success"
        @click="toPhone"
        >短信登录</el-button>
      </div>
      <el-form
               v-if="loginType=='userNumber'"
               ref="loginFormPwd"
               :model="login_data"
               class="loginPage_form"
               :label-width="0"
               label-position="left"
               :rules="rulesPwd"
               status-icon
      >
        <el-form-item class="loginPageFormText" label="" prop="userNumber">
          <el-input
              :prefix-icon="User"
              class="loginPage_form_input"
              id="username"
              v-model="login_data.userNumber"
              placeholder="请输入学工号"

          ></el-input>
        </el-form-item>
        <el-form-item class="loginPageFormText" label="" prop="password">
          <el-input
              :prefix-icon="Lock"
              class="loginPage_form_input"
              type="password"
              id="pwd"
              v-model="login_data.password"
              placeholder="请输入密码"
              maxlength="16"

          />   <!-- maxlength设置了最大长度,可能要alerget提醒一下 -->
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="">
              <el-checkbox v-model="login_data.rememberPassword" class="rememberPwd" label="记住密码"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button 
          type="success"  
          @click = "submit" 
          class="loginPageEl-botton"
          color="rgb(200,255,255,1)"
          >登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  class="loginPageEl-botton">注册</el-button>
        </el-form-item>
      </el-form>
      <el-form
              v-if="loginType=='phone'"
              ref="loginFormPhone"
              :model="login_data"
              class="loginPage_form"
              :label-width="0"
              label-position="left"
              :rules="rulesCaptcha"
              status-icon
      >
      <el-form-item class="loginPageFormText" label="" prop="userPhone">
          <el-input
              :prefix-icon="User"
              class="loginPage_form_input"
              id="userPhone"
              v-model="login_data_phone.userPhone"
              placeholder="请输入手机号"

          ></el-input>
        </el-form-item>
        <el-form-item class="loginPageFormText" label="" prop="captcha">
          <el-row>
            <el-col :span="16">
              <el-input
                :prefix-icon="Message"
                class="loginPage_form_input captchaInput"
                id="captcha"
                v-model="login_data_phone.captcha"
                placeholder="请输入验证码"
          />
            </el-col>
            <el-col :span="8">
              <el-button type="success" class="captchaButton">获取验证码</el-button>
            </el-col>
          </el-row>
          </el-form-item>
        <el-form-item>
          <el-button 
          type="success"  
          @click = "submitPhone" 
          class="loginPageEl-botton captchaButtonLogin"
          color="rgb(200,255,255,1)"
          >登录</el-button>
          </el-form-item>
        <el-form-item>
          <el-button type="primary"  class="loginPageEl-botton">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
  <div class="bottomText">
    <a>Copyright@</a>
  </div>
</div>
</template>

<script lang="ts" setup>
import {reactive, ref} from "vue";
import router from "@/router";
import type {FormInstance} from "element-plus";
import {ElMessage} from "element-plus";
import axios from "axios";
import service from "@/request";
import { Lock, User,Message} from '@element-plus/icons-vue';
const login_data=reactive({
  userNumber:'',
  password:'',
  rememberPassword:false,
})
const loginFormPwd = ref();
const loginFormPhone = ref();
const login_data_phone = reactive({
  userPhone:'',
  captcha:'',
})
const submitPhone = async (formEl: FormInstance | undefined) => {
  if(!formEl)return

  service.post('/api/user/login',login_data_phone).then(res => {
    console.log(res)
    const data = res.data;
    if(data.success){
      localStorage.setItem("token", data.token)
      localStorage.setItem("userPhone", data.user.phone)
      ElMessage({
        message: '登陆成功',
        type: 'success'
      })
      router.push('/')
    }else{
      ElMessage({
        message: data.message,
        type: 'error'
      })
    }
  })
}
var loginType = ref('userNumber');   //写成ref就获取到了，响应式
const toUserNumber=()=>{
   loginType.value='userNumber';
   console.log("进入了toUserNumber函数")
   console.log(loginType.value);
}
const toPhone=()=>{
  loginType.value = 'phone'
  console.log("进入了短信切换页面")
  console.log(loginType.value)
}
const validatePhone=(rule,value,callback)=>{
  const reg =/^[1][3-9][0-9]{9}$/;
  if(value==''||value==undefined||value==null){
    callback(new Error('请输入电话号码！'));
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
const rulesPwd = reactive({   /* 定义校验规则 */
  userNumber:[{required:true,message:'请输入用户名！',trigger:'blur'}],
  password:[{required:true,message:'请输入密码！',trigger:'blur'},
    {min:6,max:16,message:'长度需要在6到16位之间!',trigger:'blur'}
  ]
})
const rulesCaptcha = reactive({
  userPhone:[{required:true,validator:validatePhone,message:'请输入手机号！',trigger:'blur'}
],
  captcha:[{required:true,message:'请输入验证码！',trigger:'blur'},
  ]
})
const submit = async (formEl: FormInstance | undefined) => {
  if(!formEl)return
  // await formEl.validate((valid, fields) => {
  //   if(valid){
  //
  //
  //
  //   }else{
  //     ElMessage({
  //       message: '填写错误',
  //       type: 'error'
  //     })
  //   }
  // })
  service.post('/api/user/login',login_data).then(res => {
    console.log(res)
    const data = res.data;
    if(data.success){
      localStorage.setItem("token", data.token)
      localStorage.setItem("userName", data.user.name)
      ElMessage({
        message: '登陆成功',
        type: 'success'
      })
      router.push('/')
    }else{
      ElMessage({
        message: data.message,
        type: 'error'
      })
    }
  })
}
</script>

<style scoped>
.header{
  display: none !important;
}
.aside{
  display: none !important;
}
.loginContainer{
  background-image: url("../assets/images/LoginPageBackground.jpg"); 
  background-size:cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: no-repeat;
  padding: 0;
  margin: 0;
  width: 100vw;
  height: 100vh !important;
}
.topText{
  height: 20vh;
  text-align: left;
  font-size: 5vh;
  color: #FFF;
  font-weight: bolder;
  padding-top: 1vh;
  padding-left: 3vw;
}
.middleWindow{
  height: 60vh;
  text-align: right;
}
.loginWindow{
  width: 360px;
  height: 400px;
  background-color: rgba(30,30,30,0.8);
  border-radius: 2vw;
  padding-top: 4vh;
  position: absolute;
  right: 10vw;
  box-shadow:2px 5px 17px 2px rgba(21, 40, 46, 0.5);

}
.loginPage_form{
  margin-top: 60px;
  height: 70%;
  width: 80%;
  text-align: center;
  margin-left:  10% ;
  margin-right: 10% ;
}
.loginPage_form_input{
			font-size:16px;
			border:0;
			/* border-bottom:2px solid #fff; */
			padding:5px 10px;
			background:#4f484800;
			color:#fff;
      height: 50px;
}
.loginPage_form_input>>>.el-input__prefix{ /*修改内部input的小图标，用检查
  来看类名 */
color: black;
}
.bottomText{
  height: 20vh;
  text-align: left;
}
.switchButtonDiv{
  position: absolute;
  top: 0px;
  width: 100%;
  display: flex;
  height: 6vh;
}
.switchButton{
  height: 100%;
  width: 50%;
  float: left;
  padding: 0px;
  margin-left: 0px !important;
  border-bottom-color: rgb(147, 190, 30);
  border-width: 2px;
  border-style:double;
  /* background-color: rgb(0,0,150,0.5) */;
}
.switchButtonDiv>>>.el-button-bg-color{
  color: #333399;
}
.switchButtonLeft{
  border-radius: 2vw 0 0 0 ;
}
.switchButtonRight{
  border-radius: 0 2vw 0 0;
}
.loginPageEl-botton{
  width: 80%;
  /* text-align: center; */
  margin-left: 25px;
  margin-right: 25px;
  line-height: 25px;
  border-radius: 15px;
  margin-top: 10px;
  height: 40px;
  font-weight: 400px;
  font-style:inherit;
  font-size:medium;
}
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
.captchaButtonLogin{
  margin-top: 40px;
}
.el-checkbox{
  --el-checkbox-font-size: 15px;
  --el-checkbox-font-weight:1000;
  color:rgb(134, 102, 54);
}

</style>
