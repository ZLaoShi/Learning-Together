<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllAccounts, createAccount, updateAccount, deleteAccount, resetPassword } from '@/net/admin/account'

const accounts = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

const form = ref({
  username: '',
  password: '',
  role: 'user'
})

// 加载用户列表
function loadAccounts() {
  loading.value = true
  getAllAccounts((data) => {
    accounts.value = data
    loading.value = false
  })
}

// 新建/编辑用户
function handleAccount() {
  if(isEdit.value) {
    updateAccount(form.value, () => {
      ElMessage.success('更新成功')
      dialogVisible.value = false
      loadAccounts()
    })
  } else {
    createAccount(form.value, () => {
      ElMessage.success('创建成功')
      dialogVisible.value = false
      loadAccounts()
    })
  }
}

// 打开编辑对话框
function openEdit(account) {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  form.value = {...account}
  dialogVisible.value = true
}

// 打开新建对话框
function openCreate() {
  isEdit.value = false
  dialogTitle.value = '新建用户'
  form.value = {username: '', password: '', role: 'user'}
  dialogVisible.value = true
}

// 重置密码
function handleResetPassword(id) {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    resetPassword(id, value, () => {
      ElMessage.success('密码重置成功')
    })
  })
}

// 删除用户
function handleDelete(id) {
  ElMessageBox.confirm('确定要删除该用户吗?会一并删除所有相关的匹配,待处理请求等等记录', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteAccount(id, () => {
      ElMessage.success('删除成功')
      loadAccounts()
    })
  })
}

onMounted(() => {
  loadAccounts()
})
</script>

<template>
  <div class="account-container">
    <div class="header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="openCreate">新建用户</el-button>
    </div>

    <el-table 
      :data="accounts" 
      v-loading="loading"
      style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="180" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.role === 'admin' ? 'danger' : ''">
            {{ scope.row.role }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间" width="180" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button-group>
            <el-button type="primary" size="small" @click="openEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="warning" size="small" @click="handleResetPassword(scope.row.id)">
              重置密码
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
              删除
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑/新建对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="isEdit"/>
        </el-form-item>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input v-model="form.password" type="password"/>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="普通用户" value="user"/>
            <el-option label="管理员" value="admin"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAccount">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.account-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>