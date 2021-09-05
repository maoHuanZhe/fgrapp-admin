<template>
  <el-drawer
    :title="drawerTitle"
    :visible.sync="drawer"
    :direction="direction"
    :before-close="beforeClose">
    <el-empty v-if="list.length === 0" :image-size="200"></el-empty>
    <div v-else>
      <ul style="margin-right: 40px;" ref="blogUl">
        <li @click="liClick(item.id)" :data-id="item.id" style="list-style-type:none;" v-for="item in list" :key="item.id">
          <el-card class="box-card">
            <div class="text item">
              {{item.title }}
            </div>
          </el-card>
        </li>
      </ul>
    </div>
  </el-drawer>

</template>

<script>
    import Sortable from "sortablejs";
    import {page,blogSort} from "@/api/blog";
    export default {
      name: "ClassList",
      props:{
        drawerTitle:{
          type:String
        },
        drawer:{
          type:Boolean
        },
        classId:{
          type:Number
        },
        isSort:{
          type:Boolean,
          default:true
        }
      },
      watch:{
        drawer:function (newVal){
          if (newVal){
              this.queryParams.classId = this.classId;
              this.getList();
          }
        }
      },
      data(){
          return {
            queryParams: {
              pageNum: 1,
              pageSize: 999,
              classId:undefined
            },
              direction:'ltr',
            list:[]
          }
      },
      methods:{
        liClick(id){
          if (!this.isSort){
            const pathId = this.$route.params && this.$route.params.blogId;
            if (id !== parseInt(pathId)){
              this.$router.push("/detail/" + id);
              this.$router.go(0);
            }
          }
        },
        getList(){
            //获取博客列表
            page(this.queryParams).then(({data})=>{
              this.list = data.records;
              const classId = this.classId;
              const that = this;
              this.$nextTick(()=>{
                if (this.isSort && this.list.length){
                  Sortable.create(this.$refs.blogUl, {
                    animation: 150,
                    ghostClass: 'blue-background-class',
                    group: "localStorage-example",
                    store: {
                      /**
                       * Save the order of elements. Called onEnd (when the item is dropped).
                       * @param {Sortable}  sortable
                       */
                      set: function (sortable) {
                        let arr = [];
                        sortable.toArray().forEach((item,index) =>{
                          arr.push({blogId:item,classId,sortNum:index})
                        })
                        blogSort(arr).then(()=>{
                          that.$message.success("排序成功")
                        });
                      }
                    }
                  });

                }
              })
            })
        },
        beforeClose(){
          this.$emit("beforeClose")
        }
      }
    }
</script>

<style scoped>

</style>
