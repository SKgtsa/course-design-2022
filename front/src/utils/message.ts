//封装消息框
import { ElMessage } from "element-plus";
//成功
export let msgSuccess = (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'success',
    })
}
//警告
export let msgWarning = (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'warning'
    })
}
//错误
export let msgError= (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'error'
    })
}