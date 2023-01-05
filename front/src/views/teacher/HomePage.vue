<template>
  <div class="back">
    <el-container>
      <!--桌面端的顶部操作栏-->
      <el-header class="el-header" v-if="!mobile">
        <el-menu class="nav-bar-top" mode="horizontal" :ellipsis="false" @select="handleSelect"
          background-color="#f0f2ff" text-color="#3e5ca8" active-text-color="#2d67fd" router>
          <img src="../../assets/images/logo.png" alt="logo未加载">
          <el-menu-item class="logo" index="/Main">教学系统</el-menu-item>
          <div class="flex-grow" />
          <div class="nick">
              <avatar-operate/>
          </div>
        </el-menu>
      </el-header>
      <!--移动端的顶部操作栏-->
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
      </el-header>
      <el-container>
        <el-main class="mainWindow">
          <!--桌面端的右侧导航栏-->
          <div v-if="!mobile" class="rightWindow">
            <el-menu router default-active="/Teacher/Main" active-text-color="#2d67fd" background-color="rgba(0,0,0,0)"
              class="teacherRightMenu" text-color="#3e5ca8" @open="handleOpen" @close="handleClose"
              :collapse="true">
              <teacher-menu/>
            </el-menu>
          </div>
          <div class="leftMenu" :style="{
            'width': `${mobile? '100%':'97%'}`
          }">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>

  <!--移动端的右侧菜单-->
  <el-drawer with-header="false" v-model="menuDrawerOpen" :direction="'rtl'" :size="'90%'" z-index="50">
    <avatar-operate/>
    <!--菜单区域-->
    <el-menu router default-active="/Teacher/Main" active-text-color="#2d67fd" background-color="#e9eff9"
      text-color="#3e5ca8" @open="handleOpen" @close="handleClose">
      <teacher-menu/>
    </el-menu>
  </el-drawer>
</template>
<script lang="ts" setup>
import { ref, reactive, toRefs } from 'vue'
import {
  getAvatarURL,
  getNickName,

  mobile
} from '../../global/global';
import { RouterView } from 'vue-router';
import TeacherMenu from "@/components/NavMenu/TeacherMenu.vue";
import AvatarOperate from "@/components/AvatarOperate.vue";

const menuDrawerOpen = ref(false);

const openMenu = () => {
  menuDrawerOpen.value = true;
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
.teacherRightMenu{
  width: 6.5vh;
}
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
  /*  margin-top: 1vh;
   margin-bottom: 1vh; */
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

