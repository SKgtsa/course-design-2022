const utils  = {
    saveData(key,value){
        return localStorage.setItem(key,value);
    },
    removeData(key){
        return localStorage.removeItem(key);
    },
    getData(key){
        return localStorage.getItem(key);
    }
}
export default utils;