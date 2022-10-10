import axios from 'axios'


//已创建的axios实例
const service = axios.create({
    baseURL: '/api',
    timeout: 60000,
})

export default service
