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
        社会实践
        <el-button type="success" @click="add">添加</el-button>
      </div>
      <el-table :data="tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)" style="width: 80%" border
                stripe size="large">
        <!-- 显示斑马纹和边框 -->
        <el-table-column label="序号" type="index" width="80" />
        <!-- <el-table-column label="姓名" prop="studentName" width="120"  show-overflow-tooltip  /> -->
        <el-table-column label="标题" prop="practiceName" width="350" show-overflow-tooltip />

        <el-table-column>
          <template #header>
            <!-- 默认表头 -->
            <el-input class="search" v-model="search" size="large" placeholder="搜索你的社会实践" :suffix-icon="Search" />
          </template>
          <template #default="scope">
            <!-- 默认行和列 -->
            <el-button size="medium" @click="handleCheck(scope.row)" class="button" type="primary">查看</el-button>
            <el-button size="medium" @click="handleEdit(scope.row)" class="button">编辑</el-button>
            <el-button size="medium" type="danger" class="button" @click="handleDelete(scope.row)">删除</el-button>
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
        <!-- <el-form-item label="日期" prop="practiceDate">
            <el-date-picker
              type="daterange"
              range-separator="To"
              start-placeholder="Start date"
              end-placeholder="End date"
              v-model = "editForm.practiceDate"
            />
          </el-form-item> -->
        <el-form-item label="标题" prop="practiceName">
          <span v-if="typeOperation === 'check'">{{ editForm.practiceName }}</span> <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.practiceName">{{ editForm.practiceName }}</el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.practiceName"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="practiceDescription">
          <span v-if="typeOperation === 'check'">{{ editForm.practiceDescription }}</span> <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
          <el-input v-if="typeOperation === 'edit'" type="textarea" rows="15" v-model="editForm.practiceDescription">
            {{ editForm.practiceDescription }}</el-input>
          <el-input v-if="typeOperation === 'add'" type="textarea" rows="15" v-model="editForm.practiceDescription">
            {{ editForm.practiceDescription }}</el-input>
        </el-form-item>
        <!-- <el-form-item label="成员" prop="character">
            <span v-if="typeOperation==='check'">{{editForm.character}}</span> 这个editForm初始值，还得赋值为那一行的数据吧
            <el-input v-if="typeOperation==='edit'" v-model="editForm.character">{{editForm.character}}</el-input>
            <el-input v-if="typeOperation==='add'" v-model="editForm.character">{{editForm.character}}</el-input>
          </el-form-item> -->
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
import service from '../request/index'
import { messageSuccess, messageWarning, messageError, messageInfo } from '../utils/message'
import { ElMessage, ElMessageBox } from 'element-plus'
/* import { time } from 'console'; */
/* import { title } from 'process'; */
interface User {
  practiceName: string,
  practiceDescription: string,
  practiceId: string,
  studentName: string
}
/* let findPractice = reactive([{
  practiceDate:'',
  practiceName:'',
  practiceDiscription:'',
  studentName:'',
  practiceIdentify:'',
}]) */
let tableData = reactive([
  {
    //有个id在tableData中的每个对象里
    //不对用户呈现，仅储存

    practiceName: '回访母校-情系山大',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '关于临工重击的企业调研',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '义务支教',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  }, {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription: '',
    practiceId: null,
    studentName: '信步'
  },
])
/* let tableData =reactive([]); */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false);
let isShow = ref(false);
let currentPage = ref(1);
let pageSize = ref(7);
const formData = ref();
const rulesEditForm = reactive({   /* 定义校验规则 */
  practiceName: [{ required: true, message: '请输入社会实践的标题！', trigger: 'blur' },
  { max: 30, message: '长度不得超过30位!', trigger: 'blur' }
  ],
  practiceDescription: [{ required: true, message: '请输入实践的内容！', trigger: 'blur' }]
})
let editForm = reactive({
  token: '',
  practiceName: '',
  practiceDescription: '',
  practiceId: null,
  studentName: ''
});
const search = ref('')

const PracticeTableData = computed(() =>
  tableData.filter(
    (data) =>
      !search.value ||
      data.practiceName.toLowerCase().includes(search.value.toLowerCase())
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
      service.post('/api/practice/find', { token: localStorage.getItem("token") }).then(res => {
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

const handleCheck = (row) => {   //查看单个的数据 //一条一条赋值吧，一起赋值出bug了
  centerDialogVisible.value = true;
  editForm.practiceDescription = row.practiceDescription;
  editForm.practiceName = row.practiceName;
  editForm.practiceId = row.practiceId;
  editForm.studentName = row.studentName;
  console.log(editForm)
  typeOperation.value = 'check'; //查看完就完事儿
}

const handleEdit = (row) => {  //改
  centerDialogVisible.value = true;
  editForm.practiceDescription = row.practiceDescription;
  editForm.practiceName = row.practiceName;
  editForm.practiceId = row.practiceId;
  editForm.studentName = row.studentName;
  // editForm = Object.assign({}, row);//先弹对话框，然后提交，提交之后再传参数吧
  console.log(editForm)
  typeOperation.value = 'edit';
}

const handleDelete = (row) => {  //删  //异步可能有问题
  ElMessageBox.confirm(
    '确认删除该条社会实践吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      service.post('/api/practice/delete', { token: localStorage.getItem("token"), id: row.id }).then(res => {
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
  if (typeOperation.value === 'check') {
    /* handleCheck() */

  } else if (typeOperation.value === 'edit') {
    /* handleEdit() */
    service.post('/api/practice/save',
      { token: editForm.token, practiceName: editForm.practiceName, practiceDescription: editForm.practiceDescription, id: editForm.practiceId })
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
    service.post('/api/practice/save',
      { token: editForm.token, practiceName: editForm.practiceName, practiceDescription: editForm.practiceDescription })
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
  editForm.practiceName = '',
    editForm.practiceDescription = '',
    editForm.practiceId = null,
    editForm.studentName = ''
  centerDialogVisible.value = false;
  typeOperation.value = '';
};

const closeDialog = () => {
  centerDialogVisible.value = false;
  if (typeOperation.value === 'edit') {
    messageInfo("编辑取消")
  } else if (typeOperation.value === 'add') {
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
<style scoped>
.title {
  margin-top: 30px;
  height: 60px;
  font-family: 华文楷体;
  font-size: 4vh;
  font-weight: bold;
}

.pageContent {
  padding-left: 200px;
  height: 400px;
}

.search {
  /* margin-right: 30px!important; */
  width: 300px !important;
}

.button {
  width: 48px;
  height: 30px;
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
  padding-top: 30px;
  padding-left: 230px;
}
.content{
  padding-top: 80px;
  padding-left: 200px;
  background-color: rgb(255, 255, 255);
  height: 650px;
}
</style>