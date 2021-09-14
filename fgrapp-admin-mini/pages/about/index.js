// pages/about/index.js
import {getInfo,approveBlogComment,approveTopicComment,delBlogComment,delTopicComment} from "../../api/admin"
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    //加载条变量
    loadProgress:0,
    PageCur: 'about',
    ContentHeight:app.globalData.ContentHeight,
    CustomBar:app.globalData.CustomBar,
    //用户信息
    userInfo:undefined,
    //角色信息
    roles:[],
    //博客待审批评论
    blogComments:[],
    //题目待审批评论
    topicComments:[],
    //操作数据
    operateNum:{
      collectNum:0,
      likeNum:0,
      commentNum:0
    },
    //分类抽屉变量
    drawerShow:false,
    //标识是博客评论还是题目评论
    drawerType:''
  },
  // 加载方法-开始
  loadProgress(){
    let intervalId = setInterval(() => {
      let num = parseInt((100 - this.data.loadProgress)/2)
      this.setData({
        loadProgress: this.data.loadProgress+num
      })
    }, 100);
    this.setData({
      intervalId
    })
  },
  // 加载方法-结束
  closeProgress(){
    clearInterval(this.data.intervalId);
    this.setData({
      loadProgress:100
    })
    wx.nextTick(()=>{
      this.setData({
        loadProgress: 0
      })
    });
  },
  CopyLink(e) {
    wx.setClipboardData({
      data: e.currentTarget.dataset.link,
      success: res => {
        wx.showToast({
          title: '已复制',
          duration: 1000,
        })
      }
    })
  },
  //博客评论通过
  blogApprove(e){
    const id = e.currentTarget.dataset.commentid;
    approveBlogComment({id,blogId:e.currentTarget.dataset.blogid
      ,isAudit: 1}).then(()=>{
      wx.showToast({
        title: '审核成功',
      })
      this.removeBlogComments(id);
    })
  },
  blogDelete(e){
    const id = e.currentTarget.dataset.commentid;
    delBlogComment(id).then(()=>{
      wx.showToast({
        title: '删除成功',
      })
      this.removeBlogComments(id);
    })
  },
  removeBlogComments(id){
    let arr = this.data.blogComments;
    for(let i = 0;i<arr.length;i++){
      if(id === arr[i].id){
        arr.splice(i,1);
      }
    }
    this.setData({
      blogComments:arr
    })
  },
  //题目评论通过
  topicApprove(e){
    const id = e.currentTarget.dataset.commentid;
    approveTopicComment({id,topicId:e.currentTarget.dataset.topicid
      ,isAudit: 1}).then(()=>{
      wx.showToast({
        title: '审核成功',
      })
      this.removeTopicComments(id);
    })
  },
  topicDelete(e){
    const id = e.currentTarget.dataset.commentid;
    delTopicComment(id).then(()=>{
      wx.showToast({
        title: '删除成功',
      })
      this.removeTopicComments(id);
    })
  },
  removeTopicComments(id){
    let arr = this.data.topicComments;
    for(let i = 0;i<arr.length;i++){
      if(id === arr[i].id){
        arr.splice(i,1);
      }
    }
    this.setData({
      topicComments:arr
    })
  },
  // ListTouch触摸开始
  ListTouchStart(e) {
    this.setData({
      ListTouchStart: e.touches[0].pageX
    })
  },

  // ListTouch计算方向
  ListTouchMove(e) {
    this.setData({
      ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
    })
  },

  // ListTouch计算滚动
  ListTouchEnd(e) {
    if (this.data.ListTouchDirection =='left'){
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    } else {
      this.setData({
        modalName: null
      })
    }
    this.setData({
      ListTouchDirection: null
    })
  },
  NavChange(e) {
    app.bar(e.currentTarget.dataset.cur);
  },
  toBlog(){
    this.showAll('blog')
  },
  toTopic(){
    this.showAll('topic')
  },
  showAll(type){
    this.setData({
      drawerType:type,
      drawerShow:true
    })
  },
  hideModal(){
    this.setData({
      drawerShow:false
    })
  },
  //用户信息模块点击事件
  infoClick(){
    let url = "";
    if(this.data.userInfo){
      //用户已登录 跳转进详情模块
      url = "/pages/about/detail/index";
    } else {
      //未登录 跳转进登录模块
      url = "/pages/register/index";
    }
    wx.navigateTo({
      url
    })
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

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getUser();
  },

  async getUser(){
    this.loadProgress();
    const that = this;
    const token = wx.getStorageSync('token');
    if(token){
      //token存在 说明已经登录
      //获取用户信息
      await getInfo().then(({data})=>{
        that.setData({
          userInfo:data.user,
          roles:data.roles,
          blogComments:data.blogComments,
          topicComments:data.topicCommentDos,
          operateNum:{
            collectNum:data.collect,
            likeNum:data.likeNum,
            commentNum:data.commentNum
          }
        })
      })
    }
    this.closeProgress();
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: async function () {
    await this.getUser();
    wx.stopPullDownRefresh()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})