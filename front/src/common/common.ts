import service from "@/request";
import {ElMessage} from "element-plus";

//与后端检查token并更新本地存储的用户信息
const tokenCheck = async ():Promise<boolean> => {
    let result = true;
    await service.post('/user/tokenCheck',{token: localStorage.getItem('token')}).then((response) => {
        const data = response.data;
        if(data.success){
            //更新token
            localStorage.setItem("token",data.token);
            localStorage.setItem("name",data.user.name);
            localStorage.setItem("manager",data.user.manager);
        }else{
            //删除token
            localStorage.removeItem('token')
            localStorage.removeItem('name')
            localStorage.removeItem('manager')
            result = false;
            ElMessage.error(data.message)
        }
    })
    return result;
}

//异步函数属于是纯纯的坑

export {tokenCheck}
