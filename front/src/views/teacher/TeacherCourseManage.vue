<template>
  <!-- 界面显示：姓名，标题，按钮
按钮：编辑，查找，删除
顶部需不需要提示一下目前所处的版块
然后找个位置设置个添加按钮
这个查找是搞一个输入框？ -->
  <!--这个不能添加姓名是别人的社会实践吧，这个不确定有没有问题-->
  <div class="content">
    <div class="pageContent">
      <!-- :row-key="record=>record.id" -->
      <div class="title">
        课程管理
        <el-button class="addButton" @click="add">添加课程</el-button>
      </div>
      <el-table :data="tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)" style="width: 80%" border
                stripe size="large" class="courseTable">
        <!-- 显示斑马纹和边框 -->
        <el-table-column label="序号" type="index" width="80"/>
        <!-- <el-table-column label="姓名" prop="studentName" width="120"  show-overflow-tooltip  /> -->
        <el-table-column label="课程" prop="name" width="350" show-overflow-tooltip/>

        <el-table-column>
          <template #header>
            <!-- 默认表头 -->
            <!--            <el-input class="search" v-model="search" size="large" placeholder="搜索你的社会实践" :suffix-icon="Search" />-->
          </template>
          <template #default="scope">
            <!-- 默认行和列 -->
            <el-button size="small" @click="checkStudent(scope.row)" class="button" type="primary">选课学生</el-button>
            <el-button size="small" @click="handleEdit(scope.row)" class="button">编辑课程</el-button>
            <el-button size="small" type="danger" class="button" @click="handleDelete(scope.row)">删除课程</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="prev, pager, next,jumper, ->" :total="tableData.length"
                       @current-change="handleCurrentChange" v-model:current-page="currentPage" :page-size="pageSize"
                       style="text-align: center">
        </el-pagination>
      </div>
    </div>
    <!-- 用一个变量来判断是否弹出这个对话框 -->
    <!-- 再用一个变量判断是查看还是编辑，添加感觉和编辑差不多，编辑和添加用input框住，
      编辑要有初始的数值，查看用span框住，也要有初始值，添加用input框住，没有初始值-->
    <el-dialog v-model="centerDialogVisible" width="45%">
      <el-form :model="editForm" class="areaTextInput" ref="formData" :rules="rulesEditForm">
        <el-form-item label="课程名" prop="name">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.name">{{ editForm.name }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.name"></el-input>
        </el-form-item>

        <el-form-item label="简介" prop="description">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.description">{{ editForm.description }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.description"></el-input>
        </el-form-item>

        <el-form-item label="开课周" prop="weekStart">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.weekStart">{{ editForm.weekStart }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.weekStart"></el-input>
        </el-form-item>

        <el-form-item label="结课周" prop="weekEnd">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.weekEnd">{{ editForm.weekEnd }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.weekEnd"></el-input>
        </el-form-item>

        <el-form-item label="上课时间" prop="classTime">
          <!--          <el-input v-if="typeOperation === 'edit'" v-model="editForm.">{{ editForm. }}</el-input>-->
          <!--          <el-input v-if="typeOperation === 'add'" v-model="editForm."></el-input>-->
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.classTime.weekDay" placeholder="Select">
            <el-option
                v-for="weekDay in dayOptions"
                :key="weekDay.value"
                :label="weekDay.label"
                :value="weekDay.value"
            />
          </el-select>

          <el-select v-if="typeOperation === 'edit'" v-model="editForm.classTime.section" placeholder="Select">
            <el-option
                v-for="section in sectionOptions"
                :key="section.value"
                :label="section.label"
                :value="section.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.classTime.weekDay" placeholder="Select">
            <el-option
                v-for="item in dayOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.classTime.section" placeholder="Select">
            <el-option
                v-for="item in sectionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>


        </el-form-item>
        <el-form-item label="学年" prop="year">
          <el-select v-if="typeOperation === 'add'" v-model="editForm.year" placeholder="Select">
            <el-option
                v-for="item in yearOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.semester" placeholder="Select">
            <el-option
                v-for="item in semesterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.year" placeholder="Select">
            <el-option
                v-for="item in yearOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.semester" placeholder="Select">
            <el-option
                v-for="item in semesterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>


        <el-form-item label="授课地点" prop="location">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.location">{{ editForm.location }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.location"></el-input>
        </el-form-item>

        <el-form-item label="课容量" prop="capacity">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.capacity">{{ editForm.capacity }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.capacity"></el-input>
        </el-form-item>
        <el-form-item label="授课班级" prop="studentClass">
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentClass" placeholder="Select"
                     multiple="true">
            <el-option
                v-for="studentClass in classOptions"
                :key="studentClass.value"
                :label="studentClass.label"
                :value="studentClass.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.studentClass" placeholder="Select"
                     multiple="true">
            <el-option
                v-for="studentClass in classOptions"
                :key="studentClass.value"
                :label="studentClass.label"
                :value="studentClass.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="授课年级" prop="studentSection">
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentSection" placeholder="Select"
                     multiple="true">
            <el-option
                v-for="studentSection in studentSectionOptions"
                :key="studentSection.value"
                :label="studentSection.label"
                :value="studentSection.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.studentSection" placeholder="Select"
                     multiple="true">
            <el-option
                v-for="studentSection in studentSectionOptions"
                :key="studentSection.value"
                :label="studentSection.label"
                :value="studentSection.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="学分" prop="credit">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.credit">{{ editForm.credit }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.credit"></el-input>
        </el-form-item>

      </el-form>
      <div class="dialogButtonPage">
        <el-button @click="closeDialog" class="dialogButton">取消</el-button>
        <el-button type="primary" @click="sumbitEditRow" class="dialogButton">确定</el-button> <!-- 在这个方法里面来判断是啥？ -->
      </div>
    </el-dialog>
    <el-dialog v-model="studentDialogVisible" width="70%">
      <div class="studentTitle">
        选课学生
        <el-input class="studentNumberInput" v-model="input" placeholder="请输入学生学号" />
        <el-button class="addButton" @click="addStudent">添加学生</el-button>
      </div>
      <el-table :data="studentData" style="width: 80%" border
                stripe size="small" class="courseTable" max-height="400">
        <el-table-column label="序号" type="index" width="80"/>
         <el-table-column label="姓名" prop="name" width="120"  show-overflow-tooltip  />
        <el-table-column label="学号" prop="studentNumber" width="350" show-overflow-tooltip/>
        <el-table-column>
          <template #header>
          </template>
          <template #default="scope">
            <el-button size="small" type="danger" class="button" @click="deleteStudent(scope.row)">删除学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>
<script lang="ts" setup>
import {computed, ref, reactive} from 'vue'
import {Search, User} from '@element-plus/icons-vue'
import service from '../../request/index'
import {messageSuccess, messageWarning, messageError, messageInfo} from '../../utils/message'
import {ElMessage, ElMessageBox} from 'element-plus'
import {hideLoading, showLoading} from "@/utils/loading";



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
    value: 1,
    label: '大一',
  }, {
    value: 2,
    label: '大二',
  }, {
    value: 3,
    label: '大三',
  }, {
    value: 4,
    label: '大四',
  }
]
let tableData = reactive([
  {
    name: 'web',
    description: 'web课设',


  },
])
let studentData = reactive([
  {
    name:'xiaoming',
    studentNumber:'123456',
  },
  {
    name:'xiaoming',
    studentNumber:'123456',
  },
  {
    name:'xiaoming',
    studentNumber:'123456',
  },
  {
    name:'xiaoming',
    studentNumber:'123456',
  },
])
/* let tableData =reactive([]); */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false); //控制改增弹出框
let studentDialogVisible = ref(false); //控制改增弹出框
let currentPage = ref(1);
// let currentStudentPage = ref(1);
// let studentPageSize = ref(20);
let pageSize = ref(7);
let input=ref();
let formData = ref();//改增校验绑定的空form
let pageCount = ref();
let centerDialogVisibleCheck = ref(false);//查的弹出框
const rulesEditForm = reactive({   /* 定义校验规则 */
  // name: [{ required: true, message: '请输入课程名！', trigger: 'blur' }],
  // description: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // weekStart: [{ required: true, message: '请输入课程简介！', trigger: 'blur' },{max:16,min:1,message: '请输入”1-16“的教学周!',trigger: 'blur'}],
  // weekEnd: [{ required: true, message: '请输入课程简介！', trigger: 'blur' },{max:16,min:1,message: '请输入”1-16“的教学周!',trigger: 'blur'}],
  // weekDay: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // section: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // capacity: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // studentClass: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // studentSection: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // location: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // semester: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
  // credit: [{ required: true, message: '请输入课程简介！', trigger: 'blur' }],
})
let editForm = reactive({
  token: '',
  name: '',
  weekStart: '',
  weekEnd: '',
  classTime: {
    weekDay: '',
    section: '',
  },
  capacity: '',
  studentClass: '',
  studentSection: '',
  location: '',
  year: '',
  description: '',
  semester: '',
  credit: '',
  courseId: '',
});
let studentForm=reactive({
  token:'',
  studentNumber:'',
  courseId: '',
  name:'',
  id:'',
});
// const search = ref('')
// const filterTableData = computed(() =>
//     tableData.filter(
//         (data) =>
//             !search.value ||
//             data.name.toLowerCase().includes(search.value.toLowerCase())
//     )
// )
/* loginFormPhone.value.validate((valid)=>{
    if(valid){
      service.post('/api/user/loginCode',{code:login_data_phone.captcha}).then(res => {
      console.log(res)
      const data = res.data;
      if(data.success){
        localStorage.setItem("token", data.token)
        localStorage.setItem("userPhone", data.user.phone)
        messageSuccess("登录成功!")
        router.push('/')
      }else{
        messageError(data.message)
      }
  })
 */
const loadCourseTable = async () => {
  showLoading();
  await service.post('/api/course/find', { token: localStorage.getItem("token"), pageNum: currentPage.value, pageSize: pageSize.value}).then(res => {
    if (res.data.success) {
      hideLoading();
      let data = res.data;
      let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
      pageCount.value = data.totalPage;
      tableData = arr
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
loadCourseTable() //进入默认执行

const loadStudentTable = async (row) => {
  showLoading();
  await service.post('/api/course/findCourseList', { token: localStorage.getItem("token"),courseId:row.courseId}).then(res => {
    if (res.data.success) {
      hideLoading();
      let data = res.data;
      let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
      // pageCount.value = data.totalPage;
      studentData = arr
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
const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
  editForm.name = '';
  editForm.weekStart = '';
  editForm.weekEnd = '';
  editForm.classTime.weekDay = '';
  editForm.classTime.section = '';
  editForm.capacity = '';
  editForm.studentClass = '';
  editForm.studentSection = '';
  editForm.location = '';
  editForm.year = '';
  editForm.description = '';
  editForm.semester = '';
  editForm.credit = '';
}
// const addStudent = () => {
//   typeOperation.value = 'addStudent';
//   studentForm.studentNumber='';
//   studentForm.name='';
//
// }



const checkStudent = (row) => {   //查看单个的数据 //一条一条赋值吧，一起赋值出bug了
  studentDialogVisible.value = true;
  studentForm.name=row.name;
  studentForm.id=row.id;
  typeOperation.value = 'checkStudent'; //查看完就完事儿
}


const handleEdit = (row) => {  //改
  console.log(row)
  centerDialogVisible.value = true;
  editForm.name = row.name;
  editForm.weekStart = row.weekStart;
  editForm.weekEnd = row.weekEnd;
  editForm.classTime.weekDay = row.classTime.weekDay;
  editForm.classTime.section = row.classTime.section;
  editForm.capacity = row.capacity;
  editForm.studentClass = row.studentClass;
  editForm.studentSection = row.studentSection;
  editForm.location = row.location;
  editForm.year = row.year;
  editForm.description = row.description;
  editForm.semester = row.semester;
  editForm.credit = row.credit;
  editForm.courseId = row.courseId;
  // editForm = Object.assign({}, row);//先弹对话框，然后提交，提交之后再传参数吧
  typeOperation.value = 'edit';
  console.log(editForm)
}

const handleDelete = (row) => {  //删  //异步可能有问题
  ElMessageBox.confirm(
      '确认删除该课程吗?',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        service.post('/api/course/delete', {token: localStorage.getItem("token"), courseId: row.id}).then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess('删除成功!')
            loadCourseTable() //重新加载现在表单中的数据
            localStorage.setItem("token", res.data.token)
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
const deleteStudent = (row) => {  //删  //异步可能有问题
  ElMessageBox.confirm(
      '确认删除该学生吗?',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        service.post('/api/course/removeStudent', {token: localStorage.getItem("token"), courseId: row.courseId,studentNumber:row.studentNumber}).then(res => {
          if (res.data.success) {
            hideLoading()
            messageSuccess('删除成功!')
            loadStudentTable(row) //重新加载现在表单中的数据
            localStorage.setItem("token", res.data.token)
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
const addStudent = async (row) => {
  await formData.value.validate(((valid) => {
    if (valid) {
      if (typeOperation.value === 'addStudent') {
        showLoading()
        service.post('/api/course/addStudent',
            {
              token: localStorage.getItem("token"),
              studentNumber:input.value,
              courseId:row.courseId,
            })
            .then(res => {
                  if (res.data.success) {
                    hideLoading()
                    messageSuccess("添加成功！")
                    typeOperation.value = '';
                    loadStudentTable(row);
                    localStorage.setItem("token", res.data.token)
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
  typeOperation.value = '';
  formData.value = null;
};
const sumbitEditRow = async () => {
  await formData.value.validate(((valid) => {
    if (valid) {
      if (typeOperation.value === 'edit') {
        /* handleEdit() */
        showLoading();
        service.post('/api/course/save',
            {
              token: localStorage.getItem("token"),
              name: editForm.name,
              weekStart: editForm.weekStart,
              weekEnd: editForm.weekEnd,
              weekDay: editForm.classTime.weekDay,
              section: editForm.classTime.section,
              capacity: editForm.capacity,
              studentClass: editForm.studentClass,
              studentSection: editForm.studentSection,
              location: editForm.location,
              year: editForm.year,
              description: editForm.description,
              semester: editForm.semester,
              credit: editForm.credit,
              courseId:editForm.courseId,
            })
            .then(res => {  //直接把这一行的数据给出去可以吗
              if (res.data.success) {
                hideLoading();
                messageSuccess("编辑成功！")
                typeOperation.value = '';
                loadCourseTable()
                localStorage.setItem("token", res.data.token)

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
              weekDay: editForm.classTime.weekDay,
              section: editForm.classTime.section,
              capacity: editForm.capacity,
              studentClass: editForm.studentClass,
              studentSection: editForm.studentSection,
              location: editForm.location,
              year: editForm.year,
              description: editForm.description,
              semester: editForm.semester,
              credit: editForm.credit,
            })
            .then(res => {
                  if (res.data.success) {
                    hideLoading()
                    messageSuccess("添加成功！")
                    typeOperation.value = '';
                    loadCourseTable()
                    localStorage.setItem("token", res.data.token)
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
  editForm.classTime.weekDay = '';
  editForm.classTime.section = '';
  editForm.capacity = '';
  editForm.studentClass = '';
  editForm.studentSection = '';
  editForm.location = '';
  editForm.year = '';
  editForm.description = '';
  editForm.semester = '';
  editForm.credit = '';
  editForm.courseId='';
  centerDialogVisible.value = false;
  centerDialogVisibleCheck.value = false;
  typeOperation.value = '';
  formData.value = null;
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
  currentPage.value = currentPage;
  console.log(currentPage)
}


/* if(!row.id){
    tableData.splice(index,1) //存在就给他删了，这个有必要吗
}*/
</script>
<style lang="scss" scoped>
.title {
  margin-top: 30px;
  height: 60px;
  font-family: 微软雅黑;
  font-size: 6vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}
.studentTitle {
  margin-top: 30px;
  height: 60px;
  font-family: 微软雅黑;
  font-size: 3vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}.studentNumberInput{
  width: 40%;
 }


.content {
  width: 100%;
  height: 100vh;
  margin-left: 20vh;
  padding-top: 5vh;


  .pageContent {
    width: 70vw;
    height: 70vh;
    width: 70vw;
    height: 70vh;
    background-color: #FFFFFF;
    border-radius: 3vw;
    padding-left: 5vw;
    padding-top: 3vh;
    padding-bottom: 3vh;

    .addButton {
      width: 10vw;
      height: 5vh;
      border-color: #0273f1;
      border-style: solid;
      border-width: 4px;
      border-radius: 1vw;
      color: #0273f1;
      font-size: 2.5vh;
    }

    .pracitceTable {
      background-color: aqua;

      .button {
        width: 60px;
        height: 30px;
      }
    }
  }
}


.practiceDialog {
  width: 300px !important;
  height: 600px !important;
}

.areaTextInput {
  font-size: 15px;
}

.dialogButtonPage {
  text-align: center;
}

.dialogButton {
  padding-left: 5px;
  padding-right: 5px;
}

.pagination {
  padding-bottom: 10px;
  padding-left: 230px;
}
</style>
