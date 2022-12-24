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
        <el-table-column label="序号" type="index" width="80" />
        <!-- <el-table-column label="姓名" prop="studentName" width="120"  show-overflow-tooltip  /> -->
        <el-table-column label="课程" prop="name" width="350" show-overflow-tooltip />

        <el-table-column>
          <template #header>
            <!-- 默认表头 -->
            <!--            <el-input class="search" v-model="search" size="large" placeholder="搜索你的社会实践" :suffix-icon="Search" />-->
          </template>
          <template #default="scope">
            <!-- 默认行和列 -->
            <el-button size="small" @click="handleCheck(scope.row)" class="button" type="primary">选课学生</el-button>
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
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.classTime.weekDay"  placeholder="Select">
            <el-option
                v-for="item in dayOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>

          <el-select v-if="typeOperation === 'edit'" v-model="editForm.classTime.section"  placeholder="Select">
          <el-option
              v-for="item in sectionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.classTime.weekDay"  placeholder="Select">
            <el-option
                v-for="item in dayOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.classTime.section"  placeholder="Select">
            <el-option
                v-for="item in sectionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>


        </el-form-item>
        <el-form-item label="学年" prop="year">
          <el-select v-if="typeOperation === 'add'" v-model="editForm.year"  placeholder="Select">
            <el-option
                v-for="item in yearOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.semester"  placeholder="Select">
            <el-option
                v-for="item in semesterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.year"  placeholder="Select">
            <el-option
                v-for="item in yearOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.semester"  placeholder="Select">
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
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.capacity">{{ editForm.capacity}}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.capacity"></el-input>
        </el-form-item>
        <el-form-item label="授课班级" prop="studentClass">
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentClass"  placeholder="Select" multiple="true">
            <el-option
                v-for="item in classOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.studentClass"  placeholder="Select" multiple="true">
            <el-option
                v-for="item in classOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="授课年级" prop="studentSection">
          <el-select v-if="typeOperation === 'edit'" v-model="editForm.studentSection"  placeholder="Select" multiple="true">
            <el-option
                v-for="item in studentSectionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-select v-if="typeOperation === 'add'" v-model="editForm.studentClass"  placeholder="Select" multiple="true">
            <el-option
                v-for="item in studentSectionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
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
  </div>
</template>
<script lang="ts" setup>
import { computed, ref, reactive } from 'vue'
import { Search, User } from '@element-plus/icons-vue'
import service from '../../request/index'
import { messageSuccess, messageWarning, messageError, messageInfo } from '../../utils/message'
import { ElMessage, ElMessageBox } from 'element-plus'
interface User {
  name: string,
  practiceDescription: string,
  practiceId: string,
  studentName: string
}
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
const sectionOptions=[
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
const yearOptions=[
  {
    value: 2019,
    label: '2019',
  },{
    value: 2020,
    label: '2020',
  },{
    value: 2021,
    label: '2021',
  },{
    value: 2022,
    label: '2022',
  },{
    value: 2023,
    label: '2023',
  },
]
const semesterOptions=[
  {
    value: 1,
    label: '春季学期',
  },{
    value: 2,
    label: '秋季学期',
  },
]
const classOptions=[
  {
    value: 1,
    label: '1班',
  },{
    value: 2,
    label: '2班',
  },{
    value: 3,
    label: '3班',
  }
]
const studentSectionOptions=[
  {
    value: 1,
    label: '大一',
  },{
    value: 2,
    label: '大二',
  },{
    value: 3,
    label: '大三',
  },{
    value: 4,
    label: '大四',
  }
]
let tableData = reactive([
  {
    name:'web',

  },

])
/* let tableData =reactive([]); */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,checkStudent,add 编辑，查看，添加
let centerDialogVisible = ref(false);
let isShow = ref(false);
let currentPage = ref(1);
let pageSize = ref(7);
const formData = ref();
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
  weekEnd:'',
  classTime:{
    weekDay:'',
    section:'',
  },
  capacity:'',
  studentClass:'',
  studentSection:'',
  location:'',
  year:'',
  description:'',
  semester:'',
  credit:'',
  courseId: null,
});
const search = ref('')
const filterTableData = computed(() =>
    tableData.filter(
        (data) =>
            !search.value ||
            data.name.toLowerCase().includes(search.value.toLowerCase())
    )
)
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
const loadPracticeTable = async () => {   //查找所有的数据,这个接口是不是有点问题,学生端还用传userNumber吗
  /* formData.value.valid */
  formData.value.validate((valid) => {
    if (valid) {
      service.post('/api/course/findAllCourse', { token: localStorage.getItem("token") }).then(res => {
        if (res.data.success) {
          const data = res.data;
          let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
          tableData = arr
          localStorage.setItem('token', data.token)
        } else {
          messageWarning(res.data.message)
        }
      })
          .catch(function (error) {
            console.log(error)
          })
    } else {
      messageError("请完善全部信息")
    }
  })
}
loadPracticeTable() //进入默认执行

const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
}

// const handleCheck = (row) => {   //查看单个的数据 //一条一条赋值吧，一起赋值出bug了
//   centerDialogVisible.value = true;
//   editForm.practiceDescription = row.practiceDescription;
//   editForm.name = row.name;
//   editForm.practiceId = row.practiceId;
//   editForm.studentName = row.studentName;
//   console.log(editForm)
//   typeOperation.value = 'check'; //查看完就完事儿
// }

const handleEdit = (row) => {  //改
  centerDialogVisible.value = true;
  editForm.name=row.name;
  editForm.weekStart=row.weekStart;
  editForm.weekEnd = row.weekEnd;
  editForm.classTime.weekDay=row.classTime.weekDay;
  editForm.classTime.section=row.classTime.section;
  editForm.capacity=row.capacity;
  editForm.studentClass=row.studentClass;
  editForm.studentSection=row.studentSection;
  editForm.location=row.location;
  editForm.year=row.year;
  editForm.description=row.description;
  editForm.semester=row.semester;
  editForm.credit=row.credit;
  // editForm = Object.assign({}, row);//先弹对话框，然后提交，提交之后再传参数吧
  console.log(editForm)
  typeOperation.value = 'edit';
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
        service.post('/api/course/delete', { token: localStorage.getItem("token"), id: row.id }).then(res => {
          if (res.data.success) {
            messageSuccess('删除成功!')
            loadPracticeTable() //重新加载现在表单中的数据
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

const sumbitEditRow = () => {
  editForm.token = localStorage.getItem("token")
  // if (typeOperation.value === 'check') {
  //   /* handleCheck() */
  //
  // } else
    if (typeOperation.value === 'edit') {
    /* handleEdit() */
    service.post('/api/course/save',
        {
          token: editForm.token,
          name: editForm.name,
          weekStart: editForm.weekStart,
          weekEnd: editForm.weekEnd,
          weekDay: editForm.classTime.weekDay,
          section: editForm.classTime.section,
          capacity:editForm.capacity,
          studentClass: editForm.studentClass,
          studentSection: editForm.studentSection,
          location: editForm.location,
          year: editForm.year,
          description: editForm.description,
          semester: editForm.semester,
          credit: editForm.credit,
        })
        .then(res => {  //直接把这一行的数据给出去可以吗
          if (res.data.success) {
            messageSuccess("编辑成功！")
            typeOperation.value = '';
            loadPracticeTable()
            localStorage.setItem("token", res.data.token)
          } else {
            messageError("编辑失败!")
            console.log(res.data.message)
          }
        })
    isShow.value = false;
  } else if (typeOperation.value === 'add') {
    service.post('/api/course/save',
        {
          token: editForm.token,
          name: editForm.name,
          weekStart: editForm.weekStart,
          weekEnd: editForm.weekEnd,
          weekDay: editForm.classTime.weekDay,
          section: editForm.classTime.section,
          capacity:editForm.capacity,
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
                messageSuccess("添加成功！")
                typeOperation.value = '';
                loadPracticeTable()
                localStorage.setItem("token", res.data.token)
              } else {
                messageError("添加失败！")
                console.log(res.data.message)
              }
            }
        )
    isShow.value = false;
  } else {
    messageError('出现错误！')
  }
  editForm.name = '',
      editForm.weekStart='',
      editForm.weekEnd='',
      editForm.classTime.weekDay='',
      editForm.classTime.section='',
      editForm.capacity='',
      editForm.studentClass='',
      editForm.studentSection='',
      editForm.location='',
      editForm.year='',
      editForm.description='',
      editForm.semester='',
      editForm.credit='',
  centerDialogVisible.value = false;
  typeOperation.value = '';
};

const closeDialog = () => {
  centerDialogVisible.value = false;
  if (typeOperation.value === 'edit') {
    messageInfo("取消编辑")
  } else if (typeOperation.value === 'add') {
    messageInfo("取消添加")
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
    .pracitceTable{
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
