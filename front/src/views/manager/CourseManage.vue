<template>
  <div class="content">
    <div class="mainCard">
      <div class="header">
        <div class="leftPanel">
          <a class="title">课程管理</a>
          <div class="search">
                        <span class="searchSpan">
                            <el-input v-model="searchOrigin" type="text" class="searchTerm" maxlength="30"
                                      placeholder="输入课程号"/>
                            <el-button type="submit" class="searchButton" @click="check">
                                <el-icon class="icon">
                                    <Search/>
                                </el-icon>
                            </el-button>
                            <el-button v-if="isShow" type="danger" class="deleteButton" @click="delectCourse">
                                <a class="deleteText">删除课程</a>
                            </el-button>
                        </span>
          </div>
        </div>
      </div>
      <div class="divider" v-if="isShow"></div>
      <div class="main"> <!-- " -->
        <div class="menu" v-if="isShow">
                    <span class="menuButtonSpan">
                        <el-button class="menuButton" @click="changeInfo">
                            <a>课程信息</a>
                        </el-button>
                    </span>
          <span class="menuButtonSpan">
                        <el-button class="menuButton" @click="changeSelect">
                            <a>选课管理</a>
                        </el-button>
                    </span>
          <span class="menuButtonSpan">
                        <el-button class="menuButton" @click="changeScore">
                            <a>成绩管理</a>
                        </el-button>
                    </span>
        </div>
        <div class="menuDivider"></div>
        <div class="infoBox" v-if="isShow">
          <el-scrollbar v-if="typeShow == 'info' && isShow">
            <el-form :model="editForm" ref="formCourseData" :rules="rulesEditForm" label-width="auto"
                     label-position="right" class="infoForm">
                            <span style="display:flex;flex-dirction:row">
                                <el-form-item label="课程名:" prop="name">
                                    <el-input style="width:20vw" v-model="editForm.name" maxlength="15">{{
                                        editForm.name
                                      }}</el-input>
                                </el-form-item>
                                <el-button type="primary" @click="sumbitEditRow" class="submitButton">
                                    <a>提交</a>
                                </el-button>
                            </span>
              <el-form-item label="简介:" prop="description">
                <el-input type="textarea" maxlength="50" rows="3" v-model="editForm.description">{{
                    editForm.description
                  }}
                </el-input>
              </el-form-item>
              <span style="display:flex;flex-direction: row;">
                                <el-form-item label="开课周:" prop="weekStart">
                                    <el-select v-model="editForm.weekStart" placeholder="开课周">
                                        <el-option v-for="item in weekStartOptions" :key="item.value"
                                                   :label="item.label" :value="item.value"/>
                                    </el-select>
                                </el-form-item>

                                <el-form-item label="结课周:" prop="weekEnd">
                                    <el-select v-model="editForm.weekEnd" placeholder="结课周">
                                        <el-option v-for="item in weekEndOptions" :key="item.value" :label="item.label"
                                                   :value="item.value"/>
                                    </el-select>
                                </el-form-item>
                            </span>
              <el-form-item label="上课时间:" prop="time">
                <el-cascader v-model="editForm.time" :options="time" :props="mul" clearable
                             placeholder="上课时间"/>
              </el-form-item>
              <span style="display:flex;flex-direction: row;">
                                <el-form-item label="学年:" prop="year">
                                    <el-select v-model="editForm.year" placeholder="2020">
                                        <el-option v-for="item in yearOptions" :key="item.value" :label="item.label"
                                                   :value="item.value"/>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="学期:" prop="semester">
                                    <el-select v-model="editForm.semester" placeholder="春季学期">
                                        <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label"
                                                   :value="item.value"/>
                                    </el-select>
                                </el-form-item>
                            </span>

              <el-form-item label="授课地点:" prop="location">
                <el-input style="width:15vw" v-model="editForm.location" maxlength="30">{{
                    editForm.location
                  }}
                </el-input>
              </el-form-item>

              <el-form-item label="平时分权重" prop="weight">
                <el-input style="width:15vw" placeholder="输入权重,如占比30%则输入0.3" v-model="editForm.weight"
                          type="number" maxlength="3">{{
                    editForm.location
                  }}
                </el-input>
              </el-form-item>
              <el-form-item label="课容量:" prop="capacity">
                <el-input style="width:8vw" v-model="editForm.capacity" maxlength="5" type="number">{{
                    editForm.capacity
                  }}
                </el-input>
              </el-form-item>
              <el-form-item label="学分:" prop="credit">
                <el-input style="width:8vw" v-model="editForm.credit" maxlength="2" type="number">{{
                    editForm.credit
                  }}
                </el-input>
              </el-form-item>
              <el-form-item label="授课班级:" prop="studentClass">
                <el-select v-model="editForm.studentClass" placeholder="1班" multiple="true">
                  <el-option v-for="item in classOptions" :key="item.value" :label="item.label"
                             :value="item.value"/>
                </el-select>
              </el-form-item>

              <el-form-item label="授课年级:" prop="studentSection">
                <el-select v-model="editForm.studentSection" placeholder="2019" multiple="true">
                  <el-option v-for="item in studentSectionOptions" :key="item.value"
                             :label="item.label" :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-form>
          </el-scrollbar>
          <div class="tableDiv" v-if="typeShow == 'select' && isShow">
            <div class="studentTitle">
              <!--    选课学生 -->
              <el-input class="studentNumberInput" v-model="studentNumber" placeholder="请输入学生学号"/>
              <el-button class="addButton" @click="addStudent">添加学生</el-button>
            </div>
            <el-table :data="studentData.arr" style="width: 90%" border stripe size="small"
                      class="courseTable" max-height="400"
                      :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
                      :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
              >
              <el-table-column label="姓名" prop="name" width="120" show-overflow-tooltip/>
              <el-table-column label="学号" prop="userNumber" width="250" show-overflow-tooltip/>
              <el-table-column label="年级" prop="section" width="150" show-overflow-tooltip/>
              <el-table-column label="班级" prop="studentClass" width="150" show-overflow-tooltip/>
              <el-table-column>
                <template #header>
                  操作
                </template>
                <template #default="scope">
                  <el-button @click="deleteStudent(scope.row)">删除学生</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="tableDiv" v-if="typeShow == 'score' && isShow">
            <div class="studentTitle">
              修改成绩
            </div>
            <el-table :data="scoreData.arr" style="width: 80%" border stripe size="small"
                      class="courseTable" max-height="400">
              <el-table-column label="姓名" prop="studentName" width="120" show-overflow-tooltip/>
              <el-table-column label="学号" prop="studentNumber" width="210" show-overflow-tooltip/>
              <el-table-column label="平时成绩" width="80" show-overflow-tooltip>
                <template #default="scope">
                  <el-input v-model="scope.row.dailyScore" placeholder="请输入平时成绩" type="number"
                            maxlength="3">{{
                      scope.row.dailyScore
                    }}
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="期末成绩" show-overflow-tooltip>
                <template #default="scope">
                  <el-input v-model="scope.row.endScore" placeholder="请输入期末成绩" type="number"
                            maxlength="3">{{
                      scope.row.endScore
                    }}
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" show-overflow-tooltip>
                <template #default="scope">
                  <el-button size="medium" @click="submitEditRow(scope.row)" class="button"
                             type="primary">确认
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import service from '@/request';
import {hideLoading, showLoading} from '@/utils/loading';
import {messageError, messageSuccess, messageWarning} from '@/utils/message';
import {hide} from '@popperjs/core';
import {ElMessageBox} from 'element-plus';
import {reactive, ref} from 'vue';


let idCourse = ref();
let isShow = ref(false);
let studentNumber = ref();
let formCourseData = ref();//改增校验绑定的空form
let searchOrigin = ref();
let search = ref();
let backData = reactive({
  arr: [],
});
let studentData = reactive({
  arr: [],
});
let scoreData = reactive({
  arr: [],
})

let typeShow = ref('info')

const check = () => {
  search.value = searchOrigin.value; //防止用户只更改input的值不搜索
  checkCourse(search.value)
}

const changeInfo = () => {
  typeShow.value = 'info'
}
const changeSelect = async () => {
  await loadStudentTable(search.value);
  typeShow.value = 'select';


}
const changeScore = async () => {
  await getScore(search.value);
  typeShow.value = 'score';

}

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
  name: [{required: true, message: '请输入课程名！', trigger: 'blur'}],
  description: [{required: true, message: '请输入课程简介！', trigger: 'blur'}],
  weekStart: [{required: true, message: '请选择课程起始周！', trigger: 'blur'},],
  weekEnd: [{required: true, message: '请选择课程结束周！', trigger: 'blur'},],
  time: [{type: 'array', required: true, message: '请选择上课时间', trigger: 'change'}],
  capacity: [{required: true, message: '请输入课程容量！', trigger: 'blur'}],
  studentClass: [{required: true, message: '请选择学生班级！', trigger: 'blur'}],
  location: [{required: true, message: '请输入上课地址！', trigger: 'blur'}],
  weight: [{required: true, message: '请输入平时分占比!', trigger: 'blur'}],
  semester: [{required: true, message: '请选择学期！', trigger: 'blur'}],
  credit: [{required: true, message: '请输入学分！', trigger: 'blur'}],
  studentSection: [{required: true, message: '请输入上课年级！', trigger: 'blur'}]
})

const mul = {multiple: true}
let editForm = reactive({
  id: '',
  name: '',
  weekStart: '',
  weekEnd: '',
  time: [],
  capacity: '',
  studentClass: [],
  studentSection: [],
  location: '',
  year: '',
  semester: '',
  credit: '',
  description: '',
  weight: '',
});

/* 
264769460420874240 */
//输入课程号，得到token,为editForm附上初值
const checkCourse = async (id) => {
  if (search.value == '' || search.value == undefined || search.value == null) return
  showLoading();
  await service.post('/api/course/managerFind', {token: localStorage.getItem('token'), id: search.value}).then(res => {
    console.log('返回了数据')
    let data = res.data;
    console.log(data.token)
    if (data.success) {
      console.log(search.value)
      hideLoading();
      localStorage.setItem('token', data.token);
      messageSuccess("查询成功！")
      let content = data.content;
      editForm.name = content.name;
      editForm.weekStart = content.weekStart;
      editForm.weekEnd = content.weekEnd;
      editForm.time = [];
      for (let i = 0; i < content.time.length; i++) {
        let midArray = [];
        midArray.push(content.time[i].weekDay);
        midArray.push(content.time[i].section);
        editForm.time.push(midArray);
      }
      console.log('!!!')
      console.log(editForm.time)
      editForm.weight = content.weight;
      editForm.capacity = content.capacity;
      editForm.studentClass = content.studentClass;
      let arrIntSet = [];
      // 将字符串转换成数组，此时是字符串数组
      let arrString = content.studentClass.substr(1, content.studentClass.length - 2).split(',');
      for (let arrInt = 0; arrInt < arrString.length; arrInt++) {
        arrIntSet.push(parseInt(arrString[arrInt]))
      }
      // 将新的Number数组，绑定到select空间的v-model上
      editForm.studentClass = arrIntSet;
      arrIntSet = [];
      let arrInt;
      arrString = content.studentSection.substr(1, content.studentSection.length - 2).split(',');
      for (arrInt = 0; arrInt < arrString.length; arrInt++) {
        arrIntSet.push(parseInt(arrString[arrInt]))
      }
      editForm.studentSection = arrIntSet;
      editForm.location = content.location;
      editForm.year = content.year;
      editForm.description = content.description;
      editForm.semester = content.semester;
      editForm.credit = content.credit;
      editForm.id = content.id;

      //显示信息界面
      isShow.value = true;
    } else {
      hideLoading();
      messageError(data.message)
    }
  })
      .catch(function (error) {
        hideLoading();
        messageError("服务器开小差了呢");
        console.log(error)
      })
}

//提交的选项，提交之后重新调用一下方法更新一下editForm的值
let weekDayValue = ref();
let sectionValue = ref();
let midTime = reactive([]);
const sumbitEditRow = async () => {
  for (let i = 0; i < editForm.time.length; i++) {
    weekDayValue.value = editForm.time[i][0];
    sectionValue.value = editForm.time[i][1];
    midTime.push({weekDay: weekDayValue.value, section: sectionValue.value});
  }
  editForm.time = midTime;
  console.log(editForm)
  await formCourseData.value.validate(((valid) => {
    if (valid) {
      showLoading()
      service.post('/api/course/managerSave', {token: localStorage.getItem('token'), course: editForm}).then(res => {
        let data = res.data;
        console.log(res.data);
        if (data.success) {
          hideLoading();
          console.log(res.data)
          localStorage.setItem('token', data.token);
          messageSuccess('修改成功！');
          checkCourse(search.value);
        } else {
          hideLoading();
          messageError(data.message)
        }
      })
          .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
          })
    } else {
      messageWarning("请填写完整!")
    }
  }))
  formCourseData.value = null;
};

const delectCourse = async () => {
  await ElMessageBox.confirm(
      '确认删除该课程吗?',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        showLoading();
        service.post('/api/course/managerDelete', {
          token: localStorage.getItem('token'),
          id: search.value
        }).then(res => {
          let data = res.data;
          console.log(res.data);
          if (data.success) {
            hideLoading();
            messageSuccess("删除成功！")
            localStorage.setItem('token', data.token)
            isShow.value = false;
          } else {
            hideLoading()
            messageError(data.message)
          }

        }).catch(function (error) {
          hideLoading();
          messageError("服务器开小差了呢");
          console.log(error)
        })
      })
}

const loadStudentTable = async (id) => {
  showLoading();
  await service.post('/api/course/managerFindStudent', {
    token: localStorage.getItem("token"), id: id
  }).then(res => {
    console.log(id)
    console.log(res.data);
    if (res.data.success) {
      console.log(id)
      console.log(res.data)
      hideLoading();
      console.log(res.data.content)
      console.log(id)
      let data = res.data;
      localStorage.setItem('token', data.token)
      console.log(localStorage.getItem('token'))
      let content = data.content;
      studentData.arr = content;


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
};


const addStudent = async () => {
  showLoading()
  await service.post('/api/course/managerAddStudent',
      {
        token: localStorage.getItem("token"),
        courseId: search.value,
        studentNumber: studentNumber.value,
      })
      .then(res => {
            if (res.data.success) {
              console.log(res.data);
              hideLoading()
              messageSuccess("添加成功！")
              localStorage.setItem("token", res.data.token)
              loadStudentTable(search.value);
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
};
const deleteStudent = (row) => {  //删  //异步可能有问题
  ElMessageBox.confirm(
      '确认移除该学生吗?',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        console.log(search.value)
        console.log(row.userNumber)
        showLoading();
        console.log(row.id)
        service.post('/api/course/managerRemoveStudent', {
          token: localStorage.getItem("token"),
          courseId: search.value,
          studentId: row.id
        }).then(res => {
          console.log(res)
          if (res.data.success) {
            localStorage.setItem("token", res.data.token)
            hideLoading()
            messageSuccess('删除成功!')
            /* loadStudentTable(search.value) */ //重新加载现在表单中的数据
          } else {
            hideLoading();
            messageWarning(res.data.message)
            loadStudentTable(search.value);
          }
        })
            .catch(function (error) {
              hideLoading();
              messageError("服务器开小差了呢");
              console.log(error)
            })
      })

}


const getScore = async (id) => {
  showLoading();
  console.log()
  await service.post('/api/score/managerFind', {token: localStorage.getItem("token"), courseId: id}).then(res => {
    if (res.data.success) {
      hideLoading();
      console.log(id)
      let data = res.data;
      console.log(data.content)
      let arr = data.content
      scoreData.arr = arr
      localStorage.setItem('token', data.token)
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
const submitEditRow = async (row) => {
  if (row.dailyScore == '' || row.dailyScore == null || row.endScore == null || row.endScore == '') {
    messageWarning('请填写完整！');
    return;
  }
  await service.post('/api/score/managerSave',
      {
        token: localStorage.getItem("token"),
        courseId: search.value,
        studentId: row.studentId,
        dailyScore: row.dailyScore,
        endScore: row.endScore,
      })
      .then(res => {
            if (res.data.success) {
              hideLoading()
              messageSuccess("修改成功！")
              localStorage.setItem("token", res.data.token)
              getScore(search.value)
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

console.log(editForm)

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
    height: 95.1vh;
    border-radius: 1vw;
    box-shadow: 0 0 1.25vh 0 #b9ccee;

    .header {
      height: 35vh;
      width: 100%;
      /*  background-color: rgb(232, 245, 247); */
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

            .deleteButton {
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

    .divider {
      height: 0.1vh;
      width: 80vw;
      background-color: rgb(151, 151, 185);
    }

    .main {
      height: 65vh;
      width: 80vw;
      /*    background-color: bisque; */
      display: flex;
      flex-direction: row;

      .menu {
        width: 15vw;
        height: 65vh;
        background-color: rgb(255, 255, 255);

        .menuButtonSpan {
          width: 15vw;
        }

        .menuButton {
          width: 15vw;
          height: 6vh;
          font-size: 1vw;
          font-weight: 550;
          line-height: 4vh;
        }
      }

      .menuDivider {
        height: 65vh;
        width: 0.05vw;
        background-color: rgb(164, 175, 199);
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

        .submitButton {
          width: 9vw;
          height: 4vh;
          margin-left: 20vw;
        }

        .tableDiv {
          padding-left: 5vw;

          .studentTitle {
            /*    padding-left:4vw; */
            margin-top: 4vh;
            height: 6vh;
            font-family: 微软雅黑;
            font-size: 3vh;
            font-weight: 500;
            line-height: 1vh;
            color: #0273f1;
          }

          .studentNumberInput {
            width: 40%;
          }

          .addButton {
            margin-left: 3vw;
            width: 10vw;
            height: 5vh;
            border-color: #0273f1;
            border-style: solid;
            border-width: 0.3vw;
            border-radius: 1vw;
            color: #0273f1;
          }

        }
      }
    }

    /*Resize the wrap to see the search bar change!*/
  }
}
</style>