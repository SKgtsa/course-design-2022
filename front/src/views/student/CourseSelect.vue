<template>
    <div class="mainArea">
      <div :style="{'display':'flex','flex-direction':`${mobile? 'column':'row'}`}">
        <div class="timeTableCard" :style="{
          'width': `${mobile? 90: 90*ratio}vw`,
          'height': `${mobile? 70: 20 + 50*verticalRatio}vh`
        }">
          <div :style="{
            'width':`${mobile? 25: 25*ratio}vw`,
            'height':`${mobile? 60: 60*verticalRatio}`
          }" class="leftSection">
            <div  :style="{
              'width':`${mobile? 25: 25*ratio}vw`,
              'height':`${mobile? 10: 10*verticalRatio}vh`
              }">

            </div>
            <div  :style="{
              'width':`${mobile? 25: 25*ratio}vw`,
              'height':`${mobile? 10: 10*verticalRatio}vh`
              }" v-for="(item, index) in sections">
              <a>{{item}}</a>
            </div>
          </div>
          <div :style="{
            'width':`${mobile? 65: 65*ratio}vw`,
          }" class="topWeekDay">
            <div  :style="{
              'width':`${mobile? 12: 12*ratio}vw`,
              }" v-for="item in weekDays">
              <a>{{item}}</a>
            </div>
          </div>
          <div class="lunchTime" :style="{
            'width':`${mobile? 62: 62*ratio}vw`,
            'height':`${mobile? 8: 8*verticalRatio}vh`,
            'top': `${mobile? 40: 10 + 30*verticalRatio}vh`,
            'left':`${mobile? 28: 28*ratio}vw`
          }">
            <a style="align-self: center">午休时间</a>
          </div>
          <!--课表卡片-->
          <div v-for="(item, index) in pageData.courseTime" class="timeTable">
            <el-popover
                placement="top-start"
                :width="200"
                trigger="hover"
            >
              <template #reference>
                <div class="timeCard" :style="{
                  'top': `${(item.section > 2? item.section * 10 + 20 :item.section * 10 + 10)* (mobile? 1: verticalRatio)}vh`,
                   'left': `${((item.weekDay * 13 + 15) * (mobile? 1: ratio))}vw`,
                   'position':'absolute',
                   'background-color': `${baseColorSet[Math.floor(Math.random() * 6)]}`,
                   'width':`${12 * (mobile? 1: ratio)}vw`,
                   'height': `${9 * (mobile? 1: verticalRatio)}vh`
                }" >
                  <a class="courseName">{{pageData.myCourse[item.courseIndex].name}}</a>
                </div>
              </template>
              <div style="display: flex;flex-direction: column">
                <a style="font-weight: bold">{{pageData.myCourse[item.courseIndex].description}}</a>
                <a>{{pageData.myCourse[item.courseIndex].location}}</a>
              </div>
            </el-popover>
          </div>
        </div>
        <div :style="{
          'width':`${mobile? 90: 90* (1 - ratio)}vw`
        }">
          <div class="title">
            <a>选课指南</a>
          </div>
          <el-timeline class="progressPage">
            <el-timeline-item v-for="(activity, index) in activities" :key="index" :type="activity.type"
                              :size="activity.size" :hollow="activity.hollow" :timestamp="activity.timestamp">
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
          <div class="buttonSet">
            <el-button type="default" @click="courseSelected" class="opButton">
              <a>刷新课表</a></el-button>
            <el-button type="default" @click="loadDropDialog" class="opButton">
              <a>退选课程</a></el-button>
            <el-button type="default" @click="viewDetails" class="opButton">
              <a>查看已选课程</a></el-button>
          </div>
        </div>
      </div>
      <div class="courseSelectArea">
        <div class="courseSelectPage">
          <div class="courseSelectTop">
            <a class="courseSelectPageTitle">进入选课</a>
            <div class="checkBox">
              <el-button type="primary" @click="getCourseInformation" class="queryButton">
                <a>查询</a>
              </el-button>
              <el-checkbox  v-model="filter">
                <a>过滤掉冲突课程</a>
              </el-checkbox>
            </div>

          </div>

          <div class="selectTable">
            <el-table :data="pageData.courseList" height="550" :header-cell-style="{ 'text-align': 'center' }" border>
              <el-table-column prop="name" label="课程名称" align="center" width="150"  fixed="left" />
              <el-table-column prop="teacherName" label="教师姓名" align="center" width="120" />
              <el-table-column prop="description" label="课程描述" align="center" />
              <el-table-column prop="credit" align="center" label="学分" width="60" />
              <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
              <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
              <el-table-column prop="time" align="center" label="上课时间" width="150">
                <template #default="time">
                  <p v-for="(item, index) in time.row.time">
                      星期{{ time.row.time[index].weekDay }}的第{{time.row.time[index].section }}大节
                  </p>
                </template>
              </el-table-column>
              <el-table-column prop="capacity" align="center" label="课容量" width="60" />
              <el-table-column prop="studentNum" align="center" label="选课人数" width="60" />
              <el-table-column prop="location" align="center" label="上课地点" width="150" />
              <el-table-column label="选课" width="150" align="center" fixed="right">
                <template #default="scope">
                  <el-button size="large" @click="handleSelect(scope.row)">选课</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination">
              <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
                style="text-align: center">
              </el-pagination>
            </div>
          </div>
        </div>
        <el-dialog v-model="dialogTableVisibleDetails" title="查看课程信息" width="700" class="dialogPage" align="true"
            :align-center="true">
            <el-table :data="pageData.myCourse" height="500" width="620" :header-cell-style="{ 'text-align': 'center' }"
                :cell-style="{ padding: '5px 0' }" style="font-size: 16px" border>
                <el-table-column prop="name" label="课程名称" align="center" width="180" />
                <el-table-column prop="teacherName" label="教师姓名" align="center" width="100" />
                <el-table-column prop="description" label="课程描述" align="center" width="265" />
                <el-table-column prop="credit" align="center" label="学分" width="60" />
                <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
                <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
                <el-table-column prop="time" align="center" label="上课时间" width="250">
                    <template #default="time">
                        <p v-for="(item, index) in time.row.time">
                            星期{{ time.row.time[index].weekDay }}的第{{ time.row.time[index].section }}大节
                        </p>
                    </template>
                </el-table-column>
                <el-table-column prop="capacity" align="center" label="课容量" width="120" />
                <el-table-column prop="location" align="center" label="上课地点" width="150" />

            </el-table>
        </el-dialog>
        <el-dialog v-model="dialogTableVisibleDrop" title="退选课程" width="700" class="dialogPage" align="true"
            :align-center="true">
            <el-table :data="pageData.myCourse" height="500" width="620" :header-cell-style="{ 'text-align': 'center' }"
                :cell-style="{ padding: '5px 0' }" style="font-size: 16px" border>
                <el-table-column prop="name" label="课程名称" align="center" width="140" />
                <el-table-column prop="teacherName" label="教师姓名" align="center" width="80" />
                <el-table-column prop="description" label="课程描述" align="center" width="265" />
                <el-table-column prop="credit" align="center" label="学分" width="60" />
                <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
                <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
                <el-table-column prop="time" align="center" label="上课时间" width="150">
                    <template #default="time">
                        <p v-for="(item, index) in time.row.time">
                            星期{{ time.row.time[index].weekDay }}的第{{ time.row.time[index].section }}大节
                        </p>
                    </template>
                </el-table-column>
                <el-table-column prop="capacity" align="center" label="课容量" width="120" />
                <el-table-column prop="location" align="center" label="上课地点" width="150" />
                <el-table-column label="操作" width="200" align="center">
                    <template #default="scope">
                        <el-button size="large" @click="dropCourse(scope.row)">退课</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
      </div>

    </div>



</template>
<script lang="ts" setup>
import service from '@/request';
import { messageSuccess, messageWarning, messageInfo, messageError } from '@/utils/message';
import { ElMessageBox } from 'element-plus';
import { reactive, ref } from 'vue';
import { showLoading, hideLoading } from "../../utils/loading";
import {mobile} from "@/global/global";

//pc端 横向占比
const ratio = ref(0.7);
//pc端 纵向占比
const verticalRatio = ref(0.9);


let currentPage = ref(1);
let pageSize = ref(4);
let dialogTableVisibleDetails = ref(false); //查看详细信息弹出框
let dialogTableVisibleDrop = ref(false); //退课弹出框
let filter = ref(false);  //是否过滤掉冲突课程
let pageCount = ref();
const fontColorSet = [
    '#000','#FFF','#FFF','#FFF'
]
const baseColorSet = [
  '#23a35d','#85B8CB','#1D6A96','#245a74','#a92939','#ebba11','#631F16'
]
//获得已经选课的课程信息,用于更新课表
const activities = [
    {
        content: '系统关闭',
        timestamp: '2022-1-10 18:00',
        type: 'danger',
        hollow: true,
    },
    {
        content: '退补选',
        timestamp: '2022-11-05 10:00',
        type: 'success',
        hollow: true,
    },
    {
        content: '选课停止',
        timestamp: '2022-09-28 18:00',
        type: 'danger',
        size: 'large',
        hollow: true,
    },
    {
        content: '正式选课',
        timestamp: '2022-09-22 10:00',
        type: 'primary',
        hollow: true,
    },
    {
        content: '发布选课通知',
        timestamp: '2022-09-15 18:00',
        type: 'info',
        hollow: true,
    },
]

const weekDays = ['周一','周二','周三','周四','周五','周六','周日']

const sections = ['第一大节','第二大节','' ,'第三大节','第四大节','第五大节']

//页面的所有响应式数据
const pageData = reactive({
  courseList: [],//可选的课程
  myCourse : [] ,//我的课程
  courseTime: [],//课表上显示的数据 带有课名及时间信息 存储myCourse中的index代表索引
})

interface Course{
  id: 0,
  name: '',
  weekStart: 0,
  weekEnd: 0,
  time: [{
    weekDay: 0,
    section: 0
  },{
    weekDay: 0,
    section: 0
  }
  ],
  capacity: 0,
  studentClass: [],
  studentSection: [],
  location: '',
  year: 0,
  semester: '',
  credit: 0.0,
  description: '',
}

interface CourseTime{
  weekDay: 0,
  section: 0,
}

interface CourseTimeData{
  courseIndex: 0,
  weekDay: 0,
  section: 0,
}

const handlePosition = (target : CourseTimeData) => {
  console.log('position: absolute; top:' + target.section * 2 + 'vh; left: ' + target.weekDay * 2 + 'vw');
  return 'position: absolute; top:' + target.section * 2 + 'vh; left: ' + target.weekDay * 2 + 'vw';
}

/**
 * 根据myCourse更新课程时间
 */
const handleCourseTime = () => {
  let result = [];
  for(let i = 0;i < pageData.myCourse.length;i ++){
    let temp : Course = pageData.myCourse[i];
    for(let j = 0;j < temp.time.length;j ++){
      result.push({
        courseIndex: i,
        weekDay: temp.time[j].weekDay,
        section: temp.time[j].section
      })
    }
  }
  pageData.courseTime = result;
  console.log('完成myCourse更新')
  console.log(pageData)
}

//加载课表方法
//后端发给我一个已经选课的数组，我绑定到myCourse里，然后执行checkCourseTime来
//拼接课表,所以那个查看已选课程信息的按钮绑定的函数还是viewDetails,在这个函数中调用
//checkCourseTime方法
const courseSelected = () => {
  showLoading()
  console.log('进入courseSelected方法')
    service.post('/api/course/findCourseList', { token: localStorage.getItem('token') }).then(res => {
      let data = res.data;
      console.log(res);
      if (data.success) {
        hideLoading();
        localStorage.setItem('token', data.token);
        pageData.myCourse = data.content;
        console.log(pageData);
        handleCourseTime();
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

courseSelected(); //进入页面先执行一次


/**
 * 刷新可选课程的方法 会更新pageData.courseList
 */
const getCourseInformation = () => {
  showLoading()
  service.post('/api/course/findAllCourse', { token: localStorage.getItem('token'), filterOpen: filter.value, pageNum: currentPage.value, pageSize: pageSize.value }).then(res => {
    let data = res.data;
    console.log(filter.value)
    if (data.success) {
      localStorage.setItem('token', data.token);
      pageData.courseList = data.content;
      console.log(pageData)
      pageCount.value = data.totalPage;
    } else {
      messageError(data.message);
    }
    hideLoading()
  })
  .catch(function (error) {
    hideLoading();
    messageError("服务器开小差了呢");
    console.log(error)
  })
}


//退课是否退课，发送请求，退课成功，要后端课表数据，调用函数
const dropCourse = async (row) => {
  await ElMessageBox.confirm(
    '确认退课吗?',
    'Warning',
    {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }
  )
  .then(() => {
    showLoading()
    service.post('/api/course/quit', { token: localStorage.getItem("token"), courseId: row.id }).then(res => {
      if (res.data.success) {
          messageSuccess('退课成功!')
          localStorage.setItem("token", res.data.token)
          courseSelected();//退课要重新获得选课信息
          /*   getCourseInformation();//重新更改可选课程数据 */
      } else {
          messageWarning(res.data.message)
      }
      hideLoading()
    })
    .catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
  })
}

//选课
const handleSelect = async (row) => { //选课
    showLoading()
    await service.post('/api/course/select', { token: localStorage.getItem("token"), courseId: row.id }).then(res => {
        let data = res.data;
        if (data.success) {
            console.log(res.data)
            hideLoading();
            localStorage.setItem('token', data.token);
            messageSuccess('选课成功！');
            courseSelected();
            /*       getCourseInformation(); */
        } else {
            messageError(data.message);
            hideLoading()
        }
    })
        .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
        })
}


const handleCurrentChange = (current) => {  //切换页码
    currentPage.value = current;
    getCourseInformation();
}

const viewDetails = () => {
    dialogTableVisibleDetails.value = true;
}

const loadDropDialog = () => {
    dialogTableVisibleDrop.value = true;
}

</script>
<style lang="scss" scoped>
.mainArea {
  padding-left: 2vw;
  padding-top: 40px;
}
.timeTableCard{
  background-color: #FFFFFF;
  border-radius: 2vw;
  display: flex;
  flex-direction: row;
}
.topWeekDay{
  display: flex;
  flex-direction: row;
}
.lunchTime{
  background-color: #8a8f97;
  position: absolute;
  border-radius: 13px;
  display: flex;
  justify-content: center;
  color: #FFFFFF;
  font-size: 3vh;
  font-weight: bold;
}
.courseName{
  align-self: center;
}
.timeTable{

}
.timeCard{
  border-radius: 1vw;
  color: #FFFFFF;
  font-size: 2.2vh;
  display: flex;
  justify-content: center;
}
.courseCard{
  background-color: #0a8ce2;
  border-radius: 0.3vw;

}
.courseSelectArea{
  padding-top: 2vh;
}
.courseSelectTop{
  border-color: #0a8ce2;
  border-style: solid;
  border-width: 5px;
  border-top-left-radius: 3vw;
  border-top-right-radius: 3vw;
  height: 5vh;
  display: flex;
  flex-direction: row;
}
.queryButton{
  border-color: #0a8ce2;
  border-style: solid;
  border-width: 5px;
  border-top-right-radius: 3vw;
  border-top-style: none;
  border-right-style: none;
  border-bottom-style: none;
  background-color: rgba(0,0,0,0);
  color: #0a8ce2;
  font-weight: bold;
  font-size: 3vh;
  height: 6vh;
  margin: -0.4% 0;
  padding-right: 3vw;
  padding-left: 3vw;
}
.courseSelectPageTitle{
  padding-left: 2vw;
  font-size: 4vh;
  color: #0a8ce2;
  width: 30%;
}
.courseSelectPage{
  width: 90vw;
  background-color: #FFFFFF;
  border-radius: 3vw;

}
.checkBox{
  width: 70%;
  height: 5vh;

  display: flex;
  flex-direction: row-reverse;
}


</style>
