<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElDialog } from 'element-plus'
import SubjectMatch from './components/SubjectMatch.vue'
import TimeMatch from './components/TimeMatch.vue'
import PlaceMatch from './components/PlaceMatch.vue'
import ComplexMatch from './components/ComplexMatch.vue'
import { getUserMatches, getPendingMatches, confirmMatch, rejectMatch } from '@/net/user/match'
import { getUserProfileByUsername } from '@/net/user/profile'
import { getUserRequests, getPendingRequests, acceptRequest, rejectRequest } from '@/net/user/study-request'

const activeMatchTab = ref('subject')
const activeRecordTab = ref('match')
const matches = ref([])
const pendingMatches = ref([])
const studyRequests = ref([])
const pendingStudyRequests = ref([])
const showProfileDialog = ref(false)
const currentProfile = ref(null)
const currentUsername = ref(localStorage.getItem('username'))  // 添加当前用户名

onMounted(() => {
  loadAllData()
})

function loadAllData() {
  loadMatches()
  loadStudyRequests()
}

function loadMatches() {
  getUserMatches((data) => matches.value = data)
  getPendingMatches((data) => pendingMatches.value = data)
}

function loadStudyRequests() {
  getUserRequests((data) => studyRequests.value = data)
  getPendingRequests((data) => pendingStudyRequests.value = data)
}

function onConfirmMatch(id) {
  confirmMatch(id, () => {
    ElMessage.success('已接受匹配')
    loadMatches()
  })
}

function onRejectMatch(id) {
  rejectMatch(id, () => {
    ElMessage.success('已拒绝匹配')
    loadMatches()
  })
}

function onAcceptRequest(id) {
  acceptRequest(id, () => {
    ElMessage.success('已接受请求')
    loadStudyRequests()
  })
}

function onRejectRequest(id) {
  rejectRequest(id, () => {
    ElMessage.success('已拒绝请求')
    loadStudyRequests()
  })
}

function showUserProfile(username) {
  getUserProfileByUsername(username, (data) => {
    currentProfile.value = data
    showProfileDialog.value = true
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
          <el-card 
            v-for="match in matches" 
            :key="match.id" 
            class="record-item"
            :class="{'clickable': match.status === 1}"
            @click="match.status === 1 && showUserProfile(match.username2)">
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
        <div v-if="pendingMatches.length > 0 || pendingStudyRequests.length > 0" class="record-list">
          <!-- 待处理匹配请求 -->
          <template v-if="pendingMatches.length > 0">
            <div class="section-title">匹配请求</div>
            <el-card v-for="match in pendingMatches" :key="'match-'+match.id" class="record-item">
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
                <el-button type="success" size="small" @click="onConfirmMatch(match.id)">接受</el-button>
                <el-button type="danger" size="small" @click="onRejectMatch(match.id)">拒绝</el-button>
              </div>
            </el-card>
          </template>

          <!-- 待处理助学请求 -->
          <template v-if="pendingStudyRequests.length > 0">
            <div class="section-title">助学请求</div>
            <el-card v-for="request in pendingStudyRequests" :key="'request-'+request.id" class="record-item">
              <div class="record-info">
                <div>请求用户: {{ request.fromUsername }}</div>
                <div>关联帖子: {{ request.postTitle }}</div>
                <div>留言: {{ request.message }}</div>
                <div v-if="request.timeSlot">时间: 周{{ request.timeSlot.weekday }} {{ request.timeSlot.time }}</div>
                <div v-if="request.placeName">地点: {{ request.placeName }}</div>
              </div>
              <div class="record-actions">
                <el-button type="success" size="small" @click="onAcceptRequest(request.id)">接受</el-button>
                <el-button type="danger" size="small" @click="onRejectRequest(request.id)">拒绝</el-button>
              </div>
            </el-card>
          </template>
        </div>
        <div v-else class="no-record">暂无待处理请求</div>
      </el-tab-pane>

      <el-tab-pane label="助学记录" name="study">
        <div v-if="studyRequests.length > 0" class="record-list">
          <el-card 
            v-for="request in studyRequests" 
            :key="request.id" 
            class="record-item"
            :class="{'clickable': request.status === 1}"
            @click="request.status === 1 && showUserProfile(
              request.fromUsername === currentUsername ? request.toUsername : request.fromUsername
            )">
            <div class="record-info">
              <div>{{ request.fromUsername === currentUsername ? '发送给: ' + request.toUsername : '来自: ' + request.fromUsername }}</div>
              <div>关联帖子: {{ request.postTitle }}</div>
              <div>留言: {{ request.message }}</div>
              <div v-if="request.timeSlot">时间: 周{{ request.timeSlot.weekday }} {{ request.timeSlot.time }}</div>
              <div v-if="request.placeName">地点: {{ request.placeName }}</div>
              <div>状态: {{ 
                request.status === 0 ? '待处理' :
                request.status === 1 ? '已接受' :
                request.status === 2 ? '已拒绝' : '已完成'
              }}</div>
            </div>
          </el-card>
        </div>
        <div v-else class="no-record">暂无助学记录</div>
      </el-tab-pane>
    </el-tabs>

    <!-- 用户画像弹窗 -->
    <el-dialog
      v-model="showProfileDialog"
      title="用户画像"
      width="500px">
      <div v-if="currentProfile" class="profile-info">
        <div class="info-item">
          <label>真实姓名：</label>
          <span>{{ currentProfile.realName }}</span>
        </div>
        <div class="info-item">
          <label>班级：</label>
          <span>{{ currentProfile.className }}</span>
        </div>
        <div class="info-item">
          <label>联系电话：</label>
          <span>{{ currentProfile.phone }}</span>
        </div>
        <div class="info-item">
          <label>空闲时间：</label>
          <div v-for="time in currentProfile.availableTimes" :key="time.weekday">
            周{{ time.weekday }} {{ time.time }}
          </div>
        </div>
      </div>
    </el-dialog>
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
.clickable {
  cursor: pointer;
  transition: all 0.3s;
}
.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.profile-info {
  padding: 20px;
}
.info-item {
  margin-bottom: 15px;
}
.info-item label {
  font-weight: bold;
  margin-right: 10px;
  color: #666;
}
.section-title {
  margin: 10px 0;
  font-weight: bold;
  color: #666;
}
</style>