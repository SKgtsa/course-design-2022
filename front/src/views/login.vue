
<script lang ="ts" setup>
import {Login} from '../api/admin'
import{reactive,ref,onMounted,toRefs} from 'vue';
import bar from '@/assets/images/first.jpg'
import axios from 'axios';
import utils from '@/utils/utils.js'
import { Lock, User } from '@element-plus/icons-vue';
import {useRouter} from 'vue-router' //导入路由器对象
let $router = useRouter()
const login_data=reactive({
  userName:'',
  password:'',
  rememberPassword:false,
})
const loginForm = ref();
const rules = reactive({   /* 定义校验规则 */
  userName:[{required:true,message:'请输入用户名！',trigger:'blur'}],
  password:[{required:true,message:'请输入密码！',trigger:'blur'},
  {min:6,max:16,message:'长度需要在6到16位之间!',trigger:'blur'}
] 
}) 
const onSubmit= ()=>{
  let{userName,password} = login_data;
  let success = Login({});//登录成功返回，没写完
  Login({userName,password})
  let isSubmit = true;
  loginForm.value.validate((valid,fields)=>{
    if(!valid){
      isSubmit = false;
    }
  });
  if(!isSubmit){
    return;
  }
}
utils.saveData("userName",login_data.userName);
utils.saveData("password",login_data.password);
utils.saveData("rememberPassword",login_data.rememberPassword);
onMounted( ()=>{
 login_data.userName = utils.getData("userName");
 login_data.password = utils.getData("password");
 login_data.rememberPassword = utils.getData("rememberPassword");
}
)
//
/* function signup(){
  var username = document.getElementById('username').innerHTML
  var pwd = document.getElementById('pwd').innerHTML
 console.log(username)
  const params = new URLSearchParams();
  params.append('username', username);
  params.append('password', pwd);
  axios.post('http://81.68.109.154:8083/signup', params)
  .catch(
    (e)=>{
      console.log(e)
    }
  ).then((r)=>{
    console.log(r)
    alert(r.data.msg)
  })
 
} */
</script>
<template>
  <div class="login-background">
    <div class="loginPage">
    <div class="logoPage">    
      <div class="logo">
      <el-image src= ''></el-image>
    </div>
  </div>
    <div class="login-text">
      <strong>学生管理系统</strong>
      </div>
    <div class="loginPage_form" >
      <el-form ref="loginForm" :model="login_data" :label-width="0" 
      label-position="left" :rules="rules" status-icon>
        <el-form-item class="loginPageFormText" label="" prop="userName">
          <el-input 
          :prefix-icon="User"  
          class="loginPage_form_input" 
          id="username" 
          v-model="login_data.userName" 
          placeholder="请输入用户名"
        
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
          <el-checkbox v-model="login_data.rememberPassword" label="记住密码"></el-checkbox>
        </el-form-item> 
            </el-col>
        </el-row>
        <el-form-item>
          <el-button type="success" @click = "onSubmit" class="loginPageEl-botton">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="" class="loginPageEl-botton">注册</el-button>
      </el-form-item>
    </el-form>
    </div>
    </div>
    </div>
</template>

<style  scoped>
.login-background{
  background-image:url("../assets/images/aoteman.jpg"); 
  /* background-color: rgba(14, 207, 133, 0.25); */
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
.loginPage{
  width: 25%;
  height: 60%;
  /* margin: 25vh 25vh;
  height: 50vh; */
  /* top:25vh; */
  align-items: center;
  position: absolute;
  background: #c9c4ce60; 
  
}
.loginPage_form{
  width: 80%;
  text-align: center;
  margin-left:  10% ;
  margin-right: 10% ;
}
.loginPageFormText{
  font-size:20px;

}
.loginPage_form_input{
			font-size:14px;
			border:0;
			border-bottom:2px solid #fff;
			padding:5px 10px;
			background:#4f484800;
			color:#fff;
}
.logo{
width: 300px;
}
.login-text{
  font-family:Georgia, serif;
  line-height: 6vh;
  text-align: center;
  font-size: 30px;
  color: rgba(205, 22, 59, 0.25);
  margin: 5vh 5vh;
  letter-spacing: 5px;
}
.loginPageEl-botton{
  width: 80%;
  /* text-align: center; */
  margin-left: 25px;
  margin-right: 25px;
  line-height: 25px;
  border-radius: 35px;
}
.loginPage_form_input>>>.el-input__prefix{ /*修改内部input的小图标，用检查
  来看类名 */
color: black;
}
</style>