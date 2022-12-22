<template>
  <div class="back">
    <el-container>
      <el-header class="el-header">
        <el-menu class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#e9eff9" text-color="#3e5ca8" active-text-color="#2d67fd" router>
          <img src="../../assets/images/logo.png" alt="logo未加载">
          <el-menu-item index="/Main" class="logo">教学系统</el-menu-item>
          <div class="flex-grow" />
          <!--          <el-sub-menu index="1">-->
          <!--            <template #title>{{userName}}</template>-->
          <!--            <el-menu-item route="/SelfInformation" index="1-1">个人信息</el-menu-item>-->
          <!--          </el-sub-menu>-->
          <div class="nick">
            <el-dropdown trigger="click">
              <el-button class="avatar">
                <el-image class="avatarImage" :src="url" />
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="toSelfInformation">个人信息页</el-dropdown-item>
                  <el-dropdown-item @click="editAvatar">修改头像</el-dropdown-item>
                  <el-dropdown-item @click="editNick">修改昵称</el-dropdown-item>
                  <el-dropdown-item @click="drop">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <a class="nickName">{{ nick }}</a>
          </div>
          <el-dialog v-model="dialogVisibleImg">
           <!--  上传头像没有用action的写法，用到是http-request的 -->
            <section>

              <el-upload class="img-btn" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
              :http-request="uploadImg"> <!--   :action="base_url+target""，这个没有写 -->
                <img class="dialogImg" :src="getAvatarURL()" />
                <div class="head-img">
                 <!--  <el-progress type="circle" :percentage="progressPercent"></el-progress> -->
                </div>
              </el-upload>
            </section>

          </el-dialog>
        </el-menu>
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
let progressFlag = ref(false);
/* let actionURL = ref('http://localhost:5174/api/user/changeAvatar'); */
let base_url = 'http://localhost:5174';
let target = '/api/user/changeAvatar';
let progressPercent = ref();
//有个博客说写成baseURL+接口的形式就可以，直接写完整就不行，不知道为什么,我按照他这个写的,
//然后uploadImg方法是更改头像，好像是action和http-request是一样的，但是他要求action又必须写
//一个是action随便写一个，然后执行http-request,就是目前这种写法
//另一种是 :action="base_url+target",然后把http-request去了
//不知道哪个行......也可能都不行(bushi)
let uploadImg=(f)=>{
  progressFlag.value = true;
  let formData = new FormData();
  formData.append('avatar',f.file);
  //最后参考的那篇文章
  //https://blog.csdn.net/qq_40663357/article/details/100049258
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
  service.post('/api/user/changeAvatar',{avatar:formData,token:localStorage.getItem('token')}).then(res=>{
    let data = res.data;
    if(data.success){
      localStorage.setItem('token',data.token);
      setAvatarURL(f.file);
      messageSuccess("更换成功！")
    }else{
      messageError(data.message)
    }
  })
}
// 上传图片前的过滤
let beforeAvatarUpload=(file)=> {
  const isJPG = file.type === 'image/jpeg'
  const isLt2M = (file.size / 1024 / 1024) < 2

  if (!isJPG) {
    messageError('上传头像图片只能是 JPG 格式!')
  }
  if (!isLt2M) {
    messageError('上传头像图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}
/* const store = useStore()  // 该方法用于返回store 实例
console.log(store)  // store 实例对象
let avatarURL = store.getters.avatarURL; */
let url = getAvatarURL();
let nick = getNickName();
let dialogVisibleImg = ref(false)

const toSelfInformation = () => {
  router.push('/SelfInformation');
}
const editNick = () => {

}
const drop = () =>{
  router.push('/Login');
}
const editAvatar = () => {
  dialogVisibleImg.value = true;
}
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const onSubmit = () => {
  console.log('submit!')
}
let menuDataTop = reactive({
  activeIndex1: '1'
})

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
.nick{
  display:flex;
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

.dialogImg {
  width: 60%;
  padding-left: 20%;
}
</style>
