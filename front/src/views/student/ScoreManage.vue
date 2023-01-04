<template>
  <div class="mainArea">
    <div class="leftWindow">
    </div>
    <div class="rightWindow">
      <div class="mainCard" v-if="!mobile">
        <div class="head">
          <a class="title">成绩查询</a>
          <div class="selectPanel">
            <span style="display: flex;flex-dirction:row"> <a class="selectLabel">学年:</a>
              <el-select v-model="yearsValue" placeholder="2020" class="select">
                <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
              </el-select> </span>
            <span> <a class="selectLabel">学期:</a>
              <el-select v-model="semesterValue" placeholder="春季学期" class="select">
                <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
              </el-select> </span>
            <el-button type="primary" class="checkButton" @click="check"><a>查询</a></el-button>
          </div>
        </div>
        <div class="tablePage">
          <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
            :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
            :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
            <!-- 显示斑马纹和边框 -->
            <el-table-column label="课程名" prop="courseName" width="220" show-overflow-tooltip />
            <el-table-column label="平时分数" prop="dailyScore" width="160" show-overflow-tooltip />
            <el-table-column label="平时分数占总分权重" prop="weight" width="100" show-overflow-tooltip />
            <el-table-column label="期末得分" prop="endScore" width="140" show-overflow-tooltip />
            <el-table-column label="总分" prop="finalScore" width="140" show-overflow-tooltip />
            <el-table-column label="排名" prop="rank" width="150" show-overflow-tooltip />
          </el-table>
        </div>
      </div>
      <div class="mainCard" v-if="mobile">
        <div class="headMobile">
          <a class="titleMobile">成绩查询</a>
          <div class="selectPanelMobile">
            <span style="display: flex;flex-dirction:row" class="spanSelect"> <a class="selectLabelMobile">学年:</a>
              <el-select v-model="yearsValue" placeholder="2020" class="selectMobile">
                <el-option v-for="item in years" :key="item.value" :label="item.label" :value="item.value" />
              </el-select> </span>
            <span style="display: flex; flex-dirction:row" class="spanSelect"><a class="selectLabelMobile">学期:</a>
              <el-select v-model="semesterValue" placeholder="春季学期" class="selectMobile">
                <el-option v-for="item in semester" :key="item.value" :label="item.label" :value="item.value" />
              </el-select> </span>
            <el-button type="primary" class="checkButtonMobile" @click="check"><a>查询</a></el-button>
          </div>
        </div>
        <div class="tablePageMobile">
          <el-table :data="tableData.arr" stripe size="large" class="scoreTable"
            :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
            :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
            <!-- 显示斑马纹和边框 -->
            <el-table-column label="课程名" prop="courseName" width="150" show-overflow-tooltip />
            <el-table-column label="总分" prop="finalScore" width="120" show-overflow-tooltip />
            <el-table-column label="平时分数" prop="dailyScore" width="160" show-overflow-tooltip />
            <el-table-column label="平时分数占总分权重" prop="weight" width="100" show-overflow-tooltip />
            <el-table-column label="期末得分" prop="endScore" width="140" show-overflow-tooltip />
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
import { ref } from 'vue';
import { mobile } from '@/global/global';
import { showLoading, hideLoading } from '@/utils/loading';
let yearsValue = ref();
let semesterValue = ref();
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
      box-shadow: 0 0 1.25vh 0 #b9ccee;

      .head {
        height: 20vh;
        padding-left: 1.875vh;
        padding-top: 2.5vh;

        .title {
          display: flex;
          flex-direction: column;
          padding-left: 3.125vh;
          font-size: 3.125vh;
          font-weight: 600;
        }

        .selectPanel {
          margin-top: 3.75vh;
          display: flex;
          flex-direction: row;

          .selectLabel {
            font-size: 2.5vh;
            padding-right: 1.25vh;
            padding-left: 3.75vh;
          }

          .select {
            width: 14vw;
          }

          .checkButton {
            margin-left: 3.75vh;
            font-size: 2vh;
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

          .checkButtonMobile {
            margin-top: 3vh;
            margin-left:20vw ;
            width: 40vw;
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
        padding-left: 3vw;
        margin-top: 1.25vh;
        height: 60vh;
        width: 70vw;

        .scoreTable {
          border-top: 0.0625vh solid;
          border-bottom: 0.0625vh solid;
        }
      }
    }
  }
}
</style>
  