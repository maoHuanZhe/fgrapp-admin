<template>
  <div>
    <svg-icon icon-class="topic" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'topic',
  data() {
    return {
      url: '/topic'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
