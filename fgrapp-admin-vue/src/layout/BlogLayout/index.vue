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
            <el-menu-item v-for="item in mainOptions" :index="item.id+''" :key="item.id">{{item.name}}</el-menu-item>
            <el-submenu index="0">
              <template slot="title">其它分类</template>
              <el-menu-item v-for="item in otherOptions" :index="item.id+''" :key="item.id">{{item.name}}</el-menu-item>

            </el-submenu>
          </el-menu>
        </div>

        <el-link v-if="name === ''" @click="loginOrReg" :underline="false">登录/注册</el-link>
        <el-dropdown v-else class="avatar-container right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar" class="user-avatar">
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <span>{{ name }}</span>
            </el-dropdown-item>
            <el-dropdown-item v-if="roles.length" divided  @click.native="goto">
              <span>进入后台</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-row>
    </el-header>

    <el-main class="main">
      <transition name="fade-transform" mode="out-in">
          <router-view />
      </transition>
    </el-main>
    <el-footer style="text-align: center;background-color: #fff;">
      <!--      csdn      -->
      <el-tooltip class="item" effect="dark" content="CSDN地址" placement="top">
        <el-link :underline="false" type="info" href="https://blog.csdn.net/qq_39016934" target="_blank">
          <svg-icon icon-class='csdn'/></el-link>
      </el-tooltip>
      <!--      github      -->
      <el-tooltip class="item" effect="dark" content="GitHub地址" placement="top">
        <el-link :underline="false" type="info" href="https://github.com/maoHuanZhe" target="_blank">
          <svg-icon icon-class='github'/></el-link>
      </el-tooltip>
      <!--      qq      -->
      <el-tooltip class="item" effect="dark" content="QQ联系" placement="top">
        <el-link :underline="false" type="info" href="http://sighttp.qq.com/authd?IDKEY=5b0c5a703ad65f10c06e40a8f2169b3cd74487c67322f867" target="_blank">
          <svg-icon icon-class='qq'/></el-link>
      </el-tooltip><br>
      <!--      备案号      -->
      <el-tooltip class="item" effect="dark" content="备案号" placement="top">
        <el-link :underline="false" type="info" href="http://beian.miit.gov.cn/" target="_blank">皖ICP备2020015593号</el-link>
      </el-tooltip><br>
      <el-link :underline="false" type="info" href="http://beian.miit.gov.cn/" target="_blank">Copyright © 2021 fgrapp.com All Rights Reserved.</el-link>
    </el-footer>
    <el-dialog :visible.sync="showRegister" :before-close="closeRegister" width="450px" append-to-body>
      <register/>
    </el-dialog>
  </el-container>
</template>

<script>
  import {list} from "@/api/func/class";
  import Logo from "@/layout/components/Sidebar/Logo";
  import Register from "@/views/blog/register";
  import {getToken} from "@/utils/auth";
  import store from "@/store";
  import {mapGetters} from "vuex";
    export default {
      name: "index",
      computed: {
        ...mapGetters([
          'name',
          'roles',
          'showRegister',
          'avatar'
        ]),
      },
      components: { Logo,Register },
      data(){
        return{
          userName: '',
          open:false,
          loading: false,
          mainOptions:[],
          otherOptions:[],
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
          let main = [];
          let other = [];
          data.forEach(item=>{
            if (item.isMain){
              main.push(item)
            }else {
              other.push(item)
            }
          })
          this.mainOptions = main;
          this.otherOptions = other;
        })
        //获取当前用户
        if (getToken()) {
          //已登录
            if (!store.getters.name) {
              //未获取用户信息  获取用户信息
              store.dispatch('GetInfo')
            }
        }
      },
      methods:{
        goto() {
          window.open("/admin")
        },
        async logout() {
          this.$confirm('确定注销并退出系统吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$store.dispatch('LogOut').then(()=>{
              this.$router.go(0)//页面重新刷新
            })
          })
        },
        loginOrReg(){
          //登陆或注册
          store.commit('SET_SHOWREGISTER', true)
        },
        closeRegister(){
          //登陆或注册
          store.commit('SET_SHOWREGISTER', false)
        },
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

<style rel="stylesheet/scss" lang="scss">
  .avatar-container {
    margin-right: 30px;

    .avatar-wrapper {
      margin-top: 5px;
      position: relative;

      .user-avatar {
        cursor: pointer;
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }

      .el-icon-caret-bottom {
        cursor: pointer;
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
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
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-size: cover;
  }
  .title {
    margin: 0 auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  .login-code {
    width: 33%;
    height: 38px;
    float: right;
    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }
  .el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial,serif;
    font-size: 12px;
    letter-spacing: 1px;
  }
  .login-code-img {
    height: 38px;
  }
</style>
