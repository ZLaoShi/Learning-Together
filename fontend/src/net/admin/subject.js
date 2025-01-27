// 科目管理

import { get, post, put } from '../index'

/**
 * 获取科目列表
 * @param success 成功回调
 */
export function getSubjectList(success) {
    get('/api/subject/', success)
}

/**
 * 获取单个科目
 * @param id 科目ID
 * @param success 成功回调
 */
export function getSubjectById(id, success) {
    get(`/api/subject/${id}`, success)
}

/**
 * 创建科目
 * @param data {name: 科目名称}
 * @param success 成功回调
 */
export function createSubject(data, success) {
    post('/api/subject/', data, success)
}

/**
 * 更新科目
 * @param data {id: 科目ID, name: 科目名称, status: 状态}
 * @param success 成功回调
 */
export function updateSubject(data, success) {
    put('/api/subject/', data, success)
}

/**
 * 删除科目 (实际是更新状态为禁用)
 * @param id 科目ID
 * @param success 成功回调
 */
export function deleteSubject(id, success) {
    put('/api/subject/', { id, status: 0 }, success)
}