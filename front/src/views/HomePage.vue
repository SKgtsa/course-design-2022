<script lang="ts" setup>
import { ref,reactive,toRefs} from 'vue'
import logo from '../assets/images/logo.png'
import first from '../assets/images/first.jpg'
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
    activeIndex1:'1'
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
            <div class="logoDiv">
            <img :src="logo" class="img" alt="logo"/>
            </div>
            <div class="search">
              <el-input 
              class="inputSearch"
              placeholder="请输入您想搜索的内容" 
              v-model="input"
              maxlength="16"
              type="text"
              @keyup.enter.native="onSubmit"
              :suffix-icon="Search"
              />
                <!-- <el-botton @click="searchBotton" ><Search /></el-botton> -->
            </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
            <el-menu router 
        default-active="/HomePage"
        active-text-color="#ffd04b"
        background-color="#003366"
        class="el-menu-vertical-demo asideMenu"
        text-color="#fff"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-menu-item index="/SelfInformation">
          <el-icon><UserFilled /></el-icon>
          <span>个人信息</span>
        </el-menu-item>
        <el-sub-menu index="">
          <template #title>
            <el-icon><Menu /></el-icon>
            <span>课程系统</span>
          </template>
            <el-menu-item index="/CourseSelect">选课指南</el-menu-item>
            <el-menu-item index="/CourseEvaluate">课程评价</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/ScoreManage">
          <el-icon><StarFilled /></el-icon>
          <span>成绩管理</span>
        </el-menu-item>
        <el-menu-item index="/Practice">
          <el-icon><Compass /></el-icon>
          <span>社会实践</span>
        </el-menu-item>
        <el-menu-item index="/Activity">
         <el-icon><Promotion /></el-icon>
          <span>课外活动</span>
        </el-menu-item>
        <el-menu-item index="/Reward">
          <el-icon><Medal /></el-icon>
          <span>成果奖励</span>
        </el-menu-item>
      </el-menu>
        </el-aside>
        <el-main>     <!-- 在这里写一个走马灯动态展示通知 -->
    <!--       <el-carousel :interval="5000" arrow="always" height="600px">
    <el-carousel-item v-for="item in 4" :key="item">
      <h3 text="2xl" justify="center">{{ item }}</h3>
    </el-carousel-item>
  </el-carousel> -->
  <router-view>
    
  </router-view>
        <!-- <div class="mainLeft">
          这里面放个人博客主页，还没有定下来博客这个东西，先不写
          </div> -->
           <!-- 展示通知 -->
        <!-- <div class="mainRight">    
         
          在这个侧边栏放通知，形式是什么，然后是否需要走马灯
        </div> -->
    </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped lang='scss'>
.back{
  margin: 0;
  padding: 0;
}
.el-header{
    padding:0 0!important;
    height: 70px!important;
    background-color:#336699;
    display: flex; //设置显示为flex布局
    justify-content: space-between;//设置为flex左右布局
}
.headerMenu{
    height: 100%;
}
.el-aside{
    height: 100vh!important;
    margin-top: 3px;
}
.el-main{
  padding: 0;
}
.mainRight{
  position:absolute;
  background-color:#CCFFFF;
  height: 100%;
  width: 300px;
  margin-top:3px;
  right: 1px;
}
.asideMenu{
    height: 100%;
}
.flex-grow{
    flex-grow: 1;
}
 .container{
     width: 100vw;
     height: 100vh;
     display:flex;
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 600px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.logoDiv{
  height: 70px!important;
  width: 35%;
}
.logoImage{
  display: inline-block;
}
.img{
  height: 65px!important;
  margin-top: 2px;
  margin-left: 2px;
  width: 280px;
 /*  margin-top: 8px;
  margin-bottom: 8px; */
}
.search{
  position: absolute;
  right: 5px;
  width: 40%;
  height: 70px;
  
}
.inputSearch{
  position: absolute;
  width: 40%;
  height: 45%;
  right: 65px;
  margin-top: 20px;
  background-color:#b1cde9;
}
.inputSearch>>>.el-input__suffix{ /*修改内部input的小图标，用检查
  来看类名 */
color: black;
}
</style>