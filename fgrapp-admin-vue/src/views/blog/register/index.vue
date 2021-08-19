<template>
  <div class="login">
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
  </div>
</template>

<script>
    import {sendMessage} from "@/api/register";

    export default {
        name: "index",
      data(){
          return{
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
              ]
            },
          }
      },
      methods:{
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
                this.$message.success("注册成功且登录成功")
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
