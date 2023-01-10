import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import router from "@/router";
import {getDefaultAvatarURL, setAvatarURL, setManager, setNickName, setUserId} from "@/global/global";
import {ElMessage} from "element-plus";
import {messageError} from "@/utils/message";


/**
 * 仅验证token是否有效 若无效会返回false
 */
export const tokenCheck = async () => {
    if(localStorage.getItem('token') == null)
        return false;
    showLoading()
    await service.post('/api/user/tokenCheck',{token: localStorage.getItem('token')}).then((res) => {
        let data = res.data;
        if(data.success){
            localStorage.setItem('token',data.token);
            hideLoading()
            return true;
        }else{
            hideLoading()
            return false;
        }
    })
}
/**
 * 验证token是否有效，同时查询用户信息返回 若无效会自动跳回登录页 原则上只需要在登陆时使用
 */
export const tokenCheckWithUser = async () => {
    if(localStorage.getItem('token') == null)
        loginFailed();
    showLoading()
    await service.post('/api/user/myInfo',{token: localStorage.getItem('token')}).then((res) => {
        let data = res.data;
        if(data.success){
            localStorage.setItem('token',data.token);
            setAvatarURL(data.user.avatarURL)
            setNickName(data.user.nickName)
            setUserId(data.user.id);
            hideLoading()
        }else{
            hideLoading()
            loginFailed()
        }
    })
}
/**
 * 若登录失效，调用此函数即可
 */
export const loginFailed = () => {
    setManager(false)
    localStorage.setItem('token',null);
    localStorage.setItem('avatarURL',getDefaultAvatarURL())
    localStorage.setItem('nickName','')
    localStorage.setItem('userId',null)
    messageError('登录失效')
    router.push('/login').then();
}

export const handleResponseMessage = async (message : string) => {
    if(message === '登录失效'){
        loginFailed();
    }else{
        tokenCheck().then((valid) => {
            if(valid){
                messageError(message);
            }else{
                loginFailed();
            }
        })
    }
}
