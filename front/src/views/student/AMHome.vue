<template>
    <el-carousel class="carouselPage" height="100%" interval="4000">
      <el-carousel-item class="carouselItem" >
        <img style="width: 100%; height: 100%" src='../../assets/images/榜样的力量.jpg' />
      </el-carousel-item>
      <el-carousel-item class="carouselItem" >
        <img style="width: 100%; height: 100%" src='../../assets/images/音乐会.jpg' />
      </el-carousel-item>
      <el-carousel-item class="carouselItem" >
        <img style="width: 100%; height: 100%" src='../../assets/images/志愿者日.jpg' />
      </el-carousel-item>
      <el-carousel-item class="carouselItem" >
        <img style="width: 100%; height: 100%" src='../../assets/images/嫦娥上天.jpg' />
      </el-carousel-item>
      <el-carousel-item class="carouselItem" >
        <img style="width: 100%; height: 100%" src='../../assets/images/雪景.jpg' />
      </el-carousel-item>
    </el-carousel>
    <!-- v-for="item in urls" :key="item" -->
</template>
<script lang="ts" setup>
import { reactive,ref } from 'vue';
</script>
<style lang="scss" scoped>
.carouselPage{
    height: 100%;
    width: 100%;
    .carouselItem{
        height: 100%;
        width: 100%;
        .img{
            height:100%;
            width: 100%;
        }
    }
}
.el-carousel__container{
    height: 100%;
}
</style>