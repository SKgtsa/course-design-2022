<template>
  <el-menu-item index="/Manager/Main" @click="closeMenu">
    <template #title>博客主页</template>
    <el-icon class="menuButton">
      <HomeFilled />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Manager/Notice" @click="closeMenu">
    <template #title>通知管理</template>
    <el-icon class="menuButton">
    <BellFilled />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Manager/InfoManage" @click="closeMenu">
    <template #title>信息管理</template>
    <el-icon class="menuButton"><UserFilled /></el-icon>
  </el-menu-item>

  <el-menu-item index="/Manager/CourseManage" @click="closeMenu">
    <el-icon class="menuButton"><Notebook /></el-icon>
    <template #title>课程管理</template>
  </el-menu-item>
  <el-menu-item index="/Manager/CourseTime" @click="closeMenu">
    <el-icon class="menuButton">
      <Timer />
    </el-icon>
    <template #title>课程时间</template>
  </el-menu-item>
  <el-menu-item index="/Manager/RegisterStudents" @click="closeMenu">
    <el-icon  class="menuButton"><FolderOpened /></el-icon>
    <template #title>批量注册</template>
  </el-menu-item>
</template>

<script lang="ts" setup>

import {menuDrawerOpen} from "@/global/global";

const closeMenu = () => {
  menuDrawerOpen.value = false;
}
</script>

<style scoped>
.menuButton{
  font-size: 9vh;
}
</style>
