//封装消息框
import { ElMessage } from "element-plus";
//成功
export let messageSuccess = (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'success',
    })
}
//警告
export let messageWarning = (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'warning'
    })
}
//错误
export let messageError= (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'error'
    })
}
export let messageInfo = (message,duration=2000)=>{
    ElMessage({
        showClose:true,
        message,
        duration,
        type:'info'
    })
}