<template>
  <div class="home">
    <el-row :gutter="20" type="flex" justify="center">
      <el-col :span="8" v-for="(card,index) in cardList" :key="'card'-index" @click.native="cardClick(card.systemUrl)">
        <el-card  shadow="hover" :body-style="{ padding: '0px' }">
          <img :src="card.imgSrc" class="image" :alt="card.title">
          <div style="padding: 14px;">
            <span>{{card.title}}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-carousel :interval="4000" type="card" height="200px">
      <el-carousel-item v-for="item in 6" :key="item">
        <h3 class="medium">广告位虚位以待</h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>

export default {
  name: 'Home',
    data() {
      return {
          cardList:[
              {imgSrc:require("../assets/fangao-4.jpg"),title:"博客",systemUrl:"https://blog.fgrapp.com"},
              {imgSrc:require("../assets/mengnalisha-2.jpg"),title:"题库",systemUrl:"https://topic.fgrapp.com"}
          ]
      }
    },
  methods: {
      cardClick(systemUrl){
          window.open(systemUrl)
      }
  }
}
</script>

<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
