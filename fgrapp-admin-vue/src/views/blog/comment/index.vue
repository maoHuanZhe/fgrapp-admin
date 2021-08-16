<template>
  <!-- 评论模块 -->
  <div>
    <div>
      <!--      输入评论    -->
      <el-input maxlength="1024"
                show-word-limit
                type="textarea"
                placeholder="说点什么"
                v-model="addForm.content">
      </el-input>
      <el-row type="flex" justify="end">
        <el-button @click="handClick">发表评论</el-button>
      </el-row>
    </div>
    <!--   已存在评论模块   -->
    <comment-item v-for="item in list" :key="item.id" :item="item" />
  </div>
</template>

<script>
  import commentItem from "@/views/blog/comment/commentItem";
  import { addComment } from "@/api/blog";
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
        this.list = this.commentList.filter(item=> {
          let flag = item.parentId === 0;
          if (flag){
            let childArr = this.getChildArr(item.id,this.commentList);
            if (childArr.length > 0){
              item.childArr = childArr;
            }
          }
          return flag;
        })
      },
      methods:{
        getChildArr: function (id, data) {
          //查找子集
          return data.filter(chiledItem => {
            let flag = id === chiledItem.parentId;
            if (flag){
              let childArr = this.getChildArr(chiledItem.id,data);
              if (childArr.length > 0){
                chiledItem.childArr = childArr;
              }
            }
            return flag;
          })
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
