<template>
  <el-container ref="container" style="overflow: auto;" class="container" >
    <el-backtop></el-backtop>
    <el-header class="site-nav" height="50px">
      <el-row type="flex" justify="center">
        <logo style="width: 150px;" :collapse="false" />
        <div class="site-nav-bd">
          <el-menu
            background-color="#f5f5f5"
            text-color="#303133"
            active-text-color="#409EFF"
            :default-active="defaultActive"
            mode="horizontal"
            @select="handleSelect">
            <el-menu-item index="home">主页</el-menu-item>
            <el-menu-item v-for="item in classOptions" :index="item.id+''" :key="item.id">{{item.name}}</el-menu-item>
            <el-submenu index="0">
              <template slot="title">其它分类</template>
            </el-submenu>
          </el-menu>
        </div>

        <el-link :underline="false">登录/注册</el-link>
      </el-row>
    </el-header>

    <el-main class="main">
      <transition name="fade-transform" mode="out-in">
          <router-view />
      </transition>
    </el-main>
  </el-container>
</template>

<script>
  import {list} from "@/api/func/class";
  import Logo from "@/layout/components/Sidebar/Logo";
    export default {
      name: "index",
      components: { Logo },
      data(){
        return{
          classOptions:[],
          defaultActive:'home'
        }
      },
      created(){
        document.title = "FGRAPP-Blog";
        // 获取分类编号
        const classId = this.$route.params && this.$route.params.classId;
        if (classId){
          this.defaultActive = classId + '';
        }
        //获取分类列表
        list().then(({data})=>{
          this.classOptions = data;
        })
      },
      methods:{
          handleSelect(key, keyPath) {
            if (key === 'home'){
              return this.$router.push("/blog");
            }
            if (this.$route.params.classId !== key){
              this.$router.push("/blog/class/" + parseInt(key));
            }
          }
        }
    }
</script>

<style scoped>
  .site-nav {
    width: 100%;
    background-color: #f5f5f5;
    border-bottom: 1px solid #eee;
    *zoom: 1;
  }
  .site-nav .site-nav-bd {
    width: 900px;
    height: 50px;
    background: #f5f5f5;
    -webkit-backface-visibility: hidden;
    *zoom: 1;
  }
  .container {
    color: #3C3C3C;
    -webkit-font-smoothing: antialiased;
    background: #fff url('../../../src/assets/images/backgroud.png') repeat-y 0 36px;
    background-size: cover;
  }
  .main{
    width: 1190px;
    margin: auto;
  }
</style>
