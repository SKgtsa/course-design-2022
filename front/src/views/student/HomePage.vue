<template>
  <div class="back">
    <el-container>
      <el-header class="el-header">
        <el-menu class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#e9eff9" text-color="#3e5ca8" active-text-color="#2d67fd" router>
          <img src="../../assets/images/logo.png" @click="checkCopyright" alt="logo未加载">
          <el-menu-item class="logo" index="/Main">教学系统</el-menu-item>
          <div class="flex-grow" />
          <!--          <el-sub-menu index="1">-->
          <!--            <template #title>{{userName}}</template>-->
          <!--            <el-menu-item route="/SelfInformation" index="1-1">个人信息</el-menu-item>-->
          <!--          </el-sub-menu>-->
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
            <a class="nickName">{{ nick }}</a>
          </div>
          <a @click="checkCopyright" class="copyright">@版权</a>
        </el-menu>

        <el-dialog v-model="dialogCopyright">
          <!--  上传头像没有用action的写法，用到是http-request的 -->
          <h3>版权说明</h3>
          <p3>该网站归属某组所有，任何侵权行为均要付相应的法律责任!(确信)</p3>
        </el-dialog>
        <!-- 修改头像弹出框 -->
        <el-dialog v-model="dialogVisibleImg">
          <!--  上传头像没有用action的写法，用到是http-request的 -->
          <section>
            <el-upload class="img-btn" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
              :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
              <!--  :action="base_url+target""，这个没有写  --><!-- :on-change="onchangeUpload" -->
              <img class="dialogImg" :src="getAvatarURL()" />
              <div class="head-img">
                <!--  <el-progress type="circle" :percentage="progressPercent"></el-progress> -->
              </div>
            </el-upload>
          </section>
        </el-dialog>
        <!-- 修改昵称弹出框 -->
        <el-dialog v-model="dialogVisibleName" width="20vw">
          <el-form :model="nickNameData" :rules="nickNameRules" ref="nickData">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="nickNameData.nickName">{{ getNickName() }}</el-input>
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
          <div v-if="!isPass">
            <el-form :model="originFormPwd" :rules="OriginPwdRules" ref="originPwdData">
              <el-form-item label="之前密码:" prop="originPwd">
                <el-input v-model="originFormPwd.originPwd"></el-input>
              </el-form-item>
            </el-form>
            <div style="width:60%;padding-left:20%">
              <el-button @click="submitOriginPwd">
                <a>确定</a>
              </el-button>
              <el-button @click="closePwd">
                <a>取消</a>
              </el-button>
            </div>
          </div>

          <div v-if="isPass">
            <el-form :model="formEditPwd" :rules="pwdEditRules" ref="pwdEditData">
              <el-form-item label="新的密码:" prop="newPwd">
                <el-input v-model="formEditPwd.newPwd"></el-input>
              </el-form-item>
              <el-form-item label="再次输入:" prop="newPwdCheck">
                <el-input v-model="formEditPwd.newPwdCheck"></el-input>
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
      <el-container>
        <el-main class="mainWindow">
          <div class="rightWindow">
            <el-menu router default-active="/Main" active-text-color="#2d67fd" background-color="#e9eff9"
              class="el-menu-vertical-demo asideMenu" text-color="#3e5ca8" @open="handleOpen" @close="handleClose"
              :collapse="true">
              <el-menu-item index="/Main">
                <template #title>我的主页</template>
                <el-icon>
                  <HomeFilled />
                </el-icon>
              </el-menu-item>
              <el-menu-item index="/CourseSelect">
                <template #title>选课指南</template>
                <el-icon>
                  <Document />
                </el-icon>
              </el-menu-item> <el-menu-item index="/ScoreManage">
                <el-icon>
                  <DocumentChecked />
                </el-icon>
                <template #title>成绩管理</template>
              </el-menu-item>
              <el-menu-item index="/AcademicManagement/AMHome">
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
import { useStore } from 'vuex' // 引入useStore 方法
import { getAvatarURL, getNickName, setAvatarURL, setNickName } from '../../global/global';
import { RouterView } from 'vue-router';
import type { UploadFile } from 'element-plus'
import { messageError, messageSuccess } from '@/utils/message';
import service from '@/request';
import axios from 'axios';
import { dataType } from 'element-plus/es/components/table-v2/src/common';
import serviceFile from "@/request/indexFile";

//查看版权说明
let dialogCopyright = ref(false);
const checkCopyright = () =>{
  dialogCopyright.value = true;
};

let progressFlag = ref(false);
/* let actionURL = ref('http://localhost:5174/api/user/changeAvatar'); */
let base_url = 'http://localhost:5174';
let target = '/api/user/changeAvatar';
let progressPercent = ref();
//最后参考的那篇文章
  //https://blog.csdn.net/qq_40663357/article/details/100049258
  //https://blog.csdn.net/Tai4lin/article/details/96974619?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522167176624816800188526592%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=167176624816800188526592&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-1-96974619-null-null.142^v68^pc_rank_34_queryrelevant25,201^v4^add_ask,213^v2^t3_control1&utm_term=el-upload%E4%B8%8A%E4%BC%A0%E5%A4%B4%E5%83%8F
  /*   axios({ //别人写的
              url: baseURL + '/image',
              method: 'post',
              data: formdata,
              headers: {'Content-Type': 'multipart/form-data'},
              onUploadProgress: progressEvent => {
                  // progressEvent.loaded:已上传文件大小
                  // progressEvent.total:被上传文件的总大小
                  this.progressPercent = (progressEvent.loaded / progressEvent.total * 100)
              }
          }) */
  /*   let onUploadProgress =(progressEvent)=> {  //我改的....但是感觉不对？是不是在axios里面创建这个，
    //主要是传图片有个进度条，没有要
                  // progressEvent.loaded:已上传文件大小
                  // progressEvent.total:被上传文件的总大小
                  progressPercent.value = (progressEvent.loaded / progressEvent.total * 100)
              } */
//有个博客说写成baseURL+接口的形式就可以，直接写完整就不行，不知道为什么,我按照他这个写的,
//然后uploadImg方法是更改头像，好像是action和http-request是一样的，但是他要求action又必须写
//一个是action随便写一个，然后执行http-request,就是目前这种写法
//另一种是 :action="base_url+target",然后把http-request去了
let uploadImg = (f) => {
  serviceFile.post('/api/user/changeAvatar', { avatar:f.file, token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      localStorage.setItem('token', data.token);
      setAvatarURL(data.content);
      messageSuccess("更换成功！")
    } else {
      messageError(data.message)
    }
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
/* const onchangeUpload = (file) => {
  // 预保存上传的图片
  file.previewImgURL = URL.createObjectURL(file.raw)
  file.confirmProfile = true // 预览图片
} */
/* const store = useStore()  // 该方法用于返回store 实例
console.log(store)  // store 实例对象
let avatarURL = store.getters.avatarURL; */
let url = getAvatarURL();
let nick = getNickName();
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
  newPwd: '',
  newPwdCheck: '',
})
let pwdEditData = ref();

const toSelfInformation = () => { //个人主页
  router.push('/SelfInformation');
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
  const reg = /^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{8,20}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请设置您的密码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('密码中必须包含字母、数字、特殊字符,长度在8-20位之间'));
    }
  }
}


const OriginPwdRules = reactive({
  originPwd: [{ validator: validatepassword, trigger: 'blur' }],
});
const pwdEditRules = reactive({
  newPwd: [{ validator: validatepassword, trigger: 'blur' }],
  newPwdCheck: [{ validator: validatepassword, trigger: 'blur' }],
})

const submitNickName = () => {
  nickData.value.validate((valid) => {
    if (valid) {
      service.post('/api/user/editBlogInfo', { token: localStorage.getItem('token'), nickName: nickNameData.nickName }).then((res) => {
        let data = res.data;
        if (data.success) {
          localStorage.setItem('token', data.token);
          setNickName(nickNameData.nickName);
          messageSuccess("修改成功！");
        } else {
          messageError(data.message);
        }
      })
    } else {
      messageError("请填写正确！")
    }
  })
}
const closeNickNameDialog = () => {
  dialogVisibleName.value = false;
}

const submitOriginPwd = () => {
  /* dialogVisiblePwd.value = false; */
  /*   originPwdData.value.validdate((valid)=>{
      if(valid){
        if(){

        }else{

        }
      }else{
        messageError("请填写旧密码！")
      }
    }) */

  isPass.value = true;
}
const closePwd = () => {
  dialogVisiblePwd.value = false;
  isPass.value = false;
}

const submitNewPwd = () => {

}
const closeNewPwdDialog = () => {
  isPass.value = false;
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
.copyright{
  padding-top:5px;
  padding-right: 5px;
  font-family: 华文楷体;
  cursor: pointer;

}
.dialogImg {
  width: 60%;
  padding-left: 20%;
}
</style>
