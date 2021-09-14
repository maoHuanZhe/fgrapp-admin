// 获取应用实例
import {request} from "../lib/wxp"

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/weChat/about',
    method: 'get'
  })
}
//审核博客评论
export function approveBlogComment(data) {
  return request({
    url: '/func/comment',
    method: 'put',
    data: data
  })
}

export function delBlogComment(ids) {
  return request({
    url: '/func/comment/' + ids,
    method: 'delete'
  })
}
//审核题目评论
export function approveTopicComment(data) {
  return request({
    url: '/func/topicComment',
    method: 'put',
    data: data
  })
}

export function delTopicComment(ids) {
  return request({
    url: '/func/topicComment/' + ids,
    method: 'delete'
  })
}

// 查询列表
export function search(query) {
  return request({
    url: '/weChat/about/search',
    method: 'get',
    data: query
  })
}