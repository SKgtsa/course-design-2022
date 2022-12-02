<template>
<!--  <div class="content">-->
<!--    <div>-->
<!--      <div class="blogTitle">-->
<!--        <img class="img" src="../assets/images/blogLogo.png" alt="未加载">-->
<!--        博客圈——分享你的学习生活-->
<!--      </div>-->
<!--      <div class="right">-->
<!--&lt;!&ndash;        右侧信息栏&ndash;&gt;-->
<!--      </div>-->
<!--      <div class="blogContent">-->
<!--        <div class="blogSend">-->
<!--          <div>-->
<!--            <TEditor ref="editor" v-model="formState.content" :disabled='disabled' @getContent="getContent"/>-->
<!--          </div>-->
<!--          <div>-->
<!--            <el-button @click="submitBlog" class="blogButton">发送</el-button>-->
<!--          </div>-->
<!--        </div>-->
<!--        <div>-->
<!--          &lt;!&ndash;看博客模块&ndash;&gt;-->

<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--  </div>-->
  <div class="main">
    <div class="leftWindow">
      <div class="panel">
        <div class="panelCard">
          <el-button class="writeButton">
            <el-image/>
            <a>写博客</a>
          </el-button>
          <div style="height: 2vh;padding-top: 1vh">
            <div style="width: 85%;height: 2px;margin: auto;background-color: #29426d"/>
          </div>
          <div style="margin: 0 auto">
            <el-button class="squareButton">
              <a>广场</a>
            </el-button>
            <el-button class="myInfoButton">
              <a>我的博客</a>
            </el-button>
          </div>
        </div>
      </div>
      <div class="notice">
        <el-carousel  class="noticeCard" direction="vertical" :autoplay="true">
          <el-carousel-item v-for="item in imageList" :key="item">
            <el-image :src="item" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="rightWindow">
      <el-container class="operationCard">

      </el-container>
    </div>
  </div>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import TEditor from '@/components/TEditor.vue';
const blog = ref('')
const testURL = 'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp';
const imageList = [
  'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp',
  'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg'
]
const bolgButton = () => {
  axios.post('blog/submit' , {token: localStorage.getItem("token") , content: blog.value}).then(res => {
    const data = res.data;
    if(data.success){
      blog.value = '';
      ElMessage({
        message: '发送成功',
        type: 'success'
      })
      localStorage.setItem('token',data.token)
      //用新token向后端要新的blog列表并更新显示
    }else{

      ElMessage({
        message: data.message,
        type: 'error'
      })
    }
  })
}
const formState = reactive({contents :''})
const getContent = (v: string) => {
  formState.contents = v
}
</script>

<style scoped>

.main{
  display: flex;
  height: 100vh;
}
.leftWindow{
  display: flex;
  flex-direction: column;
  width: 18vw;
}
.panel{
  padding-top: 4vh;
  padding-left: 2vw;

}
.notice{
  padding-top: 2vh;
  padding-left: 2vw;
}
.panelCard{
  width: 90%;
  height: 40vh;
  background-color: #FFFFFF;
  border-radius: 1vw;
  box-shadow: 0 0 10px 0 #b9ccee;
  display: flex;
  flex-direction: column;
  padding-top: 2vh;
}
.writeButton{
  margin: 0 auto;
  width: 90%;
  height: 8vh;
  background-color: rgba(30,30,30,0.8);
  color: #FFFFFF;
  font-size: 3vh;
  text-align: left;
  font-weight: 500;
  border-radius: 1vw;
}
.squareButton{
  margin: 0 auto;
  width: 100%;
  padding-left: 3vw;
  padding-right: 3vw;
  height: 5vh;
  border-style: none;
}
.myInfoButton{
  margin: 0 auto;
  width: 100%;
  padding-left: 3vw;
  padding-right: 3vw;
  height: 5vh;
  border-style: none;
}
.noticeCard{
  background-color: #FFFFFF;
  width: 90%;
  height: 40vh;
  border-radius: 1vw;
  box-shadow: 0 0 10px 0 #b9ccee;
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  position: absolute;
  text-align: center;
}

.rightWindow{
  padding: 5vh 1vw 2vh 2vw;

}
.operationCard{
  background-color: #FFFFFF;
  width: 75vw;
  height: 80vh;
  border-radius: 2vw;
  box-shadow: 0 0 10px 0 #b9ccee;
}

.right{
  width: 30vh;
  background-color: #cccccc;
  height:100vh;
  position: sticky;
  float: right;
  z-index: 0;
  border-radius: 20px;
}
.content{
  padding-top: 80px;
  padding-left: 200px;

}
.blogSend{
  padding-right: 30vh;
  height: 150vh;
}
.blogButton{
  width: 100%;
}
.blogTitle{
  font-family: 华文楷体,serif;
  color: #79bbff;
  display:flex;
  justify-content:center;
  align-items:center;
  font-weight: bold;
  font-size: 5vh;
  margin-right: 30vh;
}
.img{
  height: 100px !important;
  margin-top: 2px;
  margin-right: 35px;
}
.blogContent{
  margin-left: 50px;
  margin-right: 50px;
}

</style>
