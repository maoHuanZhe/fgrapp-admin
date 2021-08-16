<template>
  <div class="mavonEditor">
    <div style="background: #f8f8f8;border-radius: 4px;padding: 5px;">
      <div style="color: #999aaa;">
        <!--   创作时间   -->
        <el-link type="info" :underline="false" style="margin-right: 10px;">
          {{ parseTime(form.createTime) }}</el-link>
        <!--   阅读数   -->
        <el-link type="info" :underline="false" style="margin-right: 10px;">
        <i class="el-icon-view"></i>
          {{operateNum.readNum}} 阅读</el-link>
        <!--   点赞数   -->
        <el-link :type="operateNum.canLike?'info':'danger'"
                 :underline="false" style="margin-right: 10px;"
                 @click="likeClike"
        ref="likeLink">
        <svg-icon icon-class='like'/>
          {{operateNum.likeNum}} 点赞</el-link>
        <!--   评论数   -->
        <el-link type="info" :underline="false" style="margin-right: 10px;">
        <i class="el-icon-chat-line-round"></i>
          {{commentList.length}} 评论</el-link>
      </div>
      <div style="color: #999aaa;padding-top: 5px;">
        <!--   分类专栏   -->
        <span style="margin-right: 5px;">分类专栏:</span>
        <el-tag v-for="name in form.addClassNames" size="mini" style="margin-right: 10px;">{{name}}</el-tag>
      </div>
    </div>
    <h1 style="font-size:200%;text-align: center">{{form.title}}</h1>
    <mavon-editor
      v-model="form.content"
      defaultOpen = "preview"
      :toolbarsFlag = "false"
      :subfield = "false"
    />
    <!--  评论模块  -->
    <compent :blogId="blogId" :commentList="commentList" />
  </div>
</template>

<script>
  import { updateLickNum,getDetailInfo } from "@/api/blog";
  import Compent from "@/views/blog/comment/index"
  export default {
    components:{
      Compent
    },
    data() {
      return {
        loading: false,
        commentList:[],
        operateNum: {
          readNum:0,
          likeNum:0,
          canLike:true,
        },
        blogId:undefined,
        form:{
          content:"",
          title:"",
          imgUrl:""
        }
      };
    },
    created(){
      const id = this.$route.params && this.$route.params.blogId;
      this.blogId = id;
      getDetailInfo(id).then(({data}) => {
        this.form = data.blog;
        this.commentList = data.commentDos;
        this.operateNum = data.operateNum;
      })
    },
    methods:{
      likeClike(){
        //判断用户是否登陆 登陆后才能参与点赞评论
        //判断当前用户是否已经点赞，
        let num = 0;
        if (this.operateNum.canLike){
          //之前没有点赞 现在将点赞数加一
          num = 1;
        } else {
          //之前已经点赞， 现在取消点赞 点赞数减一
          num = -1
        }
        updateLickNum(this.blogId,num).then(()=>{
          //是否点赞状态改变
          this.operateNum.canLike = !this.operateNum.canLike;
          //点赞数改变
          this.operateNum.likeNum += num;
        })
      }
    }
  };
</script>

<style scoped>
  .mavonEditor {
    width: 100%;
    height: 100%;
    background: #fff;
    padding: 0 24px 16px;
  }
  ::-moz-selection {
    background: #26a69a;
    color: #ffffff;
  }
  ::selection {
    background: #26a69a;
    color: #ffffff;
  }
  .markdown-body ::-moz-selection {
    background: #26a69a;
    color: #ffffff;
  }
  .markdown-body ::selection {
    background: #26a69a;
    color: #ffffff;
  }
</style>
