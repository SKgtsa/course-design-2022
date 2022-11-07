<script lang="ts" setup>
import {ref, reactive, toRefs} from 'vue'
import logo from '../assets/images/logo.png'
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  UserFilled,
  Menu,
  Compass,
  StarFilled,
  Medal,
  Promotion,
  Search,
} from '@element-plus/icons-vue'

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
        <el-menu
            :default-active="activeIndex"
            class="nav-bar-top"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
            background-color="#003366"
            text-color="#FFF"
            active-text-color="#FFFF66"
        >
          <el-link class="title" href="/">
            学生管理系统
          </el-link>
          <img src="../assets/images/logo.png" alt="logo未加载">
          <el-menu-item index="0" class="logo">教学系统</el-menu-item>
          <div class="flex-grow" />
          <el-sub-menu index="1">
            <template #title>{{userName}}</template>
            <el-menu-item index="1-1">个人信息</el-menu-item>
            <el-menu-item index="1-2" disabled>临时设置</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-header>
      <el-container>
        <el-aside >
          <el-menu router
                   default-active="/HomePage"
                   active-text-color="#ffd04b"
                   background-color="#003366"
                   class="el-menu-vertical-demo asideMenu"
                   text-color="#fff"
                   @open="handleOpen"
                   @close="handleClose"
          >
            <el-sub-menu index="">
              <template #title>
                <el-icon>
                  <Menu/>
                </el-icon>
                <span>课程系统</span>
              </template>
              <el-menu-item index="/CourseSelect">选课指南</el-menu-item>
              <el-menu-item index="/CourseEvaluate">课程评价</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/ScoreManage">
              <el-icon>
                <StarFilled/>
              </el-icon>
              <span>成绩管理</span>
            </el-menu-item>
            <el-menu-item index="/Practice">
              <el-icon>
                <Compass/>
              </el-icon>
              <span>社会实践</span>
            </el-menu-item>
            <el-menu-item index="/Activity">
              <el-icon>
                <Promotion/>
              </el-icon>
              <span>课外活动</span>
            </el-menu-item>
            <el-menu-item index="/Reward">
              <el-icon>
                <Medal/>
              </el-icon>
              <span>成果奖励</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view>

          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped lang='scss'>
.back {
  margin: 0;
  padding: 0;
}

.el-header {
  padding: 0 0 !important;
  height: 80px !important;
  width: 100%;
  position: fixed;
  z-index: 9999;
}

.headerMenu {
  height: 100%;
}

.el-aside {
  height: 100vh !important;
  width: 200px;
  padding-top: 80px;
  margin-top: 3px;
  position: fixed;
  top: 0;
}

.el-main {
  padding: 0;
}

.mainRight {
  position: absolute;
  background-color: #CCFFFF;
  height: 100%;
  width: 300px;
  margin-top: 3px;
  right: 1px;
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
.nav-bar-top{
  height: 83px;
}
.flex-grow {
  flex-grow: 1;
}
.logo{
  font-weight: bold;
  font-size: 3vh;
}
.title{
  font-family: 华文楷体,serif;
  display:flex;
  justify-content:center;
  align-items:center;
  font-weight: bold;
  font-size: 5vh;
  margin-right:1vh;
  margin-left: 3vh;
  color: #FFFFFF;
}
img{
  margin: 10px;
}
</style>