-<template>
  <div class="loginContainer">
    <div class="topText">
      <a>大学</a>
    </div>
    <div class="middleWindow">
      <div class="loginWindow">
        <el-form ref="loginForm"
                 :model="login_data"
                 :label-width="0"
                 label-position="left"
                 :rules="rules"
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
                <el-checkbox v-model="login_data.rememberPassword" label="记住密码"></el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="success" @click = "submit" class="loginPageEl-botton">登录</el-button>
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

const login_data=reactive({
  userNumber:'',
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

<style>
.header{
  display: none !important;
}
.aside{
  display: none !important;
}
.loginContainer{
  background-image: url("../assets/images/山大.jpg");
  background-size: cover;
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
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
  width: 40vw;
  height: 32vh;
  background-color: rgba(30,30,30,0.8);
  border-radius: 2vw;
  padding-top: 4vh;
  position: absolute;
  right: 10vw;
}
.bottomText{
  height: 20vh;
  text-align: left;
}

</style>