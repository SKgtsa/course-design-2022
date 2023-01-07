import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'origin',
      component: () => import('../views/OriginPage.vue')
    },
    {
      path: '/Login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/Register',
      name: 'Register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/personalPage',
      name: 'personal',
      component: () => import('../views/Blog/BlogPersonalView.vue')
    },{
      path: '/JiangMuzi',
      name: 'JiangMuziPersonalPage',
      component: () => import('../views/group/JiangMuziPersonalPage.vue')
    }, {
      path: '/GuanYunPeng',
      name: 'GuanYunPengPersonalPage',
      component: () => import('../views/group/GuanYunPengPersonalView.vue')
    },{
      path:'/PengShuaihao',
      name:'PengShuaihaoPersonalPage',
      component:() =>import('../views/group/PengShuaihaoPersonView.vue')
    }
    ,{
      path: '/InfoFillIn',
      name: 'InfoFillInPage',
      component: () => import('../views/InfoFillView.vue')
    },
    {
      path: '/Main',
      name: 'main',
      component: () => import('../views/student/MainView.vue')
    },
    {
      path: '/Student',
      name: 'HomePage',
      component: () => import('../views/student/HomePage.vue'),
      meta: {
        keepAlive: true // 需要缓存
      },
      children: [
        {
          path: '/Student/CourseSelect',
          name: 'CourseSelect',
          component: () => import('../views/student/CourseSelect.vue')
        },
        {
          path: '/Student/ScoreManage',
          name: 'ScoreManage',
          component: () => import('../views/student/ScoreManage.vue')
        },
        {
          path: '/Student/SelfInformation',
          name: 'SelfInformation',
          component: () => import('../views/student/SelfInformation.vue')
        },
        {
          path: '/Student/Main',
          name: 'main',
          component: () => import('../views/student/MainView.vue')
        },
        {
          path: '/Student/Practice',
          name: 'Practice',
          component: () => import('../views/student/PracticeView.vue')
        },
        {
          path: '/Student/Activity',
          name: 'Activity',
          component: () => import('../views/student/ActivityView.vue')
        },

        {
          path: '/Student/Reward',
          name: 'Reward',
          component: () => import('../views/student/RewardView.vue')
        },
      ]
    },
    {
      path: '/Teacher',
      name: 'TeacherHomePage',
      component: () => import('../views/teacher/HomePage.vue'),
      meta: {
        keepAlive: true // 需要缓存
      },
      children: [
        {
          path: '/Teacher/CourseManage',
          name: 'TeacherCourseManage',
          component: () => import('../views/teacher/CourseManage.vue')
        },
        {
          path: '/Teacher/Main',
          name: 'TeacherMain',
          component: () => import('../views/teacher/MainView.vue')
        },
        {
          path: '/Teacher/ScoreManage',
          name: 'TeacherScoreManage',
          component: () => import('../views/teacher/ScoreManage.vue')
        },
        {
          path: '/Teacher/SelfInformation',
          name: 'TeacherSelfInformation',
          component: () => import('../views/teacher/PersonPageView.vue')
        },
      ]
    },
    {
      path: '/Manager',
      name: 'ManagerHomePage',
      component: () => import('../views/manager/HomePage.vue'),
      meta: {
        keepAlive: true
      },
      children: [
        {
          path: '/Manager/Main',
          name: 'ManagerMainView',
          component: () => import('../views/manager/MainView.vue')
        },
        {
          path: '/Manager/InfoManage',
          name: 'ManagerInfoManage',
          component: () => import('../views/manager/InfoManage.vue')
        },
        {
          path: '/Manager/CourseManage',
          name: 'ManagerCourseManage',
          component: () => import('../views/manager/CourseManage.vue'),
        },
        {
          path: '/Manager/Notice',
          name: 'ManagerNotice',
          component: () => import('../views/manager/NoticeView.vue'),
        },
      ]
    }
  ]
})
//导入nprogress 为了路由跳转有进度条
import NProgress from 'nprogress/nprogress.js'
import 'nprogress/nprogress.css'

router.beforeEach(async (to, from, next) => {
  /* console.log('进入beforeEach函数') */
  NProgress.start();
  next()
})
router.afterEach((to, from) => {
  NProgress.done();
})
export default router
