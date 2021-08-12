<template>
  <div class="mavonEditor">
    <el-input
      type="text"
      placeholder="请输入标题(5~100个字)"
      v-model="form.title"
      minlength="5"
      maxlength="100"
      show-word-limit
    >
    </el-input>
      <mavon-editor
        v-model="form.content"
        defaultOpen = "preview"
        :toolbarsFlag = "false"
        :subfield = "false"
      />
  </div>
</template>
<script>
  import { getInfo } from "@/api/func/blog";
  export default {
    data() {
      return {
        loading: false,
        form:{
          content:"",
          title:"",
          imgUrl:""
        }
      };
    },
    created(){
      const id = this.$route.params && this.$route.params.blogId;
      getInfo(id).then(({data}) => {
        this.form = data;
      })
    },
    methods:{
    }
  };
</script>

<style scoped>
  .mavonEditor {
    width: 100%;
    height: 100%;
  }
</style>
