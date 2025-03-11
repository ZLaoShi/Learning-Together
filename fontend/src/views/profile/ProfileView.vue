<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserProfile, updateProfile, updateSubjects } from '@/net/user/profile'
import { getSubjectList } from '@/net/user/subject'
import { getPlaceList } from '@/net/user/place'

const form = reactive({
  realName: '',
  className: '', 
  phone: '',
  preferredPlaces: [],
  availableTimes: [], // 初始化为空数组而不是undefined
  goodSubjects: [],
  needSubjects: []
})

const subjects = ref([])
const places = ref([])

onMounted(() => {
  loadData()
})

function loadData() {
  getUserProfile((data) => {
    // 确保 availableTimes 始终是数组
    const profile = {
      ...data,
      availableTimes: data.availableTimes || []
    }
    Object.assign(form, profile)
  })
  getSubjectList((data) => subjects.value = data)
  getPlaceList((data) => places.value = data)
}

function onSubmit() {
  // 分两步提交
  updateProfile(form, () => {
    updateSubjects(form.goodSubjects, form.needSubjects, () => {
      ElMessage.success('更新成功')
    })
  })
}
</script>

<template>
  <div class="profile-container">
    <h2>个人偏好设置</h2>
    <el-form :model="form" label-width="120px">
      <el-form-item label="真实姓名">
        <el-input v-model="form.realName"/>
      </el-form-item>
      
      <el-form-item label="班级">
        <el-input v-model="form.className"/>
      </el-form-item>
      
      <el-form-item label="联系电话">
        <el-input v-model="form.phone"/>
      </el-form-item>

      <el-form-item label="擅长科目">
        <el-select v-model="form.goodSubjects" multiple placeholder="请选择">
          <el-option
            v-for="item in subjects"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="需要帮助科目">
        <el-select v-model="form.needSubjects" multiple placeholder="请选择">
          <el-option
            v-for="item in subjects"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="偏好学习地点">
        <el-select v-model="form.preferredPlaces" multiple placeholder="请选择">
          <el-option
            v-for="item in places"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="空闲时间">
        <el-button type="primary" @click="form.availableTimes.push({weekday:1,time:''})">
          添加时间段
        </el-button>
        <div v-for="(time, index) in form.availableTimes" :key="index" class="time-slot">
          <el-select v-model="time.weekday" style="width: 120px">
            <el-option label="周一" :value="1"/>
            <el-option label="周二" :value="2"/>
            <el-option label="周三" :value="3"/>
            <el-option label="周四" :value="4"/>
            <el-option label="周五" :value="5"/>
            <el-option label="周六" :value="6"/>
            <el-option label="周日" :value="7"/>
          </el-select>
          <el-time-select v-model="time.time" style="width: 200px; margin: 0 10px"/>
          <el-button type="danger" @click="form.availableTimes.splice(index, 1)">删除</el-button>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
.time-slot {
  display: flex;
  align-items: center;
  margin-top: 10px;
}
</style>