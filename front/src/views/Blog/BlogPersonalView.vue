<template>
<!--博客的个人页面-->

  <div class="page">
    <div class="leftArea">
      <div class="infoCard">
        <div class="userInfoCard">
          <el-image class="infoAvatar" :src="getBaseURL() + pageData.data.avatarURL"/>
          <a class="infoNickName" >{{pageData.data.nickName}}</a>
        </div>
        <div class="detailInfo">
          <a>{{pageData.data.name}}</a>
          <a>{{pageData.data.gender}}</a>
          <a>{{pageData.data.identity}}</a>
          <a>{{pageData.data.eMail}}</a>
        </div>
        <el-button v-if="login" @click="subscribe" class="subscribeButton"> {{pageData.data.follow? '已关注':'关注'}} </el-button>
      </div>
      <div class="achievementCard">
        <a>成就</a>
        <div v-for="(item,index) in pageData.data.achievementList" class="achievement">
          <a>{{item.name}}</a>
        </div>
      </div>
    </div>
    <div class="rightArea">
      <div class="blogCard">
        <div class="blogList">
          <ul v-infinite-scroll="refresh" class="listArea" style="overflow: auto;height: 70vh;padding-top: 2vh">
            <div v-for="(item,index) in pageData.postList"  :key="index"  style="padding-top: 3vh">
              <div class="postBox" :style="{ 'background-image': `url(${item.topImageURL})` }">
                <div class="boxContainer" style="background-color: rgba(10,10,10,0.6)" @click="jumpToDetail(item)">
                  <div class="cardContent" >
                    <a style="font-size: 4vh">{{item.heading}}</a>
                    <div class="postBoxBottom">
                      <div class="bottomLeft">
                        <a>{{item.time}}</a>
                      </div>
                      <div class="bottomRight">
                        <div class="bottomTop">
                          <el-button class="avatarButton" @click="jumpToPersonal(item.userId, $event)"><el-image :src="item.avatarURL" class="avatar"/></el-button>
                          <a class="nickName" @click="jumpToPersonal(item.userId, $event)">{{item.nickName}}</a>
                        </div>
                        <div class="bottomBottom" v-if="login">
                          <el-button class="likeCollectButton" @click="collectThis(item, $event)"><el-image class="likeCollectImage" :src="item.collect? 'http://courseback.clankalliance.cn/static/inbuild/collect-active.png':'http://courseback.clankalliance.cn/static/inbuild/collect.png'"></el-image></el-button>
                          <el-button class="likeCollectButton" @click="likeThis(item, $event)"><el-image class="likeCollectImage" :src="item.like? 'http://courseback.clankalliance.cn/static/inbuild/like-active.png':'http://courseback.clankalliance.cn/static/inbuild/like.png'"></el-image></el-button>
                          <a class="likeNum">{{item.likeNum}}</a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <el-dialog
      v-model="pageData.showDetail"
      width="80%"
      modal-append-to-body=false
  >
    <div :style="{ 'background-image': `url(${pageData.target.topImageURL})` }" class="detailTopImage"/>
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
import {defineEmits, onMounted, watch} from "@vue/runtime-core";
import {loginFailed} from "@/utils/tokenCheck";
import {getBaseURL} from "@/global/global";


let userId  = router.currentRoute.value.query.userId as String;
console.log(userId)
console.log(router)

const hasUser = ref(true)

if(userId == undefined)
  hasUser.value = false;

const login = ref(false);

const emits = defineEmits(["getContent"])
const commentContent = ref("")
const commentEditorId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))

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

//监听外部传递进来的的数据变化
watch(
    () => props.value,
    () => {
      commentContent.value = props.value
      emits("getContent", commentContent.value)
      console.log("外部传来" + commentContent.value)
    }
)
//监听富文本中的数据变化
watch(
    () => commentContent.value,
    () => {
      emits("getContent", commentContent.value)
      console.log("文本变化" + commentContent.value)
    },

)
//在onMounted中初始化编辑器
onMounted(() => {
  tinymce.init({})
})



interface Achievement  {
  name: String,
  description: String
}

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


const subscribe = () => {
  showLoading();
  service.post('api/blog/subscribe', {token: localStorage.getItem("token"), userId: userId}).then(res => {
    let data = res.data;
    if (data.success) {
      pageData.data.follow = true;
      localStorage.setItem('token', data.token)
    } else {
      loginFailed()
    }
    localStorage.setItem('token', data.token)
    hideLoading();
  })
}

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
    } else {
      loginFailed()
    }
    hideLoading();
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
    } else {
      loginFailed()
    }
    hideLoading();
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
.page{
  display: flex;
  flex-direction: row;
  background-color: #DDDDDD;
  width: 100vw;
  height: 100vh;
}
.leftArea{
  display: flex;
  flex-direction: column;
  width: 30vw;
  padding-left: 2.5vw;
  padding-top: 5vh;
}
.infoCard{
  width: 25vw;
  display: flex;
  flex-direction: column;
  background-color: #FFFFFF;
  border-radius: 2vw;
  padding-bottom: 5vh;
  padding-top: 3vh;
}
.userInfoCard{
  display: flex;
  flex-direction: row;
  border-radius: 2vw;
  background-color: #EEEEEE;
  width: 22vw;
  margin: 0 4%;
  min-height: 7vw;
  padding: 0.5vw;
}
.infoAvatar{
  width: 7vw;
  height: 7vw;
  border-radius: 3.5vw;
}
.infoNickName{
  width: 14vw;
  font-size: 3vw;
  line-height: 6vw;
  font-weight: bold;
  color: #555;
}
.detailInfo{
  display: flex;
  flex-direction: column;
  text-align: left;
  font-size: 1.5vw;
  border-radius: 2vw;
  background-color: #EEEEEE;
  width: 21vw;
  margin: 3% 4%;
  min-height: 7vw;
  padding: 1vw;
  font-weight: bold;
  color: #555;
}
.subscribeButton{
  width: 20vw;
  margin: auto;
  font-weight: bold;
}
.achievementCard{
  background-color: #FFFFFF;
  width: 25vw;
  border-radius: 2vw;
  height: 50vh;
  margin: 5% 0;
}
.rightArea{
  width: 70vw;
  display: flex;
  flex-direction: column;
  padding-right: 1.5vw;
  padding-top: 3vh;
}
.blogCard{
  width: 69vw;
  border-radius: 2vw;
  background-color: #FFFFFF;
  height: 90vh;
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
  padding-top: 1.2vw;
}
.listArea{
  width: 65vw;
  padding: 0;
  margin: 0 5%;
}
.postBox{
  z-index: 5;
  border-radius: 2vw;
  width: 62vw;
  background-color: #000;
  background-size: cover;
  background-position: center;
  color: #FFF;
  position: relative;
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
  width: 30vw;
  flex-direction: row-reverse;
  display: flex;
}
.likeCollectButton{
  width: 3vw;
  background-color: rgba(0,0,0,0);
  border-style: none;
}
.likeCollectImage{
  width: 3vw;
}
.bottomBottom{
  width: 30vw;
  flex-direction: row-reverse;
  display: flex;
}
</style>
