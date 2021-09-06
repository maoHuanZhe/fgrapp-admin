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
    children: [
      {
        path: '',
        component: (resolve) => require(['@/views/blog/list'], resolve)
      },
      {
        path: 'class/:classId',
        component: (resolve) => require(['@/views/blog/list'], resolve)
      },
      {
        path: 'detail/:blogId',
        component: (resolve) => require(['@/views/blog/detail/index'], resolve)
      }
    ]
  },
  {
    path: '*',
    redirect: '/',
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})
router.beforeEach((to, from, next) => {
  NProgress.start()
  next();
});
router.afterEach(() => {
  NProgress.done()
})
export default router
