/* import axios from 'axios'


//已创建的axios实例
const instance = axios.create({
    baseURL: 'http://81.68.109.154:8083',
    timeout: 60000,
})
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });

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