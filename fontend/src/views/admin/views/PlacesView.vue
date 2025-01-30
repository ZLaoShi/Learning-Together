<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPlaceList, createPlace, updatePlace } from '@/net/admin/place'

const places = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = ref({
  id: '',
  name: '',
  location: '',
  status: 1,
  availableTime: [{
    weekday: 1,
    time: '08:00-18:00'
  }]
})

function loadPlaces() {
  loading.value = true
  getPlaceList((data) => {
    places.value = data
    loading.value = false
  })
}

function addTimeSlot() {
  form.value.availableTime.push({
    weekday: 1,
    time: '08:00-18:00'
  })
}

function removeTimeSlot(index) {
  form.value.availableTime.splice(index, 1)
}

function openCreate() {
  isEdit.value = false
  form.value = {
    name: '',
    location: '',
    status: 1,
    availableTime: [{
      weekday: 1,
      time: '08:00-18:00'
    }]
  }
  dialogVisible.value = true
}

function openEdit(place) {
  isEdit.value = true
  form.value = {...place}
  dialogVisible.value = true
}

function handleSubmit() {
  const action = isEdit.value ? updatePlace : createPlace
  action(form.value, () => {
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadPlaces()
  })
}

onMounted(() => {
  loadPlaces()
})
</script>

<template>
  <div class="place-container">
    <div class="header">
      <h2>场地管理</h2>
      <el-button type="primary" @click="openCreate">新建场地</el-button>
    </div>

    <el-table :data="places" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" width="150" />
      <el-table-column prop="location" label="位置" width="180" />
      <el-table-column label="可用时间" min-width="200">
        <template #default="scope">
          <div v-for="time in scope.row.availableTime" :key="time.weekday">
            周{{ time.weekday }} {{ time.time }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button-group>
            <el-button type="primary" size="small" @click="openEdit(scope.row)">
              编辑
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑/新建对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑场地' : '新建场地'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location"/>
        </el-form-item>
        <el-form-item label="可用时间">
          <div v-for="(time, index) in form.availableTime" :key="index" class="time-slot">
            <el-select v-model="time.weekday" style="width: 120px">
              <el-option v-for="i in 7" :key="i" :label="`周${i}`" :value="i"/>
            </el-select>
            <el-input v-model="time.time" placeholder="如:08:00-18:00" style="width: 200px; margin: 0 10px"/>
            <el-button type="danger" size="small" @click="removeTimeSlot(index)" :disabled="form.availableTime.length === 1">
              删除
            </el-button>
          </div>
          <el-button type="primary" size="small" @click="addTimeSlot">
            添加时间段
          </el-button>
        </el-form-item>
        <el-form-item label="状态" v-if="isEdit">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.place-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.time-slot {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
</style>