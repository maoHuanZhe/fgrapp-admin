<template>
  <div>
    <div style="height: 340px;">
      <el-tabs v-model="activeName"  @tab-click="handleClick">
        <el-tab-pane label="密码登录" name="pass">
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                auto-complete="off"
                placeholder="密码"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                v-model="loginForm.code"
                auto-complete="off"
                placeholder="验证码"
                style="width: 63%"
                @keyup.enter.native="handleLogin"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" alt="验证码"/>
              </div>
            </el-form-item>
            <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
            <el-form-item style="width:100%;">
              <el-button
                :loading="RegisterLoading"
                size="medium"
                type="primary"
                style="width:100%;"
                @click.native.prevent="handleLogin"
              >
                登 录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="手机登录" name="phone">
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form">
            <el-form-item prop="phone">
              <el-input v-model="registerForm.phone" type="text" auto-complete="off" placeholder="请输入你的手机号码">
                <template slot="prepend">中国大陆+86</template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                v-model="registerForm.code"
                type="text"
                auto-complete="off"
                placeholder="请输入验证码"
              >
                <el-button slot="append" :loading="buttonText !=='获取验证码'" @click="getMessageCode">
                  {{buttonText}}
                </el-button>
              </el-input>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button
                :loading="RegisterLoading"
                size="medium"
                type="primary"
                style="width:100%;"
                @click.native.prevent="handRegister"
              > 登录/注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="邮箱登录" name="email">
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form">
            <el-form-item prop="email">
              <el-input v-model="registerForm.email" type="text" auto-complete="off" placeholder="请输入你的邮箱">
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                v-model="registerForm.code"
                type="text"
                auto-complete="off"
                placeholder="请输入验证码"
              >
                <el-button slot="append" :loading="buttonText !=='获取验证码'" @click="getMessageCodeOfEmail">
                  {{buttonText}}
                </el-button>
              </el-input>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button
                :loading="RegisterLoading"
                size="medium"
                type="primary"
                style="width:100%;"
                @click.native.prevent="handRegisterOfEmail"
              > 登录/注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div>未注册手机验证后自动登录</div>
  </div>

</template>

<script>
    import {sendMessage,sendEmailMessage} from "@/api/register";
    import {getCodeImg} from "@/api/login";
    import Cookies from "js-cookie";
    import {decrypt, encrypt} from "@/utils/jsencrypt";

    export default {
        name: "index",
      data(){
          return{
            suffix:'1',
            activeName:"phone",
            buttonText:'获取验证码',
            RegisterLoading:false,
            registerForm: {
              phone:"",
              code:""
            },
            registerRules: {
              code: [
                { required: true, trigger: "blur", message: "验证码不能为空" }
              ],
              phone: [
                { required: true, trigger: "blur", message: "手机号不能为空" }
              ],
              email: [
                { required: true, trigger: "blur", message: "邮箱不能为空" }
              ]
            },
            codeUrl: "data:image/gif;base64,",
            cookiePassword: "",
            loginForm: {
              username: "admin",
              password: "admin123",
              rememberMe: true,
              code: "",
              uuid: ""
            },
            loginRules: {
              username: [
                { required: true, trigger: "blur", message: "用户名不能为空" }
              ],
              password: [
                { required: true, trigger: "blur", message: "密码不能为空" }
              ],
              code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
            },
          }
      },
      methods:{
        getMessageCodeOfEmail(){
          if (this.registerForm.email.length > 6) {
            sendEmailMessage(this.registerForm.email).then(() => {
              this.buttonText = 60;
              let intervalID =
                window.setInterval(()=>{
                  setTimeout(()=>{
                    if (this.buttonText){
                      this.buttonText = this.buttonText - 1;
                    } else {
                      this.buttonText = '获取验证码';
                      clearInterval(intervalID)
                    }
                  },0)
                },1000)
            });
          }
        },
        handleClick(tab, event) {
          if (this.activeName ==='pass'){
            this.getCode();
          }
        },
        getCode() {
          getCodeImg().then(res => {
            this.captchaOnOff = res.captchaOnOff === undefined ? true : res.captchaOnOff;
            if (this.captchaOnOff) {
              this.codeUrl = "data:image/gif;base64," + res.data.img;
              this.loginForm.uuid = res.data.uuid;
            }
          });
        },
        getCookie() {
          const username = Cookies.get("username");
          const password = Cookies.get("password");
          const rememberMe = Cookies.get('rememberMe')
          this.loginForm = {
            username: username === undefined ? this.loginForm.username : username,
            password: password === undefined ? this.loginForm.password : decrypt(password),
            rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
          };
        },
        handleLogin() {
          this.$refs.loginForm.validate(valid => {
            if (valid) {
              this.cookiePassword = this.loginForm.password;
              this.loginForm.password = encrypt(this.loginForm.password);
              this.loading = true;
              if (this.loginForm.rememberMe) {
                Cookies.set("username", this.loginForm.username, { expires: 30 });
                Cookies.set("password", this.loginForm.password, { expires: 30 });
                Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
              } else {
                Cookies.remove("username");
                Cookies.remove("password");
                Cookies.remove('rememberMe');
              }
              this.$store.dispatch("Login", this.loginForm).then(() => {
                this.$message.success("登录成功")
                this.$router.go(0)//页面重新刷新
              }).catch(() => {
                this.loading = false;
                this.loginForm.password = this.cookiePassword;
                if (this.captchaOnOff) {
                  this.getCode();
                }
              });
            }
          });
        },
        getMessageCode(){
          if (this.registerForm.phone.length === 11){
            sendMessage(this.registerForm.phone).then(()=>{
              this.buttonText = 60;
              let intervalID =
                window.setInterval(()=>{
                  setTimeout(()=>{
                    if (this.buttonText){
                      this.buttonText = this.buttonText - 1;
                    } else {
                      this.buttonText = '获取验证码';
                      clearInterval(intervalID)
                    }
                  },0)
                },1000)
            })
          } else {
            this.$message.warning("请输入正确的手机号")
          }
        },
        handRegister(){
          this.$refs.registerForm.validate(valid => {
            if (valid) {
              this.RegisterLoading = true;
              this.$store.dispatch("PhoneRegister", this.registerForm).then(() => {
                this.open = false;
                this.RegisterLoading = false;
                this.$message.success("注册成功")
                this.$router.go(0)//页面重新刷新
              }).catch(() => {
                this.RegisterLoading = false;
              });
            }
          });
        },
        handRegisterOfEmail(){
          this.$refs.registerForm.validate(valid => {
            if (valid) {
              this.RegisterLoading = true;
              this.$store.dispatch("EmailRegister", this.registerForm).then(() => {
                this.open = false;
                this.RegisterLoading = false;
                this.$message.success("注册成功")
                this.$router.go(0)//页面重新刷新
              }).catch(() => {
                this.RegisterLoading = false;
              });
            }
          });
        },
      }
    }
</script>

<style scoped>

</style>
