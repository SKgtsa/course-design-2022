import { $get, $post } from "../utils/request.js"
import md5 from 'js-md5'
import {ElMessage} from 'element-plus'
//管理员
export let Login = async(params)=>{
    //对密码进行加密
    /* params.LoginId = md5(params.loginpassword).split('').reverse().join(); */
    let {success,message} = await $get('Admin/Login',params)
     let data = await $get('Admin/Login',params) 
    console.log(data)
}