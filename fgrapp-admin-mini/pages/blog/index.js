// index.js
// 获取应用实例
const app = getApp()
import {page} from "../../api/blog"
import {formatTime} from "../../utils/util"
Page({
  data: {
    queryParam:{
      pageNum: 1,
      pageSize: 10,
      classId: -1,
      getClassList:true
    },
    list:[],
    total:0,
    //导航栏变量
    TabCur: -1,
    //主要分类列表
    mainList:[],
    //全部分类列表
    allList:[],
    //加载条变量
    CustomBar: app.globalData.CustomBar,
    loadProgress:0,
    //分类抽屉变量
    drawerShow:false,
    PageCur: 'blog',
    ContentHeight:app.globalData.ContentHeight,
    refresher:false,
    loading:true
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
  showAll(){
    this.setData({
      drawerShow:true
    })
  },
  hideModal(){
    this.setData({
      drawerShow:false
    })
  },
  setClassList(data){
    let mainList = data.filter(item=> item.isMain);
    mainList.unshift({
      name:'最近',
      id:-1
    })
    this.setData({
      mainList,
      allList:data
    })
  },
  //博客Card点击事件 进入详情页面 且将博客编号传递过去
  cardClick(e){
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: "/pages/blog/detail/detail",
      success: function (res) {
        // 通过eventChannel向被打开页面传送数据
        res.eventChannel.emit('acceptDataFromOpenerPage', {
          data: dataset.id
        })
      }
    })
  },
  //导航栏点击事件
  tabSelect(e) {
    const dataset = e.currentTarget.dataset;
    let queryParam={
      pageNum: 1,
      pageSize: 10,
      classId: dataset.id
    }
    this.setData({
      TabCur: dataset.id,
      queryParam,
      list:[]
    })
    this.hideModal();
    this.getList();
  },
  async getList(){
    const that = this;
    let param = {
      pageNum: this.data.queryParam.pageNum,
      pageSize: this.data.queryParam.pageSize,
      getClassList:this.data.queryParam.getClassList
    }
    if(this.data.queryParam.classId > 0){
      param.classId = this.data.queryParam.classId;
    }
    await page(param).then(({data})=>{
      let classList = data.classList;
      if(classList){
        wx.setStorageSync('classList', classList)
        this.setClassList(classList)
      }
      data = data.page;
      data.records.forEach(item=>{
        if (item.classNames){
          item.classNames = item.classNames.split(",")
        }
        item.createTime = formatTime(item.createTime)
      })
      let list = this.data.list.concat(data.records);
      that.setData({
        list,
        total:data.total,
        loading:false
      })
    })
  },
  loadMore: function () {
    if(this.data.list.length < this.data.total){
      //未加载完
      this.setData({
        "queryParam.pageNum": this.data.queryParam.pageNum+1
      })
      this.getList();
    }
  },
  async onLoad() {
    this.loadProgress();
    let classList = wx.getStorageSync('classList');
    if(classList){
      this.setData({
        'queryParam.getClassList':false
      })
      //分类列表已存在
      this.setClassList(classList);
    }
    await this.getList();
    this.closeProgress();
  },
  async refresher(){
    this.setData({
      'queryParam.pageNum':1,
      list:[],
      refresher:true
    })
    await this.getList();
    this.setData({
      refresher:false
    })
  }
})
