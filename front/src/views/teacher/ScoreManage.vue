<template>
  <div class="mainWindow" :style="{
    'padding-top': `${mobile? 5:1}vh`
  }">
    <div class="mainCard" :style="{
      'width': `${mobile? '100%':'80%'}`
    }">
      <div class="head">
        <a class="studentTitle">成绩管理</a>
        <div class="selectPanel" :style="{
          'flex-direction': `${mobile? 'column':'row'}`,
          'margin-top': `${mobile? '1vh':'3vh'}`,
          'width': '100%'
        }">
          <div :style="{
            'display': 'flex',
            'flex-direction': 'row',
            'width': `${mobile? '100%':'60%'}`
          }">
            <div :style="{
              'display': 'flex',
              'flex-direction': `${mobile? 'column':'row'}`,
              'width':'50%'
            }">
              <a class="selectLabel">学年:</a>
              <el-select v-model="yearsValue" placeholder="2019" class="select">
                <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </div>
            <div :style="{
              'display': 'flex',
              'flex-direction': `${mobile? 'column':'row'}`,
              'width':'50%'
            }">
              <a class="selectLabel">学期:</a>
              <el-select v-model="semesterValue" placeholder="春季学期" class="select">
                <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </div>
          </div>
          <div :style="{
            'display': 'flex',
            'flex-direction': 'row-reverse',
            'width': `${mobile? '100%':'40%'}`,
            'padding-top':`${mobile? '2vh':'0'}`
          }">
            <el-button type="primary" style="margin: 0 3%" class="checkDownloadButton" @click="checkCourse"><a>查询</a></el-button>
            <el-button type="primary" style="margin: 0 3%" class="checkDownloadButton" plain @click="download">批量打分模板下载</el-button>
          </div>
        </div>
      </div>
      <div class="tablePage">
        <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
          :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
          :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
          <!-- 显示斑马纹和边框 -->
          <el-table-column fixed="left" label="课程名" prop="courseName"  width="150" show-overflow-tooltip />
          <el-table-column label="课程号" prop="courseId"  show-overflow-tooltip />
          <el-table-column label="通过率" prop="passRate"  show-overflow-tooltip />
          <el-table-column label="平均分" prop="average"  show-overflow-tooltip />
          <el-table-column fixed="right" label="打分">
            <template #default="scope">
              <el-button size="medium" @click="addScore(scope.row.courseId)" class="button"
                type="primary">打分</el-button>
            </template>
          </el-table-column>
          <el-table-column label="批量上传">
            <el-upload ref="upload" :http-request="uploadFile" :limit="1" :auto-upload="false">
              <template #trigger>
                <el-button type="primary">上传</el-button>
              </template></el-upload>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog v-model="addDialogVisible"
      :width="`${mobile? '95%':'70%'}`"
      >
        <div class="studentTitle">
          创建成绩
        </div>
        <el-table :data="scoreData.arr" style="width: 100%" border stripe size="small" class="courseTable"
          max-height="400">
          <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip />
          <el-table-column label="学号" width="140" prop="studentNumber" show-overflow-tooltip />
          <el-table-column>
            <template #default="scope">
              <el-input v-model="scope.row.dailyScore" placeholder="请输入平时成绩" maxlength="3" type="number"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="期末成绩" show-overflow-tooltip>
            <template #default="scope">
              <el-input v-model="scope.row.endScore" placeholder="请输入期末成绩" maxlength="3" type="number"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" show-overflow-tooltip>
            <template #default="scope">
              <el-button size="medium" @click="submitEditRow(scope.row)" class="button" type="primary">确认</el-button>
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
      <el-dialog v-model="editDialogVisible" width="70%">
        <div class="studentTitle">
          修改成绩
        </div>
        <el-table :data="scoreData.arr" style="width: 80%" border stripe size="small" class="courseTable"
          max-height="400">
          <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip />
          <el-table-column label="学号" prop="studentNumber" width="210" show-overflow-tooltip />
          <el-table-column label="平时成绩" width="80" show-overflow-tooltip>
            <template #default="scope">
              <el-input v-model="scope.row.dailyScore" placeholder="请输入平时成绩" type="number" maxlength="3">{{
  scope.row.dailyScore
}}</el-input>
            </template>
          </el-table-column>
          <el-table-column label="期末成绩" show-overflow-tooltip>
            <template #default="scope">
              <el-input v-model="scope.row.endScore" placeholder="请输入期末成绩" type="number" maxlength="3">{{
  scope.row.endScore
}}</el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" show-overflow-tooltip>
            <template #default="scope">
              <el-button size="medium" @click="submitEditRow(scope.row)" class="button" type="primary">确认</el-button>
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
</template>

<script lang="ts" setup>
import service from '@/request';
import { messageError, messageSuccess, messageWarning } from '@/utils/message';
import { reactive } from '@vue/reactivity';
import { ref } from 'vue'
import { showLoading, hideLoading } from '@/utils/loading';
import { ElMessageBox } from 'element-plus';
import serviceFile from '@/request/indexFile';
import { mobile } from '@/global/global';
import router from '@/router';

let upload = ref();
let idCourse = ref();
let addDialogVisible = ref();
let checkDialogVisible = ref();
let editDialogVisible = ref();
let yearsValue = ref();
let pageCount = ref(1);//查看选课学生给的总页数
let pageNum = ref(1);
let pageSize = ref(20);
let courseIdSave = ref();
let semesterValue = ref();
let formScoreData = ref();//改增校验绑定的空form
/* let addForm = reactive({
  arr: [{
    studentName: '小明',
    dailyScore: '99',
    endScore: '99',
  }],
});
let editForm = reactive({
  arr: [{
    studentName: '小明',
    dailyScore: '99',
    endScore: '99',
  }],
}); */

let scoreData = reactive({
  arr: [],
})

let tableData = reactive({
  arr: [],
})

const download = () => {
  window.location.href = 'http://courseback.clankalliance.cn/inbuild/%E6%89%B9%E9%87%8F%E5%BD%95%E5%85%A5%E6%88%90%E7%BB%A9%E8%A1%A8%E6%A0%BC.xlsx';
}

const handleCurrentChange = (currentPage) => {
  pageNum.value = currentPage;
  console.log(currentPage)
}
const years = [
  {
    value: 2019,
    label: '2019',
  },
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

/* const checkScore = (row) => {
  getScore(row)
  checkDialogVisible.value = true;
}
 */
const addScore = async (row) => {
  await getScore(row);
  addDialogVisible.value = true;
}

const editScore = async (row) => {
  await getScore(row);
  editDialogVisible.value = true;

}

let fileId = ref();
const loadFile = (id) => {
  fileId.value = id;
}

const uploadFile = async (f) => {
  await ElMessageBox.confirm(
    '如果某学生已经打分，该次上传将覆盖掉之前成绩，是否继续?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    showLoading();
    serviceFile.post('/api/score/batchScore', { token: localStorage.getItem('token'), id: fileId.value, file: f.file }).then(res => {
      if (res.data.success) {
        hideLoading();
        localStorage.setItem('token', res.data.token);
        messageSuccess('上传成功！')
        checkCourse();
      } else {
        hideLoading();
        messageError(res.data.message);
      }
    })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢")
      })
  })
}

const getScore = async (id) => {
  showLoading();
  console.log()
  await service.post('/api/score/teacherDetail', { token: localStorage.getItem("token"), courseId: id }).then(res => {
    if (res.data.success) {
      hideLoading();
      let data = res.data;
      console.log(data.content)
      let arr = data.content
      scoreData.arr = arr
      idCourse.value = id;
      localStorage.setItem('token', data.token)
      checkDialogVisible.value = true;
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

const checkCourse = async () => {
  showLoading();
  console.log(yearsValue.value),
    console.log(semesterValue.value),
    await service.post('/api/score/teacherFind', {
      token: localStorage.getItem('token'), year: yearsValue.value, semester: semesterValue.value
    })
      .then(res => {
        let data = res.data;
        if (data.success) {
          localStorage.setItem('token', data.token);
          tableData.arr = data.content;
          console.log(tableData.arr)
          messageSuccess("查询成功！")
          let a = yearsValue.value;
          let b = semesterValue.value;
          yearsValue.value = null;
          semesterValue.value = null;
          yearsValue.value = a;
          semesterValue.value = b;  //不要改这个赋值代码，不知道为什么，这么写可以正常渲染，不这么写不行
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

const submitEditRow = async (row) => {
  if (row.dailyScore == '' || row.dailyScore == null || row.endScore == null || row.endScore == '') {
    messageWarning('请填写完整！');
    return;
  }
  showLoading()
  if (addDialogVisible.value == true) {
    console.log(row.courseId),
      console.log(row.studentId),
      service.post('/api/score/save',
        {
          token: localStorage.getItem("token"),
          courseId: idCourse.value,
          studentId: row.studentId,
          dailyScore: row.dailyScore,
          endScore: row.endScore,
        })
        .then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess("添加成功！")
            localStorage.setItem("token", res.data.token)
            checkCourse();

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
  } else if (editDialogVisible.value == true) {
    service.post('/api/score/save',
      {
        token: localStorage.getItem("token"),
        courseId: idCourse.value,
        studentId: row.studentId,
        dailyScore: row.dailyScore,
        endScore: row.endScore,
      })
      .then(res => {
        if (res.data.success) {
          hideLoading()
          messageSuccess("修改成功！")
          localStorage.setItem("token", res.data.token)
          checkCourse();
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
  }
}
  ;
</script>

<style lang="scss" scoped>
.checkDownloadButton{
  font-size: 3vh;
  height: 5vh;
}
.studentTitle {
  margin-top: 2vh;
  height: 10vh;
  font-family: 微软雅黑;
  font-size: 5vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}
  .mainWindow {
    display: flex;
    width: 100%;
    height: 92vh;
    .mainCard {
      display: flex;
      flex-direction: column;
      background-color: #FFFFFF;
      border-radius: 1vw;
      box-shadow: 0 0 1vw 0 #b9ccee;
      height: 95%;
      margin: 0 auto;
      .head {
        height: 20vh;
        padding-left: 1.5vw;
        padding-top: 3.5vh;

        .title {
          display: flex;
          flex-direction: column;
          padding-left: 1.8vw;
          font-size: 4vh;
          font-weight: 600;
        }

        .selectPanel {
          display: flex;

          .selectLabel {
            font-size: 3vh;
            width: 11vh;
            padding-left: 2vh;
          }
          .select{
            width: 90%;
            margin: 0 auto;
          }
        }

      }
      .headMobile {
     /*    height: 27vh; */
        padding-left: 1.875vh;
        padding-top: 2.5vh;

        .titleMobile {
          display: flex;
          flex-direction: column;
          font-size: 3.125vh;
          font-weight: 600;
          text-align: center;
        }

        .selectPanelMobile {
          margin-top: 2vh;
          display: flex;
          flex-direction: column;

          .spanSelect {
            padding-top: 2vh;

            .selectLabelMobile {
              font-size: 2.5vh;
              padding-left: 3vw;
              display: flex;
              flex-direction: row;
            }

            .selectMobile {
              width: 50vw;
              padding-left: 6vw;
            }
          }
        }
      }

      .tablePage {
        display: flex;
        flex-direction: column;
        height: 63vh;
        width: 95%;
        margin: 0 auto;
        .scoreTable {
          border-top: 0.2vw solid;
          border-bottom: 0.2vw solid;
        }
      }
    }
  }

</style>
