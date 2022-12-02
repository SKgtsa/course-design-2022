<script lang="ts" setup>
import { ref, reactive, toRefs } from 'vue'
import logo from '../../assets/images/logo.png'
import {
  Menu as IconMenu,
  Menu,
  StarFilled,
  Promotion,
  House,
} from '@element-plus/icons-vue'
import router from "@/router";
import { useStore } from 'vuex' // 引入useStore 方法
const store = useStore()  // 该方法用于返回store 实例
console.log(store)  // store 实例对象



let avatarURL = store.getters.avatarURL;

console.log(avatarURL)

const handleToSelfInfo = () => {
  router.push('/SelfInformation')
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
const input = ref('')
const searchBotton = ref('')
const activeIndex1 = ref('1')
const activeIndex2 = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
/* let bannerHeight = reactive({}); */
/* const mounted=()=>{
            this.imgLoad();
            window.addEventListener('resize',() => {
                this.bannerHeight=this.$refs.bannerHeight[0].height;
                this.imgLoad();
            },false)
        },
const imgLoad=()=>{
                this.$nextTick(()=>{
                    this.bannerHeight=this.$refs.bannerHeight[0].height;
                    console.log(this.$refs.bannerHeight[0].height);
                     // document.getElementsByClassName拿到的是数组并非某一个对象
                    var testH=document.getElementById("test-div");
                    testH.style.height= this.bannerHeight+"px";
               })
            },  */
</script>
<template>
  <div class="back">
    <el-container>
      <el-header class="el-header">
        <el-menu :default-active="1" class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#e9eff9" text-color="#3e5ca8" active-text-color="#2d67fd" router>
          <img src="../../assets/images/logo.png" alt="logo未加载">
          <el-menu-item index="0" class="logo">教学系统</el-menu-item>
          <div class="flex-grow" />
          <!--          <el-sub-menu index="1">-->
          <!--            <template #title>{{userName}}</template>-->
          <!--            <el-menu-item route="/SelfInformation" index="1-1">个人信息</el-menu-item>-->
          <!--          </el-sub-menu>-->
          <div style="padding-right: 12px; padding-top: 17px">
            <el-button class="avatar" @click="handleToSelfInfo">
              <el-image class="avatarImage" :src="avatarURL" />
            </el-button>
          </div>

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
                <el-icon><House /></el-icon>
              </el-menu-item>
              <el-sub-menu index="">
                <template #title>
                  <el-icon>
                    <Menu />
                  </el-icon>
                </template>
                <el-menu-item index="/CourseSelect">选课指南</el-menu-item>
                <el-menu-item index="/CourseEvaluate">课程评价</el-menu-item>
              </el-sub-menu>
              <el-menu-item index="/ScoreManage">
                <el-icon>
                  <StarFilled />
                </el-icon>
                <template #title>成绩管理</template>
              </el-menu-item>
              <el-menu-item index="/AcademicManagement">
                <el-icon>
                  <Promotion />
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

.leftMenu{
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
  font-size: 3vh;
}

.title {
  font-family: 华文楷体, serif;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  font-size: 5vh;
  margin-right: 1vh;
  margin-left: 3vh;
  color: #FFFFFF;
}

img {
  margin: 10px;
}
</style>
