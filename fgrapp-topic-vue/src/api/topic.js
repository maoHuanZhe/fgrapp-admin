import request from '@/utils/request'
export function list() {
  return request({
    url: '/func/class/list',
    method: 'get'
  })
}
// 查询列表
export function page(query) {
  return request({
    url: '/page/topic/list',
    method: 'get',
    params: query
  })
}
// 查询详细
export function getInfo(id) {
  return request({
    url: '/page/topic/' + id,
    method: 'get'
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
  return request({
    url: '/page/topic/operateNum/' + id+"/"+num,
    method: 'put'
  })
}
// 博客点赞操作
export function updateCollectNum(id,num) {
  return request({
    url: '/page/topic/operateNum/collect/' + id+"/"+num,
    method: 'put'
  })
}
// 添加评论
export function addComment(data) {
  return request({
    url: '/page/topic',
    method: 'post',
    data: data
  })
}
