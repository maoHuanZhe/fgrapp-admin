<template>
  <div>
    <h1>博客首页</h1>
    <h1>博客首页</h1>
    <transition name="fade-transform" mode="out-in">
      <keep-alive>
        <router-view />
      </keep-alive>
    </transition>
  </div>
</template>

<script>
    export default {
        name: "index",
        created(){
          document.title = "FGRAPP-Blog"
        }
    }
</script>

<style scoped>

</style>
