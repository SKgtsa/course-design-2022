<script setup lang="ts">


</script>

<template>
          <router-view/>
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
