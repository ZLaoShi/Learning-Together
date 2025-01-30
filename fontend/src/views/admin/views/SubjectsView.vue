<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSubjectList, createSubject, updateSubject, deleteSubject } from '@/net/admin/subject'

const subjects = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = ref({
  id: '',
  name: '',
  status: 1
})

// 加载科目列表
function loadSubjects() {
  loading.value = true
  getSubjectList((data) => {
    subjects.value = data
    loading.value = false
  })
}

// 打开新建对话框
function openCreate() {
  isEdit.value = false
  form.value = { name: '', status: 1 }
  dialogVisible.value = true
}

// 打开编辑对话框  
function openEdit(subject) {
  isEdit.value = true
  form.value = { ...subject }
  dialogVisible.value = true
}

// 提交表单
function handleSubmit() {
  const action = isEdit.value ? updateSubject : createSubject
  action(form.value, () => {
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    loadSubjects()
  })
}

// 删除科目
function handleDelete(id) {
  ElMessageBox.confirm('确定要删除该科目吗?', '提示', {
    type: 'warning'
  }).then(() => {
    deleteSubject(id, () => {
      ElMessage.success('删除成功')
      loadSubjects()
    })
  })
}

onMounted(() => {
  loadSubjects()
})
</script>

<template>
  <div class="subject-container">
    <div class="header">
      <h2>科目管理</h2>
      <el-button type="primary" @click="openCreate">新建科目</el-button>
    </div>

    <el-table :data="subjects" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" width="180" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button-group>
            <el-button type="primary" size="small" @click="openEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small"
              :disabled="scope.row.status === 0"
              @click="handleDelete(scope.row.id)">
              删除
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑/新建对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑科目' : '新建科目'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="状态">
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
.subject-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>