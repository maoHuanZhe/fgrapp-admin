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
    <el-card shadow="hover"  v-for="item in commentList" :key="item.id">
      <div>
        <span>{{item.createAt}}</span>
        {{item.createTime}}
      </div>
      <div style="margin:10px 0;">
        {{item.content}}
      </div>
      <div style="background-color: slategrey;padding: 10px;color:#eee;margin: 2px 0;"
           v-for="childItem in item.childArr" :key="childItem.id">
        {{childItem.createAt}}: {{childItem.content}}
      </div>
    </el-card>
  </div>
</template>

<script>
  import {addComment} from "@/api/topic";
  import {mapGetters} from "vuex";
  import store from "@/store";
    export default {
        name: "index",
      props:{
        topicId: {
          type: String
        },
        commentList:{
          type: Array
        }
      },
      data(){
          return {
            addForm:{
              topicId:undefined,
              parentId:undefined,
              content:'',
            },
            list:[]
          }
      },
      created(){
        this.addForm.topicId = this.topicId;
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
