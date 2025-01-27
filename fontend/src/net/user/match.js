// 匹配相关

import { get, post } from '../index'

/**
 * 获取用户匹配记录
 * @param success 成功回调
 */
export function getUserMatches(success) {
    get('/api/match/user', success)
}

/**
 * 获取待确认的匹配请求
 * @param success 成功回调
 */
export function getPendingMatches(success) {
    get('/api/match/pending', success)
}

/**
 * 结对匹配
 * @param success 成功回调
 */
export function matchBySubject(success) {
    get('/api/match/subject', success)
}

/**
 * 按时间匹配
 * @param success 成功回调
 */
export function matchByTime(success) {
    get('/api/match/time', success)
}

/**
 * 按地点匹配
 * @param success 成功回调
 */
export function matchByPlace(success) {
    get('/api/match/place', success)
}

/**
 * 按互补匹配
 * @param success 成功回调
 */
export function matchByComplement(success) {
    get('/api/match/complement', success)
}

/**
 * 确认匹配
 * @param id 匹配记录ID
 * @param success 成功回调
 */
export function confirmMatch(id, success) {
    post(`/api/match/confirm/${id}`, {}, success)
}

/**
 * 拒绝匹配
 * @param id 匹配记录ID
 * @param success 成功回调
 */
export function rejectMatch(id, success) {
    post(`/api/match/reject/${id}`, {}, success)
}

/**
 * 完成匹配
 * @param id 匹配记录ID
 * @param success 成功回调
 */
export function completeMatch(id, success) {
    post(`/api/match/complete/${id}`, {}, success)
}