//全局变量

import {reactive, ref} from "vue";
const baseURL = ref('http://courseback.clankalliance.cn')
const defaultAvatarURL = ref('http://courseback.clankalliance.cn')
export let getNickName = () =>{
      return localStorage.getItem('nickName')
}

export let setNickName = (name) =>{
    localStorage.setItem('nickName',name)
}
export let getBaseURL=  ()=>{
    return baseURL.value;
}

export let setAvatarURL = (url) =>{
    localStorage.setItem('avatarURL',url)
}
export let getAvatarURL=  ()=>{
    return localStorage.getItem('avatarURL')
}

export const getDefaultAvatarURL = () => {
    return defaultAvatarURL.value;
}

export let setUserId = (userId) =>{
    localStorage.setItem('userId',userId)
}
export let getUserId=  ()=>{
    return localStorage.getItem('userId');
}

