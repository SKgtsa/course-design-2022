//全局变量

import {reactive, ref} from "vue";
let nickName = ref('爷傲♥奈我何？'); //用户名
let avatarURL = ref('https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'); //用户头像地址
export let getNickName = () =>{
      return nickName.value;
}
export let getAvatarURL=  ()=>{
      return avatarURL.value;
}
export let setNickName = (name) =>{
    nickName.value = name;
}
export let setAvatarURL = (url) =>{
    avatarURL.value = url;
}