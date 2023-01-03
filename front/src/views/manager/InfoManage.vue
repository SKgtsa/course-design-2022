<template>
    <div class="content">
        <div class="mainCard">
            <div class="header">

                <div class="leftPanel">
                    <a class="title">信息管理</a>
                    <div class="searchOrigin">
                        <span class="searchSpan">
                            <el-input v-model="searchOrigin" type="text" class="searchTerm" maxlength="15"
                                placeholder="输入学工号" />
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
                <div class="rightPanel" v-if="characterHasFound">
                    <div class="imgBox">
                        <el-image class="img" :src="userPhotoUrl" fit="cover">
                        </el-image>
                    </div>
                </div>
            </div>
            <div class="main">
                <div class="menu">
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>个人信息</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan" v-if="characterIsStudent">
                        <el-button class="menuButton" @click="clickPractice">
                            <a>社会实践</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan" v-if="characterIsStudent">
                        <el-button class="menuButton" @click="clickReward">
                            <a>成果奖励</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan" v-if="characterIsStudent">
                        <el-button class="menuButton" @click="clickActivity">
                            <a>课外活动</a>
                        </el-button>
                    </span>

                </div>
              <div class="right">
                <div v-if="informationShow">
                  <div v-if="userIsStudent">
                    <el-form :model="userStudentForm" class="areaTextInput" ref="formData">
                      <el-form-item label="学号:" prop="userNumber">
                        <span>{{ userStudentForm.userNumber }}</span>
                      </el-form-item>
                      <el-form-item label="姓名:" prop="name">
                        <span>{{ userStudentForm.name }}</span>
                      </el-form-item>
                      <el-form-item label="性别:" prop="gender">
                        <span>{{ userStudentForm.gender }}</span>
                      </el-form-item>
                      <el-form-item label="身份证号:" prop="idCardNumber">
                        <span>{{ userStudentForm.idCardNumber }}</span>
                      </el-form-item>
                      <el-form-item label="班级:" prop="studentClass">
                        <span>{{ userStudentForm.studentClass }}</span>
                      </el-form-item>
                      <el-form-item label="政治面貌:" prop="politicalAffiliation">
                        <span>{{ userStudentForm.politicalAffiliation }}</span>
                      </el-form-item>
                      <el-form-item label="民族:" prop="ethnic">
                        <span>{{ userStudentForm.ethnic }}</span>
                      </el-form-item>
                      <el-form-item label="邮箱:" prop="email">
                        <span>{{ userStudentForm.eMail }}</span>
                      </el-form-item>
                      <el-form-item label="电话:" prop="phone">
                        <span>{{ userStudentForm.phone }}</span>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div v-else>

                  </div>
                </div>
                <div v-if="practiceShow">
                  <el-table :data="practiceData.arr" stripe size="large" class="practiceTable"
                            :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
                            :cell-style="{ 'height': '1.75vh', 'font-size': '1.75vh', 'text-align': 'center', 'font-weight': '450' }">
                    <el-table-column label="社会实践标题" prop="name" width="300" show-overflow-tooltip />
                    <el-table-column label="描述" prop="description" width="300" show-overflow-tooltip></el-table-column>
                    <el-table-column  label="操作">
                      <template #default="scope">
                        <el-button size="medium" @click="checkPractice(scope.row)" class="button" type="primary">查看</el-button>
                        <el-button size="medium" type="danger" class="button" @click="deletePractice(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                                   @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                                   style="text-align: center">
                    </el-pagination>
                  </div>
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
                    <el-table-column  label="操作">
                      <template #default="scope">
                        <el-button size="medium" @click="checkReward(scope.row)" class="button" type="primary">查看</el-button>
                        <el-button size="medium" type="danger" class="button" @click="deleteReward(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                                   @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                                   style="text-align: center">
                    </el-pagination>
                  </div>
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
                    <el-table-column  label="操作">
                      <template #default="scope">
                        <el-button size="medium" @click="checkActivity(scope.row)" class="button" type="primary">查看</el-button>
                        <el-button size="medium" type="danger" class="button" @click="deleteActivity(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                                   @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                                   style="text-align: center">
                    </el-pagination>
                  </div>
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
import {reactive, ref} from 'vue';
import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import {messageError, messageSuccess, messageWarning} from "@/utils/message";
import {ElMessageBox} from "element-plus";
import {getBaseURL} from "@/global/global";
let currentPage = ref(1);
let pageSize = ref(7);
let pageCount = ref();
let searchOrigin = ref();
let search = ref();
let practiceShow=ref(false);
let informationShow=ref(false);
let activityShow=ref(false);
let rewardShow=ref(false);
let userIsStudent=ref(true);//false是老师
let userPhotoUrl = ref();
let characterIsStudent=ref(false);
let characterHasFound=ref(false);
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
let userStudentForm=reactive({
  id:'',
  userNumber:'',
  nickName:'',
  name:'',
  phone:'',
  studentClass:'',
  section:'',
  idCardNumber:'',
  gender:'',
  ethnic:'',
  politicalAffiliation:'',
  eMail:'',
  avatarURL:'',
  photoURL:'',
});

const handleCurrentChange = (current) => {
  currentPage.value = current;
  loadPracticeTable() //再执行一次索要数据方法
  console.log(currentPage)
}
const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
const clickPractice=()=>{
  practiceShow.value=true;
  activityShow.value=false;
  rewardShow.value=false;
  loadPracticeTable();
}
const clickReward=()=>{
  practiceShow.value=false;
  activityShow.value=false;
  rewardShow.value=true;
  loadRewardTable();
}
const clickActivity=()=>{
  practiceShow.value=false;
  activityShow.value=true;
  rewardShow.value=false;
  loadActivityTable();
}
const check=()=>{
  search.value=searchOrigin.value;
  checkUser(search.value);
}
const checkUser = async (userNumber) => {   //查看个人信息
  if (search.value == '' || search.value == undefined) {
    search.value='';
    return;
  }
  showLoading();
  await service.post('/api/user/managerFind', { token: localStorage.getItem("token") ,userNumber:userNumber,}).then(res => {
    if (res.data.success) {
      console.log(res);
      let data = res.data;
      console.log(data.token)
      let content = data.user;
      console.log(content)
      if(content.studentClass==undefined){
        userIsStudent.value=false;
      }else {
        userIsStudent.value=true;
        userStudentForm.id=content.id;
        userStudentForm.userNumber=content.userNumber;
        userStudentForm.nickName=content.nickName;
        userStudentForm.name = content.name;
        userStudentForm.phone = content.phone;
        userStudentForm.studentClass=content.studentClass;
        userStudentForm.idCardNumber=content.idCardNumber;
        if (content.gender == false) { userStudentForm.gender = '男'; } else userStudentForm.gender = '女';
        userStudentForm.ethnic = content.ethnic;//民族
        userStudentForm.politicalAffiliation=content.politicalAffiliation;
        userStudentForm.eMail = content.eMail;
        userStudentForm.userNumber = content.userNumber;
        userStudentForm.avatarURL=content.avatarURL;
        userStudentForm.photoURL=content.photoURL;
      }
      localStorage.setItem('token', data.token)
      console.log(userData)
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
const checkPractice = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  practiceDialog.value = true;
  practiceForm.practiceDescription = row.practiceDescription;
  practiceForm.practiceName = row.practiceName;
  practiceForm.id = row.id;
  practiceTypeOperation.value = 'check';
}
const loadPracticeTable = async () => {
  showLoading();
  await service.post('/api/practice/managerFind', { token: localStorage.getItem("token"), pageNum: currentPage.value, pageSize: pageSize.value,id:searchOrigin.value}).then(res => {
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
      hideLoading();
      messageWarning(res.data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}
// loadPracticeTable() //进入默认执行
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
        service.post('/api/practice/managerDelete', { token: localStorage.getItem("token"), practiceId: row.id }).then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess('删除成功!')
            localStorage.setItem("token", res.data.token)
            loadPracticeTable() //重新加载现在表单中的数据
          } else {
            hideLoading();
            messageWarning(res.data.message)
          }
        })
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      })
}
// loadPracticeTable() //进入默认执行
const checkReward = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  rewardDialog.value = true;
  rewardForm.rewardDescription = row.rewardDescription;
  rewardForm.rewardName = row.rewardName;
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
        service.post('/api/reward/managerDelete', { token: localStorage.getItem("token"), rewardId: row.id }).then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess('删除成功!')
            localStorage.setItem("token", res.data.token)
            loadRewardTable() //重新加载现在表单中的数据
          } else {
            hideLoading();
            messageWarning(res.data.message)
          }
        })
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      })
}
const loadRewardTable = async () => {
  showLoading();
  await service.post('/api/reward/managerFind', { token: localStorage.getItem("token"), pageNum: currentPage.value, pageSize: pageSize.value,id:searchOrigin.value}).then(res => {
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
      hideLoading();
      messageWarning(res.data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}
// loadRewardTable() //进入默认执行
const loadActivityTable = async () => {
  showLoading();
  await service.post('/api/activity/managerFind', { token: localStorage.getItem("token"), pageNum: currentPage.value, pageSize: pageSize.value,id:searchOrigin.value}).then(res => {
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
      activityData.arr = array
      console.log(activityData)
    } else {
      hideLoading();
      messageWarning(res.data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}
// loadActivityTable() //进入默认执行
const checkActivity = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  activityDialog.value = true;
  activityForm.practiceDescription = row.practiceDescription;
  activityForm.practiceName = row.practiceName;
  activityForm.date = row.date;
  activityForm.result = row.result;
  activityForm.id = row.id;
  activityTypeOperation.value = 'check';
}
const deleteActivity = async (row) => {  //删  //异步不确定是否有问题
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
        service.post('/api/activity/managerDelete', { token: localStorage.getItem("token"), practiceId: row.id }).then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess('删除成功!')
            localStorage.setItem("token", res.data.token)
            loadActivityTable() //重新加载现在表单中的数据
          } else {
            hideLoading();
            messageWarning(res.data.message)
          }
        })
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      })
}
</script>
<style lang="scss" scoped>
.right{
  float: right;
  width: 65vw;
}
.content {
    height: 100vh;
    width: 90vw;

    .mainCard {
        display: flex;
        flex-direction: column;
        background-color: #FFFFFF;
        margin-top: 4vh;
        margin-left: 9vw;
        width: 80vw;
        height: 95vh;
        border-radius: 1vw;
        box-shadow: 0 0 1.25vh 0 #b9ccee;

        .header {
            height: 35vh;
            width: 100%;
            background-color: rgb(232, 245, 247);
            display: flex;
            flex-direction: row;



            .leftPanel {
                width: 50vw;
                height: 30vh;
                display: flex;
                flex-direction: column;

                .title {
                    padding-top: 5vh;
                    padding-left: 5vw;
                    font-size: 2.5vw;
                    color: #1a91f3;
                    font-weight: 600;
                }

                .searchOrigin {
                    display: flex;

                    .searchSpan {
                        display: flex;
                        flex-direction: row;
                        width: 45vw;
                        padding-top: 5vh;
                        padding-left: 5vw;

                        .searchTerm {

                            width: 40vw;
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
                width: 30vw;
                height: 30vh;

                /*         .nameBox {
                    .name {
                        font-size: 1.6vw;
                    }
                } */
                .imgBox {
                    padding-left: 8vw;
                    padding-top: 3vh;
                    width: 15vw;
                    height: 25vh;

                    .img {
                        height: 100%;
                        width: 100%;
                    }
                }

            }
        }

        .main {
            height: 65vh;
            width: 80vw;
            background-color: bisque;
            display: flex;
            .menu {
                width: 15vw;
                height: 65vh;
                background-color: rgb(255, 255, 255);
                float: left;

                .menuButtonSpan {
                    width: 15vw;
                }

                .menuButton {
                    width: 15vw;
                    height: 6vh;
                    font-size: 1vw;
                    font-weight: 550;
                    line-height: 4vh;
                }
            }
        }




        /*Resize the wrap to see the searchOrigin bar change!*/
    }
}
</style>