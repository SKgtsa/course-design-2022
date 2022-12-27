
<template>
  <div class="pageBackground">
    <div class="registerForm">
      <div class="registerFormContent">
        <el-form ref="registerForm" :model="formData" :rules="rules" label-width="auto" label-position="right"
          status-icon>
          <el-form-item label="学工号:" prop="userNumber">
            <el-input v-model="formData.userNumber" maxlength="15" />
          </el-form-item>
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="formData.name"  maxlength="10"/>
          </el-form-item>
          <el-form-item label="用户名:" prop="nickName">
            <el-input v-model="formData.nickName"  maxlength="8"/>
          </el-form-item>
          <el-form-item label="身份:" prop="identity">
            <el-select v-model="formData.identity" placeholder="选择身份">
              <el-option label="老师" value="1" />
              <el-option label="管理员" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="身份证号:" prop="idCardNumber">
            <el-input v-model="formData.idCardNumber"  maxlength="18"/>
          </el-form-item>
          <el-form-item label="性别:" prop="gender">
            <el-radio-group v-model="formData.gender">
              <el-radio :label="false">男</el-radio> <!-- 不确定是不是这么绑定，传false和true -->
              <el-radio :label="true">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="政治面貌:" prop="politicalAffiliation">
            <el-select v-model="formData.politicalAffiliation" placeholder="选择政治面貌">
              <el-option label="群众" value="群众" />
              <el-option label="共青团员" value="共青团员" />
              <el-option label="共产党员" value="共产党员" />
            </el-select>
          </el-form-item>
          <el-form-item label="民族:" prop="ethnic">
            <el-input v-model="formData.ethnic"  maxlength="8"/>
          </el-form-item>
          <el-form-item label="邮箱:" prop="eMail">
            <el-input v-model="formData.eMail"  maxlength="20"/>
          </el-form-item>
          <el-form-item label="手机号:" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="11" formatter="number"/>
          </el-form-item>
          <el-form-item class="loginPageFormText" label="验证码:" prop="code">
            <el-row>
              <el-col :span="16">
                <el-input v-model="formData.code" class="captchaInput" id="code" placeholder="请输入验证码" maxlength="5"/>
              </el-col>
              <el-col :span="8">
                <el-button type="success" class="captchaButton" @click="sendCode" :disabled="!show">
                  <span v-show="show">获取验证码</span>
                  <span v-show="!show" class="count">{{ count }} s</span>
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="密码:" prop="password">
            <el-input v-model="formData.password" type="password" show-password  maxlength="16"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="registerPageEl-botton" @click="register">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { reactive, ref } from 'vue'
import service from "@/request";
import { messageError, messageSuccess } from "@/utils/message";
import router from '@/router';
import { Phone, Refresh } from '@element-plus/icons-vue';
import { hideLoading, showLoading } from '@/utils/loading';
/* import qs from 'qs'; */

let $router = useRouter()
const registerForm = ref()
let show = ref(true);
let count = ref();
let timer = reactive(null);

const formData = reactive({   /* 学号，电话，姓名，身份证号，密码，性别，政治面貌，民族，邮箱，电话*/
  phone: '',//电话
  userNumber: '',//学号
  password: '',
  name: '', //姓名
  idCardNumber: '', //身份证号
  gender: false, //false默认是男性
  ethnic: '', //民族
  politicalAffiliation: '', //政治面貌
  eMail: '', //邮箱
  nickName: '', //用户名
  identity: '', //身份
  code: '', //验证码
})
const sendCode = async () => {
  showLoading();
  await service.post('/api/user/registerPhone',{ phone: formData.phone }).then(res => {
    let data = res.data;
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

/* 定义校验规则 */
const validateName = (rule, value, callback) => {  //校验姓名，考虑少数民族
  const reg = /(^[\u4e00-\u9fa5]{1}[\u4e00-\u9fa5\.·.]{0,18}[\u4e00-\u9fa5]{1}$)|(^[a-zA-Z]{1}[a-zA-Z\s]{0,18}[a-zA-Z]{1}$)/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入姓名！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的姓名！'));
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

const validateIdCardNumber = (rule, value, callback) => {  //检验身份证号(精确校验)
  const reg = /^[1-9]\d{5}(19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入身份证号'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码！'));
    } else {
      callback();
    }
  }
}
const validateEMail = (rule, value, callback) => {  //检验邮箱
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入邮箱！'));
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱'));
    } else {
      callback();
    }
  }
}
const validatePhone = (rule, value, callback) => { //检验手机号(不能是座机......)
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入手机号码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
/* validator: validatepassword, 
validator: validateName
validator: validateEMail
validator: validatePhone
validator: validateIdCardNumber
*/
const rules = reactive({
  name: [{ validator: validateName, trigger: 'blur' },
  { max: 10, message: '姓名没有超过10位', trigger: 'blur' }],
  userNumber: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  identity: [{ required: true, message: '请选择您的身份', triggwe: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'blur' }],
  idCardNumber: [{ validator: validateIdCardNumber, trigger: 'blur' }],
  eMail: [{validator: validateEMail, trigger: 'blur' }],
  ethnic: [{ required: true, message: '请填写您的民族', triggwe: 'blur' }],
  politicalAffiliation: [{ required: true, message: '请选择您的政治面貌', triggwe: 'blur' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  password: [{ validator: validatepassword, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' },{
    max:5,message:'验证码长度为5位',trigger:'blur'
  }],
  nickName: [{ required: true, message: '请输入用户名', trigger: 'blur' },
  { max: 8, message: '长度请不要超过8位', trigger: 'blur' }]
})


/* phone: formData.value.phone, userNumber: formData.value.userNumber,
  password: formData.value.password, name: formData.value.name, idCardNumber: formData.value.idCardNumber,
    gender: formData.value.gender, ethnic: formData.value.ethnic, politicalAffiliation: formData.value.politicalAffiliation,
      eMail: formData.value.eMail, nickName: formData.value.nickName, indentity: formData.value.identity, code: formData.value.code  */
const register = async () => {
  console.log(formData)
  await registerForm.value.validate((valid) => {    //registerForm是上面表单ref绑定的值
    if (valid) {
      showLoading();
      console.log(formData.identity);
      console.log(formData.eMail);
      service.post('/api/user/register', {
        phone: formData.phone, userNumber: formData.userNumber,
        password: formData.password, name: formData.name, idCardNumber: formData.idCardNumber,
        gender: formData.gender, ethnic: formData.ethnic, politicalAffiliation: formData.politicalAffiliation,
        eMail: formData.eMail, nickName: formData.nickName, identity: formData.identity, code: formData.code
      }).then(res => {
        const data = res.data;
        console.log(data);
        if (data.success) {
          hideLoading();
          messageSuccess('注册成功！')
          localStorage.setItem('token', data.token);
          router.push('/Login')
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
      messageError('注册失败，请完善您的信息！')
    }
  })


}
</script>
<style scoped>
.pageBackground {
  position: fixed;
  height: 100%;
  width: 100%;
  justify-content: center;
  justify-items: center;
  vertical-align: middle;
  align-items: center;
  display: flex;
  background-image: url("../assets/images/LoginPageBackground.jpg");
  background-size: cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: no-repeat;
  padding: 0;
  margin: 0;
}

.registerForm {
  width: 30%;
  height: 95%;
  align-items: center;
  position: absolute;
  top: 3%;
  background: #c9c4ce96;
  bottom: 20%;
}

.registerFormContent {
  width: 80%;
  margin-left: 5%;
  margin-right: 10%;
  margin-top: 8%;
  align-items: center;
  display: flex;
}

.registerPageEl-botton {
  width: 100%;
  background-color: rgba(51, 126, 204, 0.3);
  /* text-align: center; */
  margin-top: 10px;
  margin-left: 25px;
  margin-right: 25px;
  line-height: 50px;
  border-radius: 35px;
}

.captchaInput {
  height: 50px;
  width: 100% ! important;
  padding-right: 0px;
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;
}

.captchaButton {
  background-color: rgba(103, 194, 58, 0.5);
  margin-top: 4px;
  margin-left: 5px;
  height: 43px !important;
  width: 90% !important;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 3px !important;
}

.buttonLogin {
  margin-top: 40px;
}
</style>
