// pages/register/index.js
import {sendMessage,phoneRegister,sendEmailMessage,registerOfEmail} from "../../api/register"
import {loginOfPassword,login} from "../../api/login"

import wxp from '../../lib/wxp'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    show:true,
    phone:"",
    code:"",
    sms:"",
    email:"898365387@qq.com",
    username:"",
    password:"",
    //倒计时标志
    downing:false,
    //验证码发送次数
    sendTime: 0,
    TabCur: '1',
    //滑动验证码-嵌入式
    captchaOpt1: {
      baseUrl: wxp.URL_BASE,       //服务器前缀，默认：https://captcha.anji-plus.com/captcha-api
      mode : 'pop',                                             //弹出式pop，固定fixed, 默认：pop
      captchaType: "blockPuzzle",                                 //验证码类型：滑块blockPuzzle，点选clickWord，默认：blockPuzzle
      imgSize : {                                                 //底图大小, 默认值：{ width: '310px',height: '155px'}
        width: '295px',
        height: '147px',
      },
      barSize:{                                                   //滑块大小，默认值：{ width: '310px',height: '40px'}
        width: '295px',
        height: '32px',
      },
      vSpace: 5                                                   //底图和verify-bar-area间距，默认值：5像素
    }
  },
  getUserProfile(){
    const that = this;
    wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        wx.showLoading({
          title: '请求中',
          mask:true
        })
        wx.login({
          timeout: 5000,
          success(res2){
            login({
              code:res2.code,
              rawData:res.rawData,
              signature:res.signature,
              encrypteData:res.encrypteData,
              iv:res.iv
            }).then(({data})=>{
              that.loginSuccess(data);
            })
          }
        })

      }
    });
  },
  swichType(){
    this.setData({
      show:!this.data.show
    })
  },
  buttonSubmit(){
    const that = this;
    let TabCur = this.data.TabCur;
    if(TabCur === '3'){
      wx.showLoading({
        title: '请求中',
        mask:true
      })
      const email = this.data.email;
      const code = this.data.code;
      registerOfEmail({
        email,
        code
      }).then(({data})=>{
        that.loginSuccess(data);
      })
    } else if(TabCur === '2'){
      wx.showLoading({
        title: '请求中',
        mask:true
      })
      const phone = this.data.phone;
      const code = this.data.sms;
      phoneRegister({
        phone,
        code
      }).then(({data})=>{
        that.loginSuccess(data);
      })
    } else if (TabCur === '1'){
      this.showCaptcha();
    }
  },
  loginSuccess(data){
    wx.setStorageSync('token', data)
    getApp().globalEvent.emit('loginSuccess')
    wx.hideLoading()
    var pages = getCurrentPages();
    var route = pages[pages.length-2].route;
    if(route === "pages/topic/detail/index" 
    || route === "pages/blog/detail/index"){
      //如果是在博客或题库详情页跳转过来的 就跳转回详情页
      return wx.navigateBack({
        delta: 1
      })
    }
    var toIndex = 1;

    pages.forEach((item,index)=>{
      //防止从用户详情页面跳转进注册页面 找到我的页面 返回我的页面
      if(item.route === "pages/about/index"){
        toIndex = pages.length - index;
      }
    })
    wx.navigateBack({
      delta: toIndex
    })
  },
  /**
   * 弹出验证框
   */
  showCaptcha(){
    this.selectComponent('.demo1').show();
  },
  tabSelect(e) {
    this.setData({
      TabCur: e.currentTarget.dataset.id
    })
  },
  emailChange(event){
    this.setData({
      'registerForm.email':event.detail
    })
  },
  codeChange(event){
    this.setData({
      'registerForm.code':event.detail
    })
  },
  success(){
    const that = this;
    const cur = this.data.TabCur;
    
    console.log(cur);
    if(cur === '1'){
      //密码登陆
      const username = this.data.username;
      const password = this.data.password;
      loginOfPassword({
        username,password
      }).then(({data})=>{
        that.loginSuccess(data);
      })
    } else if(cur === '3'){
      sendEmailMessage(this.data.email).then(({data})=>{
        wx.showToast({
          title: '发送成功',
          icon: 'success',
          duration: 1000
        })
        that.setData({
          sendTime:that.data.sendTime+1,
          downing:true
        })
        that.selectComponent('.control-count-down').start()
      })
    } else if(cur === '2'){
      sendMessage(this.data.phone).then(({data})=>{
        wx.showToast({
          title: '发送成功',
          icon: 'success',
          duration: 1000
        })
        that.setData({
          sendTime:that.data.sendTime+1,
          downing:true
        })
        that.selectComponent('.control-count-down').start()
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.setData({
      'captchaOpt1.success':this.success
    })
  },
})