<template>
    <div class="mainArea">
        <div class="mainCard">
            <div class="content">
                <el-container>
                    <el-aside class="classSchedulePage">
                        <el-table :data="tableData" style="width: 100%" height="600" :cell-style="{ padding: '22px 0' }"
                            class="tableStyle" border stripe>
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
                        <el-table :data="courseInfo"
                            height="550" :header-cell-style="{ 'text-align': 'center' }"
                            :cell-style="{ padding: '20px 0' }" style="font-size: 16px" border>
                            <el-table-column prop="name" label="课程名称" align="center" width="150" />
                            <el-table-column prop="teacherName" label="教师姓名" align="center" width="120" />
                            <el-table-column prop="description" label="课程描述" align="center" width="232" />
                            <el-table-column prop="credit" align="center" label="学分" width="60" />
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
                            <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
                                @current-change="handleCurrentChange"  :current-page="currentPage"
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
import { ElMessageBox } from 'element-plus';
import { reactive, ref } from 'vue';
import { showLoading, hideLoading } from "../../utils/loading";

let currentPage = ref(1);
let pageSize = ref(10);
let dialogTableVisibleDetails = ref(false); //查看详细信息弹出框
let dialogTableVisibleDrop = ref(false); //退课弹出框
let filter = ref(false);  //是否过滤掉冲突课程
let pageCount = ref();
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

let courseInfo = reactive([]); //给我的所有课程，用于选课
let backData = reactive([]); //我的课程，用于生成课表
let tableData = reactive([ //课表中的数据，用于显示
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

//该方法用于拼接课表数据
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
//加载课表方法
//后端发给我一个已经选课的数组，我绑定到backData里，然后执行checkCourseTime来
//拼接课表,所以那个查看已选课程信息的按钮绑定的函数还是viewDetails,在这个函数中调用
//checkCourseTime方法
const courseSelected = async () => {
    showLoading()
    await service.post('/api/course/findCourseList', { token: localStorage.getItem('token') }).then(res => {
        let data = res.data;
        if (data.success) {
            localStorage.setItem('token', data.token);
            backData = data.content;
            checkCourseTime();//调用拼接的方法
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

courseSelected(); //进入页面先执行一次


//得到可以选的课的课程信息
//进入页面动态加载
//发送请求后传给我一个数组，数组中有每个课程，我绑定到courseInfo中
const getCourseInformation = async () => {//下午说这个传参加了一个size，一个pageNum
    showLoading()
    await service.post('/api/course/findAllCourse', { token: localStorage.getItem('token'), filterOpern: filter.value, pageNum: currentPage.value, pageSize: pageSize.value }).then(res => {
        let data = res.data;
        if (data.success) {
            localStorage.setItem('token', data.token);
            courseInfo = data.content;
            pageCount.value = data.totalPage;
            hideLoading()
        } else {
            hideLoading()
            messageError(data.message);
        }
    })
        .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
        })
}

getCourseInformation();//进入页面自动加载
//进去即使没有选课也把空课表给我，让我给backData赋初始值


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
                    hideLoading()
                    messageSuccess('退课成功!')
                    courseSelected();//退课要重新获得选课信息
                    getCourseInformation();//重新更改可选课程数据
                    localStorage.setItem("token", res.data.token)
                } else {
                    messageWarning(res.data.message)
                    hideLoading()
                }
            })
        })
        .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
        })
}

//选课
const handleSelect = async (row) => { //选课
    showLoading()
    await service.post('/api/course/select', { token: localStorage.getItem("token"), courseId: row.id }).then(res => {
        let data = res.data;
        if (data.success) {
            hideLoading();
            messageSuccess('选课成功！');
            localStorage.setItem('token', data.token);
            courseSelected();
            getCourseInformation();
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

const loadDropDialog = (row) => {
    dialogTableVisibleDrop.value = true;
}

</script>
<style lang="scss" scoped>
.mainArea {
    padding-top: 4vh;
    padding-bottom: 3vh;
    padding-left: 2vw;
    width: 92vw;

    .mainCard {
        background-color: #FFFFFF;
        padding-top: 6vh;
        padding-bottom: 6vh;
        padding-left: 3vw;
        padding-right: 3vw;
        border-radius: 4vw;

        .content {
            height: 1300px;
            padding-left: vw;

            .classSchedulePage {
                width: 900px;
                height: 600px;

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
                        border: 0.5px solid #f88075 !important;
                        border-radius: 6px;
                        border: none;
                        color: rgb(71, 51, 51);
                        text-align: center;
                        display: inline-block;
                        font-size: 16px;
                        font-weight: 500;
                        -webkit-transition-duration: 0.4s;
                        transition-duration: 0.4s;
                        cursor: pointer;
                        text-decoration: none;
                        text-transform: uppercase;
                    }

                    .opButton:hover {
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
                    font-size: 28px;
                    margin-left: 6px;
                    font-weight: bold;
                    margin-bottom: 12px;
                }

                .checkBox {
                    padding-left: 40px;
                }

                .selectTable {
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
</style>
