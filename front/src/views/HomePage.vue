<script lang="ts" setup>
import { ref,reactive,toRefs} from 'vue'
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
let menuDataTop = reactive({
    activeIndex1:'1'
})
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
  <div>
    <el-container>
      <el-header>
        <el-menu
    :default-active="activeIndex2"
    class="el-menu-demo headerMenu"
    mode="horizontal"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    @select="handleSelect"
  >
    <el-menu-item index="0">logo</el-menu-item>
    <div class="flex-grow"/>
    <el-menu-item index="1">Processing Center</el-menu-item>
    <el-sub-menu index="2">
      <template #title>Workspace</template>
      <el-menu-item index="2-1">item one</el-menu-item>
      <el-menu-item index="2-2">item two</el-menu-item>
      <el-menu-item index="2-3">item three</el-menu-item>
      <el-sub-menu index="2-4">
        <template #title>item four</template>
        <el-menu-item index="2-4-1">item one</el-menu-item>
        <el-menu-item index="2-4-2">item two</el-menu-item>
        <el-menu-item index="2-4-3">item three</el-menu-item>
      </el-sub-menu>
    </el-sub-menu>
    <el-menu-item index="3" disabled>Info</el-menu-item>
    <el-menu-item index="4">Orders</el-menu-item>
  </el-menu>
      </el-header>
      <el-container>
        <el-aside width="200px">
            <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo asideMenu"
        default-active="2"
        text-color="#fff"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            课程系统
          </template>
          <el-menu-item-group title="">
            <el-menu-item index="1-1">item one</el-menu-item>
            <el-menu-item index="1-2">item two</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="Group Two">
            <el-menu-item index="1-3">item three</el-menu-item>
          </el-menu-item-group>
          <el-sub-menu index="1-4">
            <template #title>item four</template>
            <el-menu-item index="1-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-menu-item index="2">
          <span>社会实践</span>
        </el-menu-item>
        <el-menu-item index="3">
          <el-icon><document /></el-icon>
          <span>Navigator Three</span>
        </el-menu-item>
        <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <span>Navigator Four</span>
        </el-menu-item>
      </el-menu>
        </el-aside>
        <el-main>     <!-- 在这里写一个走马灯动态展示通知 -->
            <el-carousel :interval="4000" type="card" height="400px">
    <el-carousel-item  :key="1">
        <!-- <img src="../assets/images/first.jpg" fit="fill"> -->
    </el-carousel-item>
    <el-carousel-item :key="2">
       <!-- <img src="../assets/images/second.jpg" fit="fill"> -->
    </el-carousel-item>
    <el-carousel-item :key="3">
   <!--      <img src="../assets/images/third.jpg" fit="fill"> -->
    </el-carousel-item>
    <el-carousel-item :key="4">
        <!-- <img src="../assets/images/fourth.jpg" fit="fill"> -->
    </el-carousel-item>
   
  </el-carousel>
    </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped lang='scss'>
.el-header{
    padding:0 0!important;
    height: 70px!important;
}
.headerMenu{
    height: 100%;
}
.el-aside{
    height: 100vh!important;
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
</style>