<template>
  <div class="touchBox" v-touch:swipe.left="right" v-touch:swipe.right="left">
    <el-carousel
        indicator-position="none"
        :autoplay="false"
        arrow="never"
        :loop="false"
        class="carouselWindow"
        ref="carousel"
        @wheel="handleScroll"
        @change="handleCarouselChange"
    >
      <el-carousel-item style="height: 100vh">
        <div class="page" :style="{
        'height': '100vh',
        'width': `${mobile? 'auto':'100vw'}`,
      }" >
          <transition name="el-fade-in-linear">
            <div class="FirstInfoCard" :style="{
          'width': `${mobile? 95:60}%`
        }" v-if="showA">
              <div class="upperInfo">
                <div class="infoAvatar"><el-image class="infoAvatarImage" :src="getBaseURL() + pageData.data.avatarURL"/></div>
                <div class="infoName"><a class="name">{{ pageData.data.name }}</a></div>
              </div>
              <div class="bottomButton">
                <div class="pageDescription">
                  <a style="font-size: 2.5vh;color: #2b3b4e;font-weight: 600">很荣幸您能访问我的网站。我喜欢软件开发，希望将来也能成为技术大牛。</a>
                  <a style="text-align: right;font-size: 6vh;color: #333333;font-weight: bold" >项目经历:</a>
                  <a style="font-size: 3vh;color: #333333;font-weight: bold;text-align: right;">可使用滚轮</a>
                </div>
                <el-button class="startButton" @click="right"><el-icon style="color: #FFFFFF;font-size: 10vh"><ArrowRightBold /></el-icon></el-button>
              </div>
            </div>
          </transition>
        </div>
      </el-carousel-item>
      <el-carousel-item style="height: 100vh">
        <!--博客的个人页面-->
        <div class="page" :style="{
        'height': '100vh',
        'width': `${mobile? 'auto':'100vw'}`,
      }" >
          <div class="mainArea">
            <transition name="el-zoom-in-top">
              <a class="title" v-if="showBA">Web课设用博客系统</a>
            </transition>
            <transition name="el-fade-in-linear">
              <div v-infinite-scroll="refresh" v-if="showBB" class="listArea" style="overflow: auto;height: 85vh;padding-top: 2vh" :style="{
            'width': `${mobile? 95:60}%`
          }">
                <div v-for="(item,index) in pageData.postList"  :key="index"  style="padding-top: 2vh">
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
                    </div>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </el-carousel-item>
      <el-carousel-item style="height: 100vh">
        <div class="page" :style="{
        'height': '100vh',
        'width': `${mobile? 'auto':'100vw'}`,
      }" >
          <div class="mainArea">
            <transition name="el-zoom-in-top">
              <a class="title" v-if="showCA">童智训练同步小程序</a>
            </transition>
            <transition name="el-fade-in-linear">
              <div  v-if="showCB" :style="{
            'width': `${mobile? 95:60}%`,
            'flex-direction': `${mobile? 'column': 'row'}`,
            'margin': `${mobile? '0 auto':'auto'}`
          }" class="littleProgramMain">
                <el-image src="https://courseback.clankalliance.cn/inbuild/SKgtsa/2.jpg" class="littleProgramImage" :style="{
              'width': `${mobile? '30vh':'40%'}`
            }"/>
                <div :style="{
              'width': `${mobile? '100%':'60%'}`,
              'display':'flex',
              'flex-direction': 'row'
            }">
                  <div style="margin: auto">
                    <a class="littleProgramDescription">基于微信原生实现，通过蓝牙与训练设备连接并同步数据至数据库，起到训练的监督作用。</a>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </el-carousel-item>
      <el-carousel-item style="height: 100vh">
        <div class="githubPageContent">
          <div style="margin: auto;height: 8vh;display: flex">
            <a class="githubText">{{githubText}}</a>
            <transition name="el-fade-in-linear">
              <el-button class="githubButton" @click="toGithub" v-if="showGithubButton"><el-image class="githubIcon" src="https://courseback.clankalliance.cn/static/inbuild/SKgtsa/github.png" />Github个人主页</el-button>
            </transition>
          </div>
        </div>
      </el-carousel-item>
      <el-carousel-item style="height: 100vh">
        <div class="videoContainer">
          <video class="fullScreenVideo" :style="{
            'width': `${windowWidth / windowHeight < 1920 / 1080? 'auto': '100vw'}`,
            'height': `${windowWidth / windowHeight < 1920 / 1080? '100vh': 'auto'}`,
            'left': `${windowWidth / windowHeight < 1920 / 1080? (0.5*windowWidth - windowHeight*1920/1080/2 + 'px'): '0'}`,
            'top': `${windowWidth / windowHeight < 1920 / 1080? '0':  (0.5*windowHeight - windowWidth*1080/1920/2 + 'px')}`,
          }" autoplay loop muted>
            <!--获取的b站视频源-->
            <source type="video/mp4" src="https://courseback.clankalliance.cn/static/inbuild/SKgtsa/video.mp4">
          </video>
        </div>
        <div class="cover">
          <div class="endPageName">
            <a style="padding: 3vh">我的爱好</a>
          </div>
          <div class="endPageContent">
            <a  style="padding: 3vh">我热爱汽车与汽车文化，乐于了解车辆本身与其背后的故事，同时也喜欢驾驶的感觉</a>
          </div>
          <div class="endPageBottom">
            <a  style="padding: 1vh">视频来自《GranTurismo 7》</a>
          </div>
        </div>
      </el-carousel-item>
      <el-carousel-item style="height: 100vh">
        <div class="page" :style="{
        'height': '100vh',
        'width': `${mobile? 'auto':'100vw'}`,
      }" >
          <div class="endNote">
            <a>感谢您的浏览</a>
            <a>Have a nice day</a>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
  <el-dialog
      v-model="pageData.showDetail"
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
      <div class="commentEditor" v-if="login">
        <editor v-model="commentContent"
                :init="initComment"
                :disabled="disabled"
                :id="commentEditorId">
        </editor>
        <el-button @click="commentSubmit">提交</el-button>
      </div>
      <div class="comment">
        <div v-for="(item,index) in pageData.targetContent.commentList" class="commentCard">
          <div v-html="item.content" class="commentContent"/>
          <div class="commentTool">
            <div class="userInfoArea">
              <el-button class="avatarButton" @click="jumpToPersonal(pageData.target.userId, $event)"><el-image :src="getBaseURL() + item.avatarURL" class="avatar"/></el-button>
              <a class="nickName" @click="jumpToPersonal(pageData.target.userId, $event)">{{item.nickName}}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
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
import router from "@/router";
import service from "@/request";
import {ElMessage} from "element-plus";
import {hideLoading, showLoading} from "@/utils/loading";
import {reactive, ref} from "vue";
import {onMounted} from "@vue/runtime-core";
import {loginFailed} from "@/utils/tokenCheck";
import {getBaseURL, windowHeight, windowWidth} from "@/global/global";
import {mobile} from "@/global/global";

const userId  = '262483010111279104';
console.log(userId)
console.log(router)

let carousel = ref(null)

//当该值为true时允许滑动或滚动 避免页面过于敏感
const moveAvailable = ref(true);

const hasUser = ref(true)

const showA = ref(false)
const showBA = ref(false)
const showBB = ref(false)
const showCA = ref(false)
const showCB = ref(false)
const showD = ref(false)

const login = ref(false);

const commentContent = ref("")
const commentEditorId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))
const openEvaluateDrawer = ref(false)

const githubText = ref('')
const resultText = ref('当然，也欢迎到我的')
const showGithubButton = ref(false)

const openEvaluate = () => {
  console.log('openEvaluate')
  openEvaluateDrawer.value = true;
}

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

setTimeout(() => {
  showA.value = true;
},300)

const handleCarouselChange = (e ) => {
  console.log(e)
  showA.value = false;
  showBA.value = false;
  showBB.value = false;
  showCA.value = false;
  showCB.value = false;
  showD.value = false;
  githubText.value = '';
  showGithubButton.value = false;
  switch (e){
    case 0:
      setTimeout(() => {
        showA.value = true;
      },400)
    case 1:
      setTimeout(() => {
        showBA.value = true;
        setTimeout(() => {
          showBB.value = true;
        },600)
      },400)
      break;
    case 2:
      setTimeout(() => {
        showCA.value = true;
        setTimeout(() => {
          showCB.value = true;
        },600)
      },400)
      break;
    case 3:
      setTimeout(() => {
        showD.value = true;
        let i = 0;
        let j = 2;
        let timer = setInterval(() => {
          if(i <= 2){
            githubText.value += resultText.value.charAt(i);
            i ++;
          }else if(i < 9){
            if(j > 0){
              j --;
            }else{
              githubText.value += resultText.value.charAt(i);
              i ++;
            }
          }else{
            clearInterval(timer)
            setTimeout(() => {
              showGithubButton.value = true;
            },300)
          }
        },200)
      },400)
      break;
  }
}

const left = () => {
  carousel.value.prev();
}

const right = () => {
  carousel.value.next();
}

const toGithub = () => {
  window.open("https://github.com/SKgtsa")
}

//监听滚轮 切换页面
const handleScroll = (res) => {
  console.log(res)
  if(moveAvailable.value){
    moveAvailable.value = false;
    setTimeout(() => {
      moveAvailable.value = true;
    },1000)
    if(res.deltaX > 0 || res.deltaY >0){
      right();
    }else{
      left();
    }
  }
}

//在onMounted中初始化编辑器
onMounted(() => {
  tinymce.init({})
})
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
const pageData = reactive({
  data: {
    avatarURL : '',
    identity : '',
    nickName : '',
    name : '',
    gender : '',
    eMail : '',
    achievementList: [],
    follow: false,
  },
  startIndex : 0,
  length: 5,
  loadOver: false,
  requesting: false,
  postList: [],
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
  showDetail: false
})
const refresh = () => {
  if(!pageData.requesting){
    if(pageData.loadOver){
      // ElMessage({
      //   message: "已经到底了",
      //   type: 'error'
      // })
    }else{
      showLoading();
      pageData.requesting = true;
      service.post('/api/blog/getPersonalPost',{token: localStorage.getItem("token"), length: pageData.length, startIndex: pageData.startIndex, userId: userId}).then(res => {
        const data = res.data;
        console.log('开始refresh')
        console.log(res)
        login.value = data.success;
        let tempList = pageData.postList;
        for(let i = 0;i < data.content.length;i ++ ){
          data.content[i].avatarURL = getBaseURL() + data.content[i].avatarURL;
          data.content[i].topImageURL = getBaseURL() + data.content[i].topImageURL;
          tempList.push(data.content[i]);
        }
        pageData.postList = tempList;
        pageData.startIndex = data.startIndex;
        if(data.content.length < length){
          pageData.loadOver = true;
        }
        localStorage.setItem('token', data.token)
         if (!data.success) {
          pageData.loadOver = true;
           if(data.message != '登录失效' && data.message != '查找成功'){
             ElMessage({
               message: data.message,
               type: 'error'
             })
           }
        }
        pageData.requesting = false;
        hideLoading();
        console.log(pageData)
      })
    }
  }
}

const init = () => {
  if(hasUser.value){
    showLoading()
    console.log(userId)
    pageData.requesting = true;
    //初始化方法
    service.post('api/blog/getPersonal', {token: localStorage.getItem("token"), userId: userId}).then(res => {
      const data = res.data;
      console.log('初始化成功')
      console.log(res)
      localStorage.setItem('token', data.token)
      login.value = data.success;
      let temp = data.content;
      login.value = data.success;
      pageData.data = temp;
      hideLoading();
      pageData.requesting = false;
      refresh();
    })
  }else{
    ElMessage({
      message: '用户不存在',
      type: 'error'
    })
  }

}
init();
const jumpToDetail = (target: Post) => {
  console.log('jumpToDetail()')
  console.log(target)
  pageData.target = target;
  showLoading();
  service.post('api/blog/getDetail',{token: localStorage.getItem("token"), blogId: target.id}).then(res => {
    const data = res.data;
    console.log('获取正文成功')
    console.log(res)
    login.value = data.success;
    let content = data.content;
    console.log(content)
    content.content = content.content.toString().replace('<img',"<img style='max-width:100%;height:auto;'")
    pageData.targetContent = content;
    localStorage.setItem('token', data.token)
    hideLoading();
    pageData.showDetail = true;
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
        loginFailed()
        hideLoading();
      }
    })
  }
  console.log(commentContent.value);
}
</script>

<style scoped>
.infoAvatar{
  padding-left: 4vw;
  height: 100%;
  display: flex;
  padding-right: 4vw;
}
.infoAvatarImage{
  width: 20vh;
  height: 20vh;
  border-radius: 10vh;
  margin: auto;
}
.infoName{

  height: 100%;
  display: flex;
}
.name{
  margin: auto;
  font-size: 5vh;
  color: #FFFFFF;
}
.upperInfo{
  width: 100%;
  height: 50%;
  display: flex;
  flex-direction: row;
}
.bottomButton{
  width: 100%;
  height: 50%;
  background-color: #DDD;
  display: flex;
  flex-direction: row;
}
.pageDescription{
  display: flex;
  flex-direction: column;
  padding: 1.5vh;
  width: 80%;
}
.startButton{
  width: 20%;
  height: 100%;
  background-color: rgba(163,62,0,0.9);
  border-style: none;
  border-radius: 0;
}
.FirstInfoCard{
  margin: 5% auto;
  background-color: #2b3b4e;
  box-shadow: 0 0 3vh 0 #112233;
}
.title{
  color: #FFFFFF;
  font-size: 4vh;
  padding-left: 3vw;
}
.touchBox{
  width: 100%;
  height: 100vh;
  background-color: #222;
}
.littleProgramMain{
  display: flex;
}
.carouselWindow{
  width: 100%;
  height: 100vh;
}
.page{
  display: flex;
  background-color: #222;
}
.mainArea{
  display: flex;
  flex-direction: column;
  padding-top: 3vh;
  width: 100%;
}
.listArea{
  padding: 0;
  margin: 0 auto;
}
.postBox{
  z-index: 5;
  border-radius: 2vw;
  width: 95%;
  background-color: #000;
  background-size: cover;
  background-position: center;
  color: #FFF;
  margin: auto;
}
.commentCard{
  background-color: #EEEEEE;
  border-radius: 1vw;
  padding-left: 1vw;
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
  width: 100%;
}
.bottomLeft{
  width: 50%;
}
.bottomTop{
  width: 100%;
  flex-direction: row-reverse;
  display: flex;
}
.littleProgramImage{
  margin: auto;
  padding: 2vh;
}
.littleProgramDescription{
  color: #EEE;
  font-size: 3vh;
  margin: auto;
}
.githubPageContent{
  width: 100%;
  height: 100%;
  display: flex;
}
.githubText{
  color: #FFFFFF;
  font-size: 4vh;
  font-weight: bold;
  margin: auto;
}
.githubButton{
  height: 5vh;
  margin: auto;
}
.githubIcon{
  width: 4vh;
  height: 4vh;
}
.endNote{
  margin: auto;
  font-size: 6vh;
  font-weight: bold;
  color: #FFFFFF;
  display: flex;
  flex-direction: column;
  text-align: center;
}
.videoContainer {
  z-index: 0;
  position: absolute;
}
.fullScreenVideo{
  position: absolute;
  background-color: #FFFFFF;
}
.cover{
  background-color: rgba(0,0,0,0.5);
  width: 100vw;
  height: 100vh;
  z-index: 100;
  position: absolute;
  display: flex;
  flex-direction: column;
  color: #FFFFFF;
}
.endPageName{
  width: 100%;
  height: 15vh;
  display: flex;
  font-size: 6vh;
  font-weight: bold;
}
.endPageContent{
  width: 100%;
  height: 77vh;
  display: flex;
  font-size: 3vh;
}
.endPageBottom{
  width: 100%;
  display: flex;
  flex-direction: row-reverse;
}
</style>
