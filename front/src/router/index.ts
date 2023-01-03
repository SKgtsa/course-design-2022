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
      path: '/test',
      name: 'test',
      component: () => import('../views/test.vue')
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
          path: '/Student/AcademicManagement',
          name: 'AcademicManagement',
          component: () => import('../views/student/AcademicManagement.vue'),
          children: [
            {
              path: '/Student/AcademicManagement/AMHome',
              name: 'AMHome',
              component: () => import('../views/student/AMHome.vue')
            },
            {
              path: '/Student/AcademicManagement/StudentEvaluate',
              name: 'StudentEvaluate',
              component: () => import('../views/student/StudentEvaluate.vue')
            },
            {
              path: '/Student/AcademicManagement/Practice',
              name: 'Practice',
              component: () => import('../views/student/PracticeView.vue')
            },
            {
              path: '/Student/AcademicManagement/Activity',
              name: 'Activity',
              component: () => import('../views/student/ActivityView.vue')
            },

            {
              path: '/Student/AcademicManagement/Reward',
              name: 'Reward',
              component: () => import('../views/student/RewardView.vue')
            },
          ]
        },
      ]
    },
    {
      path: '/Teacher',
      name: 'TeacherHomePage',
      component: () => import('../views/teacher/TeacherHomePage.vue'),
      meta: {
        keepAlive: true // 需要缓存
      },
      children: [
        {
          path: '/Teacher/CourseManage',
          name: 'TeacherCourseManage',
          component: () => import('../views/teacher/TeacherCourseManage.vue')
        },
        {
          path: '/Teacher/Main',
          name: 'TeacherMain',
          component: () => import('../views/teacher/TeacherMainView.vue')
        },
        {
          path: '/Teacher/ScoreManage',
          name: 'TeacherScoreManage',
          component: () => import('../views/teacher/TeacherScoreManage.vue')
        },
        {
          path: '/Teacher/SelfInformation',
          name: 'TeacherSelfInformation',
          component: () => import('../views/teacher/TeacherSelfInformation.vue')
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
