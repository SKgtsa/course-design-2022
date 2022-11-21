<template>
  <div class="content">
    <div>
      <div class="blogTitle">
        <img class="img" src="../assets/images/blogLogo.png" alt="未加载">
        博客圈——分享你的学习生活
      </div>
      <div class="right">
<!--        右侧信息栏-->
      </div>
      <div class="blogContent">
        <div class="blogSend">
          <div>
            <TEditor ref="editor" v-model="formState.content" :disabled='disabled' @getContent="getContent"/>
          </div>
          <div>
            <el-button @click="submitBlog" class="blogButton">发送</el-button>
          </div>
        </div>
        <div>
          <!--看博客模块-->

        </div>
      </div>
    </div>

  </div>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import TEditor from '@/components/TEditor.vue';
const blog = ref('')
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
  background-color: #D9ECFF7E;
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
