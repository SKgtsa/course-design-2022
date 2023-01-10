<template>
  <div class="content" :style="{
    'padding-top': `${mobile ? '5vh' : '1vh'}`,
    'height': `${mobile ? '90vh' : '100vh'}`
  }">
    <div class="pageContent" :style="{
      'width': `${mobile ? '100%' : '80vw'}`
    }">
      <!-- :row-key="record=>record.id" -->
      <div class="titleBox">
        <a style="font-size: 6vh;
        font-weight: 500;
        padding-left: 2vw;
      ">课程管理</a>
        <el-button class="addButton" @click="add">
          <a> 添加课程</a>
        </el-button>
      </div>
      <div class="checkBox" v-if="!mobile">
        <a class="selectLabel">学年:</a>
        <el-select style="width:8vw;height:auto" v-model="yearsValue" placeholder="学年">
          <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <a class=" selectLabel" style="padding-left:8vw">学期:</a>
        <el-select style="width:13vw" v-model="semesterValue" placeholder="学期：">
          <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label"
            :value="item.value"></el-option>
        </el-select>
        <el-button
          style="margin-left: 5vw;width: 20vh;height:6vh;background-color: rgba(211,227,253,0.9);color: rgba(4,30,73);border-radius: 1vw;float: right"
          @click="loadCourseTable">
          <a style=" font-size:2vh;font-weight: 600;">查询创建课程信息</a>
        </el-button>
      </div>
      <div class="checkBox" v-if="mobile">
        <span style="display: flex;flex-direction: row;"><a class="selectLabel"
            style="font-size:3.1vw;line-height: 4vh;">学年:</a>
          <el-select style="width:30vw;height:auto" v-model="yearsValue" placeholder="学年">
            <el-option v-for="item in yearOptions" :key="item.value" :label="item.label"
              :value="item.value"></el-option>
          </el-select>
          <a class=" selectLabel" style="padding-left:8vw;font-size: 3.1vw;line-height: 4vh;">学期:</a>
          <el-select style="width:30vw" v-model="semesterValue" placeholder="学期：">
            <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label"
              :value="item.value"></el-option>
          </el-select>
        </span>
        <el-button
          style="margin-left: 30vw;margin-top: 3vh;width: 20vh;height:6vh;background-color: rgba(211,227,253,0.9);color: rgba(4,30,73);border-radius: 1vw;"
          @click="loadCourseTable">
          <a style=" font-size:2vh;font-weight: 600;">查询创建课程信息</a>
        </el-button>
      </div>
      <el-table :data="tableData.arr" border stripe size="large" class="courseTable"
        :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
        :cell-style="{ 'height': '1.875vh', 'font-size': '2vh', 'text-align': 'center', 'font-weight': '450' }">
        <el-table-column  label="课程号" prop="id" width="200" show-overflow-tooltip />
        <el-table-column label="课程" prop="name" width="160" show-overflow-tooltip />
        <el-table-column label="地点" prop="location" width="160" show-overflow-tooltip />
        <el-table-column label="开课周" prop="weekStart" width="100" show-overflow-tooltip />
        <el-table-column label="结束周" prop="weekEnd" width="100" show-overflow-tooltip />
        <el-table-column label="上课时间" prop="time" width="200" show-overflow-tooltip>
          <template #default="time">
            <p v-for="(item, index) in time.row.time">
              星期{{ time.row.time[index].weekDay }}的第{{
                time.row.time[index].section
              }}大节
            </p>
          </template>
        </el-table-column>
        <el-table-column width="250">
          <template #header>
            操作
          </template>
          <template #default="scope" style="display: flex;flex-direction: column">
            <!-- 默认行和列 -->
            <!-- <el-button @click="viewDetails(scope.row)" class="button" type="primary">课程详情</el-button> -->
            <el-button size="small" @click="loadStudentTable(scope.row.id)" class="button"
              type="primary">选课学生</el-button>
            <el-button size="small" @click="handleEdit(scope.row)" class="button">编辑</el-button>
            <el-button size="small" type="danger" class="button" @click="handleDelete(scope.row)">删除课程</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog v-model="centerDialogVisible" :width="mobile ? '90%' : '45%'">
    <el-form :model="editForm" class="areaTextInput" ref="formCourseData" :rules="rulesEditForm" label-width="auto"
      label-position="right">
      <el-form-item label="课程名:" prop="name">
        <el-input v-if="typeOperation === 'edit'" style="width:20vw" v-model="editForm.name" maxlength="15">{{
          editForm.name
        }}</el-input>
        <el-input v-if="typeOperation === 'add'" style="width:20vw" v-model="editForm.name" maxlength="15"></el-input>
      </el-form-item>

      <el-form-item label="简介:" prop="description">
        <el-input v-if="typeOperation === 'edit'" type="textarea" maxlength="50" rows="3"
          v-model="editForm.description">{{ editForm.description }}</el-input>
        <el-input v-if="typeOperation === 'add'" type="textarea" maxlength="50" rows="2"
          v-model="editForm.description"></el-input>
      </el-form-item>

      <el-form-item label="开课周:" prop="weekStart">
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.weekStart" placeholder="开课周">
          <el-option v-for="item in weekStartOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'add'" v-model="editForm.weekStart" placeholder="开课周">
          <el-option v-for="item in weekStartOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="结课周:" prop="weekEnd">
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.weekEnd" placeholder="结课周">
          <el-option v-for="item in weekEndOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'add'" v-model="editForm.weekEnd" placeholder="结课周">
          <el-option v-for="item in weekEndOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="上课时间:" prop="time">
        <el-cascader v-model="editForm.time" :options="time" :props="mul" clearable placeholder="上课时间" />
      </el-form-item>
      <span style="display: flex; flex-direction: row;" v-if="!mobile">
        <el-form-item label="学年:" prop="year">
          <el-select v-if="typeOperation === 'add'" v-model="editForm.year" placeholder="2020">
            <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.year" placeholder="2020">
            <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="学期:" prop="semester">
          <el-select v-if="typeOperation === 'add'" v-model="editForm.semester" placeholder="春季学期">
            <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.semester" placeholder="春季学期">
            <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </span>
      <el-form-item label="学年:" prop="year" v-if="mobile">
        <el-select v-if="typeOperation === 'add'" v-model="editForm.year" placeholder="2020">
          <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.year" placeholder="2020">
          <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="学期:" prop="semester" v-if="mobile">
        <el-select v-if="typeOperation === 'add'" v-model="editForm.semester" placeholder="春季学期">
          <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.semester" placeholder="春季学期">
          <el-option v-for="item in semesterOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>


      <el-form-item label="授课地点:" prop="location">
        <el-input v-if="typeOperation === 'edit'" style="width:15vw" v-model="editForm.location" maxlength="30">{{
          editForm.location
        }}</el-input>
        <el-input v-if="typeOperation === 'add'" style="width:15vw" v-model="editForm.location"
          maxlength="30"></el-input>
      </el-form-item>
      <el-form-item label="平时分权重:" prop="weight">
        <el-input v-if="typeOperation === 'edit'" type="number" maxlength="4" style="width:15vw" placeholder="0.3"
          v-model="editForm.weight">{{ editForm.weight }}</el-input>
        <el-input v-if="typeOperation === 'add'" type="number" maxlength="4" style="width:15vw" placeholder="0.3"
          v-model="editForm.weight"></el-input>
      </el-form-item>
      <span style="display:flex;flex-direction:row">
        <el-form-item label="课容量:" prop="capacity">
          <el-input v-if="typeOperation === 'edit'" style="width:19vw" v-model="editForm.capacity" maxlength="5"
            type="number">{{
              editForm.capacity
            }}</el-input>
          <el-input v-if="typeOperation === 'add'" style="width:19vw" v-model="editForm.capacity" maxlength="5"
            type="number"> </el-input>
        </el-form-item>
      </span>
      <el-form-item label="学分:" prop="credit">
        <el-input v-if="typeOperation === 'edit'" style="width:17vw" v-model="editForm.credit" maxlength="2"
          type="number">{{
            editForm.credit
          }}</el-input>
        <el-input v-if="typeOperation === 'add'" style="width:17vw" v-model="editForm.credit" maxlength="2"
          type="number"></el-input>
      </el-form-item>
      <el-form-item label="授课班级:" prop="studentClass">
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentClass" placeholder="1班" multiple="true">
          <el-option v-for="item in classOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'add'" v-model="editForm.studentClass" placeholder="1班" multiple="true">
          <el-option v-for="item in classOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="授课年级:" prop="studentSection">
        <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentSection" placeholder="2019" multiple="true">
          <el-option v-for="item in studentSectionOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-if="typeOperation === 'add'" v-model="editForm.studentSection" placeholder="2019" multiple="true">
          <el-option v-for="item in studentSectionOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
    </el-form>
    <div class="dialogButtonPage">
      <el-button type="primary" @click="submitEditRow" class="dialogButton">确定</el-button>
      <el-button @click="closeDialog" class="dialogButton">取消</el-button>
    </div>
  </el-dialog>
  <el-dialog v-model="studentDialogVisible" width="70%" v-if="!mobile">
    <div class="studentTitle">
      选课学生
      <el-input class="studentNumberInput" v-model="studentNumber" placeholder="请输入学生学号" />
      <el-button class="addButton" @click="addStudent">添加学生</el-button>
    </div>
    <el-table :data="studentData.arr" style="width: 90%" border stripe size="small" class="courseTable" max-height="400"
      :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
      :cell-style="{ 'height': '1.75vh', 'font-size': '1.25vh', 'text-align': 'center', 'font-weight': '450' }">>
      <el-table-column label="姓名" fixed="left" prop="name" width="150" show-overflow-tooltip />
      <el-table-column label="学号" prop="userNumber" width="250" show-overflow-tooltip />
      <el-table-column label="年级" prop="section" width="150" show-overflow-tooltip />
      <el-table-column label="班级" prop="studentClass" show-overflow-tooltip />
      <el-table-column fixed="right" width="120">
        <template #header>
          操作
        </template>
        <template #default="scope">
          <el-button @click="deleteStudent(scope.row)">删除学生</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
        @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize" style="text-align: center">
      </el-pagination>
    </div>
  </el-dialog>
  <el-dialog v-model="studentDialogVisible" width="90%" v-if="mobile">
    <div class="studentTitle">
      选课学生
      <el-input class="studentNumberInput" v-model="studentNumber" placeholder="请输入学生学号" />
      <el-button class="addButton" @click="addStudent">添加学生</el-button>
    </div>
    <el-table :data="studentData.arr" style="width: 90%" border stripe size="small" class="courseTable" max-height="400"
      :header-cell-style="{ 'height': '3.75vh', 'font-size': '2.25vh', 'text-align': 'center', 'font-weight': '800' }"
      :cell-style="{ 'height': '1.75vh', 'font-size': '1.25vh', 'text-align': 'center', 'font-weight': '450' }">>
      <el-table-column label="姓名" fixed="left" prop="name" width="80" show-overflow-tooltip />
      <el-table-column label="学号" prop="userNumber" width="200" show-overflow-tooltip />
      <el-table-column label="年级" prop="section" width="150" show-overflow-tooltip />
      <el-table-column label="班级" prop="studentClass" width="120" show-overflow-tooltip />
      <el-table-column fixed="right" width="120">
        <template #header>
          操作
        </template>
        <template #default="scope">
          <el-button @click="deleteStudent(scope.row)">删除学生</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
        @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize" style="text-align: center">
      </el-pagination>
    </div>
  </el-dialog>

</template>
<script lang="ts" setup>
import { computed, ref, reactive } from 'vue'
import service from '../../request/index'
import { messageSuccess, messageWarning, messageError, messageInfo } from '../../utils/message'
import { ElMessage, ElMessageBox, rowContextKey } from 'element-plus'
import { hideLoading, showLoading } from "@/utils/loading";
import { mobile } from "@/global/global";
const mul = { multiple: true }
let yearsValue = ref(2022);
let semesterValue = ref('春季学期');
interface time {
  weekDay: Number;
  section: Number;
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
    value: '1班',
    label: '1班',
  }, {
    value: '2班',
    label: '2班',
  }, {
    value: '3班',
    label: '3班',
  }, {
    value: '4班',
    label: '4班',
  }, {
    value: '5班',
    label: '5班',
  }, {
    value: '6班',
    label: '6班',
  },{
    value: '7班',
    label: '7班',
  },{
    value: '8班',
    label: '8班',
  },{
    value: '菁英班',
    label: '菁英班',
  },
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
let tableData = reactive(
  {
    arr: []
  }
)
const handleChange = (value) => {
  console.log(value)
}

let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false); //控制改增弹出框
let studentDialogVisible = ref(false); //控制学生弹出框
let studentNumber = ref();
let formCourseData = ref();//改增校验绑定的空form
let centerDialogVisibleCheck = ref(false);//查的弹出框
let pageCount = ref(1);//查看选课学生给的总页数
let pageNum = ref(1);
let pageSize = ref(20);
let idCourse = ref();//用于删除学生的课程id
const rulesEditForm = reactive({   /* 定义校验规则 */
  name: [{ required: true, message: '请输入课程名！', trigger: 'blur' }],
  description: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  weekStart: [{ required: true, message: '请选择课程起始周！', trigger: 'blur' },],
  weekEnd: [{ required: true, message: '请选择课程结束周！', trigger: 'blur' },],
  time: [{ type: 'array', required: true, message: '请选择上课时间', trigger: 'change' }],
  capacity: [{ required: true, message: '请输入课容量！', trigger: 'blur' }],
  studentClass: [{ required: true, message: '请选择学生班级！', trigger: 'blur' }],
  location: [{ required: true, message: '请输入上课地址！', trigger: 'blur' }],
  semester: [{ required: true, message: '请选择学期！', trigger: 'blur' }],
  credit: [{ required: true, message: '请输入学分！', trigger: 'blur' }],
  studentSection: [{ required: true, message: '请输入上课年级！', trigger: 'blur' }]
})

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
  description: '',
  semester: '',
  credit: '',
  courseId: '0',
  weight: '',
});
const loadCourseTable = async () => {
  showLoading();
  await service.post('/api/course/teacherFind', { token: localStorage.getItem("token"), semester: semesterValue.value, year: yearsValue.value }).then(res => {
    if (res.data.success) {
      hideLoading();
      let data = res.data;
      messageSuccess('查询成功')
      localStorage.setItem('token', data.token);
      tableData.arr = data.content;
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
const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
  editForm.name = '';
  editForm.weekStart = '';
  editForm.weekEnd = '';
  editForm.capacity = '';
  editForm.studentClass = [];
  editForm.studentSection = [];
  editForm.time = [];
  editForm.location = '';
  editForm.year = '';
  editForm.description = '';
  editForm.semester = '';
  editForm.credit = '';
  editForm.weight = '';
}

let studentData = reactive({
  arr: [],
});

//加载选课学生
const loadStudentTable = async (id) => {
  showLoading();
  await service.post('/api/course/findCourseStudent', {
    token: localStorage.getItem("token"), pageNum: pageNum.value,
    pageSize: pageSize.value, courseId: id
  }).then(res => {
    if (res.data.success) {
      hideLoading();
      console.log(res.data.content)
      console.log(id)
      idCourse.value = id;
      let data = res.data;
      let content = data.content;
      studentData.arr = content;
      localStorage.setItem('token', data.token)
      studentDialogVisible.value = true;

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

//编辑课程信息
const handleEdit = (row) => {  //改
  console.log(row)
  editForm.name = row.name;
  editForm.weekStart = row.weekStart;
  editForm.weekEnd = row.weekEnd;
  editForm.capacity = row.capacity;
/*   editForm.studentClass = row.studentClass; */
  editForm.courseId = row.id;
  editForm.weight = row.weight;
  let arrIntSet = [];
  editForm.time = [];
  for (let i = 0; i < row.time.length; i++) {
    let midArray = [];
    midArray.push(row.time[i].weekDay);
    midArray.push(row.time[i].section);
    editForm.time.push(midArray);
  }
  // 将字符串转换成数组，此时是字符串数组
  let arrString = row.studentClass.substr(1, row.studentClass.length - 2).split(',');
  for (let arrInt = 0; arrInt < arrString.length; arrInt++) {
    arrIntSet.push(arrString[arrInt])
  }
  // 将新的Number数组，绑定到select空间的v-model上
  editForm.studentClass = arrIntSet;
  arrIntSet = [];
  let arrInt;
  arrString = row.studentSection.substr(1, row.studentSection.length - 2).split(',');
  for (arrInt = 0; arrInt < arrString.length; arrInt++) {
    arrIntSet.push(arrString[arrInt])
  }
  editForm.studentSection = arrIntSet;
  /* for(let arrInt = 0 ;arrInt < row.studentClass.length;arrInt++){
    editForm.studentClass.push(row.studentClass[arrInt])
  }
  for(let arrInt = 0 ;arrInt < row.studentSection.length;arrInt++){
    editForm.studentSection.push(row.studentSection[arrInt])
  } */
  editForm.location = row.location;
  editForm.year = row.year;
  editForm.description = row.description;
  editForm.semester = row.semester;
  editForm.credit = row.credit;
  editForm.courseId = row.id;
  typeOperation.value = 'edit';
  centerDialogVisible.value = true;
  console.log(editForm)
}


//删除课程
const handleDelete = async (row) => {
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
      service.post('/api/course/delete', { token: localStorage.getItem("token"), courseId: row.id }).then(res => {
        if (res.data.success) {
          hideLoading()
          localStorage.setItem("token", res.data.token)
          messageSuccess('删除成功!')
          loadCourseTable() //重新加载现在表单中的数据

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
    })

}

//删除课程学生
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
      console.log(idCourse.value)
      console.log(row.userNumber)
      service.post('/api/course/removeStudent', { token: localStorage.getItem("token"), studentNumber: row.userNumber, courseId: idCourse.value, }).then(res => {
        console.log(res)
        if (res.data.success) {
          hideLoading()
          messageSuccess('删除成功!')
          localStorage.setItem("token", res.data.token)
          loadStudentTable(idCourse.value) //重新加载现在表单中的数据
        } else {
          hideLoading();
          messageWarning(res.data.message)
        }
      })
    })
    .catch(function (error) {
      hideLoading();
      messageError("服务器开小差了呢");
      console.log(error)
    })
}
const addStudent = async () => {
  showLoading()
  await service.post('/api/course/addStudent',
    {
      token: localStorage.getItem("token"),
      studentNumber: studentNumber.value,
      courseId: idCourse.value,
    })
    .then(res => {
      if (res.data.success) {
        hideLoading()
        messageSuccess("添加成功！")
        localStorage.setItem("token", res.data.token)
        loadStudentTable(idCourse.value);
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

let midTime = reactive([]);//中间变量
let weekDayValue = ref('');
let sectionValue = ref('');
const submitEditRow = async () => {
  for (let i = 0; i < editForm.time.length; i++) {
    weekDayValue.value = editForm.time[i][0];
    sectionValue.value = editForm.time[i][1];
    midTime.push({ weekDay: weekDayValue.value, section: sectionValue.value });
  }
  editForm.time = midTime;
  console.log(editForm)
  console.log(editForm.time)
  await formCourseData.value.validate(((valid) => {
    if (valid) {
      if (typeOperation.value === 'edit') {
        /* handleEdit() */
        showLoading();
        service.post('/api/course/save',
          {
            token: localStorage.getItem("token"),
            name: editForm.name,
            id: editForm.courseId,
            weekStart: editForm.weekStart,
            weekEnd: editForm.weekEnd,
            time: editForm.time,
            capacity: editForm.capacity,
            studentClass: editForm.studentClass,
            studentSection: editForm.studentSection,
            location: editForm.location,
            year: editForm.year,
            description: editForm.description,
            semester: editForm.semester,
            credit: editForm.credit,
            courseId: editForm.courseId,
            weight: editForm.weight,
          })
          .then(res => {  //直接把这一行的数据给出去可以吗
            if (res.data.success) {
              hideLoading();
              localStorage.setItem("token", res.data.token)
              messageSuccess("编辑成功！")
              typeOperation.value = '';
              loadCourseTable()
            } else {
              hideLoading();
              messageError(res.data.message)
            }
          })
          .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
          })
      } else if (typeOperation.value === 'add') {
        showLoading()
        service.post('/api/course/save',
          {
            token: localStorage.getItem("token"),
            name: editForm.name,
            weekStart: editForm.weekStart,
            weekEnd: editForm.weekEnd,
            time: editForm.time,
            capacity: editForm.capacity,
            studentClass: editForm.studentClass,
            studentSection: editForm.studentSection,
            location: editForm.location,
            year: editForm.year,
            description: editForm.description,
            semester: editForm.semester,
            credit: editForm.credit,
            weight: editForm.weight,
          })
          .then(res => {
            if (res.data.success) {
              hideLoading()
              messageSuccess("添加成功！")
              localStorage.setItem("token", res.data.token)
              typeOperation.value = '';
              loadCourseTable()
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
      } else {
        messageError('出错了！')
      }
    } else {
      messageWarning("请填写完整!")
    }
  }))
  editForm.name = '';
  editForm.weekStart = '';
  editForm.weekEnd = '';
  /* editForm.time.weekDay = '';
  editForm.time.section = ''; */
  editForm.capacity = '';
  editForm.time = [],
    editForm.location = '';
  editForm.studentClass = [];
  editForm.studentSection = [];
  editForm.year = '';
  editForm.description = '';
  editForm.semester = '';
  editForm.credit = '';
  editForm.courseId = '0';
  editForm.weight = '';
  centerDialogVisible.value = false;
  centerDialogVisibleCheck.value = false;
  typeOperation.value = '';
  formCourseData.value = null;
};

const closeDialog = () => {
  if (typeOperation.value === 'edit') {
    centerDialogVisible.value = false;
    messageInfo("编辑取消")
    typeOperation.value = '';
  } else if (typeOperation.value === 'add') {
    centerDialogVisible.value = false;
    typeOperation.value = '';
    messageInfo("添加取消")
  }
}
const handleCurrentChange = (currentPage) => {
  pageNum.value = currentPage;
  console.log(currentPage)
}


</script>
<style lang="scss" scoped>
.studentTitle {
  margin-top: 3.75vh;
  height: 7.5vh;
  font-family: 微软雅黑;
  font-size: 3vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}

.studentNumberInput {
  width: 40%;
}


.content {
  width: 100%;

  padding-top: 1vh;

  .pageContent {
    height: 80vh;
    background-color: #FFFFFF;
    border-radius: 3vw;
    padding-top: 3vh;
    padding-bottom: 3vh;
    box-shadow: 0 0 10px 0 #b9ccee;
    margin: auto;

    .titleBox {
      margin-top: 1.5vh;
      height: 5vh;
      line-height: 1vh;
      color: #0273f1;

      .addButton {
        font-size: 2vh;
        width: 15vh;
        height: 6vh;
        border-color: #0273f1;
        border-style: solid;
        border-width: 4px;
        border-radius: 1vw;
        color: #0273f1;
        margin: 0 3%;
      }

    }


    .courseTable {
      height: 75%;
      width: 98%;
      margin: auto;
    }

    .checkBox {
      padding: 3vh 2vw 2vh;

      .selectLabel {
        font-size: 1.5vw;
        padding-right: 3vw;
        line-height: 4vh;
      }

    }

    .pracitceTable {
      background-color: aqua;

      .button {
        width: 9vw;
        height: 5.5vh;
      }
    }
  }
}



.practiceDialog {
  width: 37.5vh !important;
  height: 75vh !important;
}

.areaTextInput {
  font-size: 1.875vh;
}

.dialogButtonPage {
  text-align: center;
}

.dialogButton {
  height: 5vh;
  width: 8vw;
  padding-left: 0.625vh;
  padding-right: 0.625vh;
}

.pagination {
  text-align: center;
  padding-top: 1.5vh;
  padding-bottom: 1.25vh;
}
</style>
