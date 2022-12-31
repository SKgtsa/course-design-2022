<template>
  <div class="mainArea">
    <div class="leftWindow">
    </div>
    <div class="rightWindow">
      <div class="mainCard">
        <div class="head">
          <a class="title">成绩管理</a>
          <div class="selectPanel">
            <a class="selectLabel">学年:</a>
            <el-select v-model="yearsValue" placeholder="2020" class="select">
              <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
            <a class="selectLabel">学期:</a>
            <el-select v-model="semesterValue" placeholder="春季学期" class="select">
              <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
<!--            <a class="selectLabel">课程:</a>-->
<!--            <el-select v-model="yearsValue" placeholder="课程" class="select">-->
<!--              <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />-->
<!--            </el-select>-->
            <el-button type="primary" class="checkButton" @click="checkCourse"><a>查询</a></el-button>
          </div>
        </div>
        <div class="tablePage">
          <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
            :header-cell-style="{ 'height': '30px', 'font-size': '18px', 'text-align': 'center', 'font-weight': '800' }"
            :cell-style="{ 'height': '15px', 'font-size': '16px', 'text-align': 'center', 'font-weight': '450' }">
            <!-- 显示斑马纹和边框 -->
            <el-table-column label="课程名" prop="courseName" width="200" show-overflow-tooltip />
            <el-table-column label="及格率" prop="passRate" width="150" show-overflow-tooltip />
            <el-table-column label="平均分" prop="average" width="150" show-overflow-tooltip />
            <el-table-column label="权重" prop="weight" width="150" show-overflow-tooltip />
            <el-table-column  label="操作">
              <template #default="scope">
                <el-button size="medium" @click="addScore(scope.row)" class="button"
                           type="primary">创建成绩</el-button>
                <el-button size="medium" @click="checkScore(scope.row)" class="button"
                           type="primary">查看成绩</el-button>
                <el-button size="medium" @click="editScore(scope.row)" class="button"
                           type="primary">修改成绩</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-dialog v-model="addDialogVisible" width="70%">
          <div class="studentTitle">
            创建成绩
          </div>
          <el-table :data="addForm.arr" style="width: 80%" border stripe size="small" class="courseTable"
                    max-height="400">
            <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip />
            <el-table-column label="平时成绩"  width="350" show-overflow-tooltip>
              <template #default="scope">
                <el-input v-model="scope.row.dailyScore" placeholder="请输入平时成绩"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="期末成绩"   show-overflow-tooltip >
              <template #default="scope">
                <el-input v-model="scope.row.endScore" placeholder="请输入期末成绩"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作"  width="120" show-overflow-tooltip >
              <template #default="scope">
                <el-button size="medium" @click="submitEditRow(scope.row)" class="button"
                           type="primary">确认</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                           @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize"
                           style="text-align: center">
            </el-pagination>
          </div>
        </el-dialog>
        <el-dialog v-model="checkDialogVisible" width="70%">
          <div class="studentTitle">
            查看成绩
          </div>
          <el-table :data="scoreData.arr" style="width: 80%" border stripe size="small" class="courseTable"
                    max-height="400">
            <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip />
            <el-table-column label="平时成绩" prop="dailyScore" width="350" show-overflow-tooltip />
            <el-table-column label="期末成绩" prop="endScore"  show-overflow-tooltip />
          </el-table>
          <div class="pagination">
            <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                           @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize"
                           style="text-align: center">
            </el-pagination>
          </div>
        </el-dialog>
        <el-dialog v-model="editDialogVisible" width="70%">
          <div class="studentTitle">
            修改成绩
          </div>
          <el-table :data="editForm.arr" style="width: 80%" border stripe size="small" class="courseTable"
                    max-height="400">
            <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip />
            <el-table-column label="平时成绩"  width="350" show-overflow-tooltip>
              <template #default="scope">
                <el-input v-model="scope.row.dailyScore" placeholder="请输入平时成绩"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="期末成绩"   show-overflow-tooltip >
              <template #default="scope">
                <el-input v-model="scope.row.endScore" placeholder="请输入期末成绩"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作"  width="120" show-overflow-tooltip >
              <template #default="scope">
                <el-button size="medium" @click="submitEditRow(scope.row)" class="button"
                           type="primary">确认</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                           @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize"
                           style="text-align: center">
            </el-pagination>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import service from '@/request';
import {messageError, messageSuccess, messageWarning} from '@/utils/message';
import { reactive } from '@vue/reactivity';
import { ref } from 'vue'
import { showLoading, hideLoading } from '@/utils/loading';

let addDialogVisible=ref();
let checkDialogVisible=ref();
let editDialogVisible=ref();
let yearsValue = ref();
let pageCount = ref(1);//查看选课学生给的总页数
let pageNum = ref(1);
let pageSize = ref(20);
let courseIdSave=ref();
let semesterValue = ref();
let formScoreData = ref();//改增校验绑定的空form
let addForm=reactive({
  arr: [{studentName:'小明',
    dailyScore:'99',
    endScore:'99',
  }],
});
let editForm=reactive({
  arr: [{studentName:'小明',
    dailyScore:'99',
    endScore:'99',
  }],
});
let tableData = reactive({
  arr: [{courseName:'web',
    passRate:'99%',
    average:99,
    weight:'50%',
  }],
});
let scoreData=reactive({
  arr: [{studentName:'小明',
    dailyScore:'99',
    endScore:'99',
  }],
});
const handleCurrentChange = (currentPage) => {
  pageNum.value = currentPage;
  console.log(currentPage)
}
const years = [
  {
    value: 2020,
    label: '2020',
  },
  {
    value: 2021,
    label: '2021',
  },
  {
    value: 2022,
    label: '2022',
  },
]
const semester = [
  {
    value: '春季学期',
    label: '春季学期',
  },
  {
    value: '秋季学期',
    label: '秋季学期',
  },
]
//这个表用于给学生打分
// if(yearsValue.value==2022&&semesterValue.value=='秋季学期'){
//   //如果是今年秋季学期的，调用查询今年秋季学期查看课程的方法，
//   //表格是可以给学生打分的选项，放input,输入一个传一个
//   //所有学生都打完成绩后，有个提交？如果提交成功了今年就变成查询了，表格得做的细长一点点，
//   //如果提交了他就可以看到这个课程的
//   //感觉...还能再加个属性，老师是否打分，如果打分了，就查看，没打分，就打分
// }
// const checkScore = async (row) => {
//   showLoading();
//   await service.post('/api/score/teacherDetail', { token: localStorage.getItem("token"),courseId:row.courseId}).then(res => {
//     if (res.data.success) {
//       hideLoading();
//       let data = res.data;
//       let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
//       scoreData = arr
//       localStorage.setItem('token', data.token)
//       addDialogVisible.value=true;
//     } else {
//       hideLoading();
//       messageWarning(res.data.message)
//     }
//   })
//       .catch(function (error) {
//         hideLoading();
//         messageError("服务器开小差了呢");
//         console.log(error)
//       })
// }
// const checkScore = async (row) => {
//   checkDialogVisible.value=true;
// }
const addScore = async (row) => {
  console.log(row)
  addDialogVisible.value=true;
  let i;
  for(i=0;i<addForm.arr.length;i++){
    addForm.arr[i].dailyScore=row.dailyScore;
    addForm.arr[i].endScore=row.endScore;
    addForm.arr[i].studentName=row.studentName;
  }
  console.log(editForm);
}
const editScore = async (row) => {
  console.log(row)
  editDialogVisible.value=true;
  let i;
  for(i=0;i<addForm.arr.length;i++){
    editForm.arr[i].dailyScore=row.dailyScore;
    editForm.arr[i].endScore=row.endScore;
    editForm.arr[i].studentName=row.studentName;
  }
  console.log(editForm);
}
const checkScore = async (row) => {
  showLoading();
  await service.post('/api/score/teacherDetail', { token: localStorage.getItem("token"),courseId:row.courseId}).then(res => {
    if (res.data.success) {
      hideLoading();
      let data = res.data;
      let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
      scoreData = arr
      localStorage.setItem('token', data.token)
      checkDialogVisible.value=true;
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
const checkCourse = async () => {  //进去先加载查看课表方法，然后绑定上
  showLoading();
  await service.post('/api/course/teacherFind', {
     token: localStorage.getItem('token'),semester:semesterValue.value,year:yearsValue.value})
    .then(res => {
      let data = res.data;
      if (data.success) {
        localStorage.setItem('token', data.token);
        tableData.arr = data.content;
        messageSuccess("查询成功！")
        console.log(res)
        hideLoading();
      } else {
        hideLoading();
        messageError(data.message);
      }
    })
    .catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
}
checkCourse();
const submitEditRow = async (row) => {
  console.log(addForm)
  await formScoreData.value.validate(((valid) => {
    if (valid) {
        showLoading()
        service.post('/api/score/save',
            {
              token: localStorage.getItem("token"),
              courseId:row.courseId,
              studentId:row.studentId,
              dailyScore:row.dailyScore,
              endScore:row.endScore,
            })
            .then(res => {
                  if (res.data.success) {
                    hideLoading()
                    messageSuccess("添加成功！")
                    checkScore(row);
                    localStorage.setItem("token", res.data.token)
                  } else {
                    hideLoading()
                    messageError(res.data.message)
                  }
                }
            )
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      } else {
        messageError('出错了！')
      }
  }))
  formScoreData.value = null;
};
</script>

<style lang="scss" scoped>
.studentTitle {
  margin-top: 10px;
  height: 60px;
  font-family: 微软雅黑;
  font-size: 3vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}
.mainArea {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100vh;

  .leftWindow {
    display: flex;
    width: 5vw;
  }

  .rightWindow {
    padding-left: 2vw;
    padding-top: 1vh;
    display: flex;

    .mainCard {
      display: flex;
      flex-direction: column;
      background-color: #FFFFFF;
      margin-top: 4vh;
      margin-left: 2vw;
      width: 80vw;
      height: 90vh;
      border-radius: 1vw;
      box-shadow: 0 0 10px 0 #b9ccee;

      .head {
        height: 20vh;
        padding-left: 15px;
        padding-top: 20px;

        .title {
          display: flex;
          flex-direction: column;
          padding-left: 25px;
          font-size: 25px;
          font-weight: 600;
        }

        .selectPanel {
          margin-top: 30px;
          display: flex;
          flex-direction: row;

          .selectLabel {
            font-size: 18px;
            padding-right: 10px;
            padding-left: 30px;
          }

          .checkButton {
            margin-left: 30px;
            font-size: 16px;
          }
        }

      }

      .tablePage {
        display: flex;
        flex-direction: column;
        padding-left: 3vw;
        margin-top: 10px;
        height: 60vh;
        width: 70vw;

        .scoreTable {
          border-top: 0.5px solid;
          border-bottom: 0.5px solid;
        }
      }
    }
  }
}
</style>
  