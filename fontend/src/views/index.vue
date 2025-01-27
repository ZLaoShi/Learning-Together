<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { logout } from '@/net'

const router = useRouter()
const activeIndex = ref('/index/posts')

function userLogout() {
  logout(() => {
    router.push('/auth/login')  // 退出后直接跳转到登录页
  })
}
</script>

<template>
  <el-container style="height: 100vh">
    <el-aside width="200px">
      <el-menu
        :default-active="activeIndex"
        router
        class="menu-container">
        <el-menu-item index="/index/posts">
          <el-icon><Document /></el-icon>
          <span>帖子列表</span>
        </el-menu-item>
        <el-menu-item index="/index/create">
          <el-icon><EditPen /></el-icon>
          <span>发布帖子</span>
        </el-menu-item>
        <el-menu-item index="/index/matching">
          <el-icon><Connection /></el-icon>
          <span>学习撮合</span>
        </el-menu-item>
        <el-menu-item index="/index/profile">
          <el-icon><User /></el-icon>
          <span>个人偏好</span>
        </el-menu-item>
        
        <el-divider />
        
        <el-menu-item @click="userLogout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<style scoped>
.menu-container {
  height: 100%;
  border-right: none;
}
</style>