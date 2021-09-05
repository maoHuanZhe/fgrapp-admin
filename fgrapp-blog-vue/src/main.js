import Vue from 'vue'

import Element from 'element-ui'

import 'element-ui/lib/theme-chalk/index.css';

Vue.use(Element);
import App from './App'
import store from './store'
import router from './router'

import './assets/icons' // icon
import { formatTime } from "@/utils";

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// use
Vue.use(mavonEditor)

// 全局方法挂载
Vue.prototype.parseTime = formatTime

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
