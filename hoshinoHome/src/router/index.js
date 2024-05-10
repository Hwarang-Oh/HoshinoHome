import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/notice',
      name: 'notice',
      component: () => import('../views/NoticeView.vue')
    },
    {
      path: '/notice/:id',
      name: 'noticeDetail',
      component: () => import('../components/Notices/NoticeDetail.vue'),
      props: true
    },
    {
      path: '/noticeRegist',
      name: 'noticeRegist',
      component: () => import('../components/Notices/NoticeRegist.vue')
    }
  ]
})

export default router
