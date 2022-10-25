import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: () => import('../views/MainView.vue')
    },{
      path: '/Login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    }
  ]
})
//导入nprogress 为了路由跳转有进度条
import NProgress from 'nprogress/nprogress.js'
import 'nprogress/nprogress.css'
router.beforeEach(async(to,from,next)=>{
  /* console.log('进入beforeEach函数') */
  NProgress.start();
  next()
})
router.afterEach((to,from) =>{
  NProgress.done();
})
export default router
