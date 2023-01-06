<template >
  <div class="mainPage">
    <el-header class="el-header">
      <img class="logo" src="../../assets/JiangImages/在哲logo.png">
      <div class="title">
        <a class="titleFont">HomePage</a>
      </div>
      <el-menu class="menu">
        <el-menu-item class="item">
          <a>个人简介</a>
        </el-menu-item>
      </el-menu>
      <el-menu class="menu">
        <el-menu-item class="item">
          <a>学生工作</a>
        </el-menu-item>
      </el-menu>
      <el-menu class="menu">
        <el-menu-item class="item">
          <a>学习日常</a>
        </el-menu-item>
      </el-menu>
    </el-header>
    <div>
      <el-carousel :interval="4000" indicator-position="outside" pause-on-hover arrow="never" type="card"
                   class="carousel">
        <el-carousel-item class="carouselPage">
          <img class="carouselPhoto" src="../../assets/JiangImages/权益中心合照.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPage">
          <img class="carouselPhoto" src="../../assets/JiangImages/权益中心合照.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPage">
          <img class="carouselPhoto" src="../../assets/JiangImages/权益中心合照.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPage">
          <img class="carouselPhoto" src="../../assets/JiangImages/权益中心合照.jpg">
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>

</template>

<script lang="ts" setup>
import {reactive, ref} from 'vue';
import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import {messageError, messageSuccess, messageWarning} from "@/utils/message";
import {ElMessageBox} from "element-plus";
import {getBaseURL} from "@/global/global";
</script>

<style scoped>
.mainPage{
  background-color: white;
}
.logo{
  padding-top: 1.5vh;
  height: 9vh;
  width: auto;
  object-fit: cover;
  float: left;
}
.title{
  height: 12vh;
  padding-top: 2vh;
  float: left;
  width: 20vh;
}
.titleFont{
  font-size: 6vh;
  padding-left: 3vh;
  font-family: Arial,serif;
  color: white;
  font-weight: bold;
}
.menu{
  float: right;
  height: 12vh;
  padding-top: 2vh;
  width: auto;
  background-color: gray;
}
.item{
  float: right;
}
.carousel {
  height: 80vh;
  margin-top: 0;
}

.carouselPage {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.carouselPhoto {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
}
.el-header{
  background-color: gray;
  height: 12vh;
}
</style>
