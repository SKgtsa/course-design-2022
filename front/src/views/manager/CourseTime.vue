<template>
  <div class="content" :style="{
        'padding-top': `${mobile ? '5' : '2'}vh`,
        'height': `${mobile ? 'auto' : '91vh'}`
    }">
    <div class="pageContent" :style="{
            'width': `${mobile ? 80 : 60}%`,
        }">
      <div class="pageTitle" style="padding-top: 10vh;">
        <a class="title" :style="{
                    'padding-left': `${mobile ? '7' : '10'}vw`,

                }">设置选课起止时间</a>
      </div>
      <el-divider/>
      <div v-if="!mobile" class="selectTimeBox" style="display:flex;flex-direction:column;">
        <div
            style="border-style: none;border-radius: 3vh;width: 80vh;margin-left:20vh;padding: 10vh 0vh;background-color: #99CCFF88">
          <div style="display:flex;flex-direction: row;padding-left: 2vh;"><a
              style="font-size:3vh;padding-left: 2vh;color: gray">起始时间:</a>
            <el-date-picker style="height:5vh;padding-left:3vw;width: 25vw;font-size: 3vh;" v-model="startTime"
                            type="datetime" placeholder="选择起始日期" format="YYYY/MM/DD hh:mm:ss"
                            value-format="YYYY-MM-DD hh:mm:ss"/>
          </div>
          <div style="display:flex;flex-direction: row;padding-left: 2vh;padding-top: 4vh;"><a
              style="font-size:3vh;padding-left: 2vh;color: gray">截止时间:</a>
            <el-date-picker style="height:5vh;padding-left:3vw;width: 25vw;font-size: 3vh;" v-model="endTime"
                            type="datetime" placeholder="选择截止日期" format="YYYY/MM/DD hh:mm:ss"
                            value-format="YYYY-MM-DD hh:mm:ss"/>
          </div>
        </div>
        <el-button type="primary" style="width:16vw;height: 5vh;margin-top: 8vh;margin-left: 10vw;"
                   @click="submit"><a style="font-size:2.4vh">提交</a></el-button>
      </div>
      <div v-if="mobile" style="display:flex;flex-direction:column">
        <div style="margin-top:3vh;display:flex;flex-direction: row;padding-left: 4vw;"><a>起始时间:</a>
          <el-date-picker style="padding-left: 3vw;" v-model="startTime" type="datetime"
                          placeholder="Pick a Date" format="YYYY/MM/DD hh:mm:ss"
                          value-format="YYYY-MM-DD hh:mm:ss"/>
        </div>
        <div style="margin-top:3vh;display:flex;flex-direction: row;padding-left: 4vw;"><a>截止时间:</a>
          <el-date-picker style="padding-left: 3vw;" v-model="endTime" type="datetime"
                          placeholder="Pick a Date" format="YYYY/MM/DD hh:mm:ss"
                          value-format="YYYY-MM-DD hh:mm:ss"/>
        </div>
        <el-button style="margin-top: 4vh;margin-left:22vw;width: 30vw;" type="primary" @click="submit">
          <a>提交</a>
        </el-button>
      </div>
    </div>

  </div>
</template>
<script lang="ts" setup>
import {mobile} from '@/global/global';
import service from '@/request';
import {hideLoading, showLoading} from '@/utils/loading';
import {messageError, messageSuccess} from '@/utils/message';
import {loginFailed} from '@/utils/tokenCheck';
import {formatDate} from '@vueuse/shared';
import {ref} from 'vue';

let startTime = ref();
let endTime = ref();
const currentDate = ref(new Date())
let time = ref('');
console.log(currentDate.value);

const formatDate = () => {
  var date = new Date()
  var year = date.getFullYear()
  var month = date.getMonth() + 1 < 10 ?
      '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return year + '-' + month + '-' + day
}


const submit = async () => {
  time.value = formatDate();
  if (time.value >= startTime.value && time.value >= endTime.value) {
    messageError('起止时间不能在当前时间之前！')
    return
  }
  if (endTime.value <= startTime.value) {
    messageError('截止日期时间必须大于选课日期时间！')
  } else {
    showLoading();
    console.log(startTime.value)
    console.log(endTime.value)
    await service.post('/api/course/saveCourseSelectTime', {
      token: localStorage.getItem('token'),
      startTime: startTime.value,
      endTime: endTime.value
    }).then(res => {
      let data = res.data;
      if (data.success) {
        hideLoading();
        localStorage.setItem('token', data.token);
        messageSuccess('设置成功！')
      } else {
        hideLoading();
        loginFailed()
      }
    })
        .catch(function (error) {
          hideLoading();
          loginFailed()
        })
  }
}
</script>
<style lang="scss" scoped>
.pageTitle {
  .title {
    padding-top: 8vh;
    height: 6vh;
    font-family: 微软雅黑;
    font-size: 5vh;
    font-weight: 500;
    line-height: 1vh;
    color: #0273f1;
  }
}


.selectTimeBox {
  padding-top: 3vh;
  height: 50vh;
  width: 60vw;
}

.selectTime {
  height: 30vh;
}

.content {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: repeat;

  .pageContent {
    height: 80vh;
    border-radius: 3vw;
    padding-left: 2vw;
    padding-top: 3vh;
    padding-right: 2vw;
    background-color: #FFFFFF;
    box-shadow: 0 0 1.25vh 0 #b9ccee;
    margin: 0 auto;

    .addButton {
      width: 10vw;
      height: 5vh;
      border-color: #0273f1;
      border-style: solid;
      border-width: 0.5vh;
      border-radius: 1vw;
      color: #0273f1;
      font-size: 2.5vh;
    }

    .button {
      width: 6vh;
      height: 3.75vh;
    }
  }
}
</style>