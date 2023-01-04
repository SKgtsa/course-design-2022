import {hideLoading, showLoading} from "@/utils/loading";
import service from "@/request";
import router from "@/router";
import {getDefaultAvatarURL, setAvatarURL, setManager, setNickName, setUserId} from "@/global/global";
import {ElMessage} from "element-plus";


/**
 * 仅验证token是否有效 若无效会自动跳回登录页
 */
export const tokenCheck = () => {
    if(localStorage.getItem('token') == null)
        router.push('/login').then();
    showLoading()
    service.post('/api/user/tokenCheck',{token: localStorage.getItem('token')}).then((res) => {
        let data = res.data;
        if(data.success){
            localStorage.setItem('token',data.token);
            hideLoading()
        }else{
            hideLoading()
            loginFailed()
        }
    })
}
/**
 * 验证token是否有效，同时查询用户信息返回 若无效会自动跳回登录页 原则上只需要在登陆时使用
 */
export const tokenCheckWithUser = () => {
    if(localStorage.getItem('token') == null)
        router.push('/login').then();
    showLoading()
    service.post('/api/user/myInfo',{token: localStorage.getItem('token')}).then((res) => {
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
    ElMessage({
        message: '登录失效',
        type: 'error'
    })
    router.push('/login').then();

}

