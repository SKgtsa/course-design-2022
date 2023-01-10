<template>
  <div class="page" :style="{
    'padding-top': `${mobile? 5:1}vh`
  }">
    <div class="card" :style="{
      'width': `${mobile? 100:60}%`
    }">
      <a class="tittle">同班同学</a>
      <el-scrollbar class="studentContainer">
        <div class="studentPaper">
          <div class="studentCardArea" v-for="(item,index) in pageData.studentList">
            <el-button class="studentCard" @click="jumpToPersonal(item)">
              <div class="leftArea">
                <el-image :src="getBaseURL() + item.photoURL" class="photo"/>
              </div>
              <a class="name">{{item.name}}</a>
            </el-button>
          </div>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {getBaseURL,  mobile} from "@/global/global";
import {reactive} from "vue";
import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import {loginFailed} from "@/utils/tokenCheck";
import router from "@/router";



const pageData = reactive({
  studentList: []
})

const jumpToPersonal = (item) => {
  router.push({
    path: '/personalPage',
    query: {
      userId: item.id
    }
  });
}

const init = () => {
  showLoading();
  service.post('api/user/findClassmate', { token: localStorage.getItem("token") }).then(res => {
    const data = res.data;
    console.log(res)
    if (data.success) {
      pageData.studentList = data.content;
      localStorage.setItem('token', data.token)
      hideLoading();
    } else {
      console.log(res)
      loginFailed()
      hideLoading();
    }
  })
}

init();

</script>

<style scoped>
.page{
  width: 100%;
  min-height: 92vh;
}
.card{
  background-color: #FFFFFF;
  border-radius: 3vh;
  height: 90vh;
  box-shadow: 0 0 10px 0 #b9ccee;
  margin: auto;
  display: flex;
  flex-direction: column;
}
.tittle{
  font-size: 5vh;
  color: #0a8ce2;
  padding: 2vh;
}
.studentContainer{
  margin: 0 auto;
  width: 98%;
  height: 73vh;
}
.studentPaper{
  width: 100%;
  font-size: 0;
}
.studentCardArea{
  height: 10vh;
  display: inline-block;
  padding-left: 1vh;
  padding-right: 1vh;
}
.studentCard{
  background-color: #DDD;
  box-shadow: 0 0 5px 0 #b9ccee;
  width: auto;
  padding-right: 1vh;
  height: 95%;
  border-radius: 2vh;
  display: flex;
  flex-direction: row;
}
.photo{
  width: 8vh;
  height: 8vh;
  border-radius: 4vh;
  margin: auto;
}
.name{
  font-size: 3vh;
  font-weight: bold;
  color: #1a1a1a;
  margin: auto;
}
.leftArea{
  width: 10vh;
  height: 10vh;
  display: flex;
}
</style>
