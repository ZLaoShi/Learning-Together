// 数据统计

import { get, post } from '../index'

/**
 * 获取用户活跃度统计
 * @param success 成功回调
 */
export function getUserActivityStats(success) {
    get('/api/post/admin/stats', success)
}

/**
 * 获取操作统计
 * @param success 成功回调
 */
export function getOperationStats(success) {
    get('/api/post/admin/stats', success)
}

/**
 * 导出统计数据
 * @param success 成功回调, 返回blob数据
 */
export function exportStats(success) {
    get('/api/post/admin/stats/export', success, 'blob')
}