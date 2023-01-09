<template>
  <div class="mainArea">
    <div class="mainWindow" :style="{
      'padding-top': `${mobile? 5:1}vh`
    }">
      <div class="mainCard" :style="{
        'width': `${mobile? 100:80}%`,
      }">
        <div class="head">
          <a class="title">成绩查询</a>
          <div class="selectPanel" :style="{
            'flex-direction': `${mobile? 'column':'row'}`
          }">
            <div class="selectGroup" :style="{
              'width': `${mobile? 100:60}%`
            }">
              <span style="display: flex;flex-dirction:row"> <a class="selectLabel">学年:</a>
                <el-select v-model="yearsValue" placeholder="2020" class="select">
                  <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </span>
              <span> <a class="selectLabel">学期:</a>
                <el-select v-model="semesterValue" placeholder="春季学期" class="select">
                  <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </span>
            </div>
            <el-button type="primary" class="checkButton"  @click="check" :style="{
              'width': `${mobile? 100:30}%`,
              'margin-top': `${mobile? '2vh':'0'}`
            }">
              <a>查询</a>
            </el-button>
          </div>
        </div>
        <div class="tablePage">
          <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
            :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
            :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
            <!-- 显示斑马纹和边框 -->
            <el-table-column fixed="left" label="课程名" prop="courseName" min-width="100" max-width="160" show-overflow-tooltip />
            <el-table-column label="平时分数" prop="dailyScore" min-width="80" show-overflow-tooltip />
            <el-table-column label="期末得分" prop="endScore" min-width="80" show-overflow-tooltip />
            <el-table-column label="平时分数占总分权重" prop="weight" min-width="80" show-overflow-tooltip />
            <el-table-column label="总分" prop="finalScore" min-width="80" show-overflow-tooltip />
            <el-table-column label="排名" prop="rank" min-width="80" show-overflow-tooltip />
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
import { ref } from 'vue';
import { mobile } from '@/global/global';
import { showLoading, hideLoading } from '@/utils/loading';
let yearsValue = ref(2020);
let semesterValue = ref('春季学期');
let tableData = reactive({
  arr: [],
});
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

const check = async () => {
  showLoading();
  await service.post('/api/score/studentFind', { token: localStorage.getItem('token'), year: yearsValue.value, semester: semesterValue.value })
    .then(res => {
      let data = res.data;
      if (data.success) {
        localStorage.setItem('token', data.token);
        for(let i = 0;i < data.content.length;i ++){
          data.content[i].weight = data.content[i].weight * 100 + '%'
        }
        tableData.arr = data.content;
        messageSuccess("查询成功！")
        let a = yearsValue.value;
        let b = semesterValue.value;
        yearsValue.value = null;
        semesterValue.value = null;
        yearsValue.value = a;
        semesterValue.value = b;
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
//全屏的css
.mainArea {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 93vh;

  .mainWindow {
    display: flex;
    width: 100%;
    .mainCard {
      display: flex;
      flex-direction: column;
      background-color: #FFFFFF;
      height: 90vh;
      border-radius: 3vh;
      box-shadow: 0 0 1.25vh 0 #b9ccee;
      margin: 0 auto;
      .head {
        height: 20vh;
        padding-left: 1.875vh;
        padding-top: 2.5vh;
        .title {
          display: flex;
          flex-direction: column;
          padding-left: 3.125vh;
          font-size: 4vh;
          font-weight: 600;
          color: #0a8ce2;
        }

        .selectPanel {
          margin-top: 3.75vh;
          display: flex;
          width: 95%;
          .select {
            width: 14vw;
          }

          .checkButton {
            font-size: 2vh;
            margin: auto;
            height: 5vh;
          }
        }
      }
      .tablePageMobile {
            padding-left: 7vw;
            height: 50vh;
            width: 60vw;
          }
      .tablePage {
        display: flex;
        flex-direction: column;
        margin-top: 1.25vh;
        height: 70%;
        width: 100%;
        .scoreTable {
          border-top: 0.0625vh solid;
          border-bottom: 0.0625vh solid;
          width: 95%;
          margin: 0 auto;
        }
      }
    }
  }
}
.selectGroup{
  display: flex;
  flex-direction: row;
}
.selectLabel {
  font-size: 2.5vh;
  padding-right: 1.25vh;
  padding-left: 3.75vh;
}
</style>
