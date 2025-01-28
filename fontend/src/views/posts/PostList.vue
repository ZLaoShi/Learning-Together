<script setup>
import { ref, onMounted, computed } from 'vue'
import { getPostList } from '@/net/user/post'
import { createRequest } from '@/net/user/study-request'
import { ElMessage } from 'element-plus'

const posts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)

const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return posts.value.slice(start, end)
})

const total = computed(() => posts.value.length)

onMounted(() => {
  loadPosts()
})

function loadPosts() {
  getPostList((data) => {
    posts.value = data
  })
}

function handleCurrentChange(val) {
  currentPage.value = val
}

const showDialog = ref(false)
const currentUserId = ref(parseInt(localStorage.getItem('user-id')))
const currentPost = ref(null)
const requestForm = ref({
  message: ''
})

function showRequestDialog(post) {
  currentPost.value = post
  requestForm.value.message = ''
  showDialog.value = true
}

function submitRequest() {
  const data = {
    postId: currentPost.value.id,
    toUserId: currentPost.value.userId,
    message: requestForm.value.message,
    placeId: currentPost.value.placeId,
    timeSlot: currentPost.value.timeSlot
  }
  
  createRequest(data, () => {
    ElMessage.success('请求发送成功')
    showDialog.value = false
  })
}
</script>

<template>
  <div class="post-container">
    <h2>帖子列表</h2>
    <div class="post-list">
      <el-card v-for="post in paginatedPosts" :key="post.id" class="post-card">
        <template #header>
          <div class="post-header">
            <span>{{ post.title }}</span>
            <el-tag size="small">{{ post.type === 'help' ? '求助' : '资源' }}</el-tag>
          </div>
        </template>
        <div class="post-content">
          <p>{{ post.content }}</p>
          <div class="post-info">
            <span>作者: {{ post.username }}</span>
            <span>科目: {{ post.subjectName }}</span>
            <span>地点: {{ post.placeName }}</span>
          </div>
          <!-- 只有当不是自己的帖子时才显示请求按钮 -->
          <div class="post-actions" v-if="post.userId !== currentUserId">
            <el-button 
              type="primary" 
              size="small"
              @click="showRequestDialog(post)">
              发起请求
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 请求对话框 -->
    <el-dialog
      v-model="showDialog"
      title="发起学习请求"
      width="500px">
      <el-form :model="requestForm" label-width="100px">
        <el-form-item label="留言">
          <el-input 
            v-model="requestForm.message" 
            type="textarea"
            :rows="4"
            placeholder="请输入留言"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDialog = false">取消</el-button>
          <el-button type="primary" @click="submitRequest">发送请求</el-button>
        </span>
      </template>
    </el-dialog>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        layout="prev, pager, next"/>
    </div>
  </div>
</template>

<style scoped>
.post-container {
  padding: 20px;
}
.post-list {
  margin-bottom: 20px;
}
.post-card {
  margin-bottom: 20px;
}
.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.post-content {
  padding: 10px 0;
}
.post-info {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}
.post-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>