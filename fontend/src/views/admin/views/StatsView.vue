<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import axios from 'axios'
import { getUserActivityStats } from '@/net/admin/stats'

const loading = ref(false)
const trendChart = ref(null)
const statusChart = ref(null)
const subjectChart = ref(null)
const typeChart = ref(null)

function initCharts(data) {
  // 帖子趋势图
  const trend = echarts.init(trendChart.value)
  trend.setOption({
    title: { text: '最近帖子趋势' },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: data.trendStats.map(item => item.date.slice(5))
    },
    yAxis: { type: 'value' },
    series: [{
      data: data.trendStats.map(item => item.count),
      type: 'line',
      smooth: true,
      areaStyle: {}
    }]
  })

  // 状态分布
  const status = echarts.init(statusChart.value)
  status.setOption({
    title: { text: '帖子状态分布' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: data.statusStats.map(item => ({
        name: item.status === 1 ? '已发布' : '已关闭',
        value: item.count
      }))
    }]
  })

  // 科目分布
  const subject = echarts.init(subjectChart.value)
  subject.setOption({
    title: { text: '科目分布' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.subjectStats.map(item => item.subject_name) },
    yAxis: { type: 'value' },
    series: [{
      data: data.subjectStats.map(item => item.count),
      type: 'bar'
    }]
  })

  // 类型分布
  const type = echarts.init(typeChart.value)
  type.setOption({
    title: { text: '帖子类型分布' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: data.typeStats.map(item => ({
        name: item.type === 'help' ? '求助' : '资源',
        value: item.count
      }))
    }]
  })
}

function loadStats() {
  loading.value = true
  getUserActivityStats((data) => {
    initCharts(data)
    loading.value = false
  })
}

function handleExport() {
  // 获取token
  const token = localStorage.getItem('access_token') || sessionStorage.getItem('access_token')
  if(token) {
    const authObj = JSON.parse(token)
    // 创建临时form表单
    const form = document.createElement('form')
    form.method = 'GET'
    form.action = `${axios.defaults.baseURL}/api/post/admin/stats/export`
    
    // 添加Authorization header
    const headers = new Headers()
    headers.append('Authorization', `Bearer ${authObj.token}`)
    
    // 使用fetch下载
    fetch(form.action, {
      method: 'GET',
      headers: headers
    })
    .then(response => response.blob())
    .then(blob => {
      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'stats.xlsx'
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
      ElMessage.success('下载成功')
    })
    .catch(() => {
      ElMessage.error('下载失败')
    })
  } else {
    ElMessage.error('未登录状态')
  }
}

onMounted(() => {
  loadStats()
})
</script>

<template>
  <div class="stats-container" v-loading="loading">
    <div class="header">
      <h2>数据统计</h2>
      <el-button type="primary" @click="handleExport">
        导出统计
      </el-button>
    </div>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <div ref="trendChart" style="height: 300px; margin-bottom: 20px"/>
      </el-col>
      <el-col :span="12">
        <div ref="statusChart" style="height: 300px; margin-bottom: 20px"/>
      </el-col>
      <el-col :span="12">
        <div ref="subjectChart" style="height: 300px"/>
      </el-col>
      <el-col :span="12">
        <div ref="typeChart" style="height: 300px"/>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stats-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>