import service from "../request/index"
// import {ElMessage} from 'element-plus'
//管理员
//表内参数临时删除
//登陆注册方法不会复用，提取到ts文件中会有些多余
//token过期检查会多次进行
//写代码用ts js容易出事
//注意代码规范
const Login = async()=>{
    //对密码进行加密
    /* params.LoginId = md5(params.loginpassword).split('').reverse().join(); */
    //异步函数 post方法 目标url: http://localhost:5174/api/user/login 数据:{} 回调函数，接受后端传来的response
    //进入then函数
    service.post("/user/login",{}).then(res => {
        const data = res.data;
        if(data.success){
            console.log('业务成功')
        }else{
            console.log('业务失败')
        }
    })

}

export default Login;
