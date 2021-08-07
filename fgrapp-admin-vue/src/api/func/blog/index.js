import request from '@/utils/request'
// 新增博客
export function add(data) {
  return request({
    url: '/func/blog/save',
    method: 'post',
    data: data
  })
}
// 查询列表
export function page(query) {
  return request({
    url: '/func/blog/page',
    method: 'get',
    params: query
  })
}
