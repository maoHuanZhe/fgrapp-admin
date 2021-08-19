<template>
  <div>
    <el-card shadow="hover" v-for="item in list" @click.native="toDeatil(item.id)" :key="item.id">
        <div style="font-size: 18px;
    font-weight: 500;
    line-height: 24px;
    color: #222226;
    overflow: hidden;
    white-space: normal;
    word-break: break-word;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;">
          {{item.title}}</div>
      <div style="color: #555666;
    margin-top: 8px;
    line-height: 24px;
    overflow: hidden;
    white-space: normal;
    word-break: break-word;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;">
        {{item.summary}}
      </div>
      <div style="margin-top: 8px;">
        <el-tag v-for="name in item.classNames" size="mini" style="margin-right: 10px;">{{name}}</el-tag>
      </div>
      <div style="color: #555666;margin-top: 8px;">
        <!--   创作时间   -->
        <span style="margin-right: 10px;">{{ parseTime(item.createTime) }}</span>
        <!--   阅读数   -->
        <span style="margin-right: 10px;">
        <i class="el-icon-view"></i>
          {{item.readNum}} 阅读</span>
        <!--   点赞数   -->
        <span style="margin-right: 10px;">
        <svg-icon icon-class='like'/>
          {{item.likeNum}} 点赞</span>
        <!--   评论数   -->
        <span style="margin-right: 10px;">
          <svg-icon icon-class='message'/>
          {{item.commentNum}} 评论</span>
      </div>
    </el-card>
  </div>
</template>

<script>
    import {page} from "@/api/blog";

    export default {
        name: "list",
      data(){
        return{
          // 查询参数
          queryParams: {
            pageNum: 1,
            pageSize: 10,
            title: undefined,
            classId:undefined,
            order:'fb.id-desc'
          },
          list:[]
        }
      },
      watch: {
        $route: {
          handler: function(val, oldVal){
            this.queryParams.classId = val.params.classId;
            this.getList();
          }
        }
      },
      created(){
          // 获取分类编号
        const classId = this.$route.params && this.$route.params.classId;
        if (classId){
          this.queryParams.classId = classId;
        }
        this.getList();
      },
      methods:{
          getList(){
            //获取博客列表
            page(this.queryParams).then(({data})=>{
              this.list = data.records;
              this.list.forEach(item=>{
                if (item.classNames){
                  item.classNames = item.classNames.split(",")
                }
              })
              this.total = data.total;
            })
          },
        toDeatil(id){
          this.$router.push("/blog/detail/" + id);
        }
      }
    }
</script>

<style scoped>

</style>
