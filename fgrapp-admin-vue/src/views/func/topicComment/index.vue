<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="评论内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入评论内容"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论状态" prop="isAudit">
        <el-select v-model="queryParams.isAudit" placeholder="评论状态" clearable size="small">
          <el-option
            v-for="dict in auditOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['func:class:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评论主键" align="center" prop="id" />
      <el-table-column label="问题" align="center" prop="problem" />
      <el-table-column label="评论内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="评论状态" align="center" prop="isAudit" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAudit"
            active-value="1"
            inactive-value="0"
            :disabled="scope.row.isAudit === '1'"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.parentId"
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            @click="handleAdd(scope.row)"
            v-hasPermi="['func:class:remove']"
          >回复</el-button>
          <el-button
            v-if="scope.row.isAudit === '0'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['func:class:remove']"
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
    <el-dialog title="回复评论" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="内容" prop="content">
          <el-input maxlength="1024"
                    show-word-limit
                    type="textarea"
                    placeholder="请输入内容"
                    v-model="form.content">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, dels,  update } from "@/api/func/topicComment";
import { addComment } from "@/api/topic";

export default {
  name: "Comment",
  data() {
    return {
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        content: [
          { required: true, message: "回复内容不能为空", trigger: "blur" }
        ]
      },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      auditOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: undefined,
        isAudit: undefined,
        order:'fc.id-desc'
      }
    };
  },
  created() {
    this.getList();
    this.getDicts(108).then(response => {
      this.auditOptions = response.data;
    });
  },
  methods: {
    handleStatusChange(row){
      this.$confirm('确认要通过评论编号为'+row.id+'的审核吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return update({id:row.id,blogId:row.blogId,isAudit: 1});
      }).then(() => {
        this.msgSuccess("审核成功");
      })
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.records;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除评论编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return dels(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleAdd(row) {
      this.open = true;
      this.form.parentId = row.id;
      this.form.topicId = row.topicId;
      this.form.isAudit = 1;
    },
    submitForm(){
      addComment(this.form).then(()=>{
        this.$message.success("回复成功");
        this.open = false;
        this.getList();
      })
    }
  }
};
</script>
