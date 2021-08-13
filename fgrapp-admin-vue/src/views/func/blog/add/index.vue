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
      <el-button slot="append" @click="save">发布文章</el-button>
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="分类专栏" prop="class">
          <el-tag
            :key="tag"
            v-for="tag in classNames"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)">
            {{tag}}
          </el-tag>
          <el-input
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            class="input-new-tag"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          >
          </el-input>
          <el-tag style="margin-left: 10px;" @click="showInput">+ 新建分类专栏</el-tag>
          <div style="height: 100px;border-radius: 4px;border: 1px solid #e8e8ee;padding: 0 10px;">
            <el-checkbox-group v-model="classNames">
              <el-checkbox v-for="item in classList" :label="item.name" :key="item.id"></el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
        <el-form-item label="文章类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择" clearable size="small">
            <el-option
              v-for="dict in typeOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
  import { add, getInfo, update } from "@/api/func/blog";
  import { list } from "@/api/func/class";
  export default {
    data() {
      return {
        inputVisible: false,
        inputValue: '',
        title: "",
        open: false,
        typeOption:[],
        classNames:[],
        classList:[],
        classMap:{},
        form:{
          content:"",
          title:"",
          type:"",
          imgUrl:"",
          classIds:[]
        },
        rules: {
            type: [
            { required: true, message: '请选择文章类型', trigger: 'change' }
          ],
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
    created(){
      const id = this.$route.params && this.$route.params.blogId;
      if (id) {
        getInfo(id).then(({data}) => {
          this.form = data;
          this.form.type = data.type + "";
          this.classNames = data.addClassNames;
        })
      }
    },
    methods:{
      handleClose(tag) {
        this.classNames.splice(this.classNames.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          //判断名称是否重复
          if (this.classNames.indexOf(inputValue) === -1){
            this.classNames.push(inputValue);
          }
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      cancel(){
        this.title = "";
        this.open = false;
      },
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
        if (!this.classList.length){
          list().then(({data})=>{
            this.classList = data;
            data.forEach(item =>{
              this.classMap[item.name] = item.id;
            })
          })
        }
        if (!this.typeOption.length){
          this.getDicts(107).then(response => {
            this.typeOption = response.data;
          });
        }
        this.title = "发布文章";
        this.open = true;
      },
      getClassIds(){
        //获取博客分类 分为已存在的和要创建的两种
        let classIds = [];
        let addClassNames = [];
        this.classNames.forEach(item =>{
          let classId = this.classMap[item];
          if (classId){
            //已存在
            classIds.push(classId)
          } else {
            //要新建的分类名称
            addClassNames.push(item);
          }
        })
        this.form.classIds = classIds;
        this.form.addClassNames = addClassNames;
      },
      submit(){
        this.$refs.form.validate(valid => {
          if (valid) {
            this.getClassIds()
            let result;
            if (this.form.id){
              result = update(this.form)
            } else {
              result = add(this.form)
            }
            result.then(() => {
              this.cancel();
              this.msgSuccess("保存成功");
              if (this.form.id){
                //关闭当前页面
                this.$store.dispatch("tagsView/delView", this.$route);
                this.$router.push({ path: "/func/blog/list" });
              }
              this.form = {
                content:"",
                title:"",
                type:"",
                imgUrl:"",
                classIds:[]
              }
            }).catch(()=>{
              this.cancel();
            });
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
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>
