import { createRouter, createWebHistory } from 'vue-router'
import {tokenCheck} from "@/common/common";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue')
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/HomeView2.vue')
    }
  ]
})

router.beforeEach(async(to,from,next)=>{
  //这段代码是每次跳转页面时向后端验证token的语句
  //启用后 后端不开启会导致前端页面无法跳转
  //如启用请将被注释语句解除注释，并将最下方的next()注释掉

  // if(to.path != '/user' && (localStorage.getItem('token') == null || ! await tokenCheck())){
  //   next('/user');
  // }else{
  //   next();
  // }
  next();
})

export default router
