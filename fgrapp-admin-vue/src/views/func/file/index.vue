<template>
  <div>
    <el-upload
      class="upload-demo"
      :action="action"
      :headers="headers"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="success"
      :on-error="error">
      <i class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <img v-for="url in urls" :src="url" class="avatar">
  </div>
</template>

<script>
  import { getToken } from '@/utils/auth'
  import SparkMD5 from 'spark-md5'
  export default {
    name: "upload",
    data() {
      return {
        action:"http://localhost:9657/func/file",
        // action:"http://192.168.0.100:7999/file/upload",
        fileList:[],
        headers:{
          'Admin-Token':'Bearer ' + getToken()
        },
        urls:[],
        loading:''
      };
    },
    methods: {
      //上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
      beforeUpload(file){
        this.loading = this.$loading({
          lock: true,
          text: '上传中',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      },
      //文件上传成功时的钩子
      success(response,file){
        if (file.raw.type.startsWith("image")){
          this.urls.unshift(response.url);
        }
        this.$message({
          type: 'success',
          message: '上传成功'
        });
        this.loading.close();
      },
      //文件上传失败时的钩子
      error(err){
        console.log(err)
        this.$message({
          type: 'warning',
          message: '上传失败'
        });
        this.loading.close();
      }
    }
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
  }
</style>
