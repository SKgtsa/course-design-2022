<template>
  <div class="content">
    <el-container>
      <el-aside class="aside">
        <div class="selfImage">
          <el-upload class="avatar-uploader" action="#" :show-file-list="false" :before-upload="beforeAvatarUpload"
            :http-request="uploadImg" accept=".jpg,.jpeg,.png,.JPG,.JPEG">
            <img v-if="information.photoURL" class="avatar" :src="information.photoURL" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
        <div class="selfInf">
          <a class="infTitle">个人信息</a>
          <el-table :data="tableData.arr" size="mini" class="tableStyle">
            <el-table-column prop="title" label="" width="120">
            </el-table-column>
            <!--   <el-table-column prop="value" label="" width="120">
            </el-table-column> -->
          </el-table>
          <el-button type="default" class="changeButton" @click="checkInf" plain>
            <a>查看个人信息</a>
          </el-button>
          <el-button type="default" class="changeButton" @click="editInf" plain>
            <a>修改个人信息</a>
          </el-button>
        </div>
      </el-aside>
      <el-main class="main">
        <h1>个人简介</h1>
        <TEditor ref="editor" v-model="editorData" @click="onClick" />
      </el-main>
    </el-container>
  </div>
  <el-dialog v-model="centerDialogVisibleInfCheck" width="40%">
    <el-form :model="information" class="areaTextInput" ref="formData">
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
      <el-form-item label="班级:" prop="studentClass">
        <span>{{ information.studentClass }}</span>
      </el-form-item>
      <el-form-item label="政治面貌:" prop="politicalAffiliation">
        <span>{{ information.politicalAffiliation }}</span>
      </el-form-item>
      <el-form-item label="民族:" prop="ethnic">
        <span>{{ information.ethnic }}</span>
      </el-form-item>

      <el-form-item label="邮箱:" prop="email">
        <span>
          {{ information.email }}</span>
      </el-form-item>
      <el-form-item label="电话:" prop="phone">
        <span>{{ information.phone }}</span>
      </el-form-item>
    </el-form>
  </el-dialog>


  <!-- 修改个人信息 -->
  <el-dialog v-model="centerDialogVisibleInf" width="30%">
    <el-form :model="showForm" class="areaTextInput" ref="formData" :rules="rulesEditForm">
      <el-form-item label="邮箱:" prop="email">
        <el-input v-model="showForm.email">
          {{ showForm.email }}</el-input>
      </el-form-item>
      <el-form-item label="电话:" prop="phone">
        <el-input v-model="showForm.phone">{{ showForm.phone }}</el-input>
      </el-form-item>
    </el-form>
    <div class="dialogButtonPage">
      <el-button @click="sumbitEditRow" class="dialogButton">
        <a>确定</a>
      </el-button>
      <el-button @click="closeDialog" class="dialogButton">
        <a>取消</a>
      </el-button>
    </div>
  </el-dialog>
</template>
<script lang="ts" setup>
import service from '@/request';
import { messageWarning, messageError, messageInfo, messageSuccess } from '@/utils/message';
import { reactive, ref } from "vue";
import { showLoading, hideLoading } from '@/utils/loading';
import TEditor from '../../components/TEditor.vue'
import serviceFile from '@/request/indexFile';
//变量
//控制查看，更改信息弹出框
let centerDialogVisibleInf = ref(false);
let centerDialogVisibleInfCheck = ref(false);

let information = reactive({
  name: '',       //身份证都传了,要不都返回来
  email: null,
  phone: null,
  gender: '男',
  ethnic: '',
  politicalAffiliation:null,
  userNumber: '',
  password: '',
  studentClass: '',
  idCardNumber:null,
  photoURL: '',
  id: '',
});
let tableData = reactive(
  {
    arr: [
      {
        title: "姓名",
        value: '',
      },
      {
        title: '邮箱',
        value: '',
      },
      {
        title: '电话',
        value: '',
      },
      {
        title: '性别',
        value: '',
      },
    ]
  })
let editorData = ref();
const onClick = () => {

}
let uploadImg = async (f) => {
  showLoading();
  await serviceFile.post('/api/user/changePhoto', { photo: f.file, token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    console.log(res)
    if (data.success) {
      hideLoading();
      localStorage.setItem('token', data.token);
      information.photoURL = 'http://courseback.clankalliance.cn' + data.content;
      messageSuccess("更换成功！")
    } else {
      hideLoading();
      messageError(data.message)
    }
  })
    .catch(function (error) {
      hideLoading();
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

let formData = ref();
const validateemail = (rule, value, callback) => {  //检验邮箱
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入邮箱！'));
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱'));
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
    }
  }
}

let rulesEditForm = reactive({
  email: [{ validator: validateemail, trigger: 'blur' },],
  phone: [{ validator: validatePhone, trigger: 'blur' },],
})
/* let backData = ref(); */
//函数

const loadInformationData = async () => {   //查看个人信息
  showLoading();
  service.post('/api/user/myInfo', { token: localStorage.getItem("token") }).then(res => {
    if (res.data.success) {
      console.log(res);
      const data = res.data;
      let content = data.user;
      console.log(content)
      information.name = content.name;
      information.email = content.email;
      information.phone = content.phone;
      if (content.gender == false) { information.gender = '男'; }
      else information.gender = '女';
      information.ethnic = content.ethnic,
        information.politicalAffiliation = content.politicalAffiliation,
        information.userNumber = content.userNumber,
        information.password = content.password,
        information.studentClass = content.studentClass,
        information.idCardNumber = content.idCardNumber;
      let url = 'http://courseback.clankalliance.cn' + content.photoURL;
      information.photoURL = url,
        information.id = content.id,
        localStorage.setItem('token', data.token)
      tableData.arr[0].value = information.name;
      tableData.arr[1].value = information.email;
      tableData.arr[2].value = information.phone;
      tableData.arr[3].value = information.gender;
      hideLoading()
    } else {
      hideLoading()
      messageWarning(res.data.message)
    }
  })
    .catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
}
loadInformationData();
//进入时自动执行调用 */

const checkInf = () => {
  centerDialogVisibleInfCheck.value = true;
}
let editForm = reactive({
  name: '',       //身份证都传了,要不都返回来
  email: '',
  phone: '',
  gender: '男',
  ethnic: '',
  politicalAffiliation:'',
  userNumber: '',
  password: '',
  studentClass: '',
  idCardNumber:'',
  photoURL: '',
  id: '',
});
let showForm = reactive({
  email:'',
  phone:'',
})
const editInf = () => {
  editForm.name=information.name,       //身份证都传了,要不都返回来
  editForm.email= information.email,
  editForm.phone= information.phone,
  editForm.gender= information.gender,
  editForm.ethnic= information.ethnic,
  editForm.politicalAffiliation=information.politicalAffiliation,
  editForm.userNumber= information.userNumber,
  editForm.password= information.password,
  editForm.studentClass=information.studentClass,
  editForm.idCardNumber= information.idCardNumber,
  editForm.photoURL= information.photoURL,
  editForm.id=information.id,
  showForm.email = information.email;
  showForm.phone = information.phone;
  centerDialogVisibleInf.value = true;
}
const closeDialog = () => {
  messageInfo("取消编辑")
  centerDialogVisibleInf.value = false;
}

const sumbitEditRow = async () => {
  await formData.value.validate((valid) => {
    if (valid) {
      showLoading();
      service.post('/api/user/editInfo', {
        token: localStorage.getItem("token"), //这个修改信息，绑定的值
        //是editForm绑定还是用formData绑定？
        id: editForm.id, phone: showForm.phone, email: showForm.email,
        gender: editForm.gender, ethnic: editForm.ethnic, politicalAffiliation: editForm.politicalAffiliation,
        userNumber: editForm.userNumber, name: editForm.name, studentClass: editForm.studentClass,
        idCardNumber: editForm.idCardNumber, photoURL: editForm.photoURL
      }).then(res => {
        if (res.data.success) {
          const data = res.data;
          let content = data.user;
          console.log(data.user)
          information.name = content.name;       //身份证都传了,要不都返回来
          information.email = content.email;
          information.phone = content.phone,
            information.gender = content.gender,
            information.ethnic = content.ethnic,
            information.politicalAffiliation = content.politicalAffiliation,
            information.userNumber = content.userNumber,
            information.password = content.password,
            information.studentClass = content.studentClass,
            information.idCardNumber = content.idCardNumber,
            information.photoURL = content.photoURL,
            information.id = content.id,
            localStorage.setItem('token', data.token)
          tableData.arr[0].value = information.name;
          tableData.arr[1].value = information.email;
          tableData.arr[2].value = information.phone;
          tableData.arr[3].value = information.gender;
          hideLoading();
          messageSuccess("提交成功！")
        } else {
          messageWarning(res.data.message)
        }
      })
        .catch(function (error) {
          messageError("服务器开小差了呢")
          hideLoading();
          console.log(error)
        })
    } else {
      messageError("请完善全部信息")
    }
  })
}


</script>
<style lang="scss" scoped>
.content {
  display: flex;
  flex-direction: row;
  height: 100vh;

  .aside {
    display: flex;
    flex-direction: column;
    height: 85vh;
    width: 24vw;
    padding-top: 5vh;
    padding-left: 4vw;

    /*       border-radius: 1vw;
      box-shadow: 0 0 10px 0 #b9ccee; */
    .selfImage {
      border-radius: 1vw;
      width: 18vw;
      height: 20vh;
      display: flex;
      padding-top: 1vh;
      justify-content: center;
      background-color: #FFFFFF;
      border-radius: 1vw;
      box-shadow: 0 0 10px 0 #b9ccee;

      .image {
        display: flex;
      }
    }

    .selfInf {
      border-radius: 1vw;
      box-shadow: 0 0 10px 0 #b9ccee;
      display: flex;
      flex-direction: column;
      margin-top: 2vh;
      width: 18vw;
      height: 70vh;
      background-color: #f5f8fd;

      .infTitle {
        margin-top: 1vh;
        font-size: 16px;
        font-weight: bolder;
        text-align: center;
        /*     color: #7d8be5; */
      }

      .tableStyle {
        display: flex;
        border-top: 1px solid #888;
        border-bottom: 1px solid #888;
        margin-top: 2vh;
        padding-top: 1vh;
        padding-left: 2vw;
        padding-bottom: 2vh;
      }

      .changeButton {
        display: flex;
        margin-top: 3vh;
        justify-content: center;
        margin-left: 2vw;
        width: 14vw;
        height: 4vh;
        color: #0808ad;
      }
    }
  }

  .main {
    margin-top: 5vh;
    margin-right: 3vw;
    height: 80vh;
    width: 60vw;
    border-radius: 1vw;
    box-shadow: 0 0 10px 0 #b9ccee;
    background-color: #FFFFFF;
  }
}

//下面是弹出框，在布局中独立，没有写入子样式中
.areaTextInput {
  font-size: 15px;
}

.dialogButtonPage {
  text-align: center;
}

.dialogButton {
  padding-left: 5px;
  padding-right: 5px;
}


.avatar-uploader .avatar {
  width: 146px;
  height: 146px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
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
</style>