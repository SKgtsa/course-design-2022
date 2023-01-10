<template>
  <div class="main" :style="{
    'flex-direction': `${mobile? 'column':'row'}`,
    'height': `${mobile? 'auto':'93vh'}`,
    'padding-top': `${mobile? '3vh':'0'}`,
    'padding-bottom': `${mobile? '8vh':'0'}`,
  }">
    <div class="leftWindow" :style="{
      'flex-direction': `${mobile? 'column-reverse':'column'}`,
      'padding-left': `${mobile? '0':'1vw'}`,
      'margin': `${mobile? '0':'0 1%'}`
    }">
      <div class="panel" :style="{
        'padding-bottom': `${mobile? '2vh':'0'}`
      }">
        <div class="panelCard" :style="{
          'height': `${mobile? '10vh':'10vh'}`,
          'margin': `${mobile? 'auto':'0'}`,
        }">
          <el-button class="writeButton" @click="writeAnnouncement">
            <el-image />
            <a><el-icon><EditPen /></el-icon>发布通知</a>
          </el-button>
        </div>
      </div>
    </div>
    <div class="rightWindow"  :style="{
        'padding-right':`${mobile? 0:'0.2vw'}`,
        'width':`${mobile? 'auto': '70vw'}`
      }">
      <div class="operationCard" :style="{
        'width': `${mobile? '100%':'70vw'}`,
      }">
        <div  class="listArea" style="overflow: auto;height: 70vh;padding-top: 2vh">
          <div v-for="(item,index) in pageData.announcementList"   :key="index"  style="padding-top: 2vh">
            <div class="announcementBox" :style="{ 'background-image': `url(${item.pictureUrl})` }">
              <div class="boxContainer" style="background-color: rgba(10,10,10,0.6)" @click="jumpToAnnouncementDetail(item)">
                <div class="cardContent" >
                  <a :style="{'font-size':`${mobile? 6:4}vh`}">{{item.heading}}</a>
                </div>
                <div class="announcementBoxBottom">
                  <div class="bottom">
                    <el-button class="deleteButton" @click="deleteThis(item, $event)"><el-icon><DeleteFilled /></el-icon></el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--弹出框 写通知-->
  <el-drawer
      with-header="false"
      v-model="drawerOpen"
      :direction="`${mobile? 'btt':'rtl'}`"
      :before-close="handleClose"
      :size="'90%'"
      z-index="50"
  >
    <a style="font-size: 4vh">发布通知</a>
    <el-upload
        action=""
        list-type="picture-card"
        multiple="false"
        :http-request="getFile"
        :limit="1"
        name="file"
        headers="{ 'content-type': 'multipart/form-data' }"
        data="{token: localstorage.getItem('token')}"
        ref="uploadRef"
        :on-change="handleChange"
        :before-upload="beforeAvatarUpload"
    >
      <el-icon><Plus /></el-icon>
      <template #file="{ file }">
        <div>
          <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
          <span class="el-upload-list__item-actions">
          <span
              v-if="!disabled"
              class="el-upload-list__item-delete"
              @click="handleRemove(file)"
          >
            <el-icon><Delete /></el-icon>
          </span>
        </span>
        </div>
      </template>
    </el-upload>
    <div class="writeBox">
      <div class="headingInputWrapper">
        <el-input v-model="heading" placeholder="请在此输入标题" class="headingInput"/>
      </div>
      <editor v-model="postContent"
              :init="init"
              :disabled="disabled"
              :id="blogEditorId">
      </editor>
      <el-button @click="submit">提交</el-button>
    </div>
  </el-drawer>
  <!--弹出框 显示通知正文-->
  <el-dialog
      v-model="showAnnouncementDetail"
      :width="mobile? '90%':'80%'"
      modal-append-to-body=false
  >
    <div :style="{
      'background-image': `url(${pageData.targetAnnouncement.pictureUrl})`,
      'margin': '0 auto',
      'width': '100%'
    }" class="detailTopImage"/>
    <a class="detailHeading">{{pageData.targetAnnouncement.heading}}</a>
    <div class="postContent">
      <div v-html="pageData.targetContent"/>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>

//TinyMCE使用引入
import tinymce from 'tinymce/tinymce'
import 'tinymce/skins/content/default/content.css'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver'
import 'tinymce/themes/silver/theme'
import 'tinymce/icons/default';
import 'tinymce/models/dom'
import "tinymce/icons/default/icons"
import "tinymce/plugins/image"
import "tinymce/plugins/link"
import { onMounted, defineEmits, watch } from "@vue/runtime-core"

import { reactive, ref } from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import { Delete, Plus } from '@element-plus/icons-vue'
import type { UploadFile } from 'element-plus'
import service from "@/request";
import serviceFile from "@/request/indexFile";
import {hideLoading, showLoading} from "@/utils/loading";
import router from "@/router";
import {getBaseURL, mobile} from "@/global/global";
import {loginFailed} from "@/utils/tokenCheck";
import { messageError } from '@/utils/message'
const blog = ref('')
const imageList = [
  'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp',
  'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
]
const showUpload = ref(true);
//抽屉是否打开（呈现）
const drawerOpen = ref(false);
//博客标题
const heading = ref('');
//为true代表正在请求 避免重复
const requesting = ref(false);
//封面图片
let topImage = null;
//若为true,展示博客正文
const showDetail = ref(false);

const showAnnouncementDetail = ref(false)

const pageData  = reactive({
  announcementList:[],
  targetAnnouncement: {
    id: '0',
    pictureUrl:'',
    heading:'',
  },
  targetContent: ''
})

interface Announcement{
  id: string,
  pictureUrl:string,
  heading:string,
}

const updateAnnouncement = () => {
  showLoading();
  requesting.value = true;
  service.post('/api/announcement/getMain',{token: localStorage.getItem('token')}).then((res) => {
    console.log(res)
    let data = res.data;
    if(data.success){
      localStorage.setItem('token',data.token);
      for(let i = 0;i < data.content.length;i ++){
        data.content[i].pictureUrl = getBaseURL() + data.content[i].pictureUrl;
      }
      pageData.announcementList = data.content;
      hideLoading();
      requesting.value = false;
    }else{
      hideLoading();
      loginFailed();
    }
  })
}

updateAnnouncement();

let beforeAvatarUpload = (file) => {
    const isLt1_5M = (file.size / 1024 / 1024) < 1.5
    if (!isLt1_5M) {
      messageError('上传头像图片大小不能超过 1.5MB!')
    }
    return isLt1_5M
  }
const jumpToAnnouncementDetail = (item: Announcement) => {
  showLoading();
  console.log(item)
  pageData.targetAnnouncement.id = item.id;
  pageData.targetAnnouncement.heading = item.heading;
  pageData.targetAnnouncement.pictureUrl = item.pictureUrl;
  requesting.value = true;
  service.post('/api/announcement/getDetail',{token: localStorage.getItem('token'),id: item.id}).then((res) => {
    let data = res.data;
    console.log(res)
    if(data.success){
      localStorage.setItem('token',data.token);
      pageData.targetContent= data.content;
      hideLoading();
      requesting.value = false;
      showAnnouncementDetail.value = true;
    }else{
      hideLoading();
      loginFailed();
    }
  })
}


const disabled = ref(false)
const uploadRef = ref();

const deleteThis = (target: Announcement, event: Event) => {
  event.stopPropagation();
  ElMessageBox.confirm('你确定要删除这条通知吗？','警告',{
    type: "info",
    cancelButtonText: '取消',
    confirmButtonText: '确认'
  }).then(() => {
    showLoading();
    service.post('api/announcement/delete',{token: localStorage.getItem("token"), id: target.id}).then(res => {
      const data = res.data;
      console.log('删除成功收到回调')
      console.log(res)
      if (data.success) {
        localStorage.setItem('token', data.token)
        hideLoading();
        pageData.announcementList = [];
        updateAnnouncement();
      } else {
        hideLoading();
        loginFailed();
      }
    })
  }).catch(() => {
    console.log('取消操作')
  })
}
const getFile = (item) => {
  console.log("进入getFile")
  console.log(item)
  topImage = item.file;
}

const handleChange = (file: UploadFile) => {
  console.log(file)
  topImage = file;
  showUpload.value = !showUpload.value;
}

const handleRemove = (file: UploadFile) => {
  console.log(file)
  uploadRef.value.clearFiles()
}

const emits = defineEmits(["getContent"])
//这里我选择将数据定义在props里面，方便在不同的页面也可以配置出不同的编辑器，当然也可以直接在组件中直接定义
const props = defineProps({
  value: {
    type: String,
    default: () => {
      return ""
    },
  },
  baseUrl: {
    type: String,
    default: "",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  plugins: {
    type: [String, Array],
    default: "image link",
  },//必填
  toolbar: {
    type: [String, Array],
    default:
        " alignleft aligncenter alignright alighjustify|bold underline forecolor backcolor |fontfamily fontsize|image | blockquote | undo redo ",
  },//必填
  commentToolBar: {
    type: [String,Array],
    default:
        " alignleft aligncenter alignright alighjustify|bold underline| undo redo ",
  }
})
//编辑器内容
const postContent = ref(props.value)
const commentContent = ref("")
const blogEditorId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))
const commentEditorId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))
//博客编辑器 初始化方法
const init = reactive({
  selector: '#' + blogEditorId.value, //富文本编辑器的id,
  language_url: "/tinymce/langs/zh_CN.js", // 语言包的路径，具体路径看自己的项目，文档后面附上中文js文件
  language: "zh_CN", //语言
  skin_url: "/tinymce/skins/ui/CUSTOM", // skin路径，具体路径看自己的项目
  placeholder: '请在此处编辑正文',
  height: 300, //编辑器高度
  max_height: 800,
  branding: false, //是否禁用“Powered by TinyMCE”
  menubar: false, //顶部菜单栏显示
  image_dimensions: true, //去除宽高属性
  plugins: props.plugins,  //这里的数据是在props里面就定义好了的
  toolbar: props.toolbar, //这里的数据是在props里面就定义好了的
  font_family_formats: '微软雅黑=Microsoft Yahei; Arial=arial,helvetica,sans-serif; 宋体=SimSun;  Impact=impact,chicago;', //字体
  font_size_formats: '11px 12px 14px 16px 18px 24px 36px 48px 64px 72px', //文字大小
  paste_webkit_styles: "all",
  paste_merge_formats: true,
  nonbreaking_force_tab: false,
  paste_auto_cleanup_on_paste: false,
  // resize: false,
  file_picker_types: 'file',
  images_upload_url: '/api/upload/generalUpload',
  images_upload_base_path: getBaseURL(),
  content_css: '/tinymce/skins/content/default/content.css', //以css文件方式自定义可编辑区域的css样式，css文件需自己创建并引入
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {

    if (blobInfo.blob().size / 1024 / 1024 > 2) {
      reject({ message: '上传失败，图片大小请控制在 2M 以内', remove: true })
      return
    } else {
      const ph = import.meta.env.VITE_BASE_PATH + ":" + import.meta.env.VITE_SERVER_PORT + "/"
      let params = new FormData()
      params.append('file', blobInfo.blob())
      params.append('token',localStorage.getItem('token'))
      let config = {
        headers: {
          "Content-Type": "multipart/form-data",

        }
      }
      service.post('api/upload/generalUpload', params, config).then(res => {
        console.log(res);
        if (res.data.success) {
          resolve(getBaseURL() + res.data.content)  //上传成功，在成功函数里填入图片路径
          localStorage.setItem('token',res.data.token)
          init.images_upload_url = res.data.content;
        } else {
          loginFailed()
        }
      }).catch(() => {
        loginFailed()
      })
    }
  }),

  // 文件上传
  file_picker_callback: (callback, value, meta) => {
    // Provide file and text for the link dialog
    if (meta.filetype == 'file') {
      callback('mypage.html', { text: 'My text' });
    }

    // Provide image and alt text for the image dialog
    if (meta.filetype == 'image') {
      callback('myimage.jpg', { alt: 'My alt text' });
    }

    // Provide alternative source and posted for the media dialog
    if (meta.filetype == 'media') {
      callback('movie.mp4', { source2: 'alt.ogg', poster: 'image.jpg' });
    }
  }
})
//监听外部传递进来的的数据变化
watch(
    () => props.value,
    () => {
      postContent.value = props.value
      emits("getContent", postContent.value)
      console.log("外部传来" + postContent.value)
    }
)
//监听富文本中的数据变化
watch(
    () => postContent.value,
    () => {
      emits("getContent", postContent.value)
      console.log("文本变化" + postContent.value)
    },

)
//在onMounted中初始化编辑器
onMounted(() => {
  tinymce.init({})
})

const handleClose = (done: () => void) => {
  ElMessageBox.confirm('你确定要放弃所有未保存的变更吗？').then(() => {
    done()
  }).catch(() => {
    // catch error
  })
}

const writeAnnouncement = () => {
  drawerOpen.value = true;
}

const submit = () => {
  showLoading();
  if(topImage == null){
    ElMessage({
      message: '请选择封面图片',
      type: 'error'
    })
  }else if(heading.value.length > 50){
    ElMessage({
      message: '标题应不长于50字',
      type: 'error'
    })
  }else if(heading.value.length == 0 || postContent.value.length == 0){
    ElMessage({
      message: '请填写完整',
      type: 'error'
    })
  }else{
    serviceFile.post('api/announcement/push', { token: localStorage.getItem("token"), heading: heading.value, content: postContent.value,picture: topImage }).then(res => {
      const data = res.data;
      console.log(res)
      if (data.success) {
        blog.value = '';
        ElMessage({
          message: '发送成功',
          type: 'success'
        })
        localStorage.setItem('token', data.token)
        drawerOpen.value = false;
        hideLoading();
        pageData.announcementList = [];
        updateAnnouncement();
        //用新token向后端要新的blog列表并更新显示
      } else {
        console.log(res)
        ElMessage({
          message: data.message,
          type: 'error'
        })
        hideLoading();
      }
    })
  }
  console.log(postContent.value);
}

const formState = reactive({ contents: '' })
const getContent = (v: string) => {
  formState.contents = v
}
</script>

<style scoped lang="scss">
.leftMenu{
  border-style: none;
}
.commentCard{
  background-color: #EEEEEE;
  border-radius: 1vw;
  padding: 1vh;
  box-shadow: 0 0 3px 0  rgba(140,140,140,0.8);
}
.detailHeading{
  font-size: 5vh;
}
.userInfoArea{
  display: flex;
  flex-direction: row-reverse;
}
.detailTopImage{
  margin: 0;
  width: 75vw;
  height: 40vh;
  background-position: top;
  background-attachment: fixed;
  background-size: cover;
}
.headingInputWrapper{
  padding-top: 1.5vh;
  padding-bottom: 2vh;
}
.headingInput{
  font-size: 3vh;
  height: 4vh;
}
.avatarButton{
  width: 5vw;
  height: 5vw;
  border-radius: 2.5vw;
}
.avatar{
  width: 5vw;
  height: 5vw;
  border-radius: 2.5vw;
  position: center;
  size: auto;
}
.nickName{
  line-height: 4vw;
  font-weight: bold;
  font-size: 3vw;
}
.likeNum{
  font-weight: bold;
  font-size: 2vw;
}
.cardBackground{
  z-index: 1;

}
.listArea{

}
.announcementBox{
  z-index: 5;
  border-radius: 2vw;
  width: 95%;
  margin: auto;
  background-color: #666;
  background-size: cover;
  background-position: center;
  color: #FFF;
  min-height: 20vh;
}
.boxContainer{
  border-radius: 2vw;
  padding: 1vw;
  height: 100%;
}
.cardContent{
  flex-direction: column;
  display: flex;
  height: 100%;
  min-height: 15vh;
}
.announcementBoxBottom{
  flex-direction: column-reverse;
  display: flex;
  width: 100%;
}
.bottomLeft{
  width: 29vw;
}
.bottomRight{
  width: 29vw;
  margin: 0 0;
}
.bottomTop{
  width: 30vw;
  flex-direction: row-reverse;
  display: flex;
}
.deleteButton{
  width: 3vw;
  background-color: rgba(0,0,0,0);
  border-style: none;
  color: #FFFFFF;
  font-size: 3vh;
}
.likeCollectImage{
  width: 3vw;
}
.bottom{
  width: inherit;
  flex-direction: row-reverse;
  display: flex;
  height: inherit;
}
.main {
  display: flex;
  .leftWindow {
    width: 100%;
    display: flex;
    .panel {
      padding-top: 2vh;
      display: flex;
      flex-direction: row-reverse;
      .panelCard {
        width: 97%;
        background-color: #FFFFFF;
        border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee;
        display: flex;
        flex-direction: column;
        padding-top: 1vh;
        .writeButton {
          margin: 0 auto;
          width: 90%;
          height: 8vh;
          background-color: rgba(211,227,253,0.9);
          color: rgba(4,30,73);
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
        }
      }
    }

    .notice {
      padding-top: 2vh;
      display: flex;
      flex-direction: row-reverse;
      .noticeCard {
        background-color: #FFFFFF;
        background-position: center;
        width: 95%;
        height: 100%;
        border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee;
        margin: 0 auto;
      }
    }
  }

  .rightWindow {
    padding-top: 0.5vh;
    .operationCard {
      border-radius: 2vh;
      background-color: #FFFFFF;
      height: 90vh;
      box-shadow: 0 0 10px 0 #b9ccee;
    }

  }
}

</style>
