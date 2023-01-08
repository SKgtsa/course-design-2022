<template>
  <!--博客的个人页面-->

  <div class="page" :style="{
    'flex-direction': `${mobile ? 'column' : 'row'}`,
    'height': `${mobile ? 'auto' : '100vh'}`,
    'width': `${mobile ? 'auto' : '100vw'}`,
    'padding-bottom': `${mobile ? '6vh' : '0'}`
  }">
    <div class="leftArea" :style="{
      'padding-left': `${mobile ? '0' : '2.5vw'}`,
      'width': `${mobile ? '90%' : '30vw'}`
    }">
      <div class="infoCard" :style="{
        'width': `${mobile ? '90vw' : '25vw'}`,
        'flex-direction': `${mobile ? 'row' : 'column'}`,
        'padding-bottom': `${mobile ? 1 : 5}vh`,
        'padding-top': `${mobile ? 1 : 3}vh`,
        'padding-left': `${mobile ? '1vw' : 0}`,
        'padding-right': `${mobile ? '1vw' : 0}`,
        'margin': `${mobile ? '0 3%' : '0'}`
      }">
        <div class="userInfoCard" :style="{
          'width': `${mobile ? '40vw' : '22vw'}`,
          'margin': `${mobile ? 'auto' : '0 4%'}`,
          'padding': `${mobile ? '1vw' : '0.5vw'}`
        }">
          <el-image class="infoAvatar" :style="{
            'width': `${mobile ? 20 : 7}vw`,
            'height': `${mobile ? 20 : 7}vw`,
            'border-radius': `${mobile ? 10 : 3.5}vw`
          }" :src="getBaseURL() + pageData.data.avatarURL" />
          <a class="infoNickName">{{ pageData.data.nickName }}</a>
        </div>
        <div class="detailInfo" :style="{
          'width': `${mobile ? '40vw' : '21vw'}`,
          'padding': `${mobile ? '1vw' : '1vw'}`
        }">
          <a>{{ pageData.data.name }}</a>
          <a>{{ pageData.data.gender }}</a>
          <a>{{ pageData.data.identity }}</a>
          <a>{{ pageData.data.eMail }}</a>
        </div>
        <span style="display: flex;flex-dirction:row">
          <el-button v-if="getUserId() &&  userId.toString() === getUserId().toString()" class="subscribeButton" :style="{
            'width': `${mobile ? 'auto' : '13vw'}`
          }" @click="checkInfo">个人信息</el-button>
          <el-button v-if="login" @click="subscribe" class="subscribeButton" :style="{
            'width': `${mobile ? 'auto' : '13vw'}`,
          }"> {{ pageData.data.follow ? '已关注' : '关注' }} </el-button>
        </span>
      </div>
      <div class="achievementCard" :style="{
        'width': `${mobile ? 90 : 25}vw`,
        'margin': `${mobile ? '3% 4%' : '5% 0'}`,
        'height': `${mobile ? 20 : 50}vh`
      }">
        <a style="font-size: 3vh;font-weight: bold;padding: 1.5vw;color: #555">称号</a>
        <div style="width: 100%" v-if="pageData.data.evaluateEnough">
          <div v-for="(item, index) in pageData.data.achievementList" class="achievement">
            <div class="achievementArea">
              <el-tooltip placement="top" :content="`${item.description}`">
                <a :style="{
                  'background-color': `${baseColorSet[Math.floor(Math.random() * 6)]}`
                }" class="achievement">
                  <a>{{ item.name }}</a>
                </a>
              </el-tooltip>

            </div>
          </div>
          <div class="achievement">
            <div class="achievementArea">
              <el-button :style="{
                'background-color': `${baseColorSet[Math.floor(Math.random() * 6)]}`
              }" class="achievement" @click="openEvaluate" v-if="login && userId != getUserId()">
                <a><el-icon>
                    <Plus />
                  </el-icon></a>
              </el-button>
            </div>
          </div>
        </div>
        <div style="width: 100%" v-if="!pageData.data.evaluateEnough">
          <div style="font-size: 3vh;font-weight: bold;padding: 1.5vw;color: #555">对五位同学评价之后才能查看自己的称号</div>
          <a style="font-size: 2vh;font-weight: bold;padding: 1.5vw;color: #555">评论方式:在你想留下评论的学生的主页点击</a>
          <div class="achievement">
            <div class="achievementArea">
              <el-button :style="{
                'background-color': `${baseColorSet[Math.floor(Math.random() * 6)]}`
              }" class="achievement" v-if="login">
                <a><el-icon>
                  <Plus />
                </el-icon></a>
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="rightArea" :style="{
      'padding-right': `${mobile ? 0 : '1.5vw'}`,
      'width': `${mobile ? 'auto' : '70vw'}`
    }">
      <div class="blogCard" :style="{
        'margin': `${mobile ? 'auto' : '0'}`,
        'width': `${mobile ? '95%' : 'auto'}`
      }">
        <div v-infinite-scroll="refresh" class="listArea" style="overflow: auto;height: 70vh;padding-top: 2vh">
          <div v-for="(item, index) in pageData.postList" :key="index" style="padding-top: 2vh">
            <div class="postBox" :style="{ 'background-image': `url(${item.topImageURL})` }">
              <div class="boxContainer" style="background-color: rgba(10,10,10,0.6)" @click="jumpToDetail(item)">
                <div class="cardContent">
                  <a :style="{ 'font-size': `${mobile ? 6 : 4}vh` }">{{ item.heading }}</a>
                  <div class="postBoxBottom">
                    <div class="bottomLeft">
                      <a>{{ item.time }}</a>
                    </div>
                  </div>
                </div>
                <div class="bottomTop">
                  <el-button class="avatarButton" @click="jumpToPersonal(item.userId, $event)"><el-image
                      :src="item.avatarURL" class="avatar" /></el-button>
                  <a class="nickName" @click="jumpToPersonal(item.userId, $event)">{{ item.nickName }}</a>
                </div>
                <div class="bottomBottom">
                  <el-button v-if="handleDeleteCheck(item)" class="likeCollectButton"
                    @click="deleteThisPost(item, $event)"><el-icon>
                      <DeleteFilled />
                    </el-icon></el-button>
                  <el-button class="likeCollectButton" @click="collectThis(item, $event)"><el-image
                      class="likeCollectImage"
                      :src="item.collect ? 'http://courseback.clankalliance.cn/static/inbuild/collect-active.png' : 'http://courseback.clankalliance.cn/static/inbuild/collect.png'"></el-image></el-button>
                  <a class="likeNum">{{ item.likeNum }}</a>
                  <el-button class="likeCollectButton" @click="likeThis(item, $event)"><el-image
                      class="likeCollectImage"
                      :src="item.like ? 'http://courseback.clankalliance.cn/static/inbuild/like-active.png' : 'http://courseback.clankalliance.cn/static/inbuild/like.png'"></el-image></el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--博客正文页-->
  <el-dialog v-model="pageData.showDetail" :width="mobile ? '90%' : '80%'" modal-append-to-body=false>
    <div :style="{
      'background-image': `url(${pageData.target.topImageURL})`,
      'margin': '0 auto',
      'width': '100%'
    }" class="detailTopImage" />
    <a class="detailHeading">{{ pageData.target.heading }}</a>
    <div class="userInfoArea">
      <el-button class="avatarButton" @click="jumpToPersonal(pageData.target.userId, $event)"><el-image
          :src="pageData.target.avatarURL" class="avatar" /></el-button>
      <a class="nickName" @click="jumpToPersonal(pageData.target.userId, $event)">{{ pageData.target.nickName }}</a>
    </div>
    <div class="postContent">
      <div v-html="pageData.targetContent.content" />
    </div>
    <div class="commentArea">
      <div class="commentEditor" v-if="login">
        <editor v-model="commentContent" :init="initComment" :disabled="disabled" :id="commentEditorId">
        </editor>
        <el-button @click="commentSubmit">提交</el-button>
      </div>
      <div class="comment">
        <div v-for="(item, index) in pageData.targetContent.commentList" class="commentCard">
          <div v-html="item.content" class="commentContent" />
          <div class="commentTool">
            <div class="userInfoArea">
              <el-button class="avatarButton" @click="jumpToPersonal(pageData.target.userId, $event)"><el-image
                  :src="getBaseURL() + item.avatarURL" class="avatar" /></el-button>
              <a class="nickName" @click="jumpToPersonal(pageData.target.userId, $event)">{{ item.nickName }}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
  <el-drawer v-model="openEvaluateDrawer" title="学生互评" :direction="'ttb'" :before-close="handleDrawerClose">
    <el-form :model="evaluateForm">
      <el-form-item label="称号">
        <el-input v-model="evaluateForm.name" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="evaluateForm.description" />
      </el-form-item>
    </el-form>
    <el-button @click="submitEvaluate">提交</el-button>
  </el-drawer>
  <el-dialog v-model="infoDialog" title="" :width="`${mobile ? 90 : 40}%`">
    <el-form :model="information" class="areaTextInput" ref="formData" label-position="right" label-width="auto" :rules="rules">
      <el-form-item label="照片">
        <el-upload class="avatar-uploader" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
          :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
          <img v-if="information.photoURL" class="avatar" :src="information.photoURL" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="学号:" prop="userNumber">
        <span>{{ information.userNumber }}</span>
      </el-form-item>
      <el-form-item label="姓名:" prop="name">
        <span>{{ information.name }}</span>
      </el-form-item>
      <el-form-item label="性别:" prop="gender">
        <span>{{ information.gender }}</span>
      </el-form-item>
      <el-form-item label="身份证号:" prop="idCardNumber">
        <span>{{ information.idCardNumber }}</span>
      </el-form-item>
      <el-form-item label="研究方向:" prop="phone"  v-if="identity == 1">
        <el-input v-model="information.researchDirection" maxlength="11">{{ information.phone }}</el-input>
      </el-form-item>
      <el-form-item label="班级:" prop="studentClass" v-if="identity == 0">
        <span>{{ information.studentClass }}</span>
      </el-form-item>
      <el-form-item label="年级:" prop="section" v-if="identity == 0">
        <span>{{ information.section }}</span>
      </el-form-item>
      <el-form-item label="政治面貌:" prop="politicalAffiliation">
        <span>{{ information.politicalAffiliation }}</span>
      </el-form-item>
      <el-form-item label="民族:" prop="ethnic">
        <span>{{ information.ethnic }}</span>
      </el-form-item>
      <el-form-item label="昵称:" prop="nickName">
        <el-input v-model="information.nickName" maxlength="8">{{ information.nickName }}</el-input>
      </el-form-item>
      <el-form-item label="邮箱:" prop="email">
        <el-input v-model="information.email">
          {{ information.email}}</el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="information.phone">
        {{ information.phone }}
      </el-input>
      </el-form-item>
    </el-form>
    <el-button type="primary" style="height: 4vh;" @click="sumbitEditRow"
      :style="{ 'width': `${mobile ? 30 : 9}vw`, 'margin-left': `${mobile ? 25 : 14}vw` }">
      <a>提交</a>
    </el-button>
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
import { ElMessage, ElMessageBox } from "element-plus";
import { hideLoading, showLoading } from "@/utils/loading";
import { reactive, ref } from "vue";
import { defineEmits, onMounted, watch } from "@vue/runtime-core";
import { loginFailed } from "@/utils/tokenCheck";
import { getBaseURL, getUserId } from "@/global/global";
import { mobile } from "@/global/global";
import serviceFile from '@/request/indexFile'
import { messageError, messageSuccess } from '@/utils/message'

let identity = ref(0)

let infoDialog = ref(false);
let formData = ref();
let information = reactive({
  name: '',
  email: null,
  phone: null,
  gender: '男',
  ethnic: '',
  politicalAffiliation: null,
  userNumber: '',
  studentClass: '',
  section: '',
  researchDirection: '',
  idCardNumber: null,
  photoURL: '',
  avatarURL:'',
  nickName: '',
  id: '',
});

const checkInfo = async() => {
  await loadInformationData();
  infoDialog.value = true;
}

let userId = router.currentRoute.value.query.userId as String;
console.log(userId)
console.log(router)


const loadInformationData = async () => {   //查看个人信息
  console.log('进入loadInformationData')
  pageData.requesting = true;
  showLoading();
  service.post('/api/user/myInfo', { token: localStorage.getItem("token") }).then(res => {
    console.log(res);
    if (res.data.success) {
      const data = res.data;
      let content = data.user;
      console.log(content);
      if(content.studentClass != undefined){
        identity.value = 0;
      }else if(content.researchDirection != undefined){
        identity.value = 1;
      }else{
        identity.value = 2;
      }
      information.name = content.name;
      information.email = content.email;
      information.phone = content.phone;
      if (content.gender == false) { information.gender = '男'; }
      else information.gender = '女';
      information.ethnic = content.ethnic;
      information.politicalAffiliation = content.politicalAffiliation;
      information.userNumber = content.userNumber;
      information.studentClass = content.studentClass;
      information.nickName = content.nickName;
      information.researchDirection = content.researchDirection;
      information.section = content.section;
      information.idCardNumber = content.idCardNumber;
      information.photoURL = getBaseURL() + content.photoURL;
      information.avatarURL = getBaseURL() + content.avatarUR;
      information.id = content.id;
      localStorage.setItem('token', data.token)
      console.log(information)
      pageData.requesting = false;
      hideLoading()
    } else {
      pageData.requesting = false;
      hideLoading()
      messageError(res.data.message)
    }
  })
    .catch(function (error) {
      hideLoading();
      pageData.requesting = false;
      messageError("服务器开小差了呢");
      console.log(error)
    })
}
const validateEMail = (rule, value, callback) => {  //检验邮箱
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入邮箱！'));
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱'));
    } else {
      callback();
    }
  }
}
const validatePhone = (rule, value, callback) => { //检验手机号(不能是座机......)
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入手机号码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
const rules = reactive({


  eMail: [{validator: validateEMail, trigger: 'blur' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  nickName: [{ required: true, message: '请输入用户名', trigger: 'blur' },
  { max: 8, message: '长度请不要超过8位', trigger: 'blur' }],
  researchDirection:[{ required: true, message: '请输入用户名', trigger: 'blur' }],
})

const sumbitEditRow = async () => {
  await formData.value.validate((valid) => {
    if (valid) {
      pageData.requesting = true;
      showLoading();
      service.post('/api/user/editInfo', {
        token: localStorage.getItem("token"), //这个修改信息，绑定的值
        //是information绑定还是用formData绑定？
        id: information.id, phone: information.phone, email: information.email,
        gender: ((information.gender == '女') ? true : false), ethnic: information.ethnic, politicalAffiliation: information.politicalAffiliation,
        userNumber: information.userNumber, name: information.name, studentClass: information.studentClass,
        idCardNumber: information.idCardNumber, photoURL: information.photoURL,classSection:information.section,
        researchDirection: information.researchDirection,avatarUrl:information.avatarURL,
      }).then(res => {
        console.log('返回了数据')
        console.log(res)
        if (res.data.success) {
          let data = res.data;
          localStorage.setItem('token', data.token)
          hideLoading();
          pageData.requesting = false;
          loadInformationData()
          messageSuccess(data.message)
        } else {
          hideLoading()
          pageData.requesting = false;
          messageError(res.data.message)
        }
      })
        .catch(function (error) {
          messageError("服务器开小差了呢")
          hideLoading();
          pageData.requesting = false;
          console.log(error)
        })
    } else {
      messageError("请完善全部信息")
    }
  })
}

let uploadImg = async (f) => {
  pageData.requesting = true;
  showLoading();
  serviceFile.post('/api/user/changePhoto', { photo: f.file, token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      localStorage.setItem('token', data.token);
      let url = getBaseURL() + data.content;
      console.log(url);
      hideLoading();
      pageData.requesting = false;
      loadInformationData()
    } else {
      hideLoading();
      pageData.requesting = false;
      messageError(data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        pageData.requesting = false;
        messageError("服务器开小差了呢");
        console.log(error)
      })
}
let beforeAvatarUpload = (file) => {
  const isLt2M = (file.size / 1024 / 1024) < 2
  if (!isLt2M) {
    messageError('上传头像图片大小不能超过 2MB!')
  }
  return isLt2M
}


const hasUser = ref(true)

if (userId == undefined)
  hasUser.value = false;

const login = ref(false);

const emits = defineEmits(["getContent"])
const commentContent = ref("")
const commentEditorId = ref("vue-tinymce-" + +new Date() + ((Math.random() * 1000).toFixed(0) + ""))

const openEvaluateDrawer = ref(false)

const openEvaluate = () => {
  console.log('openEvaluate')
  openEvaluateDrawer.value = true;
}

const submitEvaluate = () => {
  showLoading();
  pageData.requesting = true;
  evaluateForm.userId = userId.toString();
  evaluateForm.token = localStorage.getItem('token');
  service.post('api/achievement/evaluate', evaluateForm).then(res => {
    const data = res.data;
    console.log('评价成功收到回调')
    console.log(res)
    if (data.success) {
      localStorage.setItem('token', data.token)
      hideLoading();
      pageData.requesting = false;
      init();
    } else {
      hideLoading();
      pageData.requesting = false;
      loginFailed();
    }
  })
}

const handleDrawerClose = () => {
  ElMessageBox.confirm('你确定要取消评价吗？', '提示', {
    type: "info",
    cancelButtonText: '取消',
    confirmButtonText: '确认'
  }).then(() => {
    openEvaluateDrawer.value = false;
  }).catch(() => {
    console.log('取消操作')
  })
}

const evaluateForm = reactive({
  name: '',
  description: '',
  userId: '',
  token: ''
})

const handleDeleteCheck = (item: Post) => {
  console.log(item.userId)
  console.log(getUserId())
  if (item.userId == getUserId()) {
    return true;
  } else {
    return false;
  }
}

const baseColorSet = [
  '#23a35d', '#85B8CB', '#1D6A96', '#245a74', '#a92939', '#ebba11', '#631F16'
]

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
    type: [String, Array],
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
interface Post {
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
    avatarURL: '',
    identity: '',
    nickName: '',
    name: '',
    gender: '',
    eMail: '',
    achievementList: [],
    follow: false,
    evaluateEnough: false,
  },
  startIndex: 0,
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
  targetContent: { content: '', commentList: [] },
  showDetail: false
})

const deleteThisPost = (target: Post, event: Event) => {
  event.stopPropagation();
  ElMessageBox.confirm('你确定要删除这条博客吗？', '警告', {
    type: "info",
    cancelButtonText: '取消',
    confirmButtonText: '确认'
  }).then(() => {
    showLoading();
    pageData.requesting = true;
    service.post('api/blog/delete', { token: localStorage.getItem("token"), blogId: target.id }).then(res => {
      const data = res.data;
      console.log('删除成功收到回调')
      console.log(res)
      if (data.success) {
        localStorage.setItem('token', data.token)
        hideLoading();
        pageData.postList = [];
        pageData.startIndex = 0;
        pageData.loadOver = false;
        refresh();
      } else {
        hideLoading();
        pageData.requesting = false;
        loginFailed();
      }
    })
  }).catch(() => {
    console.log('取消操作')
  })
}

const subscribe = () => {
  showLoading();
  pageData.requesting = true;
  service.post('api/blog/subscribe', { token: localStorage.getItem("token"), userId: userId }).then(res => {
    let data = res.data;
    if (data.success) {
      pageData.data.follow = true;
      localStorage.setItem('token', data.token)
    } else {
      loginFailed()
    }
    localStorage.setItem('token', data.token)
    pageData.requesting = false;
    hideLoading();
  })
}

const refresh = () => {
  if (!pageData.requesting) {
    if (pageData.loadOver) {
      // ElMessage({
      //   message: "已经到底了",
      //   type: 'error'
      // })
    } else {
      showLoading();
      pageData.requesting = true;
      service.post('/api/blog/getPersonalPost', { token: localStorage.getItem("token"), length: pageData.length, startIndex: pageData.startIndex, userId: userId }).then(res => {
        const data = res.data;
        console.log('开始refresh')
        console.log(res)
        login.value = data.success;
        let tempList = pageData.postList;
        for (let i = 0; i < data.content.length; i++) {
          data.content[i].avatarURL = getBaseURL() + data.content[i].avatarURL;
          data.content[i].topImageURL = getBaseURL() + data.content[i].topImageURL;
          tempList.push(data.content[i]);
        }
        pageData.postList = tempList;
        pageData.startIndex = data.startIndex;
        if (data.content.length < length) {
          pageData.loadOver = true;
        }
        localStorage.setItem('token', data.token)
        if (!data.success) {
          pageData.loadOver = true;
          if (data.message != '登录失效' && data.message != '查找成功') {
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
  if (hasUser.value) {
    showLoading()
    console.log(userId)
    pageData.requesting = true;
    //初始化方法
    service.post('api/blog/getPersonal', { token: localStorage.getItem("token"), userId: userId }).then(res => {
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
  } else {
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
  pageData.requesting = true;
  showLoading();
  service.post('api/blog/like', { token: localStorage.getItem("token"), blogId: target.id }).then(res => {
    const data = res.data;
    console.log('点赞成功收到回调')
    console.log(res)
    if (data.success) {
      //这里有问题
      target.like = !target.like;
      if (target.like) {
        target.likeNum += 1;
      } else {
        target.likeNum -= 1;
      }
      localStorage.setItem('token', data.token)
    } else {
      loginFailed()
    }
    hideLoading();
    pageData.requesting = false;
  })
}

const collectThis = (target: Post, event: Event) => {
  event.stopPropagation();
  console.log('likeThis()')
  console.log(target)
  pageData.requesting = true;
  showLoading();
  service.post('api/blog/collect', { token: localStorage.getItem("token"), blogId: target.id }).then(res => {
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
    pageData.requesting = false;
  })
}

const jumpToDetail = (target: Post) => {
  console.log('jumpToDetail()')
  console.log(target)
  pageData.target = target;
  pageData.requesting = true;
  showLoading();
  service.post('api/blog/getDetail', { token: localStorage.getItem("token"), blogId: target.id }).then(res => {
    const data = res.data;
    console.log('获取正文成功')
    console.log(res)
    login.value = data.success;
    let content = data.content;
    console.log(content)
    content.content = content.content.toString().replace('<img', "<img style='max-width:100%;height:auto;'")
    pageData.targetContent = content;
    localStorage.setItem('token', data.token)
    hideLoading();
    pageData.showDetail = true;
    pageData.requesting = false;
  })
}

const jumpToPersonal = (userId: string, event: Event) => {
  console.log('jumpToPersonal()')
  event.stopPropagation();
  router.push({
    path: '/personalPage',
    query: { userId: userId }
  })
}

const commentSubmit = () => {
  showLoading();
  if (commentContent.value.length == 0) {
    ElMessage({
      message: '评论不能为空',
      type: 'error'
    })
  } else {
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
.page {
  display: flex;
  background-color: #DDDDDD;
}

.leftArea {
  display: flex;
  flex-direction: column;
  padding-top: 5vh;
}

.infoCard {
  display: flex;
  background-color: #FFFFFF;
  border-radius: 2vw;
}

.userInfoCard {
  display: flex;
  flex-direction: row;
  border-radius: 2vw;
  background-color: #EEEEEE;
  min-height: 7vw;
}

.infoAvatar {
  width: 7vw;
  height: 7vw;
  border-radius: 3.5vw;
}

.infoNickName {
  width: 14vw;
  font-size: 3vw;
  line-height: 6vw;
  font-weight: bold;
  color: #555;
}

.detailInfo {
  display: flex;
  flex-direction: column;
  text-align: left;
  font-size: 1.5vw;
  border-radius: 2vw;
  background-color: #EEEEEE;
  margin: 3% 4%;
  min-height: 7vw;
  font-weight: bold;
  color: #555;
}

.subscribeButton {
  margin: auto;
  font-weight: bold;
}

.achievementCard {
  background-color: #FFFFFF;
  border-radius: 2vw;
  display: flex;
  flex-direction: column;
}

.achievement {
  color: #FFFFFF;
  border-radius: 1vh;
  font-size: 2.5vh;
  width: auto;
  padding: 0.3vh;
  display: inline-block;
}

.achievementArea {
  padding: 0.5vh;
}

.rightArea {
  display: flex;
  flex-direction: column;
  padding-top: 3vh;

}

.blogCard {
  border-radius: 2vw;
  background-color: #FFFFFF;
  height: 90vh;
}

.listArea {
  padding: 0;
  ;
}

.postBox {
  z-index: 5;
  border-radius: 2vw;
  width: 95%;
  background-color: #000;
  background-size: cover;
  background-position: center;
  color: #FFF;
  margin: auto;
}

.commentCard {
  background-color: #EEEEEE;
  border-radius: 1vw;
  padding-left: 1vw;
  box-shadow: 0 0 3px 0 rgba(140, 140, 140, 0.8);
}

.detailHeading {
  font-size: 5vh;
}

.userInfoArea {
  display: flex;
  flex-direction: row-reverse;
}

.detailTopImage {
  margin: 0;
  width: 75vw;
  height: 40vh;
  background-position: top;
  background-attachment: fixed;
  background-size: cover;
}

.avatarButton {
  width: 5vw;
  height: 5vw;
  border-radius: 2.5vw;
}

.avatar {
  width: 5vw;
  height: 5vw;
  border-radius: 2.5vw;
  position: center;
  size: auto;
}

.nickName {
  line-height: 4vw;
  font-weight: bold;
  font-size: 3vw;
}

.likeNum {
  font-weight: bold;
  font-size: 3.5vh;
  line-height: 3.5vh;
  width: 3vh;
}

.boxContainer {
  border-radius: 2vw;
  padding: 1vw;
}

.cardContent {
  flex-direction: column;
  display: flex;
}

.postBoxBottom {
  flex-direction: row;
  display: flex;
  width: 100%;
}

.bottomLeft {
  width: 50%;
}

.bottomRight {
  width: 50%;
}

.bottomTop {
  width: 100%;
  flex-direction: row-reverse;
  display: flex;
}

.likeCollectButton {
  width: 4vh;
  background-color: rgba(0, 0, 0, 0);
  border-style: none;
  color: #FFFFFF;
  font-size: 3vh;
}

.likeCollectImage {
  width: 4vh;
}

.bottomBottom {
  width: 100%;
  flex-direction: row-reverse;
  display: flex;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 146px;
  height: 146px;
  text-align: center;
}
.avatar-uploader .avatar {
  width: 146px;
  height: 146px;
  display: block;
}
</style>
