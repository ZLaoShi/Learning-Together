<script setup>
import { ref, onMounted } from 'vue'
import { getPostList } from '@/net/user/post'

const posts = ref([])

onMounted(() => {
  loadPosts()
})

function loadPosts() {
  getPostList((data) => {
    posts.value = data
  })
}
</script>

<template>
  <div>
    <h2>帖子列表</h2>
    <el-card v-for="post in posts" :key="post.id" class="post-card">
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
      </div>
    </el-card>
  </div>
</template>

<style scoped>
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
</style>