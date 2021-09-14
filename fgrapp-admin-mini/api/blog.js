// 获取应用实例
import {request} from "../lib/wxp"

// 查询列表
export function page(query) {
  return request({
    url: '/page/list',
    method: 'get',
    data: query
  })
}
// 查询详细
export function getDetailInfo(id) {
  return request({
    url: '/page/detail/' + id,
    method: 'get'
  })
}
// 博客点赞操作
export function updateLickNum(id,num) {
  return request({
    url: '/page/operateNum/' + id+"/"+num,
    method: 'put'
  })
}
// 博客点赞操作
export function updateCollectNum(id,num) {
  return request({
    url: '/page/operateNum/collect/' + id+"/"+num,
    method: 'put'
  })
}
// 添加评论
export function addComment(data) {
  return request({
    url: '/page',
    method: 'post',
    data: data
  })
}