// 帖子相关

import { get, post, put, del } from '../index'

/**
 * 获取帖子列表 
 * @param success 成功回调
 */
export function getPostList(success) {
    get('/api/post/', success)
}

/**
 * 获取帖子详情
 * @param id 帖子ID
 * @param success 成功回调
 */
export function getPostById(id, success) {
    get(`/api/post/${id}`, success)
}

/**
 * 发布帖子
 * @param data {title:标题, content:内容, type:类型(help/resource), subjectId:科目ID, placeId:场地ID, timeSlot:时间段}
 * @param success 成功回调
 */
export function createPost(data, success) {
    post('/api/post/', data, success)
}

/**
 * 更新帖子
 * @param data {id:帖子ID, title:标题, content:内容, type:类型, subjectId:科目ID, placeId:场地ID, timeSlot:时间段}
 * @param success 成功回调
 */
export function updatePost(data, success) {
    put('/api/post/', data, success)
}

/**
 * 删除帖子(实际是关闭)
 * @param id 帖子ID
 * @param success 成功回调
 */
export function deletePost(id, success) {
    del(`/api/post/${id}`, success)
}