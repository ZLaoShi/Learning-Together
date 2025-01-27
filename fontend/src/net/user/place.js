import { get } from '../index'

/**
 * 获取场地列表
 * @param success 成功回调
 */
export function getPlaceList(success) {
    get('/api/place/', success)
}