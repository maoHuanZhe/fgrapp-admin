<template>
  <div>
    <svg-icon icon-class="blog" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'blog',
  data() {
    return {
      url: 'https://blog.fgrapp.com'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
