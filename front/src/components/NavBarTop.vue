<template>
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
    <div class="title">
      学生管理系统
    </div>
    <img src="../assets/images/logo.png" alt="logo未加载">
    <el-menu-item index="0" class="logo">教学系统</el-menu-item>
    <div class="flex-grow" />
    <el-sub-menu index="1">
      <template #title>{{userName}}</template>
      <el-menu-item index="1-1">个人信息</el-menu-item>
      <el-menu-item index="1-2" disabled>临时设置</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script lang="ts" setup>

import {ref} from "vue";
import router from "@/router";

const userName = localStorage.getItem('userName')
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[])=> {
  switch (key){
    case "0":
      router.push("/")
      break;
    case "1-1":

      break;
    case "1-2":

      break;
  }
}
</script>

<style scoped>
.nav-bar-top{
  height: 10vh;
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
  margin-left: 5vh;
  color: #FFFFFF;
}
img{
margin: 10px;
}
</style>
