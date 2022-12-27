<template>
  <div class="content">
    <div class="pageContent">
      <div class="title">
        社会实践
        <el-button class="addButton" @click="add">添加</el-button>
      </div>
      <!-- 表格数据显示 -->
      <el-table :data="tableData.arr" stripe size="large" class="practiceTable"
        :header-cell-style="{ 'height': '30px', 'font-size': '18px', 'text-align': 'center', 'font-weight': '800' }"
        :cell-style="{ 'height': '14px', 'font-size': '14px', 'text-align': 'center', 'font-weight': '450' }">
        <!-- <el-table-column label="日期" prop="date" width="240" show-overflow-tooltip /> -->
        <el-table-column label="标题" prop="name" width="400" show-overflow-tooltip />
        <el-table-column label="描述" prop="description" width="300" show-overflow-tooltip></el-table-column>
        <el-table-column width="300" label="操作">
          <template #default="scope">
            <el-button size="medium" @click="handleCheck(scope.row)" class="button" type="primary">查看</el-button>
            <el-button size="medium" @click="handleEdit(scope.row)" class="button">编辑</el-button>
            <el-button size="medium" type="danger" class="button" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="prev, pager, next,jumper, ->" :page-count="pageCount"
          @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
          style="text-align: center">
        </el-pagination>
      </div>
    </div>
    <!--  增改弹出框 -->
    <el-dialog v-model="centerDialogVisible" width="45%" draggable="true">
      <el-form :model="editForm" class="areaTextInput" ref="formData" :rules="rulesEditForm">
        <el-form-item label="日期" prop="date">
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.date" maxlength="20">{{ editForm.date }}
          </el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.date" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="标题" prop="practiceName" >
          <el-input v-if="typeOperation === 'edit'" v-model="editForm.practiceName" maxlength="30">{{ editForm.practiceName }}
          </el-input>
          <el-input v-if="typeOperation === 'add'" v-model="editForm.practiceName" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="practiceDescription">
          <el-input v-if="typeOperation === 'edit'" type="textarea" maxlength="5000" rows="15" v-model="editForm.practiceDescription">
            {{ editForm.practiceDescription }}</el-input>
          <el-input v-if="typeOperation === 'add'" type="textarea"  maxlength="5000" rows="15" v-model="editForm.practiceDescription">
          </el-input>
        </el-form-item>
        <el-form-item label="成果" prop="result">
          <el-input v-if="typeOperation === 'edit'" type="textarea" rows="6" maxlength="30" v-model="editForm.result">{{
              editForm.result
          }}</el-input>
          <el-input v-if="typeOperation === 'add'" type="textarea" rows="6" maxlength="30" v-model="editForm.result"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialogButtonPage">
        <el-button @click="closeDialog" class="dialogButton">取消</el-button>
        <el-button type="primary" @click="sumbitEditRow" class="dialogButton">确定</el-button> <!-- 在这个方法里面来判断是啥？ -->
      </div>
    </el-dialog>
    <!-- 查看弹出框 -->
    <el-dialog v-model="centerDialogVisibleCheck" width="45%" draggable="true">
      <el-form :model="editForm" class="areaTextInput">
        <el-form-item label="日期" prop="date">
          <span v-if="typeOperation === 'check'">{{ editForm.date }}</span>
        </el-form-item>
        <el-form-item label="标题" prop="practiceName">
          <span v-if="typeOperation === 'check'">{{ editForm.practiceName }}</span>
        </el-form-item>
        <el-form-item label="内容" prop="practiceDescription">
          <span v-if="typeOperation === 'check'">{{ editForm.practiceDescription }}</span>
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
import { showLoading, hideLoading } from '../../utils/loading'
import service from '../../request/index'
import { messageSuccess, messageWarning, messageError, messageInfo } from '../../utils/message'
import { ElMessage, ElMessageBox } from 'element-plus'

let tableData = reactive({
  arr: [],
});   //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation = ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false); //控制改增弹出框
let currentPage = ref(1);
let pageSize = ref(7);
let formData = ref();//改增校验绑定的空form
let pageCount = ref();
let centerDialogVisibleCheck = ref(false);//查的弹出框

/* 定义校验规则 */
const rulesEditForm = reactive({
  practiceName: [{ required: true, message: '请输入社会实践的标题！', trigger: 'blur' },
  { max: 30, message: '长度不得超过30位!', trigger: 'blur' }
  ],
  practiceDescription: [{ required: true, message: '请输入社会实践的内容！', trigger: 'blur' }],
  date: [{ required: true, message: '请输入日期', trigger: 'blur' },
  { max: 20, message: '请输入正确的日期!不要超过20位!', trigger: 'blur' }],
  result: [{ required: true, message: '请输入您的成果', trigger: 'blur' },
  { max: 30, message: '简述即可,不要超过30字', trigger: 'blur' }
  ],
})

//改查绑定的form数据
let editForm = reactive({
  practiceName: '',
  practiceDescription: '',
  date: '',
  result: '',
  id: '',
});

//查找所有的数据
const loadpracticeTable = async () => {
  showLoading();
  await service.post('/api/practice/find', { token: localStorage.getItem("token"), pageNum: currentPage.value, pageSize: pageSize.value }).then(res => {
    console.log(currentPage.value, pageSize.value)
    if (res.data.success) {
      hideLoading();
      console.log('初始化返回的res')
      console.log(res);
      let data = res.data;
      localStorage.setItem('token', data.token)
      console.log(localStorage.getItem('token'))
      let array = data.content
      pageCount.value = data.totalPage;
      tableData.arr = array
      console.log(tableData)
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
loadpracticeTable() //进入默认执行

const add = () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
  editForm.practiceName = '';
  editForm.practiceDescription = '';
  editForm.id = '';
  editForm.date = '';
  editForm.result = '';
}

const handleCheck = (row) => {   //查看单个的数据 一条一条赋值，一起赋值出bug了
  centerDialogVisibleCheck.value = true;
  editForm.practiceDescription = row.practiceDescription;
  editForm.practiceName = row.practiceName;
  editForm.date = row.date;
  editForm.result = row.result;
  editForm.id = row.id;
  typeOperation.value = 'check';
}

const handleEdit = (row) => {  //改  两边属性名字不匹配
  console.log(row)
  centerDialogVisible.value = true;
  editForm.practiceDescription = row.practiceDescription;
  editForm.practiceName = row.practiceName;
  editForm.date = row.date;
  editForm.result = row.result;
  editForm.id = row.id;
  typeOperation.value = 'edit';
  console.log(editForm)
}

const handleDelete = async (row) => {  //删  //异步不确定是否有问题
  await ElMessageBox.confirm(
    '确认删除该条社会实践吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      showLoading();
      service.post('/api/practice/delete', { token: localStorage.getItem("token"), practiceId: row.id }).then(res => {
        if (res.data.success) {
          hideLoading()
          messageSuccess('删除成功!')
          localStorage.setItem("token", res.data.token)
          loadpracticeTable() //重新加载现在表单中的数据
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

const sumbitEditRow = async () => {
  await formData.value.validate(((valid) => {
    if (valid) {
      if (typeOperation.value === 'edit') {
        /* handleEdit() */
        showLoading();
        service.post('/api/practice/save',
          {
            token: localStorage.getItem("token"), practiceName: editForm.practiceName, practiceDescription: editForm.practiceDescription,
            date: editForm.date, result: editForm.result, id: editForm.id
          })
          .then(res => {  //直接把这一行的数据给出去可以吗
            if (res.data.success) {
              hideLoading();
              localStorage.setItem("token", res.data.token)
              messageSuccess("编辑成功！")
              typeOperation.value = '';
              loadpracticeTable()
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
        console.log('未执行添加前的描述，名字，日期，成果')
        console.log(editForm.practiceDescription, editForm.practiceName, editForm.date, editForm.result)
        service.post('/api/practice/save',
          {
            token: localStorage.getItem("token"), practiceName: editForm.practiceName, practiceDescription: editForm.practiceDescription,
            date: editForm.date, result: editForm.result
          })
          .then(res => {
            if (res.data.success) {
              ('添加成功后editForm数据')
              console.log(res)
              hideLoading()
              localStorage.setItem("token", res.data.token)
              console.log(localStorage.getItem('token'))
              messageSuccess("添加成功！")
              typeOperation.value = '';
              console.log('我执行了')
              loadpracticeTable()

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

  editForm.practiceName = '',
    editForm.practiceDescription = '',
    editForm.id = '',
    editForm.date = '',
    editForm.result = '',
    centerDialogVisible.value = false;
  centerDialogVisibleCheck.value = false;
  typeOperation.value = '';
  formData.value = null;
};

const closeDialog = () => {
  if (typeOperation.value === 'check') {
    centerDialogVisibleCheck.value = false;
    typeOperation.value = '';
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
const handleCurrentChange = (current) => {
  currentPage.value = current;
  loadpracticeTable() //再执行一次索要数据方法
  console.log(currentPage)
}

</script>
<style lang="scss" scoped>
.title {
  margin-top: 15px;
  height: 60px;
  font-size: 4.5vh;
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

    .practiceTable {
      border: 2px solid;
      width: 63vw;

      .button {
        width: 48px;
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
  padding-top: 30px;
  padding-left: 230px;
}
</style>
