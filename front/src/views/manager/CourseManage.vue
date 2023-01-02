<template>
    <div class="content">
        <div class="mainCard">
            <div class="header">
                <div class="leftPanel">
                    <a class="title">课程管理</a>
                    <div class="search">
                        <span class="searchSpan">
                            <el-input v-model="search" type="text" class="searchTerm" maxlength="15"
                                placeholder="输入课程号" />
                            <el-button type="submit" class="searchButton" @click="checkCourse">
                                <el-icon class="icon">
                                    <Search />
                                </el-icon>
                            </el-button>
                            <el-button type="danger" class="delectButton">
                                <a class="delectText">删除课程</a>
                            </el-button>
                        </span>
                    </div>
                </div>
            </div>
            <div class="main">
                <div class="menu">
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>课程信息</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>选课管理</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>成绩管理</a>
                        </el-button>
                    </span>
                    
                        
                    <!-- <el-menu router default-active="/Manager/CourseManage/CourseInfo" class="el-menu-vertical-demo"
                        @open="handleOpen" @close="handleClose" active-text-color="#ffd04b">
                        <el-menu-item index="/Manager/CourseManage/CourseInfo">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>课程信息</span>
                        </el-menu-item>
                        <el-menu-item index="/Manager/CourseManage/CourseSelect">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>选课管理</span>
                        </el-menu-item>
                        <el-menu-item index="/Manager/CourseManage/ScoreManage">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>成绩管理</span>
                        </el-menu-item>
                    </el-menu> -->
                </div>
                <div class="infoBox">
                    <el-scrollbar>
                        <el-form :model="editForm" ref="formCourseData" :rules="rulesEditForm" label-width="auto"
                            label-position="right" class="infoForm">
                            <el-form-item label="课程名:" prop="name">
                                <el-input style="width:20vw" v-model="editForm.name" maxlength="15">{{
        editForm.name
}}</el-input>
                            </el-form-item>

                            <el-form-item label="简介:" prop="description">
                                <el-input type="textarea" maxlength="50" rows="3" v-model="editForm.description">{{
        editForm.description
}}</el-input>
                            </el-form-item>
                            <span style="display:flex;flex-direction: row;">
                                <el-form-item label="开课周:" prop="weekStart">
                                    <el-select v-model="editForm.weekStart" placeholder="开课周">
                                        <el-option v-for="item in weekStartOptions" :key="item.value"
                                            :label="item.label" :value="item.value" />
                                    </el-select>
                                </el-form-item>

                                <el-form-item label="结课周:" prop="weekEnd">
                                    <el-select v-model="editForm.weekEnd" placeholder="结课周">
                                        <el-option v-for="item in weekEndOptions" :key="item.value" :label="item.label"
                                            :value="item.value" />
                                    </el-select>
                                </el-form-item>
                            </span>
                            <el-form-item label="上课时间:" prop="time">
                                <el-cascader v-model="editForm.time" :options="time" :props="mul" clearable
                                    placeholder="上课时间" />
                            </el-form-item>
                            <span style="display:flex;flex-direction: row;">
                                <el-form-item label="学年:" prop="year">
                                    <el-select v-model="editForm.year" placeholder="2020">
                                        <el-option v-for="item in yearOptions" :key="item.value" :label="item.label"
                                            :value="item.value" />
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="学期:" prop="semester">
                                    <el-select v-model="editForm.semester" placeholder="春季学期">
                                        <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label"
                                            :value="item.value" />
                                    </el-select>
                                </el-form-item>
                            </span>

                            <el-form-item label="授课地点:" prop="location">
                                <el-input style="width:15vw" v-model="editForm.location" maxlength="30">{{
        editForm.location
}}</el-input>
                            </el-form-item>

                            <el-form-item label="平时分权重" prop="weight">
                                <el-input style="width:15vw" placeholder="输入权重,如占比30%则输入0.3" v-model="editForm.weight"
                                    type="number" maxlength="3">{{
        editForm.location
}}</el-input>
                            </el-form-item>
                            <el-form-item label="课容量:" prop="capacity">
                                <el-input style="width:8vw" v-model="editForm.capacity" maxlength="5" type="number">{{
        editForm.capacity
}}</el-input>
                            </el-form-item>
                            <el-form-item label="学分:" prop="credit">
                                <el-input style="width:8vw" v-model="editForm.credit" maxlength="2" type="number">{{
        editForm.credit
}}</el-input>
                            </el-form-item>
                            <el-form-item label="授课班级:" prop="studentClass">
                                <el-select v-model="editForm.studentClass" placeholder="1班" multiple="true">
                                    <el-option v-for="item in classOptions" :key="item.value" :label="item.label"
                                        :value="item.value" />
                                </el-select>
                            </el-form-item>

                            <el-form-item label="授课年级:" prop="studentSection">
                                <el-select v-model="editForm.studentSection" placeholder="2019" multiple="true">
                                    <el-option v-for="item in studentSectionOptions" :key="item.value"
                                        :label="item.label" :value="item.value" />
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </el-scrollbar>
                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
import service from '@/request';
import { hideLoading, showLoading } from '@/utils/loading';
import { messageError, messageSuccess } from '@/utils/message';
import { reactive, ref } from 'vue';


let search = ref();
let backData = reactive({
    arr: [],
});
let url = ref('https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg');

const time = [
    {
        value: 1,
        label: '星期一',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 2,
        label: '星期二',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 3,
        label: '星期三',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 4,
        label: '星期四',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 5,
        label: '星期五',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 6,
        label: '星期六',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
    {
        value: 7,
        label: '星期日',
        children: [
            {
                value: 1,
                label: '第一二节',
            },
            {
                value: 2,
                label: '第三四节',
            },
            {
                value: 3,
                label: '第五六节',
            },
            {
                value: 4,
                label: '第七八节',
            },
            {
                value: 5,
                label: '第九十节',
            },
        ]
    },
]

const dayOptions = [
    {
        value: 1,
        label: '星期一',
    },
    {
        value: 2,
        label: '星期二',
    },
    {
        value: 3,
        label: '星期三',
    },
    {
        value: 4,
        label: '星期四',
    },
    {
        value: 5,
        label: '星期五',
    },
    {
        value: 6,
        label: '星期六',
    },
    {
        value: 7,
        label: '星期日',
    },
]
const sectionOptions = [
    {
        value: 1,
        label: '1-2节',
    },
    {
        value: 2,
        label: '3-4节',
    },
    {
        value: 3,
        label: '5-6节',
    },
    {
        value: 4,
        label: '7-8节',
    },
    {
        value: 5,
        label: '9-10节',
    },

]
const yearOptions = [
    {
        value: 2019,
        label: '2019',
    }, {
        value: 2020,
        label: '2020',
    }, {
        value: 2021,
        label: '2021',
    }, {
        value: 2022,
        label: '2022',
    }, {
        value: 2023,
        label: '2023',
    },
]
const semesterOptions = [
    {
        value: '春季学期',
        label: '春季学期',
    }, {
        value: '秋季学期',
        label: '秋季学期',
    },
]
const classOptions = [
    {
        value: 1,
        label: '1班',
    }, {
        value: 2,
        label: '2班',
    }, {
        value: 3,
        label: '3班',
    }
]
const studentSectionOptions = [
    {
        value: 2020,
        label: '2020',
    }, {
        value: 2021,
        label: '2021',
    }, {
        value: 2022,
        label: '2022',
    }, {
        value: 2023,
        label: '2023',
    }
]
const weekStartOptions = [
    {
        value: 1,
        label: '第一周',
    },
    {
        value: 2,
        label: '第二周',
    },
    {
        value: 3,
        label: '第三周',
    },
    {
        value: 4,
        label: '第四周',
    },
    {
        value: 5,
        label: '第五周',
    },
    {
        value: 6,
        label: '第六周',
    },
    {
        value: 7,
        label: '第七周',
    },
    {
        value: 8,
        label: '第八周',
    },
    {
        value: 9,
        label: '第九周',
    },
    {
        value: 10,
        label: '第十周',
    },
    {
        value: 11,
        label: '第十一周',
    },
    {
        value: 12,
        label: '第十二周',
    },
]
const weekEndOptions = [
    {
        value: 1,
        label: '第一周',
    },
    {
        value: 2,
        label: '第二周',
    },
    {
        value: 3,
        label: '第三周',
    },
    {
        value: 4,
        label: '第四周',
    },
    {
        value: 5,
        label: '第五周',
    },
    {
        value: 6,
        label: '第六周',
    },
    {
        value: 7,
        label: '第七周',
    },
    {
        value: 8,
        label: '第八周',
    },
    {
        value: 9,
        label: '第九周',
    },
    {
        value: 10,
        label: '第十周',
    },
    {
        value: 11,
        label: '第十一周',
    },
    {
        value: 12,
        label: '第十二周',
    },
]
const rulesEditForm = reactive({   /* 定义校验规则 */
    name: [{ required: true, message: '请输入课程名！', trigger: 'blur' }],
    description: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
    weekStart: [{ required: true, message: '请选择课程起始周！', trigger: 'blur' },],
    weekEnd: [{ required: true, message: '请选择课程结束周！', trigger: 'blur' },],
    time: [{ type: 'array', required: true, message: '请选择上课时间', trigger: 'change' }],
    capacity: [{ required: true, message: '请输入课程容量！', trigger: 'blur' }],
    studentClass: [{ required: true, message: '请选择学生班级！', trigger: 'blur' }],
    location: [{ required: true, message: '请输入上课地址！', trigger: 'blur' }],
    weight: [{ required: true, message: '请输入平时分占比!', trigger: 'blur' }],
    semester: [{ required: true, message: '请选择学期！', trigger: 'blur' }],
    credit: [{ required: true, message: '请输入学分！', trigger: 'blur' }],
    studentSection: [{ required: true, message: '请输入上课年级！', trigger: 'blur' }]
})

const mul = { multiple: true }
let editForm = reactive({
    name: '',
    weekStart: '',
    weekEnd: '',
    time: [],
    capacity: '',
    studentClass: [],
    studentSection: [],
    location: '',
    year: '',
    weight: '',
    description: '',
    semester: '',
    credit: '',
    courseId: '',
});

const checkCourse = async () => {
    if (search.value == '' || search.value == undefined || search.value == null) return
    showLoading();
    await service.post('/api/course/managerFind', { token: localStorage.getItem('token'), id: search.value }).then(res => {
        let data = res.data;
        if (data.success) {
            hideLoading();
            localStorage.setItem('token', data.token);
            messageSuccess("查询成功！")
            let row = data.content;
            editForm.name = row.name;
            editForm.weekStart = row.weekStart;
            editForm.weekEnd = row.weekEnd;
            editForm.time = [];
            for (let i = 0; i < row.time.length; i++) {
                let midArray = [];
                midArray.push(row.time[i].weekDay);
                midArray.push(row.time[i].section);
                editForm.time.push(midArray);
            }
            console.log('!!!')
            console.log(editForm.time)
            editForm.weight = row.weight;
            editForm.capacity = row.capacity;
            editForm.studentClass = row.studentClass;
            let arrIntSet = [];
            // 将字符串转换成数组，此时是字符串数组
            let arrString = row.studentClass.substr(1, row.studentClass.length - 2).split(',');
            for (let arrInt = 0; arrInt < arrString.length; arrInt++) {
                arrIntSet.push(parseInt(arrString[arrInt]))
            }
            // 将新的Number数组，绑定到select空间的v-model上
            editForm.studentClass = arrIntSet;
            arrIntSet = [];
            let arrInt;
            arrString = row.studentSection.substr(1, row.studentSection.length - 2).split(',');
            for (arrInt = 0; arrInt < arrString.length; arrInt++) {
                arrIntSet.push(parseInt(arrString[arrInt]))
            }
            editForm.studentSection = arrIntSet;
            editForm.location = row.location;
            editForm.year = row.year;
            editForm.description = row.description;
            editForm.semester = row.semester;
            editForm.credit = row.credit;
            editForm.courseId = row.id;
        } else {
            hideLoading();
        }
    })
        .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
        })
}

console.log(editForm)

const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
</script>
<style lang="scss" scoped>
.content {
    height: 100vh;
    width: 90vw;

    .mainCard {
        display: flex;
        flex-direction: column;
        background-color: #FFFFFF;
        margin-top: 4vh;
        margin-left: 9vw;
        width: 80vw;
        height: 95vh;
        border-radius: 1vw;
        box-shadow: 0 0 1.25vh 0 #b9ccee;

        .header {
            height: 35vh;
            width: 100%;
            background-color: rgb(232, 245, 247);
            display: flex;
            flex-direction: row;



            .leftPanel {
                width: 50vw;
                height: 30vh;
                display: flex;
                flex-direction: column;

                .title {
                    padding-top: 5vh;
                    padding-left: 5vw;
                    font-size: 2.5vw;
                    color: #1a91f3;
                    font-weight: 600;
                }

                .search {
                    display: flex;

                    .searchSpan {
                        display: flex;
                        flex-direction: row;
                        width: 45vw;
                        padding-top: 5vh;
                        padding-left: 5vw;

                        .searchTerm {

                            width: 40vw;
                            border: 0.2vw solid #00B4CC;
                            border-right: none;
                            height: 6vh;
                            border-top-left-radius: 0.2vw;
                            border-bottom-left-radius: 0.2vw;
                            outline: none;
                            color: #9DBFAF;
                            font-size: 1.2vw;
                        }

                        .searchTerm:focus {
                            color: #00B4CC;
                        }

                        .searchButton {
                            width: 5vw;
                            height: 6vh;
                            border: 0.2vw solid #00B4CC;
                            background: #00B4CC;
                            text-align: center;
                            color: #fff;
                            border-radius: 0 0.2vw 0.2vw 0;
                            cursor: pointer;
                            font-size: 1.2vw;
                        }

                        .delectButton {
                            height: 6vh;
                            border: 0.1vw solid;
                        }
                    }
                }
            }

            .rightPanel {
                width: 30vw;
                height: 30vh;

                /*         .nameBox {
                    .name {
                        font-size: 1.6vw;
                    }
                } */
                .imgBox {
                    padding-left: 8vw;
                    padding-top: 3vh;
                    width: 15vw;
                    height: 25vh;

                    .img {
                        height: 100%;
                        width: 100%;
                    }
                }

            }
        }

        .main {
            height: 65vh;
            width: 80vw;
            background-color: bisque;
            display: flex;
            flex-direction: row;

            .menu {
                width: 15vw;
                height: 65vh;
                background-color: rgb(255, 255, 255);
                .menuButtonSpan{
                    width: 15vw;
                }
                .menuButton{
                    width: 15vw;
                    height: 6vh;
                    font-size:1vw;
                    font-weight: 550;
                    line-height: 4vh;
                }
            }

            .infoBox {
                width: 65vw;
                height: 65vh;

                .infoForm {
                    padding-top: 5vh;
                    padding-left: 3vw;
                    padding-right: 4vw;
                    padding-bottom: 3vw;
                }
            }
        }




        /*Resize the wrap to see the search bar change!*/
    }
}
</style>