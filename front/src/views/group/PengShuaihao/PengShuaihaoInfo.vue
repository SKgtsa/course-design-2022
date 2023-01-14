<template>
 <!--       <div class="selftBox" v-if="!mobile"> -->
      <particles v-if="!mobile">
      </particles>
      <div class="selfInfoCard" v-if="!mobile">
        <img style="height:25%;width:20%;padding-left: 20vw;padding-top: 2vh;"
          src='https://courseback.clankalliance.cn/static/inbuild/psh/头像.jpg' />
          <el-carousel :autoplay="false" class="caouselInfo" height="40vh" arrow="always">
      <el-carousel-item style="display: flex;flex-direction:column;text-align: center;">
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>姓名：彭帅豪</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>性别：男</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>政治面貌：共青团员</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>QQ：859455675</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>邮箱：859455675@qq.com</a></span>
      </el-carousel-item>
      <el-carousel-item style="text-align:center;flex-direction:column;" >
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a style="font-size:2vw;">获奖经历</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2021山东大学程序设计新生赛二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022山东大学程序设计高年级组二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022山东大学数学建模竞赛三等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022数维杯国际大学生数学建模挑战赛二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>参与一项国家级大创</a></span>
      </el-carousel-item>
      <el-carousel-item>
        <div>
          <a>这个网页命名为“烟火一格”，为风景展示的板块，加入了个人介绍的板块；网页支持PC端和移动端，
          做了两种布局效果，无论您使用电脑还是手机预览，都能都到很好的体验，网页PC端采用顶部网页名+图片渐变渲染，
        中间是导航栏，下面是走马灯，主干内容为悬浮卡片；点击走马灯图片会更换为卡片图片，介绍有滚动条，介绍部分不长，滚动条PC端体验不明显，移动端体验较明显；介绍上方是个蓝色的按钮可以点击直接回到主页，
        也可以通过导航栏，个人介绍部分引入了particles组件，封装在components组件下的particles文件夹，效果即为这个个人介绍的背景，您可以通过拖动鼠标或者点击，来与背景进行交互，以上为网页的全部内容。</a>
        </div>
        <el-button @click="toMain" style="margin-left:22vw;margin-top:6vh;background-color:rgba(70, 66, 101)"><a>回到主页</a></el-button>
      </el-carousel-item>
    </el-carousel>
      </div>
<!--     </div> -->
    <div style="height:80vh;width:80vw" v-if="mobile">
      <particles>
      </particles>
      <div class="selfInfoCard" style="height:90vh;width:80vw;margin-left: 8vw;margin-top: 3vh;">
        <img style="height:35%;width:60%;padding-left: 15vw;padding-top: 2vh;"
          src='https://courseback.clankalliance.cn/static/inbuild/psh/头像.jpg' />
          <el-carousel :autoplay="false" class="caouselInfo" height="60vh" arrow="always">
      <el-carousel-item style="display: flex;flex-direction:column;text-align: center;">
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>姓名：彭帅豪</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>性别：男</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>政治面貌：共青团员</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>QQ：859455675</a></span>
        <span style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>邮箱：859455675@qq.com</a></span>
      </el-carousel-item>
      <el-carousel-item style="text-align:center;flex-direction:column;" >
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>获奖经历</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2021山东大学程序设计新生赛二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022山东大学程序设计高年级组二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022山东大学数学建模竞赛三等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>2022数维杯国际大学生数学建模挑战赛二等奖</a></span>
        <span  style="display:flex;flex-direction:column;padding-bottom:3vh;"><a>参与一项国家级大创</a></span>
      </el-carousel-item>
      <el-carousel-item>
        <div>
          <a>这个网页命名为“烟火一格”，为风景展示的板块，加入了个人介绍的板块；网页支持PC端和移动端，
          做了两种布局效果，无论您使用电脑还是手机预览，都能都到很好的体验，网页PC端采用顶部网页名+图片渐变渲染，
        中间是导航栏，下面是走马灯，主干内容为悬浮卡片；点击走马灯图片会更换为卡片图片，介绍有滚动条，介绍部分不长，滚动条PC端体验不明显，移动端体验较明显；介绍上方是个蓝色的按钮可以点击直接回到主页，
        也可以通过导航栏，个人介绍部分引入了particles组件，封装在components组件下的particles文件夹，效果即为这个个人介绍的背景，您可以通过拖动鼠标或者点击，来与背景进行交互，以上为网页的全部内容。</a>
            <el-button @click="toMain" style="margin-left:20vw;margin-top:5vh;background-color:rgba(70, 66, 101)"><a>回到主页</a></el-button>
        </div>
      </el-carousel-item>
    </el-carousel>
      </div>
    </div>
</template>
<script lang="ts" setup>
import Particles from '@/components/particles/index.vue'
import particles from '@/components/particles/particles.js';
import { mobile } from '@/global/global';
import router from '@/router';

router.afterEach((to,from)=>{
        window.location.reload();
})
const toMain = () =>{
    router.push('/PengShuaihao');
}
</script>
<style lang="scss" scoped>
  .selftBox {
    position: relative;
    height: 100vh;
  }
  .selfInfoCard{
  z-index: 5;
  position:relative;
  margin-left: 25vw;
  margin-top:15vh;
  width: 50vw;
  height: 65vh;
  background-color:rgba(70, 66, 101, 0.4);
  border-radius: 2vw;
  .caouselInfo{
    padding-top: 4vh;
  }
}


</style>