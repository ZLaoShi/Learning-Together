// 场地管理

import {get, post, put} from '../index'

/**
 * 获取场地列表
 * @param success 成功回调
 */
export function getPlaceList(success) {
    get('/api/place/', success)
}

/**
 * 获取单个场地
 * @param id 场地ID
 * @param success 成功回调
 */
export function getPlaceById(id, success) {
    get(`/api/place/${id}`, success)  
}

/**
 * 创建场地
 * @param data {name:场地名称, location:位置, availableTime:可用时间}
 * @param success 成功回调
 */
export function createPlace(data, success) {
    post('/api/place/', data, success)
}

/**
 * 更新场地
 * @param data {id:场地ID, name:名称, location:位置, availableTime:可用时间, status:状态}
 * @param success 成功回调
 */
export function updatePlace(data, success) {
    put('/api/place/', data, success)
}
