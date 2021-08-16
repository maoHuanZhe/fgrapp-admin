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
      <mavon-editor ref="md"
        :toolbars="markdownOption"
        v-model="form.content"
        @save="save"
        @imgAdd="imgAdd"
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
  import { add, update } from "@/api/func/blog";
  import { list } from "@/api/func/class";
  import { upload } from "@/api/func/file";
  import { getInfo } from "@/api/blog";
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
      //图片文件添加回调事件(filename: 写在md中的文件名, File: File Object)
      imgAdd(filename,imgfile){
        // 第一步.将图片上传到服务器.
        upload(imgfile).then(({data})=>{
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(filename, data.url);
        })
      }
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
