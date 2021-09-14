// 获取应用实例
import {request} from "../lib/wxp"
import {encrypt} from "../utils/jsencrypt"
// 微信登陆
export function login(query) {
  return request({
    url: '/weChat/miniLogin',
    method: 'post',
    data: query
  })
}
// 密码登录接口
export function loginOfPassword(data) {
  data.password = encrypt(data.password);
  return request({
    url: '/weChat/login/password',
    method: 'post',
    data: data
  })
}
// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}
// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}