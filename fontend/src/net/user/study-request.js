// 学习请求

import { get, post } from '../index'

/**
 * 获取用户的学习请求列表
 * @param success 成功回调
 */
export function getUserRequests(success) {
    get('/api/study-request/', success)
}

/**
 * 获取待处理的学习请求
 * @param success 成功回调
 */
export function getPendingRequests(success) {
    get('/api/study-request/pending', success)
}

/**
 * 发起学习请求
 * @param data {postId:帖子ID, toUserId:接收者ID, message:留言, placeId:地点ID, timeSlot:时间段}
 * @param success 成功回调
 */
export function createRequest(data, success) {
    post('/api/study-request/', data, success)
}

/**
 * 接受学习请求
 * @param id 请求ID
 * @param success 成功回调
 */
export function acceptRequest(id, success) {
    post(`/api/study-request/accept/${id}`, {}, success)
}

/**
 * 拒绝学习请求
 * @param id 请求ID
 * @param success 成功回调
 */
export function rejectRequest(id, success) {
    post(`/api/study-request/reject/${id}`, {}, success)
}