<template>
  <div class="mainArea">
    <div class="leftWindow">
    </div>
    <div class="rightWindow">
      <div class="mainCard">
        <div class="head">
          <a class="title">成绩查询</a>
          <div class="selectPanel">
            <a class="selectLabel">学年:</a>
            <el-select v-model="yearsValue" placeholder="2020" class="select">
              <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
            <a class="selectLabel">学期:</a>
            <el-select v-model="semesterValue" placeholder="春季学期" class="select">
              <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
            <el-button type="primary" class="checkButton" @click="check"><a>查询</a></el-button>
          </div>
        </div>
        <div class="tablePage">
          <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
            :header-cell-style="{ 'height': '30px', 'font-size': '18px', 'text-align': 'center', 'font-weight': '800' }"
            :cell-style="{ 'height': '15px', 'font-size': '16px', 'text-align': 'center', 'font-weight': '450' }">
            <!-- 显示斑马纹和边框 -->
            <el-table-column label="课程名" prop="courseName" width="250" show-overflow-tooltip />
            <el-table-column label="平时分数" prop="dailyScore" width="160" show-overflow-tooltip />
            <el-table-column label="平时分数占总分权重" prop="weight" width="200" show-overflow-tooltip />
            <el-table-column label="总得分" prop="endScore" width="160" show-overflow-tooltip />
            <el-table-column label="排名" prop="rank" width="150" show-overflow-tooltip />
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import service from '@/request';
import { messageError, messageSuccess } from '@/utils/message';
import { reactive } from '@vue/reactivity';
import { ref } from 'vue'
import { showLoading, hideLoading } from '@/utils/loading';
let yearsValue = ref();
let semesterValue = ref();
let tableData = reactive({
  arr:[],
});
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

const check = async () => {
  showLoading();
  await service.post('/api/score/studentFind', { token: localStorage.getItem('token'), year: yearsValue.value, semester: semesterValue.value })
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
</script>

<style lang="scss" scoped>
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
  