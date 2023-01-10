<template>
  <div class="content" :style="{
    'padding-top': `${mobile ? '1' : '0.5'}vh`,
    'height': `${mobile ? '88' : '90'}vh`
  }">
    <div class="mainCard" :style="{
      'width': `${mobile ? '100%' : '90%'}`,
      'margin': `${mobile ? '0 auto' : '0 8%'}`
    }">
      <div class="header" :style="{
        'flex-direction': `${mobile ? 'column' : 'row'}`
      }">
        <div class="leftPanel" :style="{
          'width': `${mobile ? '100%' : '60%'}`
        }">
          <a class="title">信息管理</a>
          <div class="searchOrigin">
            <span class="searchSpan">
              <el-input v-model="searchOrigin" type="text" class="searchTerm" maxlength="15" placeholder="输入学工号" />
              <el-button type="submit" class="searchButton" @click="check">
                <el-icon class="icon">
                  <Search />
                </el-icon>
              </el-button>
              <el-button type="danger" class="deleteButton" v-if="characterHasFound">
                <a class="deleteText">删除用户</a>
              </el-button>
            </span>
          </div>
        </div>
        <div class="rightPanel" v-if="characterHasFound" :style="{
          'width': `${mobile ? '100%' : '40%'}`
        }">
          <div class="imgBox">
            <el-image class="img"
              :src="getBaseURL() + (userIsTeacher ? userTeacherForm.avatarURL : userStudentForm.avatarURL)" fit="cover">
            </el-image>
          </div>
          <div class="imgBox">
            <el-image class="img"
              :src="getBaseURL() + (userIsTeacher ? userTeacherForm.photoURL : userStudentForm.photoURL)" fit="cover">
            </el-image>
          </div>
        </div>
      </div>
    </div>
    <div class="operationArea" :style="{
      'margin': `${mobile ? '0' : '0 8%'}`
    }" v-if="findUser">
      <div class="bottomCard" :style="{
        'flex-direction': `${mobile ? 'column' : 'row'}`,
        'width': `${mobile ? 100 : 90}%`,
      }">
        <div class="selectCard" :style="{
          'width': `${mobile ? 100 : 20}%`
        }">
          <div class="menu" :style="{
            'flex-direction': `${mobile ? 'row' : 'column'}`,
          }">
            <span class="menuButtonSpan" :style="{
              'width': `${mobile && !userIsTeacher ? 25 : 100}%`
            }">
              <el-button class="menuButton" @click="clickInformation">
                <a>个人信息</a>
              </el-button>
            </span>
            <span class="menuButtonSpan" :style="{
              'width': `${mobile ? 25 : 100}%`
            }" v-if="characterIsStudent">
              <el-button class="menuButton" @click="clickPractice">
                <a>社会实践</a>
              </el-button>
            </span>
            <span class="menuButtonSpan" :style="{
              'width': `${mobile ? 25 : 100}%`
            }" v-if="characterIsStudent">
              <el-button class="menuButton" @click="clickReward">
                <a>成果奖励</a>
              </el-button>
            </span>
            <span class="menuButtonSpan" :style="{
              'width': `${mobile ? 25 : 100}%`
            }" v-if="characterIsStudent">
              <el-button class="menuButton" @click="clickActivity">
                <a>课外活动</a>
              </el-button>
            </span>
          </div>
        </div>
        <div class="operationCard" :style="{
          'width': `${mobile ? 100 : 80}%`,
          'height': `${mobile ? 90 : 50}vh`
        }">
          <el-scrollbar v-if="informationShow">
            <div v-if="informationShow" class="information">
              <div v-if="characterIsStudent">
                <el-form :model="userStudentForm" class="areaTextInput" ref="formData" :rules="rules" label-width="auto">
                  <el-form-item label="学号:" prop="userNumber">
                    <el-input v-model="userStudentForm.userNumber" :style="{
                      'width':`${mobile?'60':'40'}vw`
                    }" maxlength="16" type="number"></el-input>
                  </el-form-item>
                  <el-form-item label="姓名:" prop="name">
                    <el-input v-model="userStudentForm.name" maxlength="10"></el-input>
                  </el-form-item>
                  <el-form-item label="昵称:" prop="nickName">
                    <el-input v-model="userStudentForm.nickName" maxlength="8"></el-input>
                  </el-form-item>
                  <el-form-item label="性别:" prop="gender">
                    <el-radio-group v-model="userStudentForm.gender">
                      <el-radio :label="false">男</el-radio> 
                      <el-radio :label="true">女</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="政治面貌:" prop="politicalAffiliation">
                    <el-select v-model="userStudentForm.politicalAffiliation" 
                       placeholder="选择政治面貌">
                      <el-option label="群众" value="群众" />
                      <el-option label="共青团员" value="共青团员" />
                      <el-option label="共产党员" value="共产党员" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="班级:" prop="studentClass">
                    <el-input v-model="userStudentForm.studentClass" :style="{
                      'width':`${mobile?'30':'15'}vw`}" maxlength="2" type="number"></el-input>
                  </el-form-item>
                  <el-form-item label="学生届次:" maxlength="4" :style="{
                      'width':`${mobile?'30':'20'}vw`}" type="number" prop="section">
                    <el-input v-model="userStudentForm.section"></el-input>
                  </el-form-item>
                  <el-form-item label="身份证号:" prop="idCardNumber">
                    <el-input v-model="userStudentForm.idCardNumber" maxlength="18"></el-input>
                  </el-form-item>
                  <el-form-item label="民族:" prop="ethnic">
                    <el-input v-model="userStudentForm.ethnic" maxlength="8"></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱:" prop="email">
                    <el-input v-model="userStudentForm.email" maxlength="30"></el-input>
                  </el-form-item>
                  <el-form-item label="电话:" prop="phone">
                    <el-input v-model="userStudentForm.phone" maxlength="11"></el-input>
                  </el-form-item>
                </el-form>
              </div>
              <div v-else>
                <el-form :model="userTeacherForm" class="areaTextInput" label-width="auto" ref="formData" :rules="rules">
                  <el-form-item label="工号:" prop="userNumber">
                    <el-input v-model="userTeacherForm.userNumber" maxlength="16"></el-input>
                  </el-form-item>
                  <el-form-item label="姓名:" prop="name">
                    <el-input v-model="userTeacherForm.name" maxlength="10"></el-input>
                  </el-form-item>
                  <el-form-item label="昵称:" prop="nickName">
                    <el-input v-model="userTeacherForm.nickName" maxlength="8"></el-input>
                  </el-form-item>
                  <el-form-item label="性别:" prop="gender">
                    <el-radio-group v-model="userStudentForm.gender">
                      <el-radio :label="false">男</el-radio> 
                      <el-radio :label="true">女</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="身份证号:" prop="idCardNumber">
                    <el-input v-model="userTeacherForm.idCardNumber" maxlength="18"></el-input>
                  </el-form-item>
                  <el-form-item label="政治面貌:" prop="politicalAffiliation">
                    <el-input v-model="userTeacherForm.politicalAffiliation"></el-input>
                  </el-form-item>
                  <el-form-item label="民族:" prop="ethnic">
                    <el-input v-model="userTeacherForm.ethnic" maxlength="8"></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱:" prop="email">
                    <el-input v-model="userTeacherForm.email" maxlength="30"></el-input>
                  </el-form-item>
                  <el-form-item label="电话:" prop="phone">
                    <el-input v-model="userTeacherForm.phone" maxlength="11"></el-input>
                  </el-form-item>
                  <el-form-item label="科研方向:" prop="researchDirection">
                    <el-input v-model="userTeacherForm.researchDirection" maxlength="30"></el-input>
                  </el-form-item>
                </el-form>
              </div>
              <el-button @click="submitInfo" :style="{
                'margin-left':`${mobile?'30':'30'}vw`
              }">提交修改</el-button>
            </div>
          </el-scrollbar>
          <div v-if="practiceShow">
              <el-table :data="practiceData.arr" stripe size="large" class="practiceTable"
                :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
                :cell-style="{ 'height': '1.75vh', 'font-size': '1.75vh', 'text-align': 'center', 'font-weight': '450' }">
                <el-table-column label="社会实践标题" prop="name" width="300" show-overflow-tooltip />
                <el-table-column label="描述" prop="description" width="300" show-overflow-tooltip></el-table-column>
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="medium" @click="checkPractice(scope.row)" class="button" type="primary">查看
                    </el-button>
                    <el-button size="medium" type="danger" class="button" @click="deletePractice(scope.row)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-dialog v-model="practiceDialog" width="45%" draggable="true">
                <el-form :model="practiceForm" class="areaTextInput">
                  <el-form-item label="标题" prop="practiceName">
                    <span v-if="practiceTypeOperation === 'check'">{{ practiceForm.practiceName }}</span>
                  </el-form-item>
                  <el-form-item label="内容" prop="practiceDescription">
                    <span v-if="practiceTypeOperation === 'check'">{{ practiceForm.practiceDescription }}</span>
                  </el-form-item>
                </el-form>
              </el-dialog>
          </div>
          <div v-if="rewardShow">
              <el-table :data="rewardData.arr" stripe size="large" class="rewardTable"
                :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
                :cell-style="{ 'height': '1.75vh', 'font-size': '1.75vh', 'text-align': 'center', 'font-weight': '450' }">
                <!-- <el-table-column label="日期" prop="date" width="240" show-overflow-tooltip /> -->
                <el-table-column label="成果奖励标题" prop="name" width="300" show-overflow-tooltip />
                <el-table-column label="描述" prop="description" width="300" show-overflow-tooltip></el-table-column>
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="medium" @click="checkReward(scope.row)" class="button" type="primary">查看
                    </el-button>
                    <el-button size="medium" type="danger" class="button" @click="deleteReward(scope.row)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-dialog v-model="rewardDialog" width="45%" draggable="true">
                <el-form :model="rewardForm" class="areaTextInput">
                  <el-form-item label="标题" prop="rewardName">
                    <span v-if="rewardTypeOperation === 'check'">{{ rewardForm.rewardName }}</span>
                  </el-form-item>
                  <el-form-item label="内容" prop="rewardDescription">
                    <span v-if="rewardTypeOperation === 'check'">{{ rewardForm.rewardDescription }}</span>
                  </el-form-item>
                </el-form>
              </el-dialog>
          </div>
          <div v-if="activityShow">
              <el-table :data="activityData.arr" stripe size="large" class="practiceTable"
                :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
                :cell-style="{ 'height': '1.75vh', 'font-size': '1.75vh', 'text-align': 'center', 'font-weight': '450' }">
                <el-table-column label="课外活动标题" prop="name" width="300" show-overflow-tooltip />
                <el-table-column label="描述" prop="description" width="300" show-overflow-tooltip></el-table-column>
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="medium" @click="checkActivity(scope.row)" class="button" type="primary">查看
                    </el-button>
                    <el-button size="medium" type="danger" class="button" @click="deleteActivity(scope.row)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-dialog v-model="activityDialog" width="45%" draggable="true">
                <el-form :model="activityForm" class="areaTextInput">
                  <el-form-item label="日期" prop="date">
                    <span v-if="activityTypeOperation === 'check'">{{ activityForm.date }}</span>
                  </el-form-item>
                  <el-form-item label="标题" prop="practiceName">
                    <span v-if="activityTypeOperation === 'check'">{{ activityForm.practiceName }}</span>
                  </el-form-item>
                  <el-form-item label="内容" prop="practiceDescription">
                    <span v-if="activityTypeOperation === 'check'">{{ activityForm.practiceDescription }}</span>
                  </el-form-item>
                  <el-form-item label="成果" prop="result">
                    <span v-if="activityTypeOperation === 'check'">{{ activityForm.result }}</span>
                  </el-form-item>
                </el-form>
              </el-dialog>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { hideLoading, showLoading } from "@/utils/loading";
import service from "@/request";
import { messageError, messageSuccess, messageWarning } from "@/utils/message";
import { ElMessageBox } from "element-plus";
import { getBaseURL, mobile } from "@/global/global";
import { loginFailed } from "@/utils/tokenCheck";
import { trigger } from '@vue/reactivity';

const userIsTeacher = ref(false)
const findUser = ref(false)
let currentPage = ref(1);
let pageSize = ref(7);
let pageCount = ref();
let searchOrigin = ref();
let search = ref();
let practiceShow = ref(false);
let informationShow = ref(true);
let activityShow = ref(false);
let rewardShow = ref(false);
let characterIsStudent = ref(true);//false是老师
let characterHasFound = ref(false);
let practiceTypeOperation = ref(''); //edit,check,add 编辑，查看，添加
let rewardTypeOperation = ref('');
let activityTypeOperation = ref('');
let practiceDialog = ref(false);//查的弹出框
let rewardDialog = ref(false);//查的弹出框
let activityDialog = ref(false);//查的弹出框
let practiceData = reactive({
  arr: [],
});
let rewardData = reactive({
  arr: [],
});
let activityData = reactive({
  arr: [],
});
let userData = reactive({
  arr: [],
});
let practiceForm = reactive({
  practiceName: '',
  practiceDescription: '',
  id: '',
});
let rewardForm = reactive({
  rewardName: '',
  rewardDescription: '',
  id: '',
});
let activityForm = reactive({
  practiceName: '',
  practiceDescription: '',
  date: '',
  result: '',
  id: '',
});
let userStudentForm = reactive({
  id: '',
  userNumber: '',
  nickName: '',
  name: '',
  phone: '',
  studentClass: '',
  section: '',
  idCardNumber: '',
  gender: false,
  ethnic: '',
  politicalAffiliation: '',
  email: '',
  avatarURL: '',
  photoURL: '',
});
let userTeacherForm = reactive({
  id: '',
  userNumber: '',
  nickName: '',
  name: '',
  phone: '',
  idCardNumber: '',
  gender: '',
  ethnic: '',
  politicalAffiliation: '',
  email: '',
  avatarURL: '',
  photoURL: '',
  researchDirection: ''
});
let formData = ref();
let isLoad = ref(false);
const submitInfo = async () => {
  console.log('进入提交方法')
  await formData.value.validate((valid) => {
    console.log('校验方法')
    if (valid) {
      console.log('校验通过')
      isLoad.value = false;
      showLoading();
      if (characterIsStudent.value == true) {
        console.log(userStudentForm);
        service.post('/api/user/managerSave', {
          token: localStorage.getItem("token"), //这个修改信息，绑定的值
          //是userStudentForm绑定还是用formData绑定？
          id: userStudentForm.id,
          userNumber: userStudentForm.userNumber,
          nickName: userStudentForm.nickName,
          name: userStudentForm.name,
          phone: userStudentForm.phone,
          studentClass: userStudentForm.studentClass,
          idCardNumber: userStudentForm.idCardNumber,
          gender: userStudentForm.gender,
          ethnic: userStudentForm.ethnic,
          politicalAffiliation: userStudentForm.politicalAffiliation,
          email: userStudentForm.email,
          avatarURL: userStudentForm.avatarURL,
          photoURL: userStudentForm.photoURL,
          section: userStudentForm.section,
          researchDirection:'',
        }).then(res => {
          console.log('返回了数据')
          console.log(res)
          if (res.data.success) {
            let data = res.data;
            localStorage.setItem('token', data.token)
            hideLoading();
            isLoad.value = true;
            messageSuccess(data.message)
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
        console.log(userTeacherForm);
        service.post('/api/user/managerSave', {
          token: localStorage.getItem("token"), //这个修改信息，绑定的值
          //是userTeacherForm绑定还是用formData绑定？
          id: userTeacherForm.id,
          userNumber: userTeacherForm.userNumber,
          nickName: userTeacherForm.nickName,
          name: userTeacherForm.name,
          phone: userTeacherForm.phone,
          studentClass: '',
          idCardNumber: userTeacherForm.idCardNumber,
          gender: userTeacherForm.gender,
          ethnic: userTeacherForm.ethnic,
          politicalAffiliation: userTeacherForm.politicalAffiliation,
          email: userTeacherForm.email,
          avatarURL: userTeacherForm.avatarURL,
          photoURL: userTeacherForm.photoURL,
          researchDirection: userTeacherForm.researchDirection,
          section: '',
        }).then(res => {
          console.log('返回了数据')
          console.log(res)
          if (res.data.success) {
            let data = res.data;
            localStorage.setItem('token', data.token)
            hideLoading();
            isLoad.value = true;
            messageSuccess(data.message)
          } else {
            messageWarning(res.data.message)
          }
        })
          .catch(function (error) {
            messageError("服务器开小差了呢")
            hideLoading();
            console.log(error)
          })
      }
    } else {
      messageError("请完善全部信息")
    }
  })
}
const validateName = (rule, value, callback) => {  //校验姓名，考虑少数民族
  const reg = /(^[\u4e00-\u9fa5]{1}[\u4e00-\u9fa5\.·.]{0,18}[\u4e00-\u9fa5]{1}$)|(^[a-zA-Z]{1}[a-zA-Z\s]{0,18}[a-zA-Z]{1}$)/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入姓名！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的姓名！'));
    } else {
      callback();
    }
  }
}
const validatepassword = (rule, value, callback) => {   //校验密码复杂度
  const reg = /^(?!([A-Z]*|[a-z]*|[0-9]*|[!-/:-@\[-`{-~]*|[A-Za-z]*|[A-Z0-9]*|[A-Z!-/:-@\[-`{-~]*|[a-z0-9]*|[a-z!-/:-@\[-`{-~]*|[0-9!-/:-@\[-`{-~]*)$)[A-Za-z0-9!-/:-@\[-`{-~]{6,16}$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请设置您的密码！'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('密码中必须包含字母、数字、特殊字符,长度在6-16位之间'));
    } else {
      callback();
    }
  }
}

const validateIdCardNumber = (rule, value, callback) => {  //检验身份证号(精确校验)
  const reg = /^[1-9]\d{5}(19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
  if (value == '' || value == undefined || value == null) {
    callback(new Error('请输入身份证号'));
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码！'));
    } else {
      callback();
    }
  }
}
const validateEmail = (rule, value, callback) => {  //检验邮箱
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
  name: [{ validator: validateName, trigger: 'blur' },
  { max: 10, message: '姓名没有超过10位', trigger: 'blur' }],
  userNumber: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'blur' }],
  idCardNumber: [{ validator: validateIdCardNumber, trigger: 'blur' }],
  email: [{ validator: validateEmail, trigger: 'blur' }],
  ethnic: [{ required: true, message: '请填写您的民族', triggwe: 'blur' }],
  politicalAffiliation: [{ required: true, message: '请选择您的政治面貌', triggwe: 'blur' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  studentClass: [{ required: true, message: '请输入学生班级', trigger: 'blur' }],
  section: [{ required: true, message: '请输入学生届', trigger: 'blur' }],
  nickName: [{ required: true, message: '请输入用户名', trigger: 'blur' },
  { max: 8, message: '长度请不要超过8位', trigger: 'blur' }],
  researchDirection:[{required:true,message:'请输入科研方向',trigger:'blur'}]
})

const clickPractice = async() => {
  await loadPracticeTable();
  practiceShow.value = true;
  activityShow.value = false;
  rewardShow.value = false;
  informationShow.value = false;

}
const clickReward = async() => {
  await loadRewardTable();
  practiceShow.value = false;
  activityShow.value = false;
  rewardShow.value = true;
  informationShow.value = false;
 
}
const clickActivity = async() => {
  await loadActivityTable();
  practiceShow.value = false;
  activityShow.value = true;
  rewardShow.value = false;
  informationShow.value = false;
  
}
const clickInformation = () => {
  practiceShow.value = false;
  activityShow.value = false;
  rewardShow.value = false;
  informationShow.value = true;
}
const check = () => {
  search.value = searchOrigin.value;
  checkUser(search.value);
}
const checkUser = async (userNumber) => {   //查看个人信息
  if (userNumber == '' || userNumber == undefined || userNumber == null) {
    search.value = '';
    return;
  }
  showLoading();
  await service.post('/api/user/managerFind', {
    token: localStorage.getItem("token"),
    userNumber: userNumber,
  }).then(res => {
    console.log('返回了数据')
    let data = res.data;
    console.log(data)
    if (res.data.success) {
      console.log(search.value);
      hideLoading();
      localStorage.setItem('token', data.token);
      messageSuccess("查询成功！")
      characterHasFound.value = true;
      let content = data.content;
      if (content.studentClass == undefined) {
        characterIsStudent.value = false;
        userTeacherForm.id = content.id;
        userTeacherForm.userNumber = content.userNumber;
        userTeacherForm.nickName = content.nickName;
        userTeacherForm.name = content.name;
        userTeacherForm.phone = content.phone;
        userTeacherForm.idCardNumber = content.idCardNumber;
        userTeacherForm.gender = content.gender;
        userTeacherForm.ethnic = content.ethnic;
        userTeacherForm.politicalAffiliation = content.politicalAffiliation;
        userTeacherForm.email = content.email;
        userTeacherForm.userNumber = content.userNumber;
        userTeacherForm.avatarURL = content.avatarURL;
        userTeacherForm.photoURL = content.photoURL;
        userTeacherForm.researchDirection = content.researchDirection;
        userIsTeacher.value = true;
        console.log('查询用户为老师')
        console.log(userTeacherForm)
      } else {
        characterIsStudent.value = true;
        userStudentForm.id = content.id;
        userStudentForm.userNumber = content.userNumber;
        userStudentForm.nickName = content.nickName;
        userStudentForm.name = content.name;
        userStudentForm.phone = content.phone;
        userStudentForm.studentClass = content.studentClass;
        userStudentForm.idCardNumber = content.idCardNumber;
        userStudentForm.gender = content.gender;
        userStudentForm.ethnic = content.ethnic;
        userStudentForm.politicalAffiliation = content.politicalAffiliation;
        userStudentForm.email = content.email;
        userStudentForm.userNumber = content.userNumber;
        userStudentForm.avatarURL = content.avatarURL;
        userStudentForm.photoURL = content.photoURL;
        userStudentForm.section = content.section;
        userIsTeacher.value = false;
        console.log('查询用户为学生')
        console.log(userStudentForm)
      }
      findUser.value = true;
    } else {
      hideLoading()
      loginFailed();
    }
  })
    .catch(function (error) {
      hideLoading();
      loginFailed();
      console.log(error)
    })
}
const checkPractice = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  practiceDialog.value = true;
  practiceForm.practiceDescription = row.description;
  practiceForm.practiceName = row.name;
  practiceForm.id = row.id;
  practiceTypeOperation.value = 'check';
}
const loadPracticeTable = () => {
  showLoading();
  console.log('更新practice')
  console.log(userStudentForm)
  service.post('/api/practice/managerFind', {
    token: localStorage.getItem("token"),
    id: userStudentForm.id
  }).then(res => {
    console.log(currentPage.value, pageSize.value)
    if (res.data.success) {
      hideLoading();
      console.log('初始化返回的res')
      console.log(res);
      let data = res.data;
      localStorage.setItem('token', data.token)
      console.log(localStorage.getItem('token'))
      let array = data.content
      pageCount.value = data.totalPage;
      practiceData.arr = array
      console.log(practiceData)
    } else {
      console.log(res)
      hideLoading();
      loginFailed()
    }
  })
    .catch(function (error) {
      hideLoading();
      loginFailed()
    })
}
const deletePractice = async (row) => {  //删  //异步不确定是否有问题
  await ElMessageBox.confirm(
    '确认删除该条社会实践吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      showLoading();
      service.post('/api/practice/managerDelete', {
        token: localStorage.getItem("token"),
        id: row.id
      }).then(res => {
        if (res.data.success) {
          hideLoading()
          messageSuccess('删除成功!')
          localStorage.setItem("token", res.data.token)
          loadPracticeTable() //重新加载现在表单中的数据
        } else {
          console.log(res)
          hideLoading();
          loginFailed()
        }
      })
        .catch(function (error) {
          hideLoading();
          loginFailed()
        })
    })
}
// loadPracticeTable() //进入默认执行
const checkReward = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  rewardDialog.value = true;
  rewardForm.rewardDescription = row.description;
  rewardForm.rewardName = row.name;
  rewardForm.id = row.id;
  rewardTypeOperation.value = 'check';
}
const deleteReward = async (row) => {  //删  //异步不确定是否有问题
  await ElMessageBox.confirm(
    '确认删除该条成果奖励吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      showLoading();
      service.post('/api/reward/managerDelete', {
        token: localStorage.getItem("token"),
        id: row.id
      }).then(res => {
        if (res.data.success) {
          hideLoading()
          messageSuccess('删除成功!')
          localStorage.setItem("token", res.data.token)
          loadRewardTable() //重新加载现在表单中的数据
        } else {
          console.log(res)
          hideLoading();
          loginFailed()
        }
      })
        .catch(function (error) {
          hideLoading();
          loginFailed()
        })
    })
}
const loadRewardTable = async () => {
  showLoading();
  await service.post('/api/reward/managerFind', {
    token: localStorage.getItem("token"),
    id: userStudentForm.id
  }).then(res => {
    console.log(currentPage.value, pageSize.value)
    if (res.data.success) {
      hideLoading();
      console.log('初始化返回的res')
      console.log(res);
      let data = res.data;
      localStorage.setItem('token', data.token)
      console.log(localStorage.getItem('token'))
      let array = data.content
      pageCount.value = data.totalPage;
      rewardData.arr = array
      console.log(rewardData)
    } else {
      console.log(res)
      hideLoading();
      loginFailed()
    }
  })
    .catch(function (error) {
      hideLoading();
      loginFailed()
    })
}
// loadRewardTable() //进入默认执行
const loadActivityTable = () => {
  showLoading();
  service.post('/api/activity/managerFind', {
    token: localStorage.getItem("token"),
    id: userStudentForm.id
  }).then(res => {
    if (res.data.success) {
      hideLoading();
      console.log('初始化返回的res')
      console.log(res);
      let data = res.data;
      localStorage.setItem('token', data.token)
      console.log(localStorage.getItem('token'))
      let array = data.content
      pageCount.value = data.totalPage;
      activityData.arr = array
      console.log(activityData)
    } else {
      console.log(res)
      hideLoading();
      loginFailed()
    }
  })
    .catch(function (error) {
      hideLoading();
      loginFailed()
    })
}
// loadActivityTable() //进入默认执行
const checkActivity = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  activityDialog.value = true;
  activityForm.practiceDescription = row.description;
  activityForm.practiceName = row.name;
  activityForm.date = row.date;
  activityForm.result = row.result;
  activityForm.id = row.id;
  activityTypeOperation.value = 'check';
}
const deleteActivity = (row) => {  //删  //异步不确定是否有问题
  ElMessageBox.confirm(
    '确认删除该条社会实践吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      showLoading();
      service.post('/api/activity/managerDelete', {
        token: localStorage.getItem("token"),
        practiceId: row.id
      }).then(res => {
        if (res.data.success) {
          hideLoading()
          messageSuccess('删除成功!')
          localStorage.setItem("token", res.data.token)
          loadActivityTable() //重新加载现在表单中的数据
        } else {
          console.log(res)
          hideLoading();
          loginFailed()
        }
      })
        .catch(function (error) {
          hideLoading();
          loginFailed()
        })
    })
}
</script>
<style lang="scss" scoped>
.operationArea {
  padding-top: 1vh;
  display: flex;
  width: 100%;
}

.bottomCard {
  background-color: #FFFFFF;
  border-radius: 3vh;
  display: flex;
  width: 100%;
}

.selectCard {
  border-radius: 2vh;
  background-color: #FFFFFF;
}

.operationCard {
  background-color: #FFFFFF;
  border-radius: 2vh;
  margin-bottom: 6vh;
}

.right {
  float: right;
  width: 65vw;
}

.information {
  margin: 3vh 3vh 3vh;
  //background-color: white;
  //border-radius: 1vh;
}

.menu {
  display: flex;
  width: 100%;
  padding-top: 2vh;
}

.menuButton {
  width: 100%;
  font-size: 1vw;
  font-weight: 550;
  line-height: 4vh;
  height: 6vh;
}

.main {
  width: 100%;
  display: flex;
  background-color: black;
}

.content {
  height: 90vh;
  width: 100%;

  .mainCard {
    min-height: 25vh;
    display: flex;
    flex-direction: column;
    background-color: #FFF;
    border-radius: 1vw;
    box-shadow: 0 0 1.25vh 0 #b9ccee;

    .header {
      width: 100%;
      height: 100%;
      display: flex;

      .leftPanel {
        width: 60%;
        height: 100%;
        display: flex;
        flex-direction: column;

        .title {
          padding-top: 2vh;
          padding-left: 3vw;
          font-size: 4vh;
          color: #1a91f3;
          font-weight: 600;
        }

        .searchOrigin {
          display: flex;

          .searchSpan {
            display: flex;
            flex-direction: row;
            padding-top: 3vh;
            padding-left: 4vw;
            width: 90%;
            margin: auto;

            .searchTerm {
              width: 100%;
              border: 0.2vw solid #00B4CC;
              border-right: none;
              height: 6vh;
              border-top-left-radius: 0.2vw;
              border-bottom-left-radius: 0.2vw;
              outline: none;
              color: #9DBFAF;
              font-size: 1.2vw;
            }

            .searchTerm:focus {
              color: #00B4CC;
            }

            .searchButton {
              width: 5vw;
              height: 6vh;
              border: 0.2vw solid #00B4CC;
              background: #00B4CC;
              text-align: center;
              color: #fff;
              border-radius: 0 0.2vw 0.2vw 0;
              cursor: pointer;
              font-size: 1.2vw;
            }

            .deleteButton {
              height: 6vh;
              border: 0.1vw solid;
            }
          }
        }
      }

      .rightPanel {
        width: 40%;
        height: 100%;
        display: flex;
        flex-direction: row;
        padding-top: 2vh;
        padding-bottom: 2vh;

        .imgBox {
          width: 35%;
          height: 70%;
          margin: auto;
          box-shadow: 0 0 1.25vh 0 #b9ccee;

          .img {
            height: 100%;
            width: 100%;
          }
        }

      }
    }


    /*Resize the wrap to see the searchOrigin bar change!*/
  }
}
</style>
