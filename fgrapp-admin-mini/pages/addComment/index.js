// pages/addComment/index.js
import {addComment} from "../../api/topic"
import {addComment as addBlog} from "../../api/blog"

Page({

  /**
   * 页面的初始数据
   */
  data: {
    content:"",
    id:"",
    type:""
  },
  onContentChange(event) {
    this.setData({
      content: event.detail
    })
  },
  addCommentCommit(){
    const content = this.data.content;
    //校验评论内容
    if (content){
      if(this.data.type ==="topic"){
        this.addTopicComment(content);
      } else {
        this.addBlogComment(content);
      }
    } else {
      wx.showToast({
        title: '请输入评论内容',
        icon: 'none',
      })
    }
  },
  addTopicComment(content){
    addComment({
      topicId:this.data.id,
      content
    }).then(()=>{
      wx.navigateBack({
        delta: 1,
        success(){
          wx.showToast({
            title: '评论提交审核成功',
            icon: 'none',
          })
        }
      })
    })
  },
  addBlogComment(content){
    addBlog({
      blogId:this.data.id,
      content
    }).then(()=>{
      wx.navigateBack({
        delta: 1,
        success(){
          wx.showToast({
            title: '评论提交审核成功',
            icon: 'none',
          })
        }
      })
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const eventChannel = this.getOpenerEventChannel()
    const that = this;
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('acceptDataFromOpenerPage', function (data) {
      const id = data.id;
      const type = data.type;
      that.setData({
        id,
        type
      })
    });
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

  }
})