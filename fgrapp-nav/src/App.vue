<template>
  <div id="app">
    <el-container>
      <el-header>
        <div id="nav">
          <router-link to="/">主页</router-link> |
          <router-link to="/about">关于</router-link>
        </div>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
      <el-footer style="text-align: center;">
        <!--      csdn      -->
        <el-tooltip class="item" effect="dark" content="CSDN地址" placement="top">
          <el-link :underline="false" type="info" href="https://blog.csdn.net/qq_39016934" target="_blank"><i class="iconfont">&#xe601;</i></el-link>
        </el-tooltip>
        <!--      github      -->
        <el-tooltip class="item" effect="dark" content="GitHub地址" placement="top">
          <el-link :underline="false" type="info" href="https://github.com/maoHuanZhe" target="_blank"><i class="iconfont">&#xe600;</i></el-link>
        </el-tooltip>
        <!--      qq      -->
        <el-tooltip class="item" effect="dark" content="QQ联系" placement="top">
          <el-link :underline="false" type="info" href="http://sighttp.qq.com/authd?IDKEY=5b0c5a703ad65f10c06e40a8f2169b3cd74487c67322f867" target="_blank"><i class="iconfont">&#xe631;</i></el-link>
        </el-tooltip>
        <!--      weixin      -->
        <el-link :underline="false" type="info" href="http://beian.miit.gov.cn/" target="_blank">
          <el-popover
              placement="top-start"
              width="200"
              title="加我微信"
              trigger="hover">
            <el-image
                style="width: 200px; height: 200px"
                :src="require('./assets/wchat.jpg')"
                fit="fit"></el-image>
            <i slot="reference" class="iconfont">&#xe632;</i>
          </el-popover>
        </el-link><br>
        <!--      备案号      -->
        <el-tooltip class="item" effect="dark" content="备案号" placement="top">
          <el-link :underline="false" type="info" href="http://beian.miit.gov.cn/" target="_blank">皖ICP备2020015593号</el-link>
        </el-tooltip>
      </el-footer>
    </el-container>

  </div>
</template>
<style>
  @font-face {
    font-family: 'iconfont';  /* project id 2047640 */
    src: url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.eot');
    src: url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.eot?#iefix') format('embedded-opentype'),
    url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.woff2') format('woff2'),
    url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.woff') format('woff'),
    url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.ttf') format('truetype'),
    url('//at.alicdn.com/t/font_2047640_aq84d5tg0s4.svg#iconfont') format('svg');
  }
  .iconfont{
    font-family:"iconfont" !important;
    font-size:25px;font-style:normal;
    -webkit-font-smoothing: antialiased;
    -webkit-text-stroke-width: 0.2px;
    -moz-osx-font-smoothing: grayscale;
  }
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
