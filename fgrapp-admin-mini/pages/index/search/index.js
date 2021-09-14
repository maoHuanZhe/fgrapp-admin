// pages/index/search/index.js
import {search} from "../../../api/admin"
import {formatTime} from "../../../utils/util"
Page({

  /**
   * 页面的初始数据
   */
  data: {
    title:"",
    queryParam:{
      pageNum: 1,
      pageSize: 10,
      title: "",
      order:"createTime-desc"
    },
    list:[],
    total:0
  },
  handleConfirm(){
    this.setData({
      list:[],
      "queryParam.title":this.data.title,
      "queryParam.pageNum":1
    })
    this.getList();
  },
  getList(){
    search(this.data.queryParam).then(({data})=>{
      data.records.forEach(item=>{
        item.createTime = formatTime(item.createTime)
      })
      let list = this.data.list.concat(data.records);
      this.setData({
        list,
        total:data.total
      })
    })
  },
  cardClick(e){
    const dataset = e.currentTarget.dataset;
    let url = "/pages/topic/detail/index";
    if(dataset.type === 1){
      url = "/pages/blog/detail/detail";
    }
    wx.navigateTo({
      url,
      success: function (res) {
        // 通过eventChannel向被打开页面传送数据
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          data: dataset.id
        })
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    if(this.data.list.length < this.data.total){
      //未加载完
      this.setData({
        "queryParam.pageNum": this.data.queryParam.pageNum+1
      })
      this.getList();
    }
  },
})