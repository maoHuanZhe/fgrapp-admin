const app = getApp()
import {search} from "../../api/admin"
import {formatTime} from "../../utils/util"
Page({
  data: {
    //加载条变量
    loadProgress:0,
    PageCur: 'index',
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    Custom: app.globalData.Custom,
    ContentHeight:app.globalData.ContentHeight,
    queryParam:{
      pageNum: 1,
      pageSize: 10,
      title: "",
      order:"createTime-desc"
    },
    list:[],
    total:0
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
  NavChange(e) {
    app.bar(e.currentTarget.dataset.cur);
  },
  searchFocus(){
    wx.navigateTo({
      url: '/pages/index/search/index',
    })
  },
  loadMore(){
    if(this.data.list.length < this.data.total){
      //未加载完
      this.setData({
        "queryParam.pageNum": this.data.queryParam.pageNum+1
      })
      this.getList();
    }
  },
  async getList(){
    await search(this.data.queryParam).then(({data})=>{
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
  async onLoad() {
    this.loadProgress();
    await this.getList();
    this.closeProgress();
  },
  onReady() {
  }
})