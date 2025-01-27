// 数据统计

import { get, post } from '../index'

/**
 * 获取用户活跃度统计
 * @param success 成功回调
 */
export function getUserActivityStats(success) {
    get('/api/admin/stats/user-activity', success)
}

/**
 * 获取操作统计
 * @param success 成功回调
 */
export function getOperationStats(success) {
    get('/api/admin/stats/operation', success)
}

/**
 * 清理过期日志
 * @param success 成功回调
 */
export function cleanOldLogs(success) {
    post('/api/admin/stats/clean-logs', {}, success)
}