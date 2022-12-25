<template>
  <div class="main">
    <div class="leftWindow">
      <div class="panel">
        <div class="panelCard">
          <el-button class="writeButton">
            <el-image />
            <a>写博客</a>
          </el-button>
          <div style="height: 2vh;padding-top: 1vh">
            <div style="width: 85%;height: 2px;margin: auto;background-color: #29426d" />
          </div>
          <div class="buttonBox">
            <el-button class="opButton">
              <a>广场</a>
            </el-button>
            <el-divider class="divider" />
            <el-button class="opButton">
              <a>我的博客</a>
            </el-button>
            <el-divider class="divider" />
            <el-button class="opButton">
              <a>用户画像</a>
            </el-button>
            <el-divider class="divider" />
          </div>
        </div>
      </div>
      <div class="notice">
        <el-carousel class="noticeCard" direction="vertical" :autoplay="true">
          <el-carousel-item v-for="item in imageList" :key="item">
            <el-image :src="item" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="rightWindow">
      <el-container class="operationCard">
        博客
      </el-container>
    </div>
  </div>
</template>

<script lang="ts" setup>

import { reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import TEditor from '@/components/TEditor.vue';
const blog = ref('')
const testURL = 'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp';
const imageList = [
  'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp',
  'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
]
const bolgButton = () => {
  axios.post('blog/submit', { token: localStorage.getItem("token"), content: blog.value }).then(res => {
    const data = res.data;
    if (data.success) {
      blog.value = '';
      ElMessage({
        message: '发送成功',
        type: 'success'
      })
      localStorage.setItem('token', data.token)
      //用新token向后端要新的blog列表并更新显示
    } else {

      ElMessage({
        message: data.message,
        type: 'error'
      })
    }
  })
}
const formState = reactive({ contents: '' })
const getContent = (v: string) => {
  formState.contents = v
}
</script>

<style scoped lang="scss">
.main {
  display: flex;
  height: 100vh;

  .leftWindow {
    display: flex;
    flex-direction: column;
    width: 21vw;
    padding-left: 0vh;

    .panel {
      padding-top: 5vh;
      padding-left: 2vw;

      .panelCard {
        width: 90%;
        height: 40vh;
        background-color: #FFFFFF;
        border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee;
        display: flex;
        flex-direction: column;
        padding-top: 2vh;

        .writeButton {
          margin: 0 auto;
          width: 90%;
          height: 8vh;
          background-color: rgba(30, 30, 30, 0.8);
          color: #FFFFFF;
          font-size: 3vh;
          text-align: left;
          font-weight: 500;
          border-radius: 1vw;
        }

        .buttonBox {
          width: 100%;
          height: 100%;
          display: flex;
          flex-direction: column;

          /* .squareButton {
            border-radius:5px 5px 0 0;
            margin: 0 auto;
            width: 100%;
            height: 8vh;
            background-color: #0a55cc;
            color: #FFFFFF;
            padding-left: 3vw;
            padding-right: 3vw;
            height: 5vh;
            border-style: none;
          }

          .myInfoButton {
            margin: 0 auto;
            width: 100%;
            background-color: #afc8f1;
            padding-left: 3vw;
            padding-right: 3vw;
            height: 5vh;
            border-style: none;
          } */
          .opButton {
            justify-content: center;
            height: 5vh;
            color: #41339b;
            background: transparent;
            /* 边框样式、颜色、宽度 */
            /*      border-style: none; */
            border-bottom: 1px solid;
            /* 给边框添加圆角 */
            /*  border-radius: 2px; */
            border: none;
            text-align: center;
            display: inline-block;
            font-size: 19px;
            font-family:华文楷体;
            font-weight: bold;
            -webkit-transition-duration: 0.4s;
            /* Safari */
            transition-duration: 0.4s;
            cursor: pointer;
            text-transform: uppercase;
          }

          .opButton:hover {
            background-color: #0a55cc;
            color: #f3fa26;
          }

          .divider {
            width: 90%;
            justify-content: center;
            margin: 0px !important;
            padding-left:2vw !important;;
          }
        }
      }
    }

    .notice {
      padding-top: 2vh;
      padding-left: 2vw;

      .noticeCard {
        background-color: #FFFFFF;
        width: 90%;
        height: 30vh;
        border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee;
      }
    }
  }

  .rightWindow {
    padding: 5vh 1vw 2vh 2vw;

    .operationCard {
      background-color: #FFFFFF;
      width: 70vw;
      height: 80vh;
      border-radius: 2vw;
      box-shadow: 0 0 10px 0 #b9ccee;
    }

  }
}
</style>
