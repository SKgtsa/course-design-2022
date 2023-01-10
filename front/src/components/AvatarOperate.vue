<template>
  <div :style="{
    'padding-top': `${mobile? '0':'1.5vh'}`,
    'padding-right': `${mobile? '0':'1vw'}`,
    'padding-bottom': `${mobile? '2vh':'0'}`,
    'height' : `${mobile? '15vh':'auto'}`
  }" class="nick">
    <el-dropdown trigger="click">
      <el-button :style="{
        'width': `${mobile? 12:4}vh`,
        'height': `${mobile? 12:4}vh`,
        'border-radius': `${mobile? 6:2}vh`
      }">
        <el-image  :src="getAvatarURL()"  :style="{
        'width': `${mobile? 12:4}vh`,
        'height': `${mobile? 12:4}vh`,
        'border-radius': `${mobile? 6:2}vh`
      }"/>
      </el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="toSelfInformation">个人信息页</el-dropdown-item>
          <el-dropdown-item @click="editAvatar">修改头像</el-dropdown-item>
          <el-dropdown-item @click="editNick">修改昵称</el-dropdown-item>
          <el-dropdown-item @click="editPwd">修改密码</el-dropdown-item>
<!--            <el-dropdown-item @click="drop">退出</el-dropdown-item> -->
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <a class="nickName" :style="{
          'line-height': `${mobile? 12:4}vh`,
          'padding-right': `${mobile? '0':'0.5vw'}`,
          'padding-left': `${mobile? '2vw':'0'}`,
          'font-size': `${mobile? 6:1.75}vh`
        }">{{ getNickName() }}</a>
  </div>
  <!-- 修改昵称弹出框 -->
  <el-dialog v-model="dialogVisibleName" :width="`${mobile? 90:40}%`">
    <el-form :model="nickNameData" :rules="nickNameRules" ref="nickData">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="nickNameData.nickName" maxlength="8">{{ getNickName() }}</el-input>
      </el-form-item>
    </el-form>
    <div style="width:60%;padding-left:20%">
      <el-button @click="submitNickName">
        <a>确定</a>
      </el-button>
      <el-button @click="closeNickNameDialog">
        <a>取消</a>
      </el-button>
    </div>
  </el-dialog>
  <!-- 修改头像弹出框 -->
  <el-dialog v-model="dialogVisibleImg" width="30vh">
    <el-upload class="avatar-uploader" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
               :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
      <img v-if="getAvatarURL()" class="avatar" :src="getAvatarURL()" />
    </el-upload>
  </el-dialog>
  <!-- 修改密码弹出框 -->
  <el-dialog v-model="dialogVisiblePwd" :width="`${mobile? 90:40}%`" >
    <!-- 没有校验的时候显示，就是密码不正确或者没有执行找回密码操作的时候 -->
    <div>
      <el-form :model="formEditPwd" :rules="pwdEditRules" ref="pwdEditData">
        <el-form-item label="旧密码:" prop="oldPwd">
          <el-input v-model="formEditPwd.oldPwd"></el-input>
        </el-form-item>
        <el-form-item label="新密码:" prop="newPwd">
          <el-input v-model="formEditPwd.newPwd"></el-input>
        </el-form-item>
      </el-form>
      <div style="width:60%;padding-left:20%">
        <el-button @click="submitNewPwd">
          <a>确定</a>
        </el-button>
        <el-button @click="closeNewPwdDialog">
          <a>取消</a>
        </el-button>
      </div>
    </div>
  </el-dialog>

</template>

<script lang="ts" setup>
  import router from "@/router";
  import {reactive, ref} from "vue";
  import {getAvatarURL, getBaseURL, getNickName, getUserId, mobile, setNickName} from "@/global/global";
  import {hideLoading, showLoading} from "@/utils/loading";
  import service from "@/request";
  import {messageError, messageSuccess} from "@/utils/message";
  import serviceFile from "@/request/indexFile";
  import {reload} from "@/utils/reloadRouter";
  let nickData = ref();


  let isPass = ref(false); //这个是否让它改密码

  //初始密码表单
  let originFormPwd = reactive({
    originPwd: '',
  });
  let originPwdData = ref();

  let dialogVisibleImg = ref(false)
  let dialogVisibleName = ref(false)
  let dialogVisiblePwd = ref(false)

  let nickNameData = reactive({
    nickName: getNickName(),
  })

  const submitNickName = async () => {
    await nickData.value.validate((valid) => {
      if (valid) {
        showLoading();
        service.post('/api/user/editNickName', { token: localStorage.getItem('token'), nickName: nickNameData.nickName }).then((res) => {
          let data = res.data;
          if (data.success) {
            hideLoading();
            localStorage.setItem('token', data.token);
            setNickName(nickNameData.nickName);
            messageSuccess("修改成功！");
            reload()
          } else {
            hideLoading();
            messageError(data.message);
          }
        })
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      } else {
        messageError("请填写正确！")
      }
    })
  }
  const nickNameRules = reactive({
    nickName: [{ required: true, message: '输入您想更改的昵称', trigger: 'blur' },
      { max: 8, message: '长度请不要超过8位', trigger: 'blur' }
    ]
  });

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

  const pwdEditRules = reactive({
    oldPwd: [{ validator: validatepassword, trigger: 'blur' }],
    newPwd: [{ validator: validatepassword, trigger: 'blur' }],
  })

  const submitNewPwd = async () => {
    await pwdEditData.value.validate((valid) => {
      if (valid) {
        showLoading();
        service.post('//api/user/changePassword', {
          token: localStorage.getItem('token'),
          oldPassward: formEditPwd.oldPwd, newPassward: formEditPwd.newPwd
        }).then(res => {
          let data = res.data;
          if (data.success == true) {
            hideLoading();
            localStorage.setItem('token', data.token); //返回token吗
            messageSuccess(data.message)
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
        messageError("请正确填写！")
      }
    })
  }




  let uploadImg = async (f) => {
    showLoading();
    await serviceFile.post('/api/user/changeAvatar', { avatar: f.file, token: localStorage.getItem('token') }).then(res => {
      let data = res.data;
      console.log(res)
      if (data.success) {
        hideLoading();
        localStorage.setItem('token', data.token);
        let url = getBaseURL() + data.content;
        console.log(url);
        localStorage.setItem('avatarURL', url);
        reload()
        messageSuccess("更换成功！")
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
  // 上传图片前的过滤
  let beforeAvatarUpload = (file) => {
    const isLt1_5M = (file.size / 1024 / 1024) < 1.5
    if (!isLt1_5M) {
      messageError('上传头像图片大小不能超过 1.5MB!')
    }
    return isLt1_5M
  }

  //更改新密码
  let formEditPwd = reactive({
    oldPwd: '',
    newPwd: '',
  })
  let pwdEditData = ref();

  const toSelfInformation = () => { //个人主页
    router.push({
      path: '/personalPage',
      query: {
        userId: getUserId()
      }
    });
  }
  const closeNewPwdDialog = () => {
    dialogVisiblePwd.value = false;
  }
  const closeNickNameDialog = () => {
    dialogVisibleName.value = false;
  }

  const editNick = () => { //弹出修改昵称界面
    dialogVisibleName.value = true;
  }

  const drop = () => {  //退出
    router.push('/Login');
  }

  const editAvatar = () => {  //弹出修改图片界面
    dialogVisibleImg.value = true;
  }

  const editPwd = () => {  //弹出修改密码界面
    dialogVisiblePwd.value = true;
  }
</script>

<style scoped>
.nick{
  display: flex;
  flex-direction: row;
  height: 7.5vh;
  flex-direction: row-reverse;
}

.avatar-uploader{

}
.avatar{
  width: 20vh;
  height: 20vh;
  border-radius: 20vh;
  margin:auto;
}
.nickName{
  font-weight: bold;
  color: rgba(60, 96, 198, 0.9);
}
</style>
