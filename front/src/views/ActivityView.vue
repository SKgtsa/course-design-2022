<template>
  <!-- 界面显示：姓名，标题，按钮
    按钮：编辑，查找，删除
    顶部需不需要提示一下目前所处的版块
    然后找个位置设置个添加按钮
    这个查找是搞一个输入框？ -->
  <!--这个不能添加姓名是别人的课外活动吧，这个不确定有没有问题-->
  <div class="pageContent">
    <!-- :row-key="record=>record.id" -->
    <div class="title">
      title
      <el-button type="success" @click="add">添加</el-button>
    </div>
    <el-table :data="tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)" style="width: 80%" border
      stripe size="large">
      <!-- 显示斑马纹和边框 -->
      <el-table-column label="序号" type="index" width="80" />
      <!-- <el-table-column label="姓名" prop="studentName" width="120"  show-overflow-tooltip  /> -->
      <el-table-column label="标题" prop="activityName" width="350" show-overflow-tooltip />

      <el-table-column>
        <template #header>
          <!-- 默认表头 -->
          <el-input class="search" v-model="search" size="large" placeholder="搜索你的课外活动" :suffix-icon="Search" />
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
      <!-- <el-form-item label="日期" prop="activityDate">
                        <el-date-picker
                type="daterange"
                range-separator="To"
                start-placeholder="Start date"
                end-placeholder="End date"
                v-model = "editForm.activityDate" 
              />
                    </el-form-item> -->
      <el-form-item label="标题" prop="activityName">
        <span v-if="typeOperation === 'check'">{{ editForm.activityName }}</span> <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
        <el-input v-if="typeOperation === 'edit'" v-model="editForm.activityName">{{ editForm.activityName }}</el-input>
        <el-input v-if="typeOperation === 'add'" v-model="editForm.activityName"></el-input>
      </el-form-item>
      <el-form-item label="内容" prop="activityDescription">
        <span v-if="typeOperation === 'check'">{{ editForm.activityDescription }}</span>
        <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
        <el-input v-if="typeOperation === 'edit'" type="textarea" rows="15" v-model="editForm.activityDescription">
          {{ editForm.activityDescription }}</el-input>
        <el-input v-if="typeOperation === 'add'" type="textarea" rows="15" v-model="editForm.activityDescription">
          {{ editForm.activityDescription }}</el-input>
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
  activityName: string,
  activityDescription: string,
  activityId: string,
  studentName: string
}
/* let findactivity = reactive([{
  activityDate:'',
  activityName:'',
  activityDiscription:'',
  studentName:'',
  activityIdentify:'',
}]) */
let tableData = reactive([
  {
    //有个id在tableData中的每个对象里
    //不对用户呈现，仅储存

    activityName: '外出旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '继续旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '接着旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '还去旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '一直旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '中途旅游',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  }, {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
  {

    activityName: '宣传红色基因,致力乡村振兴',
    activityDescription: '',
    activityId: null,
    studentName: '信步'
  },
])
/* let tableData =reactive([]); */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false);
let isShow = ref(false);
let currentPage = ref(1);
let pageSize = ref(8);
const formData = ref();
const rulesEditForm = reactive({   /* 定义校验规则 */
  activityName: [{ required: true, message: '请输入课外活动的标题！', trigger: 'blur' },
  { max: 30, message: '长度不得超过30位!', trigger: 'blur' }
  ],
  activityDescription: [{ required: true, message: '请输入课外活动的内容！', trigger: 'blur' }]
})
let editForm = reactive({
  token: '',
  activityName: '',
  activityDescription: '',
  activityId: null,
  studentName: ''
});
const search = ref('')

const activityTableData = computed(() =>
  tableData.filter(
    (data) =>
      !search.value ||
      data.activityName.toLowerCase().includes(search.value.toLowerCase())
  )
)


const loadactivityTable = () => {   //查找所有的数据,这个接口是不是有点问题,学生端还用传userNumber吗
  /* formData.value.valid */
  formData.value.validate((valid) => {
    if (valid) {
      service.post('/api/activity/find', { token: localStorage.getItem("token") }).then(res => {
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
loadactivityTable() //进入默认执行

const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
}

const handleCheck = (row) => {   //查看单个的数据 //一条一条赋值吧，一起赋值出bug了
  centerDialogVisible.value = true;
  editForm.activityDescription = row.activityDescription;
  editForm.activityName = row.activityName;
  editForm.activityId = row.activityId;
  editForm.studentName = row.studentName;
  console.log(editForm)
  typeOperation.value = 'check'; //查看完就完事儿
}

const handleEdit = (row) => {  //改
  centerDialogVisible.value = true;
  editForm.activityDescription = row.activityDescription;
  editForm.activityName = row.activityName;
  editForm.activityId = row.activityId;
  editForm.studentName = row.studentName;
  // editForm = Object.assign({}, row);//先弹对话框，然后提交，提交之后再传参数吧
  console.log(editForm)
  typeOperation.value = 'edit';
}

const handleDelete = (row) => {  //删  //异步可能有问题
  ElMessageBox.confirm(
    '确认删除该条课外活动吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      service.post('/api/activity/delete', { token: localStorage.getItem("token"), id: row.id }).then(res => {
        if (res.data.success) {
          messageSuccess('删除成功!')
          loadactivityTable() //重新加载现在表单中的数据
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
    service.post('/api/activity/save',
      { token: editForm.token, activityName: editForm.activityName, activityDescription: editForm.activityDescription, id: editForm.activityId })
      .then(res => {  //直接把这一行的数据给出去可以吗
        if (res.data.success) {
          messageSuccess("编辑成功！")
          typeOperation.value = '';
          loadactivityTable()
          localStorage.setItem("token", res.data.token)
        } else {
          messageError("编辑失败!")
          console.log(res.data.message)
        }
      })
    isShow.value = false;
  } else if (typeOperation.value === 'add') {
    service.post('/api/activity/save',
      { token: editForm.token, activityName: editForm.activityName, activityDescription: editForm.activityDescription })
      .then(res => {
        if (res.data.success) {
          messageSuccess("添加成功！")
          typeOperation.value = '';
          loadactivityTable()
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
  editForm.activityName = '',
    editForm.activityDescription = '',
    editForm.activityId = null,
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
  height: 60px;
}

.pageContent {
  padding-left: 100px;
  height: 420px;
}

.search {
  /* margin-right: 30px!important; */
  width: 300px !important;
}

.button {
  width: 48px;
  height: 30px;
}

.activityDialog {
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
</style>