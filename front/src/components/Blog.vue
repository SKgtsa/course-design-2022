<template>
  <transition name="el-zoom-in-bottom">
    <div class="main" :style="{
    'flex-direction': `${mobile? 'column':'row'}`,
    'height': `${mobile? 'auto':'93vh'}`,
    'padding-top': `${mobile? '3vh':'0'}`,
    'padding-bottom': `${mobile? '8vh':'0'}`,
  }" v-if="initOver">
      <div class="leftWindow" :style="{
      'flex-direction': `${mobile? 'column-reverse':'column'}`,
      'padding-left': `${mobile? '0':'1vw'}`,
      'margin': `${mobile? '0':'0 1%'}`
    }">
        <div class="panel" :style="{
        'padding-bottom': `${mobile? '2vh':'0'}`
      }">
          <div class="panelCard" :style="{
          'height': `${mobile? 'auto':'40vh'}`,
          'margin': `${mobile? 'auto':'0'}`,
        }">
            <div class="buttonBox">
              <el-button class="writeButton" @click="writeBlog">
                <el-image />
                <a>写博客</a>
              </el-button>
              <el-menu
                  default-active="1"
                  class="leftMenu"
                  @select="handleMenuOpen"
                  :mode="`${mobile? 'horizontal':'vertical'}`"
              >
                <el-menu-item index="1">广场</el-menu-item>
                <el-menu-item index="2">我的关注</el-menu-item>
                <el-menu-item index="3">我的博客</el-menu-item>
                <el-menu-item index="4">我的收藏</el-menu-item>
              </el-menu>
            </div>
          </div>
        </div>
        <div class="notice" :style="{
        'margin': `${mobile? '0':'0'}`,
        'width': `${mobile? '100%':'auto'}`
      }">
          <el-carousel class="noticeCard"
                       :direction="`${mobile? 'horizontal':'vertical'}`"
                       :autoplay="true"
                       v-touch:swipe.left="right" v-touch:swipe.right="left"
                       ref="carousel"
          >
            <el-carousel-item v-for="(item,index) in pageData.announcementList" :key="item">
              <div :style="{
              'background-image': `url(${item.pictureUrl})`,
              'background-size':'cover',
              'height':'100%'
            }" @click="jumpToAnnouncementDetail(item)">
                <div style="width: 100%;height: 100%;border-radius: inherit;background-color: rgba(0,0,0,0.6);display: flex;vertical-align: bottom">
                  <a style="color: #FFFFFF;font-size: 4vh;margin: auto">{{item.heading}}</a>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div class="rightWindow"  :style="{
        'padding-right':`${mobile? 0:'0.2vw'}`,
        'width':`${mobile? 'auto': '70vw'}`
      }">
        <div class="operationCard" :style="{
        'width': `${mobile? '100%':'70vw'}`,
      }">
          <div class="searchArea">
            <el-input v-model="pageData.input" style="font-size: 3vh;line-height: 3vh;height: 4vh"/>
            <el-button @click="search" style="height: 4vh;width: 20%">搜索</el-button>
          </div>
          <div v-infinite-scroll="refresh" class="listArea" style="overflow: auto;height: 90%;">
            <div v-for="(item,index) in pageData.postList"   :key="index"  style="padding-top: 2vh">
              <div class="postBox" :style="{ 'background-image': `url(${item.topImageURL})` }">
                <div class="boxContainer" style="background-color: rgba(10,10,10,0.6)" @click="jumpToDetail(item)">
                  <div class="cardContent" >
                    <a :style="{'font-size':`${mobile? 6:4}vh`}">{{item.heading}}</a>
                    <div class="postBoxBottom">
                      <div class="bottomLeft">
                        <a>{{item.time}}</a>
                      </div>
                    </div>
                  </div>
                  <div class="bottomTop">
                    <el-button class="avatarButton" @click="jumpToPersonal(item.userId, $event)"><el-image :src="item.avatarURL" class="avatar"/></el-button>
                    <a class="nickName" @click="jumpToPersonal(item.userId, $event)">{{item.nickName}}</a>
                  </div>
                  <div class="bottomBottom">
                    <el-button v-if="handleDeleteCheck(item) || getManager()" class="likeCollectButton" @click="deleteThisPost(item, $event)"><el-icon><DeleteFilled /></el-icon></el-button>
                    <el-button class="likeCollectButton" @click="collectThis(item, $event)"><el-image class="likeCollectImage" :src="item.collect? 'https://courseback.clankalliance.cn/static/inbuild/collect-active.png':'https://courseback.clankalliance.cn/static/inbuild/collect.png'"></el-image></el-button>
                    <a class="likeNum">{{item.likeNum}}</a>
                    <el-button class="likeCollectButton" @click="likeThis(item, $event)"><el-image class="likeCollectImage" :src="item.like? 'https://courseback.clankalliance.cn/static/inbuild/like-active.png':'https://courseback.clankalliance.cn/static/inbuild/like.png'"></el-image></el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </transition>
  <!--弹出框 写博客-->
  <el-drawer
      with-header="false"
      v-model="drawerOpen"
      :direction="`${mobile? 'btt':'rtl'}`"
      :before-close="handleClose"
      :size="'90%'"
      z-index="50"
  >
    <a style="font-size: 4vh">撰写博客</a>
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
  <!--弹出框 显示博客详细正文-->
  <el-dialog
      v-model="showDetail"
      :width="mobile? '90%':'80%'"
      modal-append-to-body=false
  >
    <div :style="{
      'background-image': `url(${pageData.target.topImageURL})`,
      'margin': '0 auto',
      'width': '100%'
    }" class="detailTopImage"/>
    <a class="detailHeading">{{pageData.target.heading}}</a>
    <div class="userInfoArea">
      <el-button class="avatarButton" @click="jumpToPersonal(pageData.target.userId, $event)"><el-image :src="pageData.target.avatarURL" class="avatar"/></el-button>
      <a class="nickName" @click="jumpToPersonal(pageData.target.userId, $event)">{{pageData.target.nickName}}</a>
    </div>
    <div class="postContent">
      <div v-html="pageData.targetContent.content"/>
    </div>
    <div class="commentArea">
      <div class="commentEditor">
        <editor v-model="commentContent"
                :init="initComment"
                :disabled="disabled"
                :id="commentEditorId">
        </editor>
        <el-button @click="commentSubmit">提交</el-button>
      </div>
      <div class="comment">
        <div  v-for="(item,index) in pageData.targetContent.commentList" class="commentCardArea">
          <div class="commentCard">
            <div v-html="item.content" class="commentContent"/>
            <div class="commentTool">
              <div class="userInfoArea">
                <el-button class="avatarButton" @click="jumpToPersonal(pageData.target.userId, $event)"><el-image :src="getBaseURL() + item.avatarURL" class="avatar"/></el-button>
                <a class="nickName" @click="jumpToPersonal(pageData.target.userId, $event)">{{item.nickName}}</a>
              </div>
              <el-button v-if="handleDeleteCheck(item) || getManager()" class="likeCollectButton" @click="deleteThisComment(item, $event)"><el-icon><DeleteFilled /></el-icon></el-button>
            </div>
          </div>
        </div>

      </div>
    </div>
  </el-dialog>
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
      <div v-html="pageData.targetAnnouncement.content"/>
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
import {getBaseURL, getManager, getUserId, mobile} from "@/global/global";
import {loginFailed} from "@/utils/tokenCheck";

const handleDeleteCheck = (item: Post) => {
  console.log(item.userId)
  console.log(getUserId())
  if(item.userId == getUserId()){
    return true;
  }else{
    return false;
  }
}
const initOver = ref(false);

const blog = ref('')
const showUpload = ref(true);
//博客列表陈列分页用
const startIndex = ref(0);
const length = 6;
//抽屉是否打开（呈现）
const drawerOpen = ref(false);
//博客标题
const heading = ref('');
//为true代表正在请求 避免重复
const requesting = ref(false);
//封面图片
let topImage = null;
//切换refresh方法的url
const targetURL = ref('api/blog/getMain')
//若为true,代表已经到底
const loadOver = ref(false);
//若为true,展示博客正文
const showDetail = ref(false);

const showAnnouncementDetail = ref(false)

console.log(getUserId())

let carousel = ref(null)

const left = () => {
  carousel.value.prev();
}

const right = () => {
  carousel.value.next();
}

const pageData  = reactive({
  postList:[],
  target: {
    id: '',
    heading: '',
    nickName: '',
    time: '',
    topImageURL: '',
    avatarURL: '',
    userId: '',
    likeNum: 0,
    like: false,
    collect: false,
  },
  targetContent: {content: '',commentList: []},
  announcementList: [],
  targetAnnouncement: {
    id: '0',
    pictureUrl:'',
    heading:'',
    content: '',
  },
  keyWord: '',
  input: '',
})

interface Announcement{
  id: string,
  pictureUrl:string,
  heading:string,
}

const search = () => {
  pageData.keyWord = pageData.input;
  startIndex.value = 0;
  loadOver.value = false;
  pageData.postList = [];
  refresh();
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
      refresh();
    }else{
      hideLoading();
      loginFailed();
    }
  })
}

updateAnnouncement();

const deleteThisComment = (target: Comment, event: Event) => {
  event.stopPropagation();
  ElMessageBox.confirm('你确定要删除这条博客吗？','警告',{
    type: "info",
    cancelButtonText: '取消',
    confirmButtonText: '确认'
  }).then(() => {
    showLoading();
    console.log(target.id)
    service.post('api/blog/deleteComment',{token: localStorage.getItem("token"), id: target.id}).then(res => {
      const data = res.data;
      console.log('删除成功收到回调')
      console.log(res)
      if (data.success) {
        localStorage.setItem('token', data.token)
        hideLoading();
        jumpToDetail(pageData.target)
      } else {
        hideLoading();
        loginFailed();
      }
    })
  }).catch(() => {
    console.log('取消操作')
  })
}

const deleteThisPost = (target: Post, event: Event) => {
  event.stopPropagation();
  ElMessageBox.confirm('你确定要删除这条博客吗？','警告',{
    type: "info",
    cancelButtonText: '取消',
    confirmButtonText: '确认'
  }).then(() => {
    showLoading();
    service.post('api/blog/delete',{token: localStorage.getItem("token"), blogId: target.id}).then(res => {
      const data = res.data;
      console.log('删除成功收到回调')
      console.log(res)
      if (data.success) {
        localStorage.setItem('token', data.token)
        hideLoading();
        pageData.keyWord = '';
        pageData.postList = [];
        startIndex.value = 0;
        loadOver.value = false;
        refresh();
      } else {
        hideLoading();
        loginFailed();
      }
    })
  }).catch(() => {
    console.log('取消操作')
  })
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
      pageData.targetAnnouncement.content = data.content;
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

interface Post{
  id: string,
  heading: string,
  nickName: string,
  time: string,
  topImageURL: string,
  avatarURL: string,
  userId: string,
  likeNum: number,
  like: boolean,
  collect: boolean;
}

interface Comment{
  id: string,
  content: string,
  nickName: string,
  avatarURL: string,
  userId: string,
}

const likeThis = (target: Post, event: Event) => {
  event.stopPropagation();
  console.log('likeThis()')
  console.log(target)
  showLoading();
  service.post('api/blog/like',{token: localStorage.getItem("token"), blogId: target.id}).then(res => {
    const data = res.data;
    console.log('点赞成功收到回调')
    console.log(res)
    if (data.success) {
      //这里有问题
      target.like = !target.like;
      if(target.like){
        target.likeNum += 1;
      }else{
        target.likeNum -= 1;
      }
      localStorage.setItem('token', data.token)
      hideLoading();
    } else {
      hideLoading();
      loginFailed();
    }
  })
}

const collectThis = (target: Post, event: Event) => {
  event.stopPropagation();
  console.log('likeThis()')
  console.log(target)
  showLoading();
  service.post('api/blog/collect',{token: localStorage.getItem("token"), blogId: target.id}).then(res => {
    const data = res.data;
    console.log('收藏成功收到回调')
    console.log(res)
    if (data.success) {
      target.collect = !target.collect;
      localStorage.setItem('token', data.token)
      hideLoading();
    } else {
      hideLoading();
      loginFailed();
    }
  })
}

const jumpToDetail = (target: Post) => {
  console.log('jumpToDetail()')
  console.log(target)
  pageData.target = target;
  showLoading();
  service.post('api/blog/getDetail',{token: localStorage.getItem("token"), blogId: target.id}).then(res => {
    const data = res.data;
    console.log('获取正文成功')
    console.log(res)
    if (data.success) {
      let content = data.content;
      content.content = content.content.toString().replace('<img',"<img style='max-width:100%;height:auto;'")
      pageData.targetContent = content;
      localStorage.setItem('token', data.token)
      hideLoading();
    } else {
      hideLoading();
      loginFailed();
    }
    showDetail.value = true;
  })
}

const jumpToPersonal = (userId : string, event: Event) => {
  console.log('jumpToPersonal()')
  event.stopPropagation();
  router.push({
    path: '/personalPage',
    query: {userId: userId}
  })
}

const handleMenuOpen = (key: string, keyPath: string[]) => {
  console.log("打开" + key)
  switch (key){
    case '1':
      targetURL.value = 'api/blog/getMain';
      break;
    case '2':
      targetURL.value = 'api/blog/getLike';
      break;
    case '3':
      targetURL.value = 'api/blog/getMine';
      break;
    case '4':
      targetURL.value = 'api/blog/getCollect';
      break;
  }
  pageData.keyWord = '';
  startIndex.value = 0;
  loadOver.value = false;
  pageData.postList = [];
  refresh();
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
//评论编辑器 初始化方法
const initComment = reactive({
  selector: '#' + commentEditorId.value, //富文本编辑器的id,
  language_url: "/tinymce/langs/zh_CN.js", // 语言包的路径，具体路径看自己的项目，文档后面附上中文js文件
  language: "zh_CN", //语言
  skin_url: "/tinymce/skins/ui/CUSTOM", // skin路径，具体路径看自己的项目
  placeholder: '请在此处评论',
  height: 200, //编辑器高度
  max_height: 200,
  branding: false, //是否禁用“Powered by TinyMCE”
  menubar: false, //顶部菜单栏显示
  image_dimensions: true, //去除宽高属性
  plugins: props.plugins,  //这里的数据是在props里面就定义好了的
  toolbar: props.commentToolBar, //这里的数据是在props里面就定义好了的
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

const writeBlog = () => {
  drawerOpen.value = true;
}

const refresh = () => {
  if(!requesting.value){
    if(loadOver.value){
      // ElMessage({
      //   message: "已经到底了",
      //   type: 'error'
      // })
    }else{
      showLoading();
      requesting.value = true;
      console.log(pageData.keyWord)
      service.post(targetURL.value,{token: localStorage.getItem("token"), length: length, startIndex: startIndex.value, keyWord: pageData.keyWord}).then(res => {
        const data = res.data;
        console.log('开始refresh')
        console.log(res)
        if (data.success) {
          blog.value = '';
          let tempList = pageData.postList;
          for(let i = 0;i < data.content.length;i ++ ){
            data.content[i].avatarURL = getBaseURL() + data.content[i].avatarURL;
            data.content[i].topImageURL = getBaseURL() + data.content[i].topImageURL;
            tempList.push(data.content[i]);
          }
          pageData.postList = tempList;
          console.log(pageData.postList)
          startIndex.value = data.startIndex;
          if(data.content.length < length){
            loadOver.value = true;
          }
          localStorage.setItem('token', data.token)
          if(!initOver.value)
            initOver.value = true;
          hideLoading();
        } else {
          hideLoading();
          loadOver.value = true;
          loginFailed()
        }
        requesting.value = false;
      })
    }
  }
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
    serviceFile.post('api/blog/submit', { token: localStorage.getItem("token"), heading: heading.value, content: postContent.value,topImage: topImage }).then(res => {
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
        pageData.keyWord = '';
        loadOver.value = false;
        pageData.postList = [];
        startIndex.value = 0;
        refresh();
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
const commentSubmit = () => {
  showLoading();
  if(commentContent.value.length == 0){
    ElMessage({
      message: '评论不能为空',
      type: 'error'
    })
  }else{
    service.post('api/blog/comment', { token: localStorage.getItem("token"), blogId: pageData.target.id, content: commentContent.value }).then(res => {
      const data = res.data;
      console.log(res)
      if (data.success) {
        blog.value = '';
        ElMessage({
          message: '发送成功',
          type: 'success'
        })
        localStorage.setItem('token', data.token)
        hideLoading();
        jumpToDetail(pageData.target);
        commentContent.value = "";
        //用新token向后端要新的blog列表并更新显示
      } else {
        console.log(res)
        hideLoading();
        loginFailed()
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
.searchArea{
  display: flex;
  flex-direction: row;
  padding-top: 2vh;
  padding-left: 2vw;
  padding-right: 2vw;
}
.commentCardArea{
  padding-top: 1.5vh;
}
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
  width: 5vh;
  height: 5vh;
  border-radius: 2.5vh;

}
.avatar{
  width: 5vh;
  height: 5vh;
  border-radius: 2.5vh;
  position: center;
}
.nickName{
  line-height: 4vh;
  font-weight: bold;
  font-size: 3vh;
}
.likeNum{
  font-weight: bold;
  font-size: 3.5vh;
  line-height: 3.5vh;
  width: 3vh;
}
.cardBackground{
  z-index: 1;

}
.listArea{

}
.postBox{
  z-index: 5;
  border-radius: 2vw;
  width: 95%;
  margin: auto;
  background-color: #666;
  background-size: cover;
  background-position: center;
  color: #FFF;

}
.boxContainer{
  border-radius: 2vw;
  padding: 1vw;
}
.cardContent{
  flex-direction: column;
  display: flex;
}
.postBoxBottom{
  flex-direction: row;
  display: flex;
  width: 58vw;
}
.bottomLeft{
  width: 29vw;
}
.bottomRight{
  width: 29vw;
  margin: 0 0;
}
.bottomTop{
  width: 100%;
  flex-direction: row-reverse;
  display: flex;
}
.likeCollectButton{
  width: 4vh;
  background-color: rgba(0,0,0,0);
  border-style: none;
  color: #FFFFFF;
  font-size: 3vh;
}
.likeCollectImage{
  width: 4vh;
}
.bottomBottom{
  width: 100%;
  flex-direction: row-reverse;
  display: flex;
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
        background-color: #222;
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
