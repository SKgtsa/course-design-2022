<template>
    <div class="mainArea">
        <div class="mainCard">
            <div class="content">
                <el-container>
                    <el-aside class="classSchedulePage">
                        <el-table :data="tableData" style="width: 100%" height="600" @cell-click="checkDetails"
                            :cell-style="{ padding: '22px 0' }" class="tableStyle" border stripe>
                            <el-table-column class="cellStyle" prop="title" label="节次" width="180"></el-table-column>
                            <el-table-column class="cellStyle" prop="Monday" label="周一"></el-table-column>
                            <el-table-column class="cellStyle" prop="Tuesday" label="周二"></el-table-column>
                            <el-table-column class="cellStyle" prop="Wednesday" label="周三"></el-table-column>
                            <el-table-column class="cellStyle" prop="Thursday" label="周四"></el-table-column>
                            <el-table-column class="cellStyle" prop="Friday" label="周五"></el-table-column>
                            <el-table-column class="cellStyle" prop="Saturday" label="周六"></el-table-column>
                            <el-table-column class="cellStyle" prop="Sunday" label="周日"></el-table-column>
                        </el-table>
                    </el-aside>
                    <el-main class="operationPage">
                        <div class="title">
                            <a>选课指南</a>
                        </div>
                        <el-timeline class="progressPage">
                            <el-timeline-item v-for="(activity, index) in activities" :key="index" :type="activity.type"
                                :size="activity.size" :hollow="activity.hollow" :timestamp="activity.timestamp">
                                <!-- 报错，但是跑起来没有问题 -->
                                {{ activity.content }}
                            </el-timeline-item>
                        </el-timeline>
                        <div class="buttonSet">
                            <!--         需要更新课表按钮(虽然这个有点鸡肋吧，选一节就更新一节了)
                需要能够查看自己所有选课的详细信息
                能够退课 -->
                            <el-button type="default" @click="courseSelected" class="opButton">
                                <a>刷新课表</a></el-button>
                            <el-button type="default" @click="loadDropDialog" class="opButton">
                                <a>退选课程</a></el-button>
                            <el-button type="default" @click="viewDetails" class="opButton">
                                <a>查看已选课程</a></el-button>
                        </div>
                        <!--       <div class="progressPage">
                <el-steps :active="2" align-center finish-status="success">
                  <el-step title="选课通知" />
                  <el-step title="正式选课" />
                  <el-step title="退补选" />
                  <el-step title="系统关闭" />
                </el-steps>
              </div> -->
                    </el-main>
                </el-container>
                <div class="courseSelectPage">
                    <span>
                        <a class="courseSelectPageTitle">进入选课</a>
                        <el-checkbox v-model="filter" label="" size="large" class="checkBox">
                            <a>过滤掉冲突课程</a>
                        </el-checkbox>
                    </span>
                    <div class="selectTable">
                        <el-table :data="courseInfo.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
                            height="550" width="600" :header-cell-style="{ 'text-align': 'center' }"
                            :cell-style="{ padding: '20px 0' }" style="font-size: 16px" border>
                            <!-- 还得实现分页,我把后端给我的处理一下吧......,感觉这个显示不太好显示 -->
                            <el-table-column prop="name" label="课程名称" align="center" width="150" />
                            <el-table-column prop="teacherName" label="教师姓名" align="center" width="120" />
                            <el-table-column prop="description" label="课程描述" align="center" width="265" />
                            <el-table-column prop="credit" align="center" label="学分" width="60"/>
                            <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
                            <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
                            <el-table-column prop="classTime" align="center" label="上课时间" width="150">
                                <template #default="time">
                                    <p v-for="(item, index) in time.row.classTime">
                                        第{{ time.row.classTime[index].weekDay }}周的第{{
                                                time.row.classTime[index].section
                                        }}大节
                                    </p>
                                </template>
                            </el-table-column>
                            <el-table-column prop="capacity" align="center" label="课容量" width="120" />
                            <el-table-column prop="location" align="center" label="上课地点" width="150" />
                            <el-table-column label="选课" width="150" align="center">
                                <template #default="scope">
                                    <el-button size="large" @click="handleSelect(scope.row)">选课</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination">
                            <el-pagination background layout="prev, pager, next,jumper, ->" :total="courseInfo.length"
                                @current-change="handleCurrentChange" v-model:current-page="currentPage"
                                :page-size="pageSize" style="text-align: center">
                            </el-pagination>
                        </div>
                    </div>
                </div>
            </div>
            <el-dialog v-model="dialogTableVisibleDetails" title="查看课程信息" width="700" class="dialogPage" align="true"
                :align-center="true">
                <el-table :data="backData" height="500" width="620" :header-cell-style="{ 'text-align': 'center' }"
                    :cell-style="{ padding: '5px 0' }" style="font-size: 16px" border>
                    <el-table-column prop="name" label="课程名称" align="center" width="180" />
                    <el-table-column prop="teacherName" label="教师姓名" align="center" width="100" />
                    <el-table-column prop="description" label="课程描述" align="center" width="265" />
                    <el-table-column prop="credit" align="center" label="学分" width="60" />
                    <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
                    <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
                    <el-table-column prop="classTime" align="center" label="上课时间" width="250">
                        <template #default="time">
                            <p v-for="(item, index) in time.row.classTime">
                                第{{ time.row.classTime[index].weekDay }}周的第{{ time.row.classTime[index].section }}大节
                            </p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="capacity" align="center" label="课容量" width="120" />
                    <el-table-column prop="location" align="center" label="上课地点" width="150" />

                </el-table>
            </el-dialog>
            <el-dialog v-model="dialogTableVisibleDrop" title="退选课程" width="700" class="dialogPage" align="true"
                :align-center="true">
                <el-table :data="backData" height="500" width="620" :header-cell-style="{ 'text-align': 'center' }"
                    :cell-style="{ padding: '5px 0' }" style="font-size: 16px" border>
                    <el-table-column prop="name" label="课程名称" align="center" width="140" />
                    <el-table-column prop="teacherName" label="教师姓名" align="center" width="80" />
                    <el-table-column prop="description" label="课程描述" align="center" width="265" />
                    <el-table-column prop="credit" align="center" label="学分" width="60" />
                    <el-table-column prop="weekStart" align="center" label="开始周" width="60" />
                    <el-table-column prop="weekEnd" align="center" label="结束周" width="60" />
                    <el-table-column prop="classTime" align="center" label="上课时间" width="150">
                        <template #default="time">
                            <p v-for="(item, index) in time.row.classTime">
                                第{{ time.row.classTime[index].weekDay }}周的第{{ time.row.classTime[index].section }}大节
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
import { ChatSquare, MoreFilled } from '@element-plus/icons-vue';
import { ElAside, ElButton, ElContainer, ElDialog, ElMain, ElMessageBox, ElPagination, ElStep, ElSteps, ElTable, ElTableColumn, ElTimeline, ElTimelineItem, rowContextKey } from 'element-plus';
import Color from 'element-plus/es/components/color-picker/src/color';
import { find, size } from 'lodash';
import { reactive, ref } from 'vue';

let currentPage = ref(1);
let pageSize = ref(10);
let dialogTableVisibleDetails = ref(false);
let dialogTableVisibleDrop = ref(false);
let filter = ref(false);
//获得已经选课的课程信息,用于更新课表
const activities = [
    {
        content: '系统关闭',
        timestamp: '2018-10-10 18:00',
        type: 'danger',
        hollow: true,
    },
    {
        content: '退补选',
        timestamp: '2022-10-05 10:00',
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
        /*  icon: MoreFilled, */
    },
]
//从这开始删交互的时候
//******************************************************************************* 
let backData = reactive([  //后端传的已经选了的课的数据,希望给的weekDay和section是一一对应的
    {
        name: '数据结构',
        teacherName: "侯孟波",
        location: '6区606',
        classTime: [
            {
                weekDay: 1,
                section: 2,
            },
            {
                weekDay: 2,
                section: 3,
            },
        ],
        weekStart: 1,
        weekEnd: 16,
        capacity: 50,
        credit: 6,
        description:"倾情大作，为你讲述数据结构的前世今生",
    },
    {
        name: '体育',
        teacherName: "张飞",
        location: '篮球场',
        classTime: [
            {
                weekDay: 3,
                section: 4,
            },
        ],
        weekStart: 1,
        weekEnd: 16,
        credit: 1,
        capacity: 40,
        description:"倾情大作，为你讲述篮球的前世今生",
    },
    {
        name: '计算机组成原理',
        teacherName: "冯诺依曼",
        location: '2区303',
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        weekStart: 1,
        weekEnd: 16,
        credit: 2,
        capacity: 60,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
])

let courseInfo = reactive([  //希望后端给我的,然后我选课表的table就绑定这个
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
    {
        name: '计算机组织与结构',
        teacherName: '波波',
        weekStart: 1, //第几周开始
        weekEnd: 16, //第几周结束
        classTime: [
            {
                weekDay: 2,
                section: 5,
            },
            {
                weekDay: 5,
                section: 4,
            },
        ],
        capacity: 80,
        location: '5区107',
        credit: 5,
        description:"倾情大作，为你讲述计算机的前世今生",
    },
   
])
let tableData = reactive([ //课表中的数据
    {
        title: '第一大节(8:00-9:50)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第二大节(10:10-12:00)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: "第三大节(14:00-15:50)",
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第四大节(16:10-18:00)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第五大节(17:00-20:50)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
])
//用这个来拼接课表中的数据
const checkCourseTime = () => {   //更新课表页

    for (let i = 0; i < backData.length; i++) {
        for (let j = 0; j < backData[i].classTime.length; j++) {
            let n = backData[i].classTime[j].section - 1; //数组下标从0开始 */
            if (backData[i].classTime[j].weekDay == 1) {
                console.log(backData[i].name);
                tableData[n].Monday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 2) {
                tableData[n].Tuesday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 3) {
                tableData[n].Wednesday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 4) {
                tableData[n].Thursday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 5) {
                tableData[n].Friday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 6) {
                tableData[n].Saturday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 7) {
                tableData[n].Sunday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            }
        }
    }
}
checkCourseTime();
const checkDetails = (row, column, cell, event) => {
    if (row != 1 && column != 1 && cell.value != '') {
        messageSuccess("点击成功!")
    }
}
const mouseHover = (row, column, cell, event) => {
}
//到这删完
//交互的时候给它解开
//***************************************************************

/* let courseInfo = reactive([]); //后端给我的所有课程，用于选课
let backData = reactive([]); //我的课程，用于生成课表
let tableData = reactive([ //课表中的数据
    {
        title: '第一大节(8:00-9:50)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第二大节(10:10-12:00)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: "第三大节(14:00-15:50)",
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第四大节(16:10-18:00)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
    {
        title: '第五大节(17:00-20:50)',
        Monday: '',
        Tuesday: '',
        Wednesday: '',
        Thursday: '',
        Friday: '',
        Saturday: '',
        Sunday: '',
    },
])
//用这个来拼接课表中的数据
const checkCourseTime = () => {   //更新课表页

    for (let i = 0; i < backData.length; i++) {
        for (let j = 0; j < backData[i].classTime.length; j++) {
             let n = backData[i].classTime[j].section - 1; //数组下标从0开始
             if (backData[i].classTime[j].weekDay == 1) {
                console.log(backData[i].name);
                tableData[n].Monday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 2) {
                tableData[n].Tuesday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 3) {
                tableData[n].Wednesday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 4) {
                tableData[n].Thursday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 5) {
                tableData[n].Friday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 6) {
                tableData[n].Saturday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            } else if (backData[i].classTime[j].weekDay == 7) {
                tableData[n].Sunday = '课程名称:' + backData[i].name + "  " + '@上课地址:' + backData[i].location;
            }
        }
    }
}
checkCourseTime();
//加载所有的课程信息

getCourseInformation();//进入页面自动加载

//课表方法
const courseSelected =async () =>{
//后端发给我一个已经选课的数组，我绑定到backData里，然后执行checkCourseTime来
//拼接课表,所以那个查看已选课程信息的按钮绑定的函数还是viewDetails,在这个函数中调用
//checkCourseTime方法
    await service.post('/api/course/findCourseList',{token:localStorage.getItem('token')}).then(res=>{
        let data = res.data;
        if(data.success){
            localStorage.setItem('token',data.token);
            backData.value = data.content;  //如果不对用values试试
            checkCourseTime();//调用拼接的方法
        }else{
            messageError(data.message);
        }
    })
}
 const getCourseInformation =async () =>{ //进入页面动态加载
//发送请求后传给我一个数组，数组中有每个课程，我绑定到courseInfo中
    await service.post('/api/course/findAllCourse',{token:localStorage.getItem('token'),filterOpern:filter}).then(res=>{
        let data = res.data;
        if(data.success){
            localStorage.setItem('token',data.token);
            courseInfo.value = data.content;
        }else{
            console.log(data.message);
        }
    })
}
//进去即使没有选课也把空课表给我，让我给backData赋初始值
courseSelected();
 //退课是否退课，发送请求，退课成功，要后端课表数据，调用函数
 const dropCourse = (row) => { 
    ElMessageBox.confirm(
    '确认退课吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      service.post('/api/course/quit', { token: localStorage.getItem("token"), courseId: row.id}).then(res => {
        if (res.data.success) {
          messageSuccess('退课成功!')
          checkCourseTime() //重新加载现在表单中的数据
          localStorage.setItem("token", res.data.token)
        } else {
          messageWarning(res.data.message)
        }
      })
    })
    .catch(() => {
      messageInfo("取消删除")
    })
}

//选课
const handleSelect =async(row) => { //选课
    await service.post('/api/course/select',token: localStorage.getItem("token"), courseId: row.id).then(res=>{
        let data = res.data;
        if(data.success){
            messageSuccess('选课成功！');
            localStorage.setItem('token',data.token);
            courseSelected();//加载已经选完的课程信息！更新课表
        }else{
            messageError(data.message);
        }
    })
}
******************************************************************
 */


const handleCurrentChange = (currentPage) => {  //切换页码
    currentPage.value = currentPage;

}

const viewDetails = () => {
    dialogTableVisibleDetails.value = true;
}

const loadDropDialog = (row) => {
    dialogTableVisibleDrop.value = true;
}

</script>
<style lang="scss" scoped>
.mainArea {
    padding-top: 4vh;
    padding-bottom: 3vh;
    padding-left: 3vw;

    .mainCard {
        background-color: #FFFFFF;
        padding-top: 6vh;
        padding-bottom: 6vh;
        padding-left: 0vw;
        padding-right: 3vw;
        border-radius: 4vw;

        .content {


            /*     background-color: rgb(145, 193, 62); */
            height: 1300px;
            padding-left: 5vw;

            .classSchedulePage {
                width: 900px;
                height: 600px;
                /*         background-color: aqua; */

                .tableStyle {
                    border: 0.5px solid;
                    cursor: pointer;
                }
            }

            .operationPage {
                height: 600px;
                background-color: #f8fad7;
                border-top: 1px solid;
                border-right: 1px solid;
                border-bottom: 1px solid;

                .title {
                    padding-top: 10px;
                    font-size: 18px;
                    font-weight: 550;
                    text-align: center;
                }

                .progressPage {
                    padding-top: 40px;
                    padding-left: 65px;
                }

                .buttonSet {
                    display: flex;
                    flex-direction: column;
                    padding-left: 48px;
                    .opButton {
                        display: flex;
                        flex-direction: column;
                        margin-top: 3vh;
                        justify-content: center;
                        margin-left: 1vw;
                        width: 15vw;
                        height: 4vh;
                        background: transparent;
                        /* 边框样式、颜色、宽度 */
                        border: 0.5px solid #f88075 !important;
                        /* 给边框添加圆角 */
                        border-radius: 6px;
                        border: none;
                        color: rgb(71, 51, 51);
                        text-align: center;
                        display: inline-block;
                        font-size: 16px;
                        font-weight: 500;
                        -webkit-transition-duration: 0.4s;
                        /* Safari */
                        transition-duration: 0.4s;
                        cursor: pointer;
                        text-decoration: none;
                        text-transform: uppercase;
                    }

                    .opButton:hover {
                        /*         background-color: #a2cff1; */
                        color: rgb(78, 94, 244);

                    }
                }
            }

            .courseSelectPage {
                padding-top: 30px;
                padding-bottom: 15px;
                height: 680px;
                background-color: #c4dcf2;

                .courseSelectPageTitle {
                    padding-top: 10px;
                    font-family: LiSu;
                    font-size: 35px;
                    margin-left: 6px;
                    margin-bottom: 12px;
                }

                .checkBox {
                    padding-left: 40px;
                    text-color:black;
                }

                .selectTable {
                    /*               --el-table-tr-bg-color : #d5cccc !important;

        //表格内背景颜色
            --el-table-header-text-color:'#202124' !important;
            //表头颜色
            --el-table-row-hover-bg-color: reb(135, 135, 113, 0.3) !important;
*/
                    //鼠标移动到这一行颜色
                    /*  --el-table-row-hover-bg-color:rgb(107, 68, 68)!important; */
                    padding-left: 10px;
                    padding-right: 10px;
                }

                .pagination {
                    padding-top: 30px;
                    float: right;
                }

            }
        }

    }
}



//scss的,有的想强制修改element-plus的默认样式，那我再创一个普通css的？
</style>
<style scoped>
.content.courseSelectPage.selectTable>>>.--el-table-tr-bg-color {
    color: #dd1c1c !important;
}
</style>
