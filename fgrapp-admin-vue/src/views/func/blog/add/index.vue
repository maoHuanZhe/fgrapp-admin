<template>
  <div class="mavonEditor">
    <el-input
      type="text"
      placeholder="请输入标题(5~100个字)"
      v-model="form.title"
      minlength="5"
      maxlength="100"
      show-word-limit
    >
    </el-input>
      <mavon-editor
        :toolbars="markdownOption"
        v-model="form.content"
        @change="change"
        @save="save"
        @fullScreen="fullScreen"
        @readModel="readModel"
        @htmlCode="htmlCode"
        @subfieldToggle="subfieldToggle"
        @previewToggle="previewToggle"
        @helpToggle="helpToggle"
        @navigationToggle="navigationToggle"
        @imgAdd="imgAdd"
        @imgDel="imgDel"
        :ishljs = "true"
      />
  </div>
</template>
<script>
  import {add} from "@/api/func/blog";
  export default {
    data() {
      return {
        form:{
          content:"",
          title:"",
          imgUrl:""
        },
        markdownOption: {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          strikethrough: true, // 中划线
          mark: true, // 标记
          superscript: true, // 上角标
          subscript: true, // 下角标
          quote: true, // 引用
          ol: true, // 有序列表
          ul: true, // 无序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          code: true, // code
          table: true, // 表格
          fullscreen: true, // 全屏编辑
          readmodel: true, // 沉浸式阅读
          htmlcode: true, // 展示html源码
          help: true, // 帮助
          /* 1.3.5 */
          undo: true, // 上一步
          redo: true, // 下一步
          trash: true, // 清空
          save: true, // 保存（触发events中的save事件）
          /* 1.4.2 */
          navigation: true, // 导航目录
          /* 2.1.8 */
          alignleft: true, // 左对齐
          aligncenter: true, // 居中
          alignright: true, // 右对齐
          /* 2.2.1 */
          subfield: true, // 单双栏模式
          preview: true, // 预览
        }
      };
    },
    methods:{
      //编辑区发生变化的回调事件(render: value 经过markdown解析后的结果)
      change(value,render){
        console.log("change::"+value);
        console.log("change::"+render);
      },
      //ctrl + s 的回调事件(保存按键,同样触发该回调)
      save(value,render){
        //判断是否输入标题
        if (!this.form.title){
          return this.$message({
            message: '标题长度应该在5~100个字之间',
            type: 'warning'
          });
        }
        add({
          title:this.form.title,
          imgUrl:this.form.imgUrl,
          content:render
        }).then(response => {
          this.msgSuccess("新增成功");
          this.form = {
            content:"",
            title:"",
            imgUrl:""
          }
        });
      },
      //切换全屏编辑的回调事件(boolean: 全屏开启状态)
      fullScreen(status,value){
        console.log(value);
        console.log(status);
      },
      //切换沉浸式阅读的回调事件(boolean: 阅读开启状态)
      readModel(status,value){
        console.log(value);
        console.log(status);
      },
      //查看html源码的回调事件(boolean: 源码开启状态)
      htmlCode(status,value){
        console.log(value);
        console.log(status);
      },
      //切换单双栏编辑的回调事件(boolean: 双栏开启状态)
      subfieldToggle(status,value){
        console.log(value);
        console.log(status);
      },
      //切换预览编辑的回调事件(boolean: 预览开启状态)
      previewToggle(status,value){
        console.log(value);
        console.log(status);
      },
      //查看帮助的回调事件(boolean: 帮助开启状态)
      helpToggle(status,value){
        console.log(value);
        console.log(status);
      },
      //切换导航目录的回调事件(boolean: 导航开启状态)
      navigationToggle(status,value){
        console.log(value);
        console.log(status);
      },
      //图片文件添加回调事件(filename: 写在md中的文件名, File: File Object)
      imgAdd(filename,imgfile){
        // 第一步.将图片上传到服务器.
        // var formdata = new FormData();
        // formdata.append('image', $file);
        // axios({
        //   url: 'server url',
        //   method: 'post',
        //   data: formdata,
        //   headers: { 'Content-Type': 'multipart/form-data' },
        // }).then((url) => {
        //   // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        //   /**
        //    * $vm 指为mavonEditor实例，可以通过如下两种方式获取
        //    * 1. 通过引入对象获取: `import {mavonEditor} from ...` 等方式引入后，`$vm`为`mavonEditor`
        //    * 2. 通过$refs获取: html声明ref : `<mavon-editor ref=md ></mavon-editor>，`$vm`为 `this.$refs.md`
        //    */
        //   $vm.$img2Url(pos, url);
        // })
      },
      //图片文件添加回调事件(filename: 写在md中的文件名, File: File Object)
      imgDel(filename){
        console.log(filename);
      },

    }
  };
</script>

<style scoped>
  .mavonEditor {
    width: 100%;
    height: 100%;
  }
</style>
