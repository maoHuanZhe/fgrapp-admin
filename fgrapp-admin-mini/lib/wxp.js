import {
  promisifyAll
} from 'miniprogram-api-promise';

//const URL_BASE = 'http://localhost:9658'
const URL_BASE = 'https://server.fgrapp.com:9658'
const wxp = {
  URL_BASE
}
promisifyAll(wx, wxp)

 export function request(args) {
    args.url = URL_BASE + args.url;
    args.header = {}
    const token = wx.getStorageSync('token')
    if (token) {
      args.header['Admin-Token'] = 'Bearer ' +token // 让每个请求携带自定义token 请根据实际情况自行修改
    }
  return new Promise(function(resolve, reject) {
    wxp.request(args)
    .then((response)=>{
      resolve(response.data);
    })
    .catch(function (reason) {
      console.log('reason', reason)
      reject(reason)
    })
  })
}
export function requestOnLogin(args) {
  let token = wx.getStorageSync('token')
  if (!token){
    return new Promise((resolve, reject)=>{
      let pageStack = getCurrentPages()
      if (pageStack && pageStack.length > 0) {
        //跳转到登录页面
        wx.navigateTo({
          url: "/pages/register/index"
        })
        getApp().globalEvent.once("loginSuccess", ()=>{
          request(args).then(res=>{
            resolve(res)
          }, err=>{
            console.log('err', err);
            reject(err)
          })
        })
      }else{
        reject('page valid err')
      }
    })
  }
  return request(args)
}
export default wxp