// app.js
import Event from './lib/event'
App({
  // 引入`towxml3.0`解析方法
  towxml: require('/towxml/index'),
  globalEvent: (wx.globalEvent = new Event()),
  onLaunch() {
    wx.getSystemInfo({
      success: e => {
        this.globalData.ContentHeight = e.windowHeight-55-17-84-49;
        this.globalData.StatusBar = e.statusBarHeight;
        let capsule = wx.getMenuButtonBoundingClientRect();
        if (capsule) {
          this.globalData.Custom = capsule;
          this.globalData.CustomBar = capsule.bottom + capsule.top - e.statusBarHeight;
        } else {
          this.globalData.CustomBar = e.statusBarHeight + 50;
        }
      }
    })
    //请求分类列表缓存
    wx.removeStorageSync('classList')
  },
  bar(url){
    wx.redirectTo({
      url: '/pages/'+url+'/index'
    })
    // wx.navigateTo({
    //   url: '/pages/'+url+'/index'
    // })
    // wx.redirectTo({
    //   url: '/pages/'+url+'/index'
    // })
    // wx.reLaunch({
    //   url: '/pages/'+e.currentTarget.dataset.cur+'/index',
    // })
  },
  globalData: {
    bgColor:"bg-white"
  }
})
