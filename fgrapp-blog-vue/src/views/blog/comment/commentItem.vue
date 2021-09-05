<template>
  <el-card shadow="hover">
    <div>
      {{item.createAt}}
    </div>
    <div>
      {{item.content}}
    </div>
    <div>
      {{item.createTime}}
    </div>
    <div>
      <el-button @click="handParentClick(item)">回复</el-button>
    </div>
    <div style="margin: 10px;" v-if="item.show">
      <!--      输入评论    -->
      <el-input maxlength="1024"
                show-word-limit
                type="textarea"
                placeholder="说点什么"
                v-model="addForm.content">
      </el-input>

      <el-row type="flex" justify="end">
        <el-button @click="handClick(item)">发表回复</el-button>
      </el-row>
    </div>
    <comment-item v-for="childItem in item.childArr" :key="childItem.id" :item="childItem" />
  </el-card>
</template>

<script>
  import Vue from 'vue'
  import {addComment} from "@/api/blog";
    export default {
      name: "commentItem",
      props:{
          item:{
            type:Object
          }
      },
      data(){
        return {
          addForm:{
            blogId:undefined,
            parentId:undefined,
            content:'',
          },
        }
      },
      methods:{
        handParentClick(item){
          Vue.set(item,'show',true);
        },
        handClick(item){
          this.addForm.blogId = item.blogId;
          this.addForm.parentId = item.id;
          if (this.addForm.content){
            addComment(this.addForm).then(()=>{
              this.msgSuccess("评论提交审核成功");
              this.addForm.parentId = undefined;
              this.addForm.content = '';
              item.show = false;
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
