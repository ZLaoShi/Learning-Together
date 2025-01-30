import { createRouter, createWebHistory } from 'vue-router'
import { unauthorized, isAdmin } from '@/net'

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
    },
    {
      path: '/admin',
      component: () => import('@/views/admin/index.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {path: '', redirect: '/admin/accounts'},
        {path: 'accounts', component: () => import('@/views/admin/views/AccountsView.vue')},
        {path: 'posts', component: () => import('@/views/admin/views/PostsView.vue')},
        {path: 'subjects', component: () => import('@/views/admin/views/SubjectsView.vue')},
        {path: 'places', component: () => import('@/views/admin/views/PlacesView.vue')},
        {path: 'stats', component: () => import('@/views/admin/views/StatsView.vue')}
      ]
    }
  ]
})

// router.beforeEach((to, from, next) => {
//   if(to.meta.requiresAuth && unauthorized()) {
//     next('/auth/login')
//   } else if(!unauthorized() && to.path.startsWith('/auth')) {
//     next('/index/posts')
//   } else next()
// })

router.beforeEach((to, from, next) => {
  // 需要登录但未登录
  if(to.meta.requiresAuth && unauthorized()) {
    next('/auth/login')
  } 
  // 需要管理员权限但不是管理员
  else if(to.meta.requiresAdmin && !isAdmin()) {
    next('/index/posts')
  }
  else {
    next()
  }
})

export default router