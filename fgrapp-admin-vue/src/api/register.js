import request from '@/utils/request'

// 发送验证码
export function sendMessage(phone) {
  return request({
    url: '/register/'+phone,
    method: 'get'
  })
}

// 注册登录接口
export function phoneRegister(data) {
  return request({
    url: '/register/phone',
    method: 'post',
    data: data
  })
}
