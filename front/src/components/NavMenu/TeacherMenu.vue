<template>
  <el-menu-item index="/Teacher/Main"   @click="closeMenu">
    <template #title>首页</template>
    <el-icon class="menuButton">
      <HomeFilled />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Teacher/CourseManage"  @click="closeMenu">
    <template #title>课程管理</template>
    <el-icon class="menuButton">
      <Document />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Teacher/ScoreManage"  @click="closeMenu">
    <template #title>成绩管理</template>
    <el-icon class="menuButton">
      <HomeFilled />
    </el-icon>
  </el-menu-item>
  <el-menu-item :index=" '/Teacher/SelfInformation?userId=' + getUserId()" @click="closeMenu">
    <template #title>教师主页</template>
    <el-icon class="menuButton">
      <Avatar />
    </el-icon>
  </el-menu-item>
</template>

<script lang="ts" setup>
import {getUserId, menuDrawerOpen} from "@/global/global";

const closeMenu = () => {
  menuDrawerOpen.value = false;
}
</script>

<style scoped>
.menuButton{
  font-size: 9vh;
}
</style>
