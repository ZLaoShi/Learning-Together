// 用户管理

import { get, post, put, del } from '../index'

/**
 * 获取所有用户列表
 * @param success 成功回调
 */
export function getAllAccounts(success) {
    get('/api/admin/account/', success)
}

/**
 * 创建新用户
 * @param data {username:用户名, password:密码, role:角色}
 * @param success 成功回调
 */
export function createAccount(data, success) {
    post('/api/admin/account/', data, success)
}

/**
 * 更新用户信息
 * @param data {id:用户ID, username:用户名, role:角色}
 * @param success 成功回调
 */
export function updateAccount(data, success) {
    put('/api/admin/account/', data, success)
}

/**
 * 删除用户
 * @param id 用户ID
 * @param success 成功回调
 */
export function deleteAccount(id, success) {
    del(`/api/admin/account/${id}`, success)
}

/**
 * 重置用户密码
 * @param id 用户ID
 * @param newPassword 新密码
 * @param success 成功回调
 */
export function resetPassword(id, newPassword, success) {
    // 修改为查询参数形式
    post(`/api/admin/account/reset-password?id=${id}&newPassword=${newPassword}`, {}, success)
}