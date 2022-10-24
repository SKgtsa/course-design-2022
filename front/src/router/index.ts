import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/AboutView.vue')
    },
    {
      path:'/login',
      name:'login',
      component: () => import('../views/login.vue')
    },
    {
      path:'/HomePage',
      name:'HomePage',
      component:() => import('../views/HomePage.vue')
    },
    {
      path:'/Register',
      name:'Register',
      component:()=> import('../views/Register.vue')
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
