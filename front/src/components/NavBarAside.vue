<template>

  <el-menu router
           default-active="/HomePage"
           active-text-color="#FFFF66"
           background-color="#3399CC"
           class="nav-bar-aside"
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
</template>

<script lang="ts" setup>
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>

<style scoped>
.nav-bar-aside{
  height: 93vh;
}
</style>
