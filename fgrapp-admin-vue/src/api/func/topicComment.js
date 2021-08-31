import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/func/topicComment/page',
    method: 'get',
    params: query
  })
}
export function list() {
  return request({
    url: '/func/topicComment/list',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: '/func/topicComment',
    method: 'put',
    data: data
  })
}

export function dels(ids) {
  return request({
    url: '/func/topicComment/' + ids,
    method: 'delete'
  })
}
