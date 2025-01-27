<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { matchBySubject } from '@/net/user/match'

const emit = defineEmits(['match-success'])
const loading = ref(false)
const matches = ref([])
const hasMatched = ref(false)  // 添加匹配状态标记

function startMatch() {
  loading.value = true
  matchBySubject((data) => {
    matches.value = data
    hasMatched.value = true  // 设置匹配状态
    emit('match-success')
    ElMessage.success('匹配成功')
    loading.value = false
  })
}
</script>


<template>
  <div class="match-panel">
    <div class="match-header">
      <h3>结对匹配</h3>
      <el-button 
        type="primary" 
        :loading="loading"
        @click="startMatch">
        {{ loading ? '匹配中...' : (hasMatched ? '重新匹配' : '开始匹配') }}
      </el-button>
    </div>
    
    <div v-if="matches.length > 0" class="match-list">
      <el-card v-for="match in matches" :key="match.id" class="match-item">
        <div class="match-info">
          <div>匹配用户: {{ match.username2 }}</div>
          <div>匹配分数: {{ match.matchScore }}</div>
        </div>
      </el-card>
    </div>
    <div v-else class="no-match">
      暂无匹配结果
    </div>
  </div>
</template>

<style scoped>
.match-panel {
  padding: 20px;
}
.match-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.match-list {
  display: grid;
  gap: 20px;
}
.match-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.match-info {
  line-height: 1.8;
}
.match-actions {
  display: flex;
  gap: 10px;
}
.no-match {
  text-align: center;
  color: #999;
  padding: 40px;
}
</style>