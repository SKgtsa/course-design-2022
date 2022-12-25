<template>
  <div class="main">
    <div class="leftWindow">
      <div class="panel">
        <div class="panelCard">
          <el-button class="writeButton" @click="writeBlog">
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
        <ul v-infinite-scroll="refresh" class="infinite-list" style="overflow: auto">
          <li v-for="i in postList" :key="i" class="infinite-list-item">{{ i }}</li>
        </ul>
      </el-container>
    </div>
  </div>
  <!--弹出框 写博客-->
  <el-drawer
      v-model="drawerOpen"
      title="撰写博客"
      :direction="'rtl'"
      :before-close="handleClose"
      :size="'90%'"
  >
    <div class="writeBox">
      <editor v-model="postContent"
              :init="init"
              :disabled="disabled"
              :id="tinymceId">
      </editor>
    </div>
  </el-drawer>


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
import service from "@/request";
const blog = ref('')
const testURL = 'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp';
const imageList = [
  'http://localhost:5174/static/file/8DFDB35A-C058-4CEA-8CA3-5A076B5D4240.webp',
  'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
]
//博客列表陈列分页用
const startIndex = ref(0);
const length = 5;
//抽屉是否打开（呈现）
const drawerOpen = ref(false);

const postList = ref([
  {
    id: 12345,
    heading: '文章A',
    nickName: '作者A',
    avatarURL: 'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
    time: 'aaaa',
    likeNum: '30',
    like: true,
  },{
    id: 12345,
    heading: '文章B',
    nickName: '作者B',
    avatarURL: 'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
    time: 'aaaa',
    likeNum: '30',
    like: true,
  },{
    id: 12345,
    heading: '文章C',
    nickName: '作者C',
    avatarURL: 'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
    time: 'aaaa',
    likeNum: '30',
    like: true,
  }
]);


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
        "bold forecolor backcolor |fontfamily fontsize|image link",
  },//必填
})
//编辑器内容
const postContent = ref(props.value)
const tinymceId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))
//定义一个对象 init初始化
const init = reactive({
  selector: '#' + tinymceId.value, //富文本编辑器的id,
  language_url: "/tinymce/langs/zh_CN.js", // 语言包的路径，具体路径看自己的项目，文档后面附上中文js文件
  language: "zh_CN", //语言
  skin_url: "/tinymce/skins/ui/oxide", // skin路径，具体路径看自己的项目
  placeholder: '分享你的学习生活',
  height: 200, //编辑器高度
  branding: false, //是否禁用“Powered by TinyMCE”
  menubar: false, //顶部菜单栏显示
  image_dimensions: false, //去除宽高属性
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
  images_upload_base_path: 'http://localhost:5174',
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
          resolve(ph + res.data.msg)  //上传成功，在成功函数里填入图片路径
          localStorage.setItem('token',res.data.token)
          init.images_upload_url = res.data.content;
        } else {
          reject('HTTP Error: 上传失败');
          return
        }
      }).catch(() => {
        reject('上传出错，服务器开小差了呢')
        return
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
      myValue.value = props.value
      emits("getContent", myValue.value)
    }
)
//监听富文本中的数据变化
watch(
    () => myValue.value,
    () => {
      emits("getContent", myValue.value)
    }
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
  //在此处添加加载开始

  //刷新列表方法 自动延长列表，无限滚动每次触底触发该方法
  service.post('blog/getMain',{token: localStorage.getItem("token"), length: length, startIndex: startIndex}).then(res => {
    const data = res.data;
    if (data.success) {
      blog.value = '';
      postList.value.push(data.content);
      startIndex.value = data.startIndex;
      localStorage.setItem('token', data.token)
    } else {
      ElMessage({
        message: data.message,
        type: 'error'
      })
    }
    //在此处添加加载结束

  })
}

//页面刷新时就进行列表刷新方法
refresh();


const bolgButton = () => {
  service.post('blog/submit', { token: localStorage.getItem("token"), content: blog.value }).then(res => {
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
.writeBox{
}
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
            font-family: 华文楷体;
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
            padding-left: 2vw !important;
          ;
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
