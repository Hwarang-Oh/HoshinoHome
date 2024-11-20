import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import axios from 'axios'

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
      path: '/map',
      name: 'map',
      component: () => import('@/views/MapView.vue'),
      children: [
        {
          path: 'houseDetail/:house_code',
          name: 'house_detail',
          component: () => import('@/components/Map/HouseDetail.vue'),
          props: true
        }
      ]
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
      path: '/notice/modify/:id',
      name: 'noticeModify',
      component: () => import('../components/Notices/NoticeModify.vue'),
      props: true,
      meta: { requiresAdmin: true }
    },
    {
      path: '/notice/regist',
      name: 'noticeRegist',
      component: () => import('../components/Notices/NoticeRegist.vue'),
      meta: { requiresAdmin: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/User/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../components/User/Register.vue')
    },
    {
      path: '/dongStory',
      name: 'dongStory',
      component: () => import('@/views/DongStoryView.vue')
    },
    {
      path: '/dongStory/regist',
      name: 'dongStoryRegist',
      component: () => import('../components/DongStory/DongStoryRegist.vue')
    },
    {
      path: '/dongStory/:id',
      name: 'dongStoryDetail',
      component: () => import('../components/DongStory/DongStoryDetail.vue'),
      props: true
    },
    {
      path: '/dongStory/modify/:id',
      name: 'dongStoryModify',
      component: () => import('../components/DongStory/DongStoryModify.vue'),
      props: true
    },
    {
      path: '/chatgpt',
      name: 'chatgpt',
      component: () => import('@/views/ChatView.vue')
    }
  ]
})

// 네비게이션 가드 추가
router.beforeEach(async (to, from, next) => {
  const publicPages = ['/login', '/register', '/', '/about', '/notice', '/map', '/dongstory']
  const isPublicPage = publicPages.some(
    (page) => to.path === page || to.path.startsWith(`${page}/`)
  )
  const loggedIn = localStorage.getItem('token')

  if (!isPublicPage && !loggedIn) {
    alert('로그인이 필요합니다. 로그인 해주세요.')
    return next('/')
  }

  // 관리자 권한이 필요한 페이지 확인
  if (to.matched.some((record) => record.meta.requiresAdmin)) {
    const token = localStorage.getItem('token')
    if (!token) {
      alert('관리자 권한이 필요합니다.')
      return next('/')
    }
    try {
      const response = await axios.get('http://localhost:8080/auth/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      if (response.data.user_type !== '1234') {
        alert('관리자 권한이 필요합니다.')
        return next('/')
      }
    } catch (error) {
      alert('사용자 정보를 불러오지 못했습니다.')
      return next('/')
    }
  }

  next()
})

export default router
