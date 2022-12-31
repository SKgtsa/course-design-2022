<template>
</template>
<script setup lang="ts">
import {loginFailed, tokenCheckWithUser} from "@/utils/tokenCheck";
import router from "@/router";
import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import {getBaseURL, setAvatarURL, setNickName, setUserId} from "@/global/global";
import {identityJump} from "@/utils/identityJump";

const tokenCheckWithUser = () => {
  if(localStorage.getItem('token') == null)
    router.push('/login').then();
  showLoading()
  service.post('/api/user/myInfo',{token: localStorage.getItem('token')}).then((res) => {
    let data = res.data;
    if(data.success){
      localStorage.setItem('token',data.token);
      console.log('将头像URL设置为')
      console.log(data.user.avatarURL)
      setAvatarURL(getBaseURL() + data.user.avatarURL)
      setNickName(data.user.nickName)
      setUserId(data.user.id);
      hideLoading()
      identityJump(data.character)
    }else{
      console.log('登录失败')
      hideLoading()
      loginFailed()
    }
  })
}

tokenCheckWithUser();

</script>
<style lang="scss" scoped>
</style>
