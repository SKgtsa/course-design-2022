<template>
  <el-menu-item index="/Student/Main"  @click="closeMenu">
    <template #title>首页</template>
    <el-icon class="menuButton">
      <HomeFilled />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Student/CourseSelect"  @click="closeMenu">
    <template #title>选课指南</template>
    <el-icon class="menuButton">
      <Document />
    </el-icon>
  </el-menu-item>
  <el-menu-item index="/Student/ScoreManage"  @click="closeMenu">
    <el-icon class="menuButton">
      <DocumentChecked />
    </el-icon>
    <template #title>成绩管理</template>
  </el-menu-item>
  <el-menu-item index="/Student/Activity"  @click="closeMenu">
    <el-icon class="menuButton">
      <Sunrise />
    </el-icon>
    <template #title>课外活动</template>
  </el-menu-item>
  <el-menu-item index="/Student/Practice"  @click="closeMenu">
    <el-icon class="menuButton">
      <Briefcase />
    </el-icon>
    <template #title>社会实践</template>
  </el-menu-item>
  <el-menu-item index="/Student/Reward"  @click="closeMenu">
    <el-icon class="menuButton">
      <TrophyBase />
    </el-icon>
    <template #title>成果奖励</template>
  </el-menu-item>
  <el-menu-item index="/Student/Classmate"  @click="closeMenu">
    <el-icon class="menuButton">
      <UserFilled />
    </el-icon>
    <template #title>我的同学</template>
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
