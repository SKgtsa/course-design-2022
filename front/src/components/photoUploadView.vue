<template>
  <el-upload class="img-btn" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
    :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
    </el-upload>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'

import type { UploadFile } from 'element-plus'
import service from "@/request";

const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const disabled = ref(false)

const handleRemove = (file: UploadFile) => {
  console.log(file)
  service.post('/upload/change_avatar', { token: localStorage.getItem('token'), file: file }).then(response => {
    const data = response.data;
    console.log(response)
    if (data.success) {
      console.log('success')
    }
  })
}

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!
  dialogVisible.value = true
}

const handleDownload = (file: UploadFile) => {
  console.log(file)
  service.post('/upload/change_avatar', { token: localStorage.getItem('token'), file: file }).then(response => {
    const data = response.data;
    console.log(response)
    if (data.success) {
      console.log('success')
    }
  })
}
</script>
