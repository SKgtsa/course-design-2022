<template>
  <div class="container">
    <div>
      <div>
        <!--发博客模块-->
        <div>
          <el-input
            v-model="blog"
            :rows="3"
            type="textarea"
            placeholder="在这里发你的博客吧"
          />
        </div>
        <div>
          <el-button @click="submitBlog" class="blogButton">发送</el-button>
        </div>
      </div>
      <div>
        <!--看博客模块-->

      </div>
    </div>
    <div>
      <!--右侧信息栏-->

    </div>
  </div>
</template>

<script lang="ts" setup>

import {ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";

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
</script>

<style scoped>

</style>
