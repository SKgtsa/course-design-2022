 import axios from 'axios'


//已创建的axios实例
const service = axios.create({
    //这里本来的baseURL填错了
    baseURL: '/api',
    timeout: 60000,
})
 export default service;
 //拦截函数不需要 目前业务不涉及
// axios.interceptors.request.use(function (config) {
//     // 在发送请求之前做些什么
//     return config;
//   }, function (error) {
//     // 对请求错误做些什么
//     return Promise.reject(error);
//   });
/*

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response;
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  });
axios.defaults.headers.post['Content-Type'] =
 'application/x-www-form-urlencoded;charset=UTF-8';
export default axios;  */
