<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllPosts, updatePostStatus } from '@/net/admin/post'

const posts = ref([])
const loading = ref(false)

function loadPosts() {
  loading.value = true
  getAllPosts((data) => {
    posts.value = data
    loading.value = false
  })
}

function handleStatus(id, status) {
  const actionText = status === 1 ? 
    (posts.value.find(p => p.id === id).status === 0 ? '通过审核' : '开启') : 
    '关闭'
    
  ElMessageBox.confirm(
    `确定${actionText}该帖子?`,
    '提示',
    {
      type: status === 1 ? 'info' : 'warning',
    }
  ).then(() => {
    updatePostStatus(id, status, () => {
      ElMessage.success('操作成功')
      loadPosts()
    })
  })
}

onMounted(() => {
  loadPosts()
})
</script>

<template>
  <div class="posts-container">
    <h2>帖子管理</h2>
    
    <el-table :data="posts" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="发布者" width="120" />
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="scope">
          {{ scope.row.type === 'help' ? '求助' : '资源' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="
            scope.row.status === 0 ? 'info' :
            scope.row.status === 1 ? 'success' : 'danger'
          ">
            {{ 
              scope.row.status === 0 ? '待审核' :
              scope.row.status === 1 ? '已发布' : '已关闭'
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button-group>
            <!-- 状态为0(待审核)显示通过按钮 -->
            <el-button 
              v-if="scope.row.status === 0"
              type="success" 
              size="small"
              @click="handleStatus(scope.row.id, 1)">
              通过
            </el-button>
            <!-- 状态为2(已关闭)显示开启按钮 -->
            <el-button
              v-if="scope.row.status === 2"
              type="primary"
              size="small"
              @click="handleStatus(scope.row.id, 1)">
              开启
            </el-button>
            <!-- 状态不为2时显示关闭按钮 -->
            <el-button 
              v-if="scope.row.status !== 2"
              type="danger" 
              size="small"
              @click="handleStatus(scope.row.id, 2)">
              关闭
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.posts-container {
  padding: 20px;
}
</style>