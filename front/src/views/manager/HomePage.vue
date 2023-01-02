<template>
  <div class="back">
    <el-container>
      <el-header class="el-header" v-if="!mobile">
        <el-menu class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#f0f2ff" text-color="#3e5ca8" active-text-color="#2d67fd" router>
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
        </el-menu>
        <!-- 修改头像弹出框 -->
        <el-dialog v-model="dialogVisibleImg" style=" width: 50vh!important;height: 37.5vh!important;">
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
      <el-header v-if="mobile">
        <el-menu style="height: 10vh;background-color: #0a8ce2; display: flex;flex-direction: row" mode="horizontal"
          :ellipsis="false">
          <a style="font-size: 5vh;color: #FFFFFF;font-weight: bold;line-height: 10vh">教学系统</a>
          <div class="flex-grow" />
          <div style="padding-right: 2vw;padding-top: 2.5vh">
            <el-button class="menuButton" @click="openMenu">
              <el-image :src="'http://courseback.clankalliance.cn/inbuild/menu.png'" class="menuButtonImage" />
            </el-button>
          </div>
        </el-menu>
        <!--移动端的右侧菜单-->
        <el-drawer with-header="false" v-model="menuDrawerOpen" :direction="'rtl'" :size="'90%'" z-index="50">
          <div class="nickMobile">
            <el-dropdown trigger="click">
              <el-button class="avatarMobile">
                <el-image class="avatarImageMobile" :src="getAvatarURL()" />
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
          </div>
          <a class="nickNameMobile">{{ getNickName() }}</a>
          <el-menu router default-active="/Manager/Main" active-text-color="#2d67fd" background-color="#e9eff9"
            text-color="#3e5ca8" @open="handleOpen" @close="handleClose">
            <el-menu-item index="/Manager/Main" @click="closeMenuDrawer">
              <template #title>博客主页</template>
              <el-icon>
                <HomeFilled />
              </el-icon>
            </el-menu-item>
            <el-menu-item index="/Manager/noticeView" @click="closeMenuDrawer">
              <template #title>通知管理</template>
              <el-icon>
                <Document />
              </el-icon>
            </el-menu-item> 
            <el-menu-item index="/Manager/InfoManage" @click="closeMenuDrawer">
              <template #title>信息管理</template>
              <el-icon>
                <Document />
              </el-icon>
            </el-menu-item> 
         
            <el-menu-item index="/Manager/CourseManage" @click="closeMenuDrawer">
              <el-icon>
                <DocumentChecked />
              </el-icon>
              <template #title>课程管理</template>
            </el-menu-item>
          </el-menu>
        </el-drawer>
      </el-header>
      <el-container>
        <el-main class="mainWindow">
          <div class="rightWindow">
            <el-menu v-if="!mobile" router default-active="/Manager/Main" active-text-color="#2d67fd"
              background-color="#f0f2ff" class="el-menu-vertical-demo asideMenu" text-color="#3e5ca8" @open="handleOpen"
              @close="handleClose" :collapse="true">
              <el-menu-item index="/Manager/Main">
                <template #title>我的主页</template>
                <el-icon>
                  <HomeFilled />
                </el-icon>
              </el-menu-item>
              <el-menu-item index="/Manager/Notice">
                <template #title>通知管理</template>
                <el-icon>
                  <Document />
                </el-icon>
              </el-menu-item> 
              <el-menu-item index="/Manager/InfoManage">
                <template #title>信息管理</template>
                <el-icon>
                  <Document />
                </el-icon>
              </el-menu-item> 
              <el-menu-item index="/Manager/CourseManage">
                <el-icon>
                  <DocumentChecked />
                </el-icon>
                <template #title>课程管理</template>
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

const menuDrawerOpen = ref(false);

const openMenu = () => {
  menuDrawerOpen.value = true;
}

const closeMenuDrawer = () => {
  menuDrawerOpen.value = false;
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
  menuDrawerOpen.value = false;
  router.push({
    path: '/personalPage',
    query: {
      userId: getUserId()
    }
  });
}

const editNick = () => { //弹出修改昵称界面
  dialogVisibleName.value = true;
  menuDrawerOpen.value = false;
}

const drop = () => {  //退出
  localStorage.setItem('token', null);
  localStorage.setItem('nickName', '');
  localStorage.setItem('userId', null)
  menuDrawerOpen.value = false;
  router.push('/Login');
}

const editAvatar = () => {  //弹出修改图片界面
  dialogVisibleImg.value = true;
  menuDrawerOpen.value = false;
}

const editPwd = () => {  //弹出修改密码界面
  dialogVisiblePwd.value = true;
  menuDrawerOpen.value = false;
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
  background-color: #f0f2ff;
}

.menuButton {
  background-color: rgba(0, 0, 0, 0);
  border-style: none;
  width: 5vh;
  height: 5vh;
}

.menuButtonImage {
  width: 5vh;
}

.nav-bar-top {
  height: 7vh;
  border-style: none;
}

.el-header {
  height: 7vh;
  padding: 0;
}

.headerMenu {
  height: 100%;
}

.avatar {
  width: 4vh;
  height: 4vh;
  border-radius: 2vh;
}

.avatarImage {
  width: 4vh;
  height: 4vh;
  border-radius: 2vh;
}

.avatarMobile {
  width: 12vh;
  height: 12vh;
  border-radius: 6vh;
}

.avatarImageMobile {
  width: 12vh;
  height: 12vh;
  border-radius: 6vh;
}

.el-aside {
  width: 25vh;
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
  height: 8.75vh !important;
  width: 35%;
}

.logoImage {
  display: inline-block;
}

.img {
  height: 8.125vh !important;
  margin-top: 0.25vh;
  margin-left: 0.25vh;
  width: 35vh;
  /*  margin-top: 8vh;
   margin-bottom: 8vh; */
}



.flex-grow {
  flex-grow: 1;
}

.logo {
  font-weight: bold;
  font-size: 3.5vh;
}

img {
  padding-right: 0.625vh;
  margin: 1.25vh;
}

.nick {
  display: flex;
  flex-direction: row;
  padding-top: 1.5vh;
  height: 7.5vh;
  flex-direction: row-reverse;
  padding-right: 1vw;
}

.nickMobile {
  display: flex;
  flex-direction: row;
  height: 7.5vh;
  flex-direction: row-reverse;
  padding-bottom: 2vh;
}

.nickName {
  line-height: 4vh;
  font-size: 1.75vh;
  padding-right: 0.5vw;
  font-weight: bold;
  color: rgba(60, 96, 198, 0.9);
}

.nickNameMobile {
  line-height: 12vh;
  font-size: 6vh;
  font-weight: bold;
  color: rgba(60, 96, 198, 0.9);
  padding-left: 2vw;
}

.dialogImg {
  /* padding-left: 20%; */
  justify-content: center;
}

.avatar-uploader .avatar {
  width: 22.25vh;
  height: 22.25vh;
  display: block;

}
</style>

