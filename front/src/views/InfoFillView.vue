<template>
<!--信息补充页-->
  <el-carousel
      indicator-position="none"
      :autoplay="false"
      arrow="never"
      :loop="false"
      class="carouselWindow"
      ref="carousel"
  >
    <el-carousel-item class="carouselItem">
      <div class="avatarCard" style="background-color: #D5DEEF;color: rgba(69,82,122)">
        <a class="noticeBig" :style="{
          'text-align': `${mobile? 'center':'left'}`
        }">我们需要收集一些您的信息</a>
        <div class="avatar">
          <el-upload action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
                     :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
            <img :src="getAvatarURL()" :style="{
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
      <div class="avatarCard" style="background-color: #B2CAEE;color: rgba(69,97,146)">
        <a  class="noticeBig" :style="{
          'text-align': `${mobile? 'center':'left'}`
        }">我们需要收集一些您的信息</a>
        <div class="avatar">
          <el-upload action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
                     :http-request="uploadPhoto" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
            <img :src="photoURL" :style="{
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
    <el-carousel-item class="carouselItem">
      <div class="avatarCard" style="background-color: #89AFE0;color: rgba(30,67,119)">
        <a  class="noticeBig" :style="{
          'text-align': `${mobile? 'center':'left'}`
        }">马上就好啦</a>
        <div v-if="character == 0"  class="inputArea" :style="{
          'flex-direction': `${mobile? 'column': 'row'}`
        }">
          <a>请输入您的昵称</a>
          <el-input v-model="nickName" :style="{
            'width': `${mobile? 80:40}%`
          }"/>
        </div>
        <div v-if="character == 1" class="inputArea" :style="{
          'flex-direction': `${mobile? 'column': 'row'}`
        }">
          <a>请输入您的科研方向</a>
          <el-input v-model="researchDirection"  :style="{
            'width': `${mobile? 80:40}%`,
            'font-size': '3vh',
            'height': '6vh'
          }"/>
        </div>
        <div class="buttonArea">
          <el-button class="submitButton" style="color: rgba(30,67,119);" @click="submitFillIn">确认<el-icon><ArrowRightBold /></el-icon></el-button>
        </div>
      </div>
    </el-carousel-item>
    <el-carousel-item class="carouselItem">
      <div class="avatarCard" style="background-color: #3A5985;color: #fff">
        <a  class="noticeBig" :style="{
          'text-align': `${mobile? 'center':'left'}`,
          'padding-top':'3vh'
        }">大功告成</a>
        <a class="notice">开启你的学习生活</a>
        <el-button class="submitButton" style="color: #fff;" @click="jump"><el-icon><ArrowRightBold /></el-icon></el-button>
      </div>
    </el-carousel-item>
  </el-carousel>
</template>

<script lang="ts" setup>

// 上传图片前的过滤
import {messageError, messageSuccess} from "@/utils/message";
import {hideLoading, showLoading} from "@/utils/loading";
import serviceFile from "@/request/indexFile";
import {getAvatarURL, getBaseURL, mobile, setAvatarURL, setNickName, setUserId} from "@/global/global";
import {ref} from "vue";
import service from "@/request";
import {loginFailed} from "@/utils/tokenCheck";
import {identityJump} from "@/utils/identityJump";

let carousel = ref(null)

const photoURL = ref('')
const nickName = ref('')
const researchDirection = ref('')
const character = ref(0)

const jump = () => {
  identityJump(character.value);
}

const submitNickName = () => {
  showLoading();
  service.post('/api/user/editNickName', { token: localStorage.getItem('token'), nickName: nickName.value }).then((res) => {
    let data = res.data;
    if (data.success) {
      hideLoading();
      localStorage.setItem('token', data.token);
      setNickName(nickName.value);
      carousel.value.next();
    } else {
      hideLoading();
      messageError(data.message);
    }
  })
}
const submitResearchDirection = () => {
  console.log('进入submitResearchDirection')
  console.log({ token: localStorage.getItem('token'), researchDirection: researchDirection.value })
  showLoading();
  service.post('/api/user/changeResearchDirection', { token: localStorage.getItem('token'), researchDirection: researchDirection.value }).then((res) => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      hideLoading();
      localStorage.setItem('token', data.token);
      setNickName(nickName.value);
      carousel.value.next();
    } else {
      hideLoading();
      messageError(data.message);
    }
  })
}



const submitFillIn = () => {
  switch (character.value){
    case 0:
      submitNickName();
      break;
    case 1:
      submitResearchDirection();
      break;
  }
}

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
      getUserInfo()
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
    if(data.success){
      data.user.avatarURL = getBaseURL() + data.user.avatarURL;
      data.user.photoURL = getBaseURL() + data.user.photoURL;
      setAvatarURL(data.user.avatarURL)
      setUserId(data.user.id);
      character.value = data.character;
      localStorage.setItem('token', data.token);
      photoURL.value = data.user.photoURL
      nickName.value = data.user.nickName;
      researchDirection.value = data.user.researchDirection;
      hideLoading()
      carousel.value.next()
    }else{
      hideLoading()
      loginFailed()
    }
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
      carousel.value.next()
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
  font-weight: bold;
  text-align: center;
}
.noticeBig{
  padding: 2vh;
  font-size: 6vh;
  font-weight: bold;
}
.inputArea{
  margin: 0 auto;
  padding-top: 5vh;
  display: flex;
  font-size: 4vh;

}
.submitButton{
  background-color: rgba(0,0,0,0);
  border-style: none;
  font-size: 6vh;
  margin: 30% 30%;
}
.buttonArea{
  width: 100%;
  display: flex;
  flex-direction: row-reverse;
}
</style>
