import { get } from '../index'

/**
 * 获取科目列表
 * @param success 成功回调
 */
export function getSubjectList(success) {
    get('/api/subject/', success)
}