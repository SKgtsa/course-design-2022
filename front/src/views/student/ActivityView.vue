<template>
  <div class="content">
    <div class="pageContent">
      <div class="title">
        课外活动
        <el-button class="addButton" @click="add">添加</el-button>
      </div>
      <el-table :data="tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)" stripe size="large"
        class="activityTable"
        :header-cell-style="{ 'height': '30px', 'font-size': '18px', 'text-align': 'center', 'font-weight': '800' }"
        :cell-style="{ 'height': '14px', 'font-size': '14px', 'text-align': 'center', 'font-weight': '450' }">
        <!-- 显示斑马纹和边框 -->
        <el-table-column label="日期" prop="date" width="240" show-overflow-tooltip />
        <el-table-column label="标题" prop="activityName" width="400" show-overflow-tooltip />
        <el-table-column width="300" label="操作">
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
    <el-dialog v-model="centerDialogVisible" width="45%" draggable="true">
      <el-form :model="editForm" class="areaTextInput" ref="formData" :rules="rulesEditForm">
        <el-form-item label="日期" prop="date">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.date">{{ editForm.date }}
          </el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.date"></el-input>
        </el-form-item>
        <el-form-item label="标题" prop="activityName">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.activityName">{{ editForm.activityName }}
          </el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.activityName"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="activityDescription">
          <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
          <el-input v-if="typeOperation === 'edit'" type="textarea" rows="15" v-model="editForm.activityDescription">
            {{ editForm.activityDescription }}</el-input>
          <el-input v-if="typeOperation === 'add'" type="textarea" rows="15" v-model="editForm.activityDescription">
          </el-input>
        </el-form-item>
        <el-form-item label="成果" prop="result">
          <el-input v-if="typeOperation === 'edit'" type="textarea" rows="6" v-model="editForm.result">{{
              editForm.result
          }}</el-input>
          <el-input v-if="typeOperation === 'add'" type="textarea" rows="6" v-model="editForm.result"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialogButtonPage">
        <el-button @click="closeDialog" class="dialogButton">取消</el-button>
        <el-button type="primary" @click="sumbitEditRow" class="dialogButton">确定</el-button> <!-- 在这个方法里面来判断是啥？ -->
      </div>
    </el-dialog>

    <el-dialog v-model="centerDialogVisibleCheck" width="45%" draggable="true">
      <el-form :model="editForm" class="areaTextInput">
        <el-form-item label="日期" prop="date">
          <span v-if="typeOperation === 'check'">{{ editForm.date }}</span>
        </el-form-item>
        <el-form-item label="标题" prop="activityName">
          <span v-if="typeOperation === 'check'">{{ editForm.activityName }}</span>
        </el-form-item>
        <el-form-item label="内容" prop="activityDescription">
          <span v-if="typeOperation === 'check'">{{ editForm.activityDescription }}</span>
        </el-form-item>
        <el-form-item label="成果" prop="result">
          <span v-if="typeOperation === 'check'">{{ editForm.result }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref, reactive } from 'vue'
import { Search, User } from '@element-plus/icons-vue'
import service from '../../request/index'
import { messageSuccess, messageWarning, messageError, messageInfo } from '../../utils/message'
import { ElMessage, ElMessageBox } from 'element-plus'
import { editorProps } from '@tinymce/tinymce-vue/lib/cjs/main/ts/components/EditorPropTypes'
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
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
  {
    activityName: '课外活动名字',
    activityDescription: '课外活动描述',
    date: '2021年5月6日',
    result: '被表扬了',
  },
]) 
 /* let tableData =reactive([]);  */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false);
let isShow = ref(false);
let currentPage = ref(1);
let pageSize = ref(7);
let formData = ref();
let centerDialogVisibleCheck = ref(false);
const rulesEditForm = reactive({   /* 定义校验规则 */
  activityName: [{ required: true, message: '请输入课外活动的标题！', trigger: 'blur' },
  { max: 30, message: '长度不得超过30位!', trigger: 'blur' }
  ],
  activityDescription: [{ required: true, message: '请输入活动的内容！', trigger: 'blur' }],
  date: [{ required: true, message: '请输入日期', trigger: 'blur' },
  { max: 20, message: '请输入正确的日期', trigger: 'blur' }],
  result: [{ required: true, message: '请输入您的成果', trigger: 'blur' },
  { max: 30, message: '不要太长，简要概述即可', trigger: 'blur' }
  ],
})
let editForm = reactive({
  activityName: '',
  activityDescription: '',
  date: '',
  result: '',
  id: '',
});


const loadactivityTable = async () => {   //查找所有的数据
  /* formData.value.valid */
  await service.post('/api/activity/find', { token: localStorage.getItem("token"), pageNum: currentPage, pageSize: pageSize }).then(res => {
    if (res.data.success) {
      let data = res.data;
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
}
loadactivityTable() //进入默认执行

const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
  editForm.activityName = '';
  editForm.activityDescription = '';
  editForm.id = '';
  editForm.date = '';
  editForm.result = '';
}

const handleCheck = (row) => {   //查看单个的数据 //一条一条赋值吧，一起赋值出bug了
  centerDialogVisibleCheck.value = true;
  editForm.activityDescription = row.activityDescription;
  editForm.activityName = row.activityName;
  editForm.date = row.date;
  editForm.result = row.result;
  editForm.id = row.id;
  typeOperation.value = 'check'; //查看完就完事儿
}

const handleEdit = (row) => {  //改
  centerDialogVisible.value = true;
  editForm.activityDescription = row.activityDescription;
  editForm.activityName = row.activityName;
  editForm.date = row.date;
  editForm.result = row.result;
  editForm.id = row.id;
  typeOperation.value = 'edit';
}

const handleDelete = (row) => {  //删  //没有异步，不确定是否有问题
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
      service.post('/api/activity/delete', { token: localStorage.getItem("token"), activityId: row.id }).then(res => {
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

const sumbitEditRow = async () => {
  await formData.value.validate(((valid) => {
    if (valid) {
      if (typeOperation.value === 'edit') {
        /* handleEdit() */
        service.post('/api/activity/save',
          {
            token: localStorage.getItem("token"), activityName: editForm.activityName, activityDescription: editForm.activityDescription,
            date: editForm.date, result: editForm.result, id: editForm.id
          })
          .then(res => {  //直接把这一行的数据给出去可以吗
            if (res.data.success) {
              messageSuccess("编辑成功！")
              typeOperation.value = '';
              loadactivityTable()
              localStorage.setItem("token", res.data.token)
            } else {
              messageError(res.data.message)
            }
          })
        isShow.value = false;
      } else if (typeOperation.value === 'add') {
        service.post('/api/activity/save',
          {
            token: localStorage.getItem("token"), activityName: editForm.activityName, activityDescription: editForm.activityDescription,
            date: editForm.date, result: editForm.result
          })
          .then(res => {
            if (res.data.success) {
              messageSuccess("添加成功！")
              typeOperation.value = '';
              loadactivityTable()
              localStorage.setItem("token", res.data.token)
            } else {
              messageError(res.data.message)
            }
          }
          )
        isShow.value = false;
      } else {
        messageError('出现错误！')
      }
    } else {
      messageWarning("请填写完整!")
    }
  }))

  editForm.activityName = '',
    editForm.activityDescription = '',
    editForm.id = '',
    editForm.date = '',
    editForm.result = '',
    centerDialogVisible.value = false;
  centerDialogVisibleCheck.value = false;
  typeOperation.value = '';
  formData.value = '';
};

const closeDialog = () => {
  if (typeOperation.value === 'check') {
    centerDialogVisibleCheck.value = false;
  } else if (typeOperation.value === 'edit') {
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

</script>
<style lang="scss" scoped>
.title {
  margin-top: 15px;
  height: 60px;
  font-family: LiSu;
  font-size: 6vh;
  font-weight: 500;
  line-height: 1vh;
  color: #0273f1;
}


.content {
  width: 100%;
  height: 100%;
  background-image: url("../../assets/images/activity.jpg");
  background-size: cover;
  background-attachment: fixed;
  background-position: center center;
  background-repeat: repeat;

  .pageContent {
    width: 70vw;
    height: 70vh;
    border-radius: 3vw;
    padding-left: 2vw;
    padding-top: 3vh;
    padding-right: 2vw;

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

    ::v-deep .el-table th {
      //更改table 头部背景
      background-color: rgba(19, 69, 193, 0.867);
      color: #f2dc19;
      /*  color: '#fff';
      background-color: '#0a3370'; */
      font-weight: '700';
    }

    ::v-deep .el-table {
      //表格边框
      border: solid 1px #922eef;
      // box-sizing: border-box;
    }

    ::v-deep .el-table tr {
      //内部的单元行
      background-color: rgb(174, 233, 246);
    }


    ::v-deep .el-table tbody tr:hover>td {
      background: #40b3dc !important;
    }

    .activityTable {
      border: 2px solid;

      .button {
        width: 48px;
        height: 30px;
      }
    }
  }
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
