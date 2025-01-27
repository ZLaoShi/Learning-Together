import { createRouter, createWebHistory } from 'vue-router'
import { unauthorized } from '@/net'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/index/posts'
    },
    {
      path: '/auth',
      name: 'auth',
      component: () => import('@/views/WelcomeView.vue'),
      children: [
        {path: '', redirect: '/auth/login'},
        {path: 'login', name: 'auth-login', component: () => import('@/views/welcome/LoginPage.vue')},
        {path: '/register', name: 'auth-register', component: () => import('@/views/welcome/RegisterPage.vue')}
      ]
    },
    {
      path: '/index',
      component: () => import('@/views/index.vue'),
      children: [
        {path: '', redirect: '/index/posts'},
        {
          path: 'posts',
          component: () => import('@/views/posts/PostList.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'create',
          component: () => import('@/views/posts/PostCreate.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'matching',
          component: () => import('@/views/match/MatchView.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'profile',
          component: () => import('@/views/profile/ProfileView.vue'),
          meta: { requiresAuth: true }
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  if(to.meta.requiresAuth && unauthorized()) {
    next('/auth/login')
  } else if(!unauthorized() && to.path.startsWith('/auth')) {
    next('/index/posts')
  } else next()
})

export default router