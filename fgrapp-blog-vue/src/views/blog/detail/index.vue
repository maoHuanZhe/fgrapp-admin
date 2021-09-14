<template>
  <div class="mavonEditor">
    <el-backtop :visibility-height="0"  :bottom="150">
      <div style="    width: 50px;">
        <div @click.stop="switchDefilePre" class="rightDiv">
          <svg-icon  icon-class="left"/>
        </div>
        <div @click.stop="likeClike" class="rightDiv">
          <svg-icon  :icon-class="operateNum.canLike?'like':'likefill'"/>
        </div>
        <div  @click.stop="toComment" class="rightDiv">
          <svg-icon icon-class='message'/>
        </div>
        <div @click.stop>
          <el-dropdown trigger="click" @command="handleCommand" class="rightDiv" style="font-size: 20px;color: #409EFF;">
            <span>
            <svg-icon icon-class='list'/>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="index" v-for="(name,index) in form.addClassNames" :key="'dropdown-'+index">{{name}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div @click.stop="topicClick" class="rightDiv" style="color: #1989fa;">
          题
        </div>
        <div @click.stop="switchDefileNext" class="rightDiv">
          <svg-icon  icon-class="right"/>
        </div>
        <div class="rightDiv" style="color: #1989fa;">
          UP
        </div>
      </div>
    </el-backtop>
    <h1 style="    font-size: 28px;
    word-wrap: break-word;
    color: #222226;
    font-weight: 600;
    margin: 0;
    word-break: break-all;">{{form.title}}</h1>
    <div style="background: #f8f8f8;border-radius: 10px;padding: 5px 20px;
    margin-top: 10px;">
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
        <el-link @click="toComment" type="info" :underline="false" style="margin-right: 10px;">
          <svg-icon icon-class='message'/>
          {{commentNum}} 评论</el-link>
      </div>
      <div style="color: #999aaa;padding-top: 5px;">
        <!--   分类专栏   -->
        <span style="margin-right: 5px;">分类专栏:</span>
        <el-tag v-for="name in form.addClassNames" :key="name" size="mini" style="margin-right: 10px;">{{name}}</el-tag>
      </div>
    </div>
    <mavon-editor style="margin: 10px 0;"
      v-model="form.content"
      defaultOpen = "preview"
      :toolbarsFlag = "false"
      :subfield = "false"
    />
    <!--  评论模块  -->
    <compent ref="comment" :blogId="blogId" :commentList="commentList" />
    <class-list :drawerTitle="drawerTitle" :drawer="drawer" :isSort="false" :classId="classId" @beforeClose="handleClose"/>
  </div>
</template>

<script>
  import { updateLickNum,getDetailInfo } from "@/api/blog";
  import Compent from "@/views/blog/comment/index"
  import {mapGetters} from "vuex";
  import store from "@/store";
  import ClassList from "@/components/class/ClassList";
  export default {
    components:{
      Compent,
      ClassList
    },
    computed: {
      ...mapGetters([
        'token',
      ])
    },
    data() {
      return {
        classId:undefined,
        drawer: false,
        drawerTitle:'',
        isActive:false,
        loading: false,
        commentNum: 0,
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
      this.getInfo(id);
    },
    methods:{
        switchDefilePre(){
            if (this.index === 0){
                return this.$message.warning("已经是分类内第一篇了")
            }
            this.getInfo(this.ids[this.index-1])
        },
        switchDefileNext(){
            if (this.index === (this.ids.length -1)){
                return this.$message.warning("已经是分类内最后一篇了")
            }
            this.getInfo(this.ids[this.index+1])
        },
        getInfo(id){
            this.blogId = id;
            getDetailInfo(id).then(({data}) => {
                this.ids = data.ids;
                this.index= data.ids.indexOf(data.blog.id);
                this.form = data.blog;
                this.commentList = this.getRootList(data.commentDos);
                this.commentNum = data.commentDos.length;
                this.operateNum = data.operateNum;
            })
        },
        topicClick(){
            window.open('https://topic.fgrapp.com/class/'+this.form.classIds[0])
        },
      handleCommand(command) {
          this.classId = this.form.classIds[command];
        this.drawerTitle = this.form.addClassNames[command];
          this.drawer = true;
      },
      handleClose(){
        this.drawer = false;
      },
      toComment(){
        scrollTo({'top':this.$refs.comment.$el.offsetTop-20,
          behavior: "smooth"});
        this.$nextTick(()=>{
          this.$refs.comment.$refs.commentInput.focus();
        })
      },
      getRootList(list){
        return list.filter(item=> {
          let flag = item.parentId === 0;
          if (flag){
            let childArr = this.getChildArr(item.id,list);
            if (childArr.length > 0){
              item.childArr = childArr;
            }
          }
          return flag;
        })
      },
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
      likeClike(){
        //判断用户是否登陆 登陆后才能参与点赞评论
        if (this.token){
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
            this.msgSuccess("操作成功");
            //是否点赞状态改变
            this.operateNum.canLike = !this.operateNum.canLike;
            //点赞数改变
            this.operateNum.likeNum += num;
          })
        } else {
          store.commit('SET_SHOWREGISTER', true)
        }
      }
    }
  };
</script>

<style scoped>
  .rightDiv{
    height: 100%;
    width: 100%;
    background-color: #f2f5f6;
    box-shadow: 0 0 6px rgba(0,0,0, .12);
    text-align: center;
    line-height: 40px;
  }
  .mavonEditor {
    max-width: 1140px;
    width: 100%;
    height: 100%;
    background: #fff;
    border-radius: 10px;
    padding: 16px;
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
