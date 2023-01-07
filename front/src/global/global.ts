//全局变量

import {ref} from "vue";
//切换本地或云上后端
// const baseURL = ref('http://courseback.clankalliance.cn')
const baseURL = ref('http://localhost:5174')

const defaultAvatarURL = ref('http://courseback.clankalliance.cn/inbuild/default.png')
export const windowWidth = ref(0)
export const windowHeight = ref(0)
export const mobile = ref(false)
export const menuDrawerOpen = ref(false);
export const showRouter = ref(true);

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
export let setManager = (manager) =>{
    localStorage.setItem('manager',manager)
}
export let getManager=  ()=>{
    return localStorage.getItem('manager');
}
