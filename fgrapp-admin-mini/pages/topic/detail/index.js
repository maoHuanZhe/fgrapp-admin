// pages/detail/detail.js
import {getDetailInfo,updateLickNum,updateCollectNum} from "../../../api/topic"
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {    
    //加载条变量
    loadProgress:0,
    CustomBar:app.globalData.CustomBar,
    showShare: false,
    options: [
        { name: '微信', icon: 'wechat',openType:'share' },
        { name: '分享海报', icon: 'poster' }
    ],
    show:false,
    article:'',
    //信息
    info:undefined,
    //评论数量
    commentNum: 0,
    //评论列表
    commentList:[],
    //操作数量
    operateNum: {
      readNum:0,
      likeNum:0,
      collectNum:0,
      canLike:true,
      canCollect:true
    }
  },
  switchDetailNext(){
    let index = this.data.index+1;
    let ids = this.data.ids;
    if(index === ids.length){
      wx.showToast({
        title: '已经是最后一题了',
      })
    } else {
      this.getInfo(ids[index])
    }
  },
  switchDetailPer(){
    let index = this.data.index;
    let ids = this.data.ids;
    if(index === 0){
      wx.showToast({
        title: '已经是第一题了',
      })
    } else {
      this.getInfo(ids[index-1])
    }
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
  addShow(){
    this.setData({
      showShare:true
    });
  },  
  onClose() {
    this.setData({ showShare: false });
  },
  onSelect(event) {
    this.onClose();
  },
  showClick(){
    this.setData({
      show: !this.data.show
    })
  },
  //点赞操作
  likeClick(){
    wx.showLoading({
      title: '请求中',
    })
    const that = this;
    //判断当前用户是否已经点赞，
    let num = 0;
    if (this.data.operateNum.canLike){
      //之前没有点赞 现在将点赞数加一
      num = 1;
    } else {
      //之前已经点赞， 现在取消点赞 点赞数减一
      num = -1
    }
    updateLickNum(this.data.info.id,num).then(({data})=>{
      wx.hideLoading();
      if (that.data.operateNum.canLike){
        //之前没有点赞 现在将点赞数加一
        wx.showToast({
          title: '点赞成功',
          icon: 'none',
        })
      } else {
        //之前已经点赞， 现在取消点赞 点赞数减一
        wx.showToast({
          title: '取消点赞成功',
          icon: 'none',
        })
      }
      that.setData({
        'operateNum':data
      })
    })
  },
  //收藏操作
  collectClick(){
    wx.showLoading({
      title: '请求中',
    })
    const that = this;
    //判断当前用户是否已经点赞，
    let num = 0;
    if (this.data.operateNum.canCollect){
      //之前没有点赞 现在将点赞数加一
      num = 1;
    } else {
      //之前已经点赞， 现在取消点赞 点赞数减一
      num = -1
    }
    updateCollectNum(this.data.info.id,num).then(({data})=>{
      wx.hideLoading();
      if (that.data.operateNum.canCollect){
        //之前没有点赞 现在将点赞数加一
        wx.showToast({
          title: '收藏成功',
          icon: 'none',
        })
      } else {
        //之前已经点赞， 现在取消点赞 点赞数减一
        wx.showToast({
          title: '取消收藏成功',
          icon: 'none',
        })
      }
      that.setData({
        'operateNum':data
      })
    })
  },
  //评论操作
  addCommentModel(){
    const that = this;
    wx.navigateTo({
      url: '/pages/addComment/index',
      success: function (res) {
        // 通过eventChannel向被打开页面传送数据
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          type:'topic',
          id:that.data.info.id
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(options.id){
      return this.getInfo(options.id)
    }
    const that = this;
    const eventChannel = this.getOpenerEventChannel()
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('acceptDataFromOpenerPage', function (data) {
      that.getInfo(data.data);
    })
  },
  getInfo(id){
    this.loadProgress();
    getDetailInfo(id).then(({data})=>{
      let ids = data.ids;
      let index = ids.indexOf(data.topic.id);
      let result = app.towxml(data.topic.answer,'markdown',{
        //base:'https://xxx.com',				// 相对资源的base路径
        theme:'light',					// 主题，默认`light`
        events:{					// 为元素绑定的事件方法
          tap:(e)=>{
            console.log('tap',e);
          }
        }
      });
      this.setData({
        ids,
        index,
        article:result,
        info:data.topic,
        operateNum:data.operateNum,
        commentList:this.getRootList(data.commentDos),
        commentNum:data.commentDos.length
      })
      this.closeProgress();
    })
  },
  getRootList(list){
    return list.filter(item=> {
      let flag = item.parentId === 0;
      if (flag){
        let childArr = this.getChildArr(item.id,list);
        if (childArr.length > 0){
          item.childArr = childArr;
        }
      }
      return flag;
    })
  },
  getChildArr: function (id, data) {
    //查找子集
    return data.filter(chiledItem => {
      let flag = id === chiledItem.parentId;
      if (flag){
        let childArr = this.getChildArr(chiledItem.id,data);
        if (childArr.length > 0){
          chiledItem.childArr = childArr;
        }
      }
      return flag;
    })
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
  onPullDownRefresh: function () {

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
    let info= this.data.info;
    return {
      title: info.problem,
      path: '//pages/topic/detail/index?id='+info.id
    }
  }
})