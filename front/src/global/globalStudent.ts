//全局变量

import {reactive, ref} from "vue";
let nickName = '爷傲♥奈我何？'; //用户名
let avatarURL = 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'; //用户头像地址
export let getNickName = () =>{
      return nickName;
}
export let getAvatarURL=  ()=>{
      return avatarURL;
}
export let setNickName = (name) =>{
    nickName = name;
}
export let setAvatarURL = (url) =>{
    avatarURL = url;
}