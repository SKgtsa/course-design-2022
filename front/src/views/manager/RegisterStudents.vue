<template>
  <div class="content" :style="{
        'padding-top': `${mobile ? '5' : '2'}vh`,
        'height': `${mobile ? 'auto' : '91vh'}`
    }">
    <div class="pageContent" :style="{
            'width': `${mobile ? 80 : 60}%`,
        }">
      <div class="pageTitle" style="padding-top: 6vh">
        <a class="title" :style="{
                    'padding-left': `${mobile ? '7' : '20'}vw`,
                }">学生批量注册</a>
      </div>
      <el-divider/>
      <div style="padding-top: 3vh">
        <a style="font-size: 3vh;font-family: 微软雅黑;color: gray">请下载批量注册模板，按照模板填写后并上传</a>
        <el-button size="large" type="primary" icon="download" style="margin: 0 3%;font-size: 2.5vh"
                   class="checkDownloadButton" plain @click="download">
          批量注册模板下载
        </el-button>
      </div>
      <div style="padding-top: 6vh;">
        <el-upload ref="upload" drag action="#" :http-request="uploadFile" :limit="1">
          <el-icon class="el-icon--upload" :auto-upload="true">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            将文件拖曳到此处
<!--            <em>点击上传</em>-->
          </div>
          <template #tip>
            <div class="el-upload__tip">
            </div>
          </template>
        </el-upload>
        <el-upload style="float: right" ref="upload" :http-request="(file) => {
                    return uploadFile(file);
                  }" :limit="1" :auto-upload="true">
          <el-button icon="upload" type="primary" size="large" style="font-size: 2.5vh;margin-top: 5vh">上传</el-button>
        </el-upload>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {mobile} from '@/global/global';
import serviceFile from '@/request';
import {hideLoading, showLoading} from '@/utils/loading';
import {messageError, messageSuccess} from '@/utils/message';
import {loginFailed} from '@/utils/tokenCheck';
import {ref} from 'vue';

const download = () => {
    window.location.href =
        'https://courseback.clankalliance.cn/static/inbuild/%E5%AD%A6%E7%94%9F%E6%89%B9%E9%87%8F%E6%B3%A8%E5%86%8C%E6%A8%A1%E6%9D%BF.xlsx';
}

let upload = ref();

const uploadFile = (f) => {
  console.log(f);
  showLoading();
  console.log(serviceFile)
  serviceFile.post('/api/user/batchRegister', { token: localStorage.getItem('token'), file: f.file},{
    headers: {
      "Content-Type": "multipart/form-data"
    }
  }).then(res => {
      let data = res.data;
      console.log(res)
      if (data.success) {
          hideLoading();
          messageSuccess('上传成功！')
          localStorage.setItem('token',data.token);
      } else {
          hideLoading();
          loginFailed();
      }
  })
  .catch(function (error) {
    console.log(error)
    hideLoading();
    messageError("服务器开小差了呢");
    loginFailed();
  })
}


</script>
<style lang="scss" scoped>
.pageTitle {
  .title {
    padding-top: 8vh;
    height: 6vh;
    font-family: 微软雅黑;
    font-size: 5vh;
    font-weight: 500;
    line-height: 1vh;
    color: #0273f1;
  }
}


.selectTimeBox {
  padding-top: 10vh;
  height: 50vh;
  width: 60vw;
}

.selectTime {
  height: 30vh;
}

.content {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: repeat;

  .pageContent {
    height: 80vh;
    border-radius: 3vw;
    padding-left: 2vw;
    padding-top: 3vh;
    padding-right: 2vw;
    background-color: #FFFFFF;
    box-shadow: 0 0 1.25vh 0 #b9ccee;
    margin: 0 auto;
  }
}
</style>
