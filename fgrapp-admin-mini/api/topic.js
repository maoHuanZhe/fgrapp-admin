// 获取应用实例
import {request,requestOnLogin} from "../lib/wxp"

// 查询列表
export function page(query) {
  return request({
    url: '/page/topic/list',
    method: 'get',
    data: query
  })
}
// 查询详细
export function getDetailInfo(id) {
  return request({
    url: '/page/topic/detail/' + id,
    method: 'get'
  })
}
// 博客点赞操作
export function updateLickNum(id,num) {
  return requestOnLogin({
    url: '/page/topic/operateNum/' + id+"/"+num,
    method: 'put'
  })
}
export function updateCollectNum(id,num) {
  return requestOnLogin({
    url: '/page/topic/operateNum/collect/' + id+"/"+num,
    method: 'put'
  })
}
// 添加评论
export function addComment(data) {
  return requestOnLogin({
    url: '/page/topic',
    method: 'post',
    data: data
  })
}