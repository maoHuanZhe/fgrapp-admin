<template>
  <!-- 评论模块 -->
  <div>
    <div style="margin: 10px;">
      <!--      输入评论    -->
      <el-input ref="commentInput" maxlength="1024"
                show-word-limit
                type="textarea"
                placeholder="说点什么"
                v-model="addForm.content"
                @focus="checkLogin">
      </el-input>
      <el-row type="flex" justify="end">
        <el-button @click="handClick">发表评论</el-button>
      </el-row>
    </div>
    <!--   已存在评论模块   -->
    <comment-item v-for="item in commentList" :key="item.id" :item="item" />
  </div>
</template>

<script>
  import commentItem from "@/views/blog/comment/commentItem";
  import {addComment} from "@/api/blog";
  import {mapGetters} from "vuex";
  import store from "@/store";
    export default {
        name: "index",
      components:{
        commentItem
      },
      props:{
        blogId: {
          type: String
        },
        commentList:{
          type: Array
        }
      },
      data(){
          return {
            addForm:{
              blogId:undefined,
              parentId:undefined,
              content:'',
            },
            list:[]
          }
      },
      created(){
        this.addForm.blogId = this.blogId;
      },
      computed: {
        ...mapGetters([
          'token',
        ]),
      },
      methods:{
        checkLogin(){
          //判断用户是否登陆 登陆后才能参与点赞评论
          if (!this.token){
            store.commit('SET_SHOWREGISTER', true)
          }
        },
        handClick(){
          if (this.addForm.content){
            addComment(this.addForm).then(()=>{
              this.msgSuccess("评论提交审核成功");
              this.addForm.parentId = undefined;
              this.addForm.content = '';
            })
          } else {
            return this.$message({
              message: '请输入评论内容',
              type: 'warning'
            });
          }
        }
      }
    }
</script>

<style scoped>

</style>
