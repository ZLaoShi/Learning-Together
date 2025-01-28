// 用户画像

import { get, post } from '../index'

/**
 * 获取用户画像
 * @param success 成功回调
 */
export function getUserProfile(success) {
    get('/api/profile/', success)
}

/**
 * 根据用户名查询用户画像
 * @param username 用户名
 * @param success 成功回调
 */
export function getUserProfileByUsername(username, success) {
    get(`/api/profile/${username}`, success)
}

/**
 * 更新用户画像
 * @param data {realName:真实姓名, className:班级, phone:电话, preferredPlaces:偏好场地[], availableTimes:空闲时间[]}
 * @param success 成功回调
 */
export function updateProfile(data, success) {
    post('/api/profile/', data, success)
}

/**
 * 更新用户科目绑定
 * @param goodSubjects 擅长的科目ID数组
 * @param needSubjects 需要帮助的科目ID数组
 * @param success 成功回调
 */
export function updateSubjects(goodSubjects, needSubjects, success) {
    post('/api/profile/subjects', {
        goodSubjects: goodSubjects,
        needSubjects: needSubjects
    }, success)
}