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
    },
    {
      path:'/Register',
      name:'Register',
      component:() => import('../views/RegisterView.vue')
    },
/*     {
      path: '/',
      name: 'home',
      component: () => import('../views/AboutView.vue')
    }, */
    {
      path:'/HomePage',
      name:'HomePage',
      component:() => import('../views/HomePage.vue'),
      meta:{
        keepAlive:true // 需要缓存
    },
      children:[
        {
          path:'/CourseSelect',
          name:'CourseSelect',
          component:() =>import('../views/CourseSelect.vue')
        },
        {
          path:'/CourseEvaluate',
          name:'CourseEvaluate',
          component:() =>import('../views/CourseEvaluate.vue')
        },
        {
          path:'/Activity',
          name:'Activity',
          component:() =>import('../views/ActivityView.vue')
        },
        {
          path:'/Practice',
          name:'Practice',
          component:() =>import('../views/PracticeView.vue')
        },
        {
          path:'/Reward',
          name:'Reward',
          component:() =>import('../views/RewardView.vue')
        },
        {
          path:'/ScoreManage',
          name:'RScoreManage',
          component:() =>import('../views/ScoreManage.vue')
        },
        {
          path:'/SelfInformation',
          name:'SelfInformation',
          component:() =>import('../views/SelfInformation.vue')
        }
      ]
    },
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
