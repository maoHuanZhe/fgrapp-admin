<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'blog',
  data() {
    return {
      url: '/blog'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
