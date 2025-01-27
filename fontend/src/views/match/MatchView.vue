<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import SubjectMatch from './components/SubjectMatch.vue'
import TimeMatch from './components/TimeMatch.vue'
import PlaceMatch from './components/PlaceMatch.vue'
import ComplexMatch from './components/ComplexMatch.vue'
import { getUserMatches, getPendingMatches, confirmMatch, rejectMatch } from '@/net/user/match'

const activeMatchTab = ref('subject')
const activeRecordTab = ref('match')
const matches = ref([])
const pendingMatches = ref([])
const studyRecords = ref([])

onMounted(() => {
  loadMatches()
})

function loadMatches() {
  getUserMatches((data) => matches.value = data)
  getPendingMatches((data) => pendingMatches.value = data)
}

function onConfirm(id) {
  confirmMatch(id, () => {
    ElMessage.success('已接受匹配')
    loadMatches()
  })
}

function onReject(id) {
  rejectMatch(id, () => {
    ElMessage.success('已拒绝匹配')
    loadMatches()
  })
}
</script>

<template>
  <div class="match-container">
    <!-- 顶部匹配菜单 -->
    <el-tabs v-model="activeMatchTab" class="match-tabs">
      <el-tab-pane label="结对匹配" name="subject">
        <SubjectMatch @match-success="loadMatches"/>
      </el-tab-pane>
      <el-tab-pane label="时间匹配" name="time">
        <TimeMatch @match-success="loadMatches"/>
      </el-tab-pane>
      <el-tab-pane label="地点匹配" name="place">
        <PlaceMatch @match-success="loadMatches"/>
      </el-tab-pane>
      <el-tab-pane label="互补匹配" name="complex">
        <ComplexMatch @match-success="loadMatches"/>
      </el-tab-pane>
    </el-tabs>

    <el-divider />

    <!-- 底部记录区 -->
    <el-tabs v-model="activeRecordTab" class="record-tabs">
      <el-tab-pane label="匹配记录" name="match">
        <div v-if="matches.length > 0" class="record-list">
          <el-card v-for="match in matches" :key="match.id" class="record-item">
            <div class="record-info">
              <div>匹配用户: {{ match.username2 }}</div>
              <div>匹配类型: {{ 
                match.matchType === 'subject' ? '结对匹配' :
                match.matchType === 'time' ? '时间匹配' :
                match.matchType === 'place' ? '地点匹配' : '互补匹配'
              }}</div>
              <div>匹配分数: {{ match.matchScore }}</div>
              <div>状态: {{ 
                match.status === 0 ? '待确认' :
                match.status === 1 ? '已接受' :
                match.status === 2 ? '已拒绝' : '已完成'
              }}</div>
            </div>
          </el-card>
        </div>
        <div v-else class="no-record">暂无匹配记录</div>
      </el-tab-pane>

      <el-tab-pane label="待处理请求" name="pending">
        <div v-if="pendingMatches.length > 0" class="record-list">
          <el-card v-for="match in pendingMatches" :key="match.id" class="record-item">
            <div class="record-info">
              <div>匹配用户: {{ match.username1 }}</div>
              <div>匹配类型: {{ 
                match.matchType === 'subject' ? '结对匹配' :
                match.matchType === 'time' ? '时间匹配' :
                match.matchType === 'place' ? '地点匹配' : '互补匹配'
              }}</div>
              <div>匹配分数: {{ match.matchScore }}</div>
            </div>
            <div class="record-actions">
              <el-button type="success" size="small" @click="onConfirm(match.id)">接受</el-button>
              <el-button type="danger" size="small" @click="onReject(match.id)">拒绝</el-button>
            </div>
          </el-card>
        </div>
        <div v-else class="no-record">暂无待处理请求</div>
      </el-tab-pane>

      <el-tab-pane label="助学记录" name="study">
        <div v-if="studyRecords.length > 0" class="record-list">
          <el-card v-for="record in studyRecords" :key="record.id" class="record-item">
            <div class="record-info">
              <div>学习伙伴: {{ record.partnerName }}</div>
              <div>学习科目: {{ record.subjectName }}</div>
              <div>学习地点: {{ record.placeName }}</div>
              <div>学习时间: 周{{ record.timeSlot.weekday }} {{ record.timeSlot.time }}</div>
              <div>状态: {{ record.status === 1 ? '进行中' : '已完成' }}</div>
            </div>
          </el-card>
        </div>
        <div v-else class="no-record">暂无助学记录</div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.match-container {
  padding: 20px;
}
.match-tabs, .record-tabs {
  margin-bottom: 20px;
}
.record-list {
  display: grid;
  gap: 20px;
  max-height: 400px;
  overflow-y: auto;
}
.record-item {
  margin-bottom: 10px;
}
.record-info {
  line-height: 1.8;
}
.record-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.no-record {
  text-align: center;
  color: #999;
  padding: 40px;
}
</style>