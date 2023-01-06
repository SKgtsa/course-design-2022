<template>
<!--信息补充页-->
  <el-carousel
      indicator-position="none"
      :autoplay="false"
      arrow="never"
      :loop="false"
      class="carouselWindow"
  >
    <el-carousel-item class="carouselItem">
      <div class="avatarCard">
        <div class="avatar">
          <el-upload action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
                     :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
            <img v-if="getAvatarURL()" :src="getAvatarURL()" :style="{
            'width': `${mobile? '60vw':'30vh'}`,
            'height': `${mobile? '60vw':'30vh'}`,
            'border-radius': `${mobile? '30vw':'15vh'}`,
            'margin': `auto`,
          }" />
          </el-upload>
        </div>
        <a class="notice">请选择您的头像</a>
      </div>
    </el-carousel-item>
    <el-carousel-item class="carouselItem">
      <div class="avatarCard">
        <div class="avatar">
          <el-upload action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
                     :http-request="uploadPhoto" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
            <img v-if="getAvatarURL()" :src="getAvatarURL()" :style="{
            'width': `${mobile? '60vw':'30vh'}`,
            'height': `${mobile? '60vw':'30vh'}`,
            'border-radius': `${mobile? '30vw':'15vh'}`,
            'margin': `auto`,
          }" />
          </el-upload>
        </div>
        <a class="notice">请选择您的照片</a>
      </div>
    </el-carousel-item>
  </el-carousel>
</template>

<script lang="ts" setup>

// 上传图片前的过滤
import {messageError, messageSuccess} from "@/utils/message";
import {hideLoading, showLoading} from "@/utils/loading";
import serviceFile from "@/request/indexFile";
import {getAvatarURL, getBaseURL, mobile} from "@/global/global";
import {ref} from "vue";
import service from "@/request";

const photoURL = ref('')
const character = ref(0)

let uploadImg = async (f) => {
  showLoading();
  serviceFile.post('/api/user/changeAvatar', { avatar: f.file, token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      hideLoading();
      localStorage.setItem('token', data.token);
      let url = getBaseURL() + data.content;
      console.log(url);
      localStorage.setItem('avatarURL', url);

      messageSuccess("更换成功！")
    } else {
      hideLoading();
      messageError(data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}

const getUserInfo = () => {
  showLoading();
  service.post('/api/user/myInfo',{token: localStorage.getItem('token')}).then(res => {
    let data = res.data;
  })
}

let uploadPhoto = async (f) => {
  showLoading();
  serviceFile.post('/api/user/changePhoto', { photo: f.file, token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      hideLoading();
      localStorage.setItem('token', data.token);
      let url = getBaseURL() + data.content;
      console.log(url);
      messageSuccess("更换成功！")
    } else {
      hideLoading();
      messageError(data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}

let beforeAvatarUpload = (file) => {
  const isLt2M = (file.size / 1024 / 1024) < 2
  if (!isLt2M) {
    messageError('上传图片大小不能超过 2MB!')
  }
  return isLt2M
}

</script>

<style scoped>
.carouselWindow{
  width: 100%;
  height: 100vh;
}
.carouselItem{
  width: 100%;
  height: 100vh;
}
.avatarCard{
  background-color: #0a8ce2;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.avatar{
  margin: 3% auto;
}
.notice{
  font-size: 5vh;
  color: #FFFFFF;
  text-align: center;
  font-weight: bold;
}
</style>
