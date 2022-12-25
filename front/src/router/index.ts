import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
     {
       path: '/',
       name: 'origin',
       component: () => import('../views/originPage.vue')
     },
    {
      path: '/Login',
      name: 'Login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/test',
      name: 'test',
      component: () => import('../views/test.vue')
    },
    {
      path: '/Register',
      name: 'Register',
      component: () => import('../views/RegisterView.vue')
    },
    /*     {
          path: '/',
          name: 'home',
          component: () => import('../views/AboutView.vue')
        }, */
    // {
    //     path: '/CourseSelect',
    //     name: 'CourseSelect',
    //     component: () => import('../views/student/CourseSelect.vue')
    // },
    {
      path: '/Main',
      name: 'main',
      component: () => import('../views/student/MainView.vue')
    },
    {
      path: '/HomePage',
      name: 'HomePage',
      component: () => import('../views/student/HomePage.vue'),
      meta: {
        keepAlive: true // 需要缓存
      },
      children: [
        {
          path: '/CourseSelect',
          name: 'CourseSelect',
          component: () => import('../views/student/CourseSelect.vue')
        },
        {
          path: '/ScoreManage',
          name: 'ScoreManage',
          component: () => import('../views/student/ScoreManage.vue')
        },
        {
          path: '/SelfInformation',
          name: 'SelfInformation',
          component: () => import('../views/student/SelfInformation.vue')
        },
        {
          path: '/Main',
          name: 'main',
          component: () => import('../views/student/MainView.vue')
        },
        {
          path: '/AcademicManagement',
          name: 'AcademicManagement',
          component: () => import('../views/student/AcademicManagement.vue'),
          children: [
            {
              path:'/AcademicManagement/AMHome',
              name:'AMHome',
              component:()=> import('../views/student/AMHome.vue')
            },
            {
              path: '/AcademicManagement/StudentEvaluate',
              name: 'StudentEvaluate',
              component: () => import('../views/student/StudentEvaluate.vue')
            },
            {
              path: '/AcademicManagement/Practice',
              name: 'Practice',
              component: () => import('../views/student/PracticeView.vue')
            },
            {
              path: '/AcademicManagement/Activity',
              name: 'Activity',
              component: () => import('../views/student/ActivityView.vue')
            },

            {
              path: '/AcademicManagement/Reward',
              name: 'Reward',
              component: () => import('../views/student/RewardView.vue')
            },
          ]
        },
      ]
    },
    {
      path: '/TeacherHomePage',
      name: 'TeacherHomePage',
      component: () => import('../views/teacher/TeacherHomePage.vue'),
      meta: {
        keepAlive: true // 需要缓存
      },
      children: [
        {
          path: '/TeacherCourseManage',
          name: 'TeacherCourseManage',
          component: () => import('../views/teacher/TeacherCourseManage.vue')
        },
        {
          path: '/TeacherMain',
          name: 'TeacherMain',
          component: () => import('../views/teacher/TeacherMainView.vue')
        },
        {
          path: '/TeacherScoreManage',
          name: 'TeacherScoreManage',
          component: () => import('../views/teacher/TeacherScoreManage.vue')
        },
        {
          path: '/TeacherSelfInformation',
          name: 'TeacherSelfInformation',
          component: () => import('../views/teacher/TeacherSelfInformation.vue')
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
