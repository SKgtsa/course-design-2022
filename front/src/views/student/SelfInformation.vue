<template>
  <div class="content">
    <el-container>
      <el-aside class="aside">
        <div class="selfImage">
          <AvatarUploadTestViewVue class="image" />
        </div>
        <div class="selfInf">
          <a class="infTitle">个人信息</a>
          <el-table :data="tableData" size="mini" class="tableStyle">
            <el-table-column prop="title" label="" width="120">
            </el-table-column>
            <el-table-column prop="value" label="" width="120">
            </el-table-column>
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
        <TEditor />
      </el-main>
    </el-container>
  </div>
  <el-dialog v-model="centerDialogVisibleInfCheck" width="40%">
    <el-form :model="information" class="areaTextInput">
      <el-form-item label="学号:" prop="userNumber">
        <span>{{ information.userNumber }}</span>
      </el-form-item>
      <el-form-item label="姓名:" prop="name">
        <span>{{ information.name }}</span>
      </el-form-item>
      <el-form-item label="性别:" prop="gender">
        <span>{{ information.gender }}</span>
      </el-form-item>
      <el-form-item label="身份证号:" prop="idcardNumber">
        <span>{{ information.idcardNumber }}</span>
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

      <el-form-item label="邮箱:" prop="eMail">
        <span>
          {{ information.eMail }}</span>
      </el-form-item>
      <el-form-item label="电话:" prop="phone">
        <span>{{ information.phone }}</span>
      </el-form-item>
      <el-form-item label="地址:" prop="location">
        <span>{{ information.location }}</span>
      </el-form-item>
    </el-form>
  </el-dialog>


  <!-- 修改个人信息 -->
  <el-dialog v-model="centerDialogVisibleInf" width="30%">
    <el-form :model="editForm" class="areaTextInput" ref="formData" :rules="rulesEditForm">
      <el-form-item label="邮箱:" prop="eMail">
        <el-input v-model="editForm.eMail">
          {{ editForm.eMail }}</el-input>
      </el-form-item>
      <el-form-item label="电话:" prop="phone">
        <el-input v-model="editForm.phone">{{ editForm.phone }}</el-input>
      </el-form-item>
      <el-form-item label="地址:" prop="location">
        <el-input v-model="editForm.location">{{ editForm.location }}</el-input>
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
import AvatarUploadTestViewVue from '@/components/AvatarUploadTestView.vue';
import service from '@/request';
import { messageWarning, messageError, messageInfo, messageSuccess } from '@/utils/message';
import { reactive, ref } from "vue";
import { setNickName, setAvatarURL } from '../../global/global';
import TEditor from '../../components/TEditor.vue'
//变量

let centerDialogVisibleInf = ref(false);
let centerDialogVisibleNick = ref(false);
let centerDialogVisibleInfCheck = ref(false);
/* let information = reactive({}); */
let selfId = ref('');
/* nickName.value = '';
avatarURL.value = ''; */
let information = reactive({
  name: '彭帅豪',
  eMail: '859455675@qq.com',
  phone: '12345678910',
  location: '山东大学软件园校区',
  gender: '',
  ethnic: '',
  politicalAffiliation: '',
  userNumber: '',
  password: '',
  studentClass: '',
  idcardNumber: '',
  photoURL: '',
  id: '',
})

const tableData = reactive([
  {
    title: "姓名",
    value: information.name,
  },
  {
    title: '邮箱',
    value: information.eMail,
  },
  {
    title: '电话',
    value: information.phone,
  },
  {
    title: '地址',
    value: information.location,
  },
])

/* const formData = ref(); */
let editForm = reactive({  //家庭成员这个感觉可以不弄了，显示的时候不太好显示
  name: information.name,       //身份证都传了,要不都返回来
  eMail: information.eMail,
  phone: information.phone,
  location: information.location,
  gender: information.gender,
  ethnic: information.ethnic,
  politicalAffiliation: information.politicalAffiliation,
  userNumber: information.userNumber,
  password: information.password,
  studentClass: information.studentClass,
  idcardNumber: information.idcardNumber,
  photoURL: information.photoURL,
  id: information.id,
});
/* let editForm = reactive({}); */
let formData = ref();
const validateEMail = (rule, value, callback) => {  //检验邮箱
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

const rulesEditForm = reactive({
  eMail: [{ validator: validateEMail, trigger: 'blur' },],
  phone: [{ validator: validatePhone, trigger: 'blur' },],
  location: [{ require: true, message: "请输入地址", trigger: 'blur' }]

})
/* let backData = ref(); */
//函数

/* const loadInformationData =async () => {   //查看个人信息
  service.post('/api/user/myInfo', { token: localStorage.getItem("token") }).then(res => {
    if (res.data.success) {
      const data = res.data;
      let arr = data.content; //拿到了返回的数组
      backData = arr
      localStorage.setItem('token', data.token)
    } else {
      messageWarning(res.data.message)
    }
  })
}
loadInformationData();//进入时自动执行调用 */

const getInf = () => {
  service.post('/api/user/myInfo', { token: localStorage.getItem('token') }).then(res => {
    let data = res.data;
    if (data.success) {
      localStorage.setItem('token', data.token);
      information = data.content;
    } else {
      messageError(data.message);
    }
  })
}

const checkInf = () => {
  centerDialogVisibleInfCheck.value = true;

}
const editInf = () => {
  getInf(); //编辑的时候先调用获取数据的
  editForm = information; //把information的信息给editForm
  centerDialogVisibleInf.value = true;
}
const closeDialog = () => {
  messageInfo("取消编辑")
  centerDialogVisibleInf.value = false;
}

const sumbitEditRow = () => {
  formData.value.validate((valid) => {
    if (valid) {
      service.post('/api/user/editInfo', {
        token: localStorage.getItem("token"), //这个修改信息，绑定的值
        //是editForm绑定还是用formData绑定？
        id: editForm.id, phone: editForm.phone, eMail: editForm.eMail,
        gender: editForm.gender, ethnic: editForm.ethnic, politicalAffiliation: editForm.politicalAffiliation,
        userNumber: editForm.userNumber, name: editForm.name, studentClass: editForm.studentClass,
        idcardNumber: editForm.idcardNumber, photoURL: editForm.photoURL
      }).then(res => {
        if (res.data.success) {
          const data = res.data;
          information = data.content;
          localStorage.setItem('token', data.token)
          messageSuccess("提交成功！")
        } else {
          messageWarning(res.data.message)
        }
      })
        .catch(function (error) {
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
</style>