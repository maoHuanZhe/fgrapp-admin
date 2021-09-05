import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/views/Layout'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
Vue.use(VueRouter)

const routes = [
  {
    path: '',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: (resolve) => require(['@/views/topic/list'], resolve)
      },
      {
        path: 'class/:classId',
        component: (resolve) => require(['@/views/topic/list'], resolve)
      },
      {
        path: 'detail/:topicId',
        component: (resolve) => require(['@/views/topic/detail/index'], resolve)
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  NProgress.start()
  window._hmt.push(['_trackPageview', '/#' + to.fullPath])
  next();
});
router.afterEach(() => {
  NProgress.done()
})
export default router
