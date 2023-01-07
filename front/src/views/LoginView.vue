
<template xmlns="http://www.w3.org/1999/html">
  <div class="loginContainer">

    <div>
      <img class="img" src="../assets/images/logo.png" alt="未加载">
      <a class="topText">教务系统</a>
    </div>
    <div class="middleWindow">
      <div class="loginWindow"> <!-- 登录界面哪个窗口 -->
        <div class="switchButtonDiv">
          <el-button class="switchButton switchButtonLeft" color="rgba(30,30,30,0.8)" type="success"
            @click="toUserNumber">
            <a style="font-size:2vh;">学号登录</a></el-button>
          <el-button class="switchButton switchButtonRight" color="rgba(30,30,30,0.8)" type="success"
            @click="toPhone"><a style="font-size:2vh;">短信登录</a></el-button>
        </div>
        <el-form v-if="loginType == 'userNumber'" ref="loginFormPwd" :model="login_data" class="loginPage_form"
          :label-width="0" label-position="left" :rules="rulesPwd" status-icon>
          <el-form-item class="loginPageFormText" label="" prop="userNumber">
            <el-input :prefix-icon="User" class="loginPage_form_input" id="username" v-model="login_data.userNumber"
              maxlength="15" placeholder="请输入学工号"></el-input>
          </el-form-item>
          <el-form-item class="loginPageFormText" label="" prop="password">
            <el-input :prefix-icon="Lock" class="loginPage_form_input" type="password" id="pwd"
              v-model="login_data.password" placeholder="请输入密码" maxlength="16" />
            <!-- maxlength设置了最大长度,可能要alerget提醒一下 -->
          </el-form-item>
          <a  class="findPwd" @click="findPwd">忘记密码?</a>
          <el-form-item>
            <el-button type="success" @click="submitPwd" class="loginPageEl-botton buttonLogin"
              color="rgb(51,126,204,0.3)"><a style="font-size:2vh;">登录</a></el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" color="rgb(51,126,204,0.3)" class="loginPageEl-botton"
              @click="toRegister"><a style="font-size:2vh;">注册</a></el-button>
          </el-form-item>
        </el-form>
        <el-form v-if="loginType == 'phone'" ref="loginFormPhone" :model="login_data_phone" class="loginPage_form"
          :label-width="0" label-position="left" :rules="rulesCaptcha" status-icon>
          <el-form-item class="loginPageFormText" label="" prop="userPhone">
            <el-input :prefix-icon="User" class="loginPage_form_input" id="userPhone"
              v-model="login_data_phone.userPhone" placeholder="请输入手机号" maxlength="11"></el-input>
          </el-form-item>
          <el-form-item class="loginPageFormText" label="" prop="captcha">
            <el-row>
              <el-col :span="16">
                <el-input :prefix-icon="Message" class="loginPage_form_input captchaInput" id="captcha" maxlength="5"
                  v-model="login_data_phone.captcha" placeholder="请输入验证码" />
              </el-col>
              <el-col :span="8">
                <el-button type="success" class="captchaButton" @click="sendCode" :disabled="!show">
                  <span v-show="show"><a style="font-size:2vh;">发送</a></span>
                  <span v-show="!show" class="count">{{ count }} s</span>
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <a  class="findPwd" @click="findPwd">忘记密码?</a>
          <el-form-item>
            <el-button type="success" @click="submitPhone" class="loginPageEl-botton buttonLogin"
              color="rgb(51,126,204,0.3)"><a style="font-size:2vh;">登录</a></el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="loginPageEl-botton" color="rgb(51,126,204,0.3)"
              @click="toRegister"><a style="font-size:2vh;">注册</a></el-button>
          </el-form-item>
        </el-form>
        <el-dialog title="" v-model="dialogFindPwd" :width="mobile ? '90%' : '30%'">
          <el-form :model="findPwdData" ref="findData" :rules="findPwdRules" label-position="right" label-width="auto">
            <el-form-item label="学工号" prop="userNumber" placeholder="请输入学工号">
              <el-input v-model="findPwdData.userNumber" placeholder="" maxlength="30"></el-input>
            </el-form-item>
            <el-form-item class="loginPageFormText" label="手机号" prop="phone">
              <el-input v-model="findPwdData.phone" placeholder="请输入手机号" maxlength="11"></el-input>
            </el-form-item>
            <el-form-item class="loginPageFormText" label="验证码" prop="code">
              <el-row>
                <el-col :span="16">
                  <el-input style="height:6vh ;" maxlength="5"
                    v-model="findPwdData.code" placeholder="请输入验证码" />
                </el-col>
                <el-col :span="8">
                  <el-button type="success" class="captchaButton" @click="submitFindCode" :disabled="!show">
                    <span v-show="show">获取验证码</span>
                    <span v-show="!show" class="count">{{ count }} s</span>
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item label="新密码" prop="password">
              <el-input v-model="findPwdData.password" placeholder="" maxlength="16"></el-input>
            </el-form-item>
          </el-form>
          <el-button @click="submitFindPwd">提交</el-button>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import router from "@/router";
import type { FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import axios from "axios";
import service from "@/request";
import { Lock, User, Message, Phone } from '@element-plus/icons-vue';
import { messageError, messageSuccess } from "@/utils/message";
import { hideLoading, showLoading } from "@/utils/loading";
import { getBaseURL, setAvatarURL, setNickName, setUserId } from "@/global/global";
import { identityJump } from "@/utils/identityJump";
import { mobile } from "@/global/global";
const login_data = reactive({
  userNumber: '',
  password: '',
  ticket: '',
  randstr: ''
})
const loginFormPwd = ref();
const loginFormPhone = ref();
let show = ref(true);
let count = ref();
let timer = reactive(null);
let dialogFindPwd = ref(false)
const login_data_phone = reactive({
  userPhone: '',
  captcha: '',
})
let findPwdData = reactive({
  userNumber: '',
  phone: '',
  code: '',
  password: '',
});
let findData = ref();
const sendCode = async () => {
  showLoading();
  await service.post('/api/user/loginPhone', { phone: login_data_phone.userPhone }).then(res => {
    const data = res.data;
    if (data.success) {
      hideLoading();
      show.value = false;
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
    } else {
      hideLoading();
      messageError(data.message)
    }
  })
    .catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
}

const submitPwd = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  loginFormPwd.value.validate((valid) => {
    if (valid) {
      console.log('检验通过');
      (window as any).captcha
      const captcha = new (window as any).TencentCaptcha('198139954', function (res){
        console.log('进入验证')
        showLoading();
        login_data.randstr = res.randstr;
        login_data.ticket = res.ticket;
        service.post('/api/user/login', login_data).then(res => {
          console.log(res)
          const data = res.data;
          if (data.success) {
            console.log(login_data.password, login_data.userNumber)
            localStorage.setItem("token", data.token)
            let url = getBaseURL() + data.user.avatarURL;
            console.log(localStorage.getItem('token'))
            setUserId(data.user.id);
            setNickName(data.user.nickName);
            setAvatarURL(url);
            hideLoading();
            /*       localStorage.setItem("userName", data.user.name) */
            messageSuccess(data.message)
            if(data.needSupplement){
              router.push('/InfoFillIn')
            }else{
              identityJump(data.character)
            }
          } else {
            hideLoading();
            console.log('!data.success')
            messageError(data.message)
          }
        })
          .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
          })
      })
      captcha.show();
    } else {
      console.log('检验失败')
      messageError("请填写正确的用户名和密码！")
    }
  })
}

const submitPhone = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  console.log(login_data_phone.userPhone)
  loginFormPhone.value.validate((valid) => {
    if (valid) {
      showLoading();
      service.post('/api/user/loginCode', { code: login_data_phone.captcha, phone: login_data_phone.userPhone }).then(res => {
        console.log(res)
        const data = res.data;
        if (data.success) {
          localStorage.setItem("token", data.token)
          let url = getBaseURL() + data.user.avatarURL
          setNickName(data.user.nickName);
          setUserId(data.user.id);
          setAvatarURL(url);
          messageSuccess("登录成功!")
          hideLoading()
          identityJump(data.character)
        } else {
          hideLoading();
          messageError(data.message)
        }
      })
        .catch(function (error) {
          hideLoading();
          messageError("服务器开小差了呢");
          console.log(error)
        })
    }
    else {
      messageError("请填写正确的手机号和验证码！")
    }
  })
}

const submitFindCode = async () => {
  if (findPwdData.phone != '' && findPwdData.userNumber != '') {
    showLoading();
    await service.post('/api/user/findPasswordPhone', { userNumber: findPwdData.userNumber ,phone: findPwdData.phone, }).then(res => {
      let data = res.data;
      if (data.success) {
        hideLoading();
        messageSuccess('发送成功');
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
      } else {
        hideLoading();
        messageError(data.message)
      }
    }).catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
  } else {
    messageError('请将学号和电话号码填写完整！')
  }
}

const submitFindPwd = async () => {   //找回密码提交
  await findData.value.validate((valid) => {
    if (valid) {
      showLoading();
      service.post('/api/user/findPasswordCode', { phone: findPwdData.phone, code: findPwdData.code, password: findPwdData.password }).then(res => {
        let data = res.data;
        if (data.success) {
          hideLoading();
          messageSuccess('修改成功！');
          localStorage.setItem('token', data.token);
        } else {
          hideLoading();
          messageError(data.message)
        }
      })
        .catch(function (error) {
          hideLoading();
          messageError("服务器开小差了呢");
          console.log(error)
        })
    } else {
      messageError('请正确填写！')
    }
  })
}

const findPwd = () => {
  dialogFindPwd.value = true;
}

const toRegister = () => {
  router.push('/Register')
}
var loginType = ref('userNumber');   //写成ref就获取到了，响应式
const toUserNumber = () => {
  loginType.value = 'userNumber';
  console.log("进入了toUserNumber函数")
  console.log(loginType.value);
}
const toPhone = () => {
  loginType.value = 'phone'
  console.log("进入了短信切换页面")
  console.log(loginType.value)
}
const validatePhone = (rule, value, callback) => {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入电话号码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
const validatepassword = (rule, value, callback) => {   //校验密码复杂度
  const reg = /^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{6,16}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请设置您的密码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('密码中必须包含字母、数字、特殊字符,长度在6-16位之间'));
    } else {
      callback();
    }
  }
}
const rulesPwd = reactive({   /* 定义校验规则 */
  userNumber: [{ required: true, message: '请输入学工号！', trigger: 'blur' }, {
    max: 16, message: '学号没有超过16位!', trigger: 'blur'
  }],
  password: [{ required: true, message: '请输入密码！', trigger: 'blur' },
  { min: 6, max: 16, message: '长度需要在6到16位之间!', trigger: 'blur' }
  ]
})

const rulesCaptcha = reactive({
  userPhone: [{ validator: validatePhone, trigger: 'blur' }
  ],
  captcha: [{ required: true, message: '请输入验证码！', trigger: 'blur' },
  {
    max: 6, message: '验证码没有超过6位!', trigger: 'blur'
  }
  ]
})
const findPwdRules = reactive({
  userNumber: [{ required: true, message: '请输入学工号！', trigger: 'blur' }, {
    max: 16, message: '学号没有超过16位!', trigger: 'blur'
  }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码！', trigger: 'blur' },
  {
    max: 6, message: '验证码没有超过6位!', trigger: 'blur'
  }
  ],
  password: [{ validator: validatepassword, trigger: 'blur' }],
})
</script>

<style scoped>
.header {
  display: none !important;
}

.aside {
  display: none !important;
}

.loginContainer {
  background-image: url("../assets/images/LoginPageBackground.jpg");
  background-size: cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: no-repeat;
  padding: 0;
  margin: 0;
  width: 100vw;
  height: 100vh !important;

}

.topText {
  font-family: 微软雅黑;
  /*height: 20vh;*/
  text-align: left;
  font-size: 6vh;
  color: #ffffff;
  font-weight: bolder;
  margin-bottom: 3vh;

}

.middleWindow {
  height: 60vh;
  text-align: center;
}

.loginWindow {
  width: 45vh;
  height: 60vh;
  background-color: rgba(30, 30, 30, 0.8);
  border-radius: 2vw;
  padding-top: 0vh;
  position: absolute;
  right: 10vw;
  box-shadow: 0.25vh 0.625vh 2.125vh 0.25vh rgba(21, 40, 46, 0.5);

}

.loginPage_form {
  margin-top: 10vh;
  height: 70%;
  width: 80%;
  text-align: center;
  margin-left: 10%;
  margin-right: 10%;
}

.loginPage_form_input {
  font-size: 2vh;
  border: 0;
  /* border-bottom:2px solid #fff; */
  padding: 0.625vh 1.25vh;
  background: #4f484800;
  color: #fff;
  height: 6.25vh;
}

.loginPage_form_input>>>.el-input__prefix {
  /*修改内部input的小图标，用检查
  来看类名 */
  color: black;
}

.bottomText {
  height: 20vh;
  text-align: center;
}

.switchButtonDiv {
  position: absolute;
  top: 0vh;
  width: 100%;
  display: flex;
  height: 6vh;
}

.switchButton {
  height: 100%;
  width: 50%;
  float: left;
  padding: 0vh;
  margin-left: 0vh !important;
  border-bottom-color: rgb(255, 255, 255);
  border-width: 0.25vh;
  border-style: double;
  /* background-color: rgb(0,0,150,0.5) */
  ;
}

.switchButtonDiv>>>.el-button-bg-color {
  color: #a0cfff;
}

.switchButtonLeft {
  border-radius: 2vw 0 0 0;
}

.switchButtonRight {
  border-radius: 0 2vw 0 0;
}

.loginPageEl-botton {
  width: 80%;
  /* text-align: center; */
  margin-left: 3.125vh;
  margin-right: 3.125vh;
  line-height: 3.125vh;
  border-radius: 1.875vh;
  margin-top: 1.25vh;
  height: 5vh;
  font-weight: bold;
  font-style: inherit;
  font-size: medium;
}

.captchaInput {
  height: 6.25vh;
  width: 100% ! important;
  padding-right: 0px;
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;
}

.captchaButton {
  background-color: rgba(103, 194, 58, 0.5);
  margin-top: 0.5vh;
  margin-left: 0.625vh;
  height: 5.375vh !important;
  width: 90% !important;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 0.375vh !important;
}

.buttonLogin {
  margin-top: 5vh;
}
.findPwd{
  color: #318eeb;
  font-size: 1.1vw;
  padding-left: 12vw;
}
.findPwd:hover{
  cursor: pointer;
  font-weight: 550;
}
.el-checkbox {
  --el-checkbox-font-size: 1.875vh;
  --el-checkbox-font-weight: 1000;
  color: rgb(255, 255, 255);
}

.spacing {
  padding-top: 3.75vh;
}

.img {
  height: 10vh !important;
  padding-left: 5vh;
  padding-right: 3vh;
  padding-top: 3vh;
}
</style>
