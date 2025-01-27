// 帖子管理

import { get, put } from '../index'

/**
 * 获取所有帖子(包括已关闭)
 * @param success 成功回调
 */
export function getAllPosts(success) {
    get('/api/post/admin/post', success)
}

/**
 * 更新帖子状态
 * @param id 帖子ID
 * @param status 状态(0待审核,1已发布,2已关闭)
 * @param success 成功回调
 */
export function updatePostStatus(id, status, success) {
    put('/api/post/admin/status', {
        id: id,
        status: status
    }, success)
}

/**
 * 获取帖子统计数据
 * @param success 成功回调
 */
export function getPostStats(success) {
    get('/api/post/admin/stats', success)
}

/**
 * 导出帖子统计数据
 * @param success 成功回调
 */
export function exportPostStats(success) {
    get('/api/post/admin/stats/export', success, 'blob')  // 指定返回类型为blob
}