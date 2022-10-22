import axios from 'axios'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {BASE_URL} from '../config/conster.js'
//已创建的axios实例
const instance = axios.create({
    baseURL: BASE_URL,
    timeout: 60000,
})
axios.interceptors.request.use(function (config) {
    NProgress.start();
    // 在发送请求之前做些什么
    return config;
  }, function (error) {
    NProgress.done();
    // 对请求错误做些什么
    return Promise.reject(error);
  });

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    NProgress.done();
    return response;
  }, function (error) {
    NProgress.done();
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  });
export let $get =async (url,params) =>{
    let {data} =await instance.get(url,{params})
    return data
} 
//定义post请求方法
export let $post = async (url,params) =>{
    let {data} = await instance.get(url,params)
    return data
}
axios.defaults.headers.post['Content-Type'] =
 'application/x-www-form-urlencoded;charset=UTF-8';
export default axios; 