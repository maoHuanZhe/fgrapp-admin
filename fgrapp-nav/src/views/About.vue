<template>
  <div>
    <el-descriptions class="margin-top" title="个人信息" :column="3" :size="size" border>
<!--      基础信息-->
      <el-descriptions-item>
        <template slot="label">
          姓名
        </template>
        樊光瑞
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          毕业院校
        </template>
        合肥师范学院
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          居住地
        </template>
        安徽省合肥市
      </el-descriptions-item>
<!--      联系方式-->
      <el-descriptions-item>
        <template slot="label">
          手机号
        </template>
        15178292346
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          邮箱
        </template>
        mail_fanguangrui@163.com
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          QQ号
        </template>
        898365387
      </el-descriptions-item>
<!--工作信息-->
      <el-descriptions-item>
        <template slot="label">
          毕业时间
        </template>
        2018-07-01
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          东家
        </template>
        北京南天软件公司
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          职位
        </template>
        JAVA工程师
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
