<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { logout } from '@/net'
import { 
  User, Setting, List, DataLine, SwitchButton , House
} from '@element-plus/icons-vue'

const router = useRouter()
const activeIndex = ref('/admin/users')

function userLogout() {
  logout(() => {
    router.push('/auth/login')
  })
}

</script>>

<template>
  <el-container style="height: 100vh">
    <!-- 侧边栏 -->
    <el-aside width="200px">
      <el-menu
        :default-active="activeIndex"
        router
        class="menu-container">
        <!-- 用户管理 -->
        <el-menu-item index="/admin/accounts">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>

        <!-- 帖子管理 -->
        <el-menu-item index="/admin/posts">
          <el-icon><List /></el-icon>
          <span>帖子管理</span>
        </el-menu-item>

        <!-- 系统配置 -->
        <el-sub-menu>
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统配置</span>
          </template>
          <el-menu-item index="/admin/subjects">科目管理</el-menu-item>
          <el-menu-item index="/admin/places">场地管理</el-menu-item>
        </el-sub-menu>

        <!-- 数据统计 -->
        <el-menu-item index="/admin/stats">
          <el-icon><DataLine /></el-icon>
          <span>数据统计</span>
        </el-menu-item>

        <el-divider />
        
        <el-menu-item @click="userLogout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
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