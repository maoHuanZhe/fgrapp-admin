import request from '@/utils/request'

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId) {
  return request({
    url: '/monitor/online/' + tokenId,
    method: 'delete'
  })
}
// 封禁用户
export function forceDisable(tokenId,time) {
  return request({
    url: '/monitor/online/' + tokenId+'/'+time,
    method: 'delete'
  })
}
