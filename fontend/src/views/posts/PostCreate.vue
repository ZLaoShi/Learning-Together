<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { createPost } from '@/net/user/post'
import { getSubjectList } from '@/net/user/subject'
import { getPlaceList } from '@/net/user/place'

const form = reactive({
  title: '',
  content: '',
  type: 'help',
  subjectId: '',
  placeId: '',
  timeSlot: {
    weekday: 1,
    time: ''
  },
  inviteEnabled: 1
})

const subjects = ref([])
const places = ref([])

onMounted(() => {
  loadOptions()
})

function loadOptions() {
  getSubjectList((data) => subjects.value = data)
  getPlaceList((data) => places.value = data)
}

function onSubmit() {
  createPost(form, () => {
    ElMessage.success('发布成功')
    router.push('/posts')
  })
}
</script>

<template>
  <div class="create-container">
    <h2>发布新帖</h2>
    <el-form :model="form" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="请输入标题"/>
      </el-form-item>
      
      <el-form-item label="类型">
        <el-radio-group v-model="form.type">
          <el-radio label="help">求助</el-radio>
          <el-radio label="resource">资源</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="科目">
        <el-select v-model="form.subjectId" placeholder="请选择科目">
          <el-option 
            v-for="item in subjects" 
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="学习地点">
        <el-select v-model="form.placeId" placeholder="请选择地点">
          <el-option
            v-for="item in places"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="学习时间">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-select 
              v-model="form.timeSlot.weekday"
              style="width: 100%">  <!-- 添加宽度100% -->
              <el-option label="周一" :value="1"/>
              <el-option label="周二" :value="2"/>
              <el-option label="周三" :value="3"/>
              <el-option label="周四" :value="4"/>
              <el-option label="周五" :value="5"/>
              <el-option label="周六" :value="6"/>
              <el-option label="周日" :value="7"/>
            </el-select>
          </el-col>
          <el-col :span="12">
            <el-time-select 
              v-model="form.timeSlot.time"
              style="width: 100px"/>  <!-- 添加宽度100% -->
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item label="内容">
        <el-input 
          v-model="form.content"
          type="textarea"
          :rows="6"
          placeholder="请输入内容"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">发布</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.create-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
</style>