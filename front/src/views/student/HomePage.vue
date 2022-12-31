<template>
  <div class="back">
    <el-container>
      <el-header class="el-header" v-if="!mobile">
        <el-menu class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#e9eff9" text-color="#3e5ca8" active-text-color="#2d67fd" router>
          <img src="../../assets/images/logo.png" @click="checkCopyright" alt="logo未加载">
          <el-menu-item class="logo" index="/Main">教学系统</el-menu-item>
          <div class="flex-grow" />
          <div class="nick">
            <el-dropdown trigger="click">
              <el-button class="avatar">
                <el-image class="avatarImage" :src="getAvatarURL()" />
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="toSelfInformation">个人信息页</el-dropdown-item>
                  <el-dropdown-item @click="editAvatar">修改头像</el-dropdown-item>
                  <el-dropdown-item @click="editNick">修改昵称</el-dropdown-item>
                  <el-dropdown-item @click="editPwd">修改密码</el-dropdown-item>
                  <el-dropdown-item @click="drop">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <a class="nickName">{{ getNickName() }}</a>
          </div>
          <a @click="checkCopyright" class="copyright">@版权</a>
        </el-menu>

        <el-dialog v-model="dialogCopyright">
          <h3>版权说明</h3>
          <p3>该网站归属某组所有，任何侵权行为均要付相应的法律责任!(确信)</p3>
        </el-dialog>
        <!-- 修改头像弹出框 -->
        <el-dialog v-model="dialogVisibleImg" style=" width: 400px!important;height: 300px!important;">
          <section>
            <el-upload class="avatar-uploader" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
              :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
              <img v-if="getAvatarURL()" class="avatar" :src="getAvatarURL()" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
              <div class="head-img">
              </div>
            </el-upload>
          </section>
        </el-dialog>
        <!-- 修改昵称弹出框 -->
        <el-dialog v-model="dialogVisibleName" width="20vw">
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
        <!-- 修改密码弹出框 -->
        <el-dialog v-model="dialogVisiblePwd" width="25vw">
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
      </el-header>
      <el-header v-if="mobile" >
        <el-menu style="height: 10vh;width: 100vw;background-color: #0a8ce2; display: flex;flex-direction: row">
          <a style="font-size: 5vh;color: #FFFFFF;font-weight: bold;line-height: 10vh">教学系统</a>
          <div class="flex-grow">
            <el-button>菜单</el-button>
          </div>
        </el-menu>
      </el-header>
      <el-container>
        <el-main class="mainWindow">
          <div class="rightWindow">
            <el-menu v-if="!mobile" router default-active="/Student/Main" active-text-color="#2d67fd" background-color="#e9eff9"
              class="el-menu-vertical-demo asideMenu" text-color="#3e5ca8" @open="handleOpen" @close="handleClose"
              :collapse="true">
              <el-menu-item index="/Student/Main">
                <template #title>我的主页</template>
                <el-icon>
                  <HomeFilled />
                </el-icon>
              </el-menu-item>
              <el-menu-item index="/Student/CourseSelect">
                <template #title>选课指南</template>
                <el-icon>
                  <Document />
                </el-icon>
              </el-menu-item> <el-menu-item index="/Student/ScoreManage">
                <el-icon>
                  <DocumentChecked />
                </el-icon>
                <template #title>成绩管理</template>
              </el-menu-item>
              <el-menu-item index="/Student/AcademicManagement/AMHome">
                <el-icon>
                  <PictureFilled />
                </el-icon>
                <template #title>学工管理</template>
              </el-menu-item>
            </el-menu>
          </div>
          <div class="leftMenu">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script lang="ts" setup>
import { ref, reactive, toRefs } from 'vue'
import logo from '../../assets/images/logo.png'
import {
  Menu as IconMenu,
  Menu,
  StarFilled,
  Promotion,
  House,
  Setting,
  Document,
  Picture,
  PictureFilled,
  Calendar,
  HomeFilled,
  List,
  DocumentChecked,
} from '@element-plus/icons-vue'
import router from "@/router";
import {
  getAvatarURL,
  getBaseURL,
  getNickName,
  getUserId,
  setAvatarURL,
  setNickName,
    mobile
} from '../../global/global';
import { RouterView } from 'vue-router';
import { messageError, messageSuccess } from '@/utils/message';
import service from '@/request';
import serviceFile from "@/request/indexFile";
import { hideLoading, showLoading } from '@/utils/loading';

//查看版权说明
let dialogCopyright = ref(false);
const checkCopyright = () => {
  dialogCopyright.value = true;
};

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
      setAvatarURL(url);

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
  const isLt2M = (file.size / 1024 / 1024) < 2
  if (!isLt2M) {
    messageError('上传头像图片大小不能超过 2MB!')
  }
  return isLt2M
}
let dialogVisibleImg = ref(false)
let dialogVisibleName = ref(false)
let dialogVisiblePwd = ref(false)
let nickData = ref();
let isPass = ref(false); //这个是否让它改密码
let nickNameData = reactive({
  nickName: getNickName(),
})

//初始密码表单
let originFormPwd = reactive({
  originPwd: '',
});
let originPwdData = ref();

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

const submitNickName = async () => {
  await nickData.value.validate((valid) => {
    if (valid) {
      showLoading();
      service.post('/api/user/editBlogInfo', { token: localStorage.getItem('token'), nickName: nickNameData.nickName }).then((res) => {
        let data = res.data;
        if (data.success) {
          hideLoading();
          localStorage.setItem('token', data.token);
          setNickName(nickNameData.nickName);
          messageSuccess("修改成功！");
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
const closeNickNameDialog = () => {
  dialogVisibleName.value = false;
}

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
const closeNewPwdDialog = () => {
  dialogVisiblePwd.value = false;
}
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
<style scoped lang='scss'>
.back {
  margin: 0;
  padding: 0;
  background-color: #e9eff9;
}

.el-header {

  padding: 0;
  //padding: 0 0 !important;
  //height: 80px !important;
  //width: 100%;
  //position: fixed;
  //z-index: 9999;
}

.headerMenu {
  height: 100%;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 20px;
}

.avatarImage {
  width: 40px;
  height: 40px;
  border-radius: 20px;
}

.el-aside {
  width: 200px;
  padding-top: 2.2vh;
}

.el-main {
  padding: 0;
}

.mainWindow {
  display: flex;
  flex-direction: row-reverse;
}

.leftMenu {
  width: 100%;
}

.asideMenu {
  height: 100%;
}

.flex-grow {
  flex-grow: 1;
}

.container {
  width: 100vw;
  height: 100vh;
  display: flex;
}

.logoDiv {
  height: 70px !important;
  width: 35%;
}

.logoImage {
  display: inline-block;
}

.img {
  height: 65px !important;
  margin-top: 2px;
  margin-left: 2px;
  width: 280px;
  /*  margin-top: 8px;
   margin-bottom: 8px; */
}

.nav-bar-top {
  height: 83px;
}

.flex-grow {
  flex-grow: 1;
}

.logo {
  font-weight: bold;
  font-size: 3.5vh;
}

.title {
  font-family: 华文楷体, serif;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  font-size: 7vh;
  margin-right: 1vh;
  margin-left: 3vh;
  color: #FFFFFF;
}

img {
  padding-right: 5px;
  margin: 10px;
}

.nick {
  display: flex;
  flex-direction: row;
  padding-right: 100px;
  padding-top: 17px;
  height: 60px;
}

.nickName {
  padding-top: 10px;
  padding-left: 13px;
  position: bottom;
  font-size: 14px;
}

.copyright {
  padding-top: 5px;
  padding-right: 5px;
  font-size: 14px;
  cursor: pointer;

}

.dialogImg {
  /* padding-left: 20%; */
  justify-content: center;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;

}
</style>

