<template>

  <div class="app-container">
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="编号" align="center" key="id" prop="id" />
      <el-table-column label="标题" align="center" key="title" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" :show-overflow-tooltip="true" />
      <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="160"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope" v-if="scope.row.userId !== 1">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:user:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:user:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
  import { page } from "@/api/func/blog";
    export default {
        name: "index",
      data(){
          return{
            // 列信息
            columns: [
              { key: 0, label: `编号`, visible: true },
              { key: 1, label: `标题`, visible: true },
              { key: 2, label: `创建人`, visible: true },
              { key: 3, label: `创建时间`, visible: true }
            ],
            // 用户表格数据
            list: null,
            // 总条数
            total: 0,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 查询参数
            queryParams: {
              pageNum: 1,
              pageSize: 10,
              userName: undefined,
              phonenumber: undefined,
              status: undefined,
              deptId: undefined
            },
          }
      },
      methods:{
        /** 查询用户列表 */
        getList() {
          this.loading = true;
          page(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
              this.userList = response.rows;
              this.total = response.total;
              this.loading = false;
            }
          );
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
          this.ids = selection.map(item => item.id);
          this.single = selection.length !== 1;
          this.multiple = !selection.length;
        },
      }
    }
</script>

<style scoped>

</style>
