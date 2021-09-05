<template>
  <div>
    <el-empty v-if="list.length === 0" :image-size="200"></el-empty>
    <ul v-else
        v-infinite-scroll="load"
        infinite-scroll-disabled="disabled">
      <li
        style="list-style-type:none;"
        v-for="item in list"
        @click="toDeatil(item.id)"
        :key="item.id">
        <el-card shadow="hover">
          <div style="font-size: 18px;
    font-weight: 500;
    line-height: 24px;
    color: #222226;
    overflow: hidden;
    white-space: normal;
    word-break: break-word;
    display: -webkit-box;">
            {{item.problem}}
          </div>
          <div style="margin-top: 8px;">
            <el-tag v-for="(name,index) in item.classNames" :key="index +'class'+item.id" size="mini" style="margin-right: 10px;">{{name}}</el-tag>
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
            <!--   收藏数   -->
            <span style="margin-right: 10px;">
        <svg-icon icon-class='rate'/>
          {{item.collectNum}} 收藏</span>
            <!--   评论数   -->
            <span style="margin-right: 10px;">
          <svg-icon icon-class='message'/>
          {{item.commentNum}} 评论</span>
          </div>
        </el-card>
      </li>
    </ul>
  </div>
</template>

<script>
    import {page} from "@/api/topic";

    export default {
        name: "list",
      data(){
        return{
          // 查询参数
          queryParams: {
            pageNum: 1,
            pageSize: 10,
            title: undefined,
            classId:undefined
          },
          list:[],
          total:0,
          loading:false
        }
      },
      watch: {
        $route: {
          handler: function(val, oldVal){
            this.queryParams.classId = val.params.classId;
            this.queryParams.pageNum = 1;
            this.list = [];
            this.getList();
          }
        }
      },
      computed: {
        noMore () {
          return this.total === this.list.length
        },
        disabled () {
          return this.loading || this.noMore
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
          load () {
            this.loading = true
            this.queryParams.pageNum++;
            this.getList();
          },
          getList(){
            //获取博客列表
            page(this.queryParams).then(({data})=>{
              data.records.forEach(item=>{
                if (item.classNames){
                  item.classNames = item.classNames.split(",")
                }
              })
              this.list = this.list.concat(data.records);
              this.total = data.total;
              this.loading = false;
            })
          },
        toDeatil(id){
          this.$router.push("/detail/" + id);
        }
      }
    }
</script>

<style scoped>

</style>
