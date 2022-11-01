<template>
<!-- 界面显示：日期，标题，按钮
按钮：编辑，查找，删除
顶部需不需要提示一下目前所处的版块
然后找个位置设置个添加按钮
这个查找是搞一个输入框？ -->
<div class="title">
  title
<el-button type="success" @click="add">添加</el-button>
</div>
<div class="pageContent">    <!-- :row-key="record=>record.id" -->
  <el-table 
  :data="PracticeTableData" 
  style="width: 100%"
 
  :default-sort="{prop: 'date', order: 'descending'}"
  border 
  stripe
  size="large"  
  > <!-- 显示斑马纹和边框 -->
    <el-table-column label="序号" type="index" width="100"/>
    <el-table-column label="描述" prop="practiceDescription" sortable show-overflow-tooltip  />
    <el-table-column label="标题" prop="practiceName" show-overflow-tooltip />
    <el-table-column>
      <template #header> <!-- 默认表头 -->
        <el-input 
        class="search" 
        v-model="search" 
        size="large" 
        placeholder="搜索你的社会实践"
        :suffix-icon="Search"
         />
      </template>
      <template #default="scope"> <!-- 默认行和列 -->
        <el-button size="small" @click="handleCheck(scope.row)" type="primary">查看</el-button>
        <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
        <el-button
          size="small"
          type="danger"
          @click="handleDelete(scope.row)"
          >删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
  <!-- 用一个变量来判断是否弹出这个对话框 -->
  <!-- 再用一个变量判断是查看还是编辑，添加感觉和编辑差不多，编辑和添加用input框住，
    编辑要有初始的数值，查看用span框住，也要有初始值，添加用input框住，没有初始值-->
   <el-dialog v-model="centerDialogVisible">  
			<el-form :model="editForm">
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
          <span v-if="typeOperation==='check'">{{editForm.practiceName}}</span> <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
					<el-input v-if="typeOperation==='edit'" v-model="editForm.practiceName">{{editForm.practiceName}}</el-input>
          <el-input v-if="typeOperation==='add'" v-model="editForm.practiceName"></el-input>
				</el-form-item>
				<el-form-item label="内容" prop="practiceDescription">
          <span v-if="typeOperation==='check'">{{editForm.practiceDescription}}</span> <!-- 这个editForm初始值，还得赋值为那一行的数据吧 -->
					<el-input v-if="typeOperation==='edit'" v-model="editForm.practiceDescription">{{editForm.practiceDescription}}</el-input>
          <el-input v-if="typeOperation==='add'" v-model="editForm.practiceDescription">{{editForm.practiceDescription}}</el-input>
				</el-form-item>
        <!-- <el-form-item label="成员" prop="character">
          <span v-if="typeOperation==='check'">{{editForm.character}}</span> 这个editForm初始值，还得赋值为那一行的数据吧
					<el-input v-if="typeOperation==='edit'" v-model="editForm.character">{{editForm.character}}</el-input>
          <el-input v-if="typeOperation==='add'" v-model="editForm.character">{{editForm.character}}</el-input>
				</el-form-item> -->
			</el-form>
			<div>
 				<el-button @click="closeDialog">取消</el-button>
				<el-button type="primary" @click="sumbitEditRow">确定</el-button>  <!-- 在这个方法里面来判断是啥？ -->
 			</div>
		</el-dialog>
</template>
<script lang="ts" setup>
import { computed, ref,reactive} from 'vue'
import{Search, User} from '@element-plus/icons-vue'
import service from '../request/index'
import {msgSuccess,msgWarning,msgError} from '../utils/message'
/* import { time } from 'console'; */
/* import { title } from 'process'; */
interface User {
  practiceName:string,
  practiceDescription:string,
  practiceId: string,
  studentName:string
}
/* let findPractice = reactive([{
  practiceDate:'',
  practiceName:'',
  practiceDiscription:'',
  studentName:'',
  practiceIdentify:'',
}]) */
let tableData= reactive([
  {
    //有个id在tableData中的每个对象里
    //不对用户呈现，仅储存

    practiceName: '回访母校-情系山大',
    practiceDescription:'',
    practiceId: null,
    studentName:''
  },
  {

    practiceName: '宣传红色基因,致力乡村振兴',
    practiceDescription:'',
    practiceId: null,
    studentName:''
  },
  {

    practiceName: '关于临工重击的企业调研',
    practiceDescription:'',
    practiceId: null,
    studentName:''
  },
  {

    practiceName: '义务支教',
    practiceDescription:'',
    practiceId: null,
    studentName:''
  },
])
/* let tableData =reactive([]); */ //table中的所有数据，数组中应该是很多个对象的集合
let typeOperation =ref(''); //edit,check,add 编辑，查看，添加
let centerDialogVisible = ref(false);
let isShow = ref(false);
let editForm =reactive({
  token: '',
  practiceName: '回访母校-情系山大',
  practiceDescription:'',
  practiceId: null,
  studentName:''
});

const value1 = ref('')
const search = ref('')

const PracticeTableData = computed(() =>
  tableData.filter(
    (data) =>
      !search.value ||
      data.pracitceName.toLowerCase().includes(search.value.toLowerCase())
  )
)

const checkInformation=()=>{
  // if(editForm.practiceDate!=''&&editForm.practiceName!=''&&editForm.practiceDescription!=''&&editForm.character!=''){
  //   return true;
  // }else{
  //   msgWarning("数据不能为空")
  // }
}

const loadPracticeTable = async() =>{   //查找所有的数据,这个接口是不是有点问题
  await service.post('/api/practice/find',{token:localStorage.getItem("token")}).then(res => {
    if (res.data.success) {
      const data = res.data;
      let arr = data.content //拿到了返回的数组,这个是data.data还是data.token
      tableData = arr
      localStorage.setItem('token',data.token)
    } else {
      msgWarning(res.data.msg)
      }
    })
  .catch(function(error) {
    console.log(error)
  })
}
loadPracticeTable() //进入默认执行

const add = async () => {
  centerDialogVisible.value = true;
  typeOperation.value = 'add';
}

const handleCheck = async(row)=>{   //查看单个的数据
  centerDialogVisible.value = true;
  editForm = Object.assign({}, row);
  console.log(editForm)
  typeOperation.value = 'check'; //查看完就完事儿
}

const handleEdit = async(row) => {  //改
  centerDialogVisible.value = true;
  editForm.practiceDescription = row.practiceDescription;
  editForm.practiceName = row.practiceName;
  editForm.practiceId = row.practiceId;
  editForm.studentName = row.studentName;
  // editForm = Object.assign({}, row);//先弹对话框，然后提交，提交之后再传参数吧
  console.log(editForm)
  typeOperation.value = 'edit';
}

const handleDelete= async (row)=>{  //删
        if(confirm('确认是否删除')){
         await service.post('/api/practice/delete',{token:localStorage.getItem("token") ,id:row.id}).then(res=>{
            if(res.data.success){
              msgSuccess('删除成功!')
              loadPracticeTable() //重新加载现在表单中的数据
              localStorage.setItem("token",res.data.token)
            }else{
              msgWarning(res.data.message)
            }
          })
        }
      }
const sumbitEditRow=()=>{
  editForm.token = localStorage.getItem("token")
  if(typeOperation.value==='check'){
    /* handleCheck() */
    
  }else if(typeOperation.value==='edit'){
    /* handleEdit() */
    service.post('api/practice/save',editForm).then(res=>{  //直接把这一行的数据给出去可以吗
      if(res.data.success){
        msgSuccess("编辑成功！")
        typeOperation.value='';
        loadPracticeTable()
        localStorage.setItem("token",res.data.token)
      }else{
        msgError("编辑失败!")
        console.log(res.data.msg)
      }
    })
    isShow.value = false;
  }else if(typeOperation.value==='add'){
    service.post('/api/practice/save',editForm).then(res=>{
      if(res.data.success){
        msgSuccess("添加成功！")
        typeOperation.value='';
        loadPracticeTable()
        localStorage.setItem("token",res.data.token)
      }else{
        msgError("添加失败！")
        console.log(res.data.msg)
      }
    }
  )
    isShow.value = false;
  }else{

  }
  centerDialogVisible.value = false;
  typeOperation.value = '';
};

const closeDialog = () =>{

}


        /* if(!row.id){  
            tableData.splice(index,1) //存在就给他删了，这个有必要吗
        }*/
</script>
<style scoped>

.title{
  height: 60px;
}
.pageContent{
  height: 80%;
}
.search{
  /* margin-right: 30px!important; */
}
</style>