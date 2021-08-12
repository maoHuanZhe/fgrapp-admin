import request from '@/utils/request'
// 新增博客
export function add(data) {
  return request({
    url: '/func/blog',
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
// 查询详细
export function getInfo(id) {
  return request({
    url: '/func/blog/' + id,
    method: 'get'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/func/blog/',
    method: 'put',
    data: data
  })
}

// 删除
export function del(ids) {
  return request({
    url: '/func/blog/' + ids,
    method: 'delete'
  })
}
