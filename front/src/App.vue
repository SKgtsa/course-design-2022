<script setup lang="ts">
import NavBarTop from "@/components/NavBarTop.vue";
import NavBarAside from "@/components/NavBarAside.vue";
import router from "@/router";


</script>

<template>
<!--   <div>
    <el-container>
      <el-header class="header" style="padding: 0; margin: 0; height: 100%; width: 100%">
        <nav-bar-top/>
      </el-header>
      <el-container>
        <el-aside class="aside">
          <nav-bar-aside/>
        </el-aside>
        <el-main> -->
          <router-view/>
 <!--        </el-main>
      </el-container>
    </el-container>
  </div> -->
</template>

<style scoped>
html,body,el-container,el-main,el-footer,el-aside,router-view{
  /*设置内部填充为0，几个布局元素之间没有间距*/
  padding: 0px;
  /*外部间距也是如此设置*/
  margin: 0;
  /*统一设置高度为100%*/
  height: 100%;
  width: 100vw !important;
}
</style>
