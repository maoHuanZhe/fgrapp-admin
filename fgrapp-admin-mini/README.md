```shell script
npm init 
# 一路回车 最后输入yes 
npm i @vant/weapp -S --production
```

### 修改 app.json

将 app.json 中的 `"style": "v2"` 去除

### 步骤三 修改 project.config.json

开发者工具创建的项目，`miniprogramRoot` 默认为 `miniprogram`，`package.json` 在其外部，npm 构建无法正常工作。

需要手动在 `project.config.json` 内添加如下配置，使开发者工具可以正确索引到 npm 依赖的位置。

```json
{
  ...
  "setting": {
    ...
    "packNpmManually": true,
    "packNpmRelationList": [
      {
        "packageJsonPath": "./package.json",
        "miniprogramNpmDistDir": "./"
      }
    ]
  }
}
```

![](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\Snipaste_2021-08-20_23-19-08.png)

新建page：my

添加导航栏：修改app.json,添加如下内容

```json
  "tabBar": {
    "list": [
      {
        "pagePath": "pages/index/index",
        "text": "主页"
      },
      {
        "pagePath": "pages/my",
        "text": "我的"
      }
    ]
  },
```

# API Promise化

扩展微信小程序api支持promise

# 安装

```text
npm install --save miniprogram-api-promise
```

# 使用

新建lib文件夹，创建wxp.js文件，添加内容

```js
import {
  promisifyAll
} from 'miniprogram-api-promise';

const URL_BASE = 'http://localhost:9657'
const wxp = {
  URL_BASE
}
promisifyAll(wx, wxp)

 export function request(args) {
    args.url = URL_BASE + args.url;
    args.header = {}
  return new Promise(function(resolve, reject) {
    wxp.request(args)
    .then((response)=>{
      console.log(response);
      resolve(response.data);
    })
    .catch(function (reason) {
      console.log('reason', reason)
      reject(reason)
    })
})

}

export default wxp
```

新增api文件夹，新建blog.js文件

```js
// 获取应用实例
import {request} from "../lib/wxp"

// 查询列表
export function page(query) {
  return request({
    url: '/page/list',
    method: 'get',
    data: query
  })
}
```

修改index.js

```js
// index.js
// 获取应用实例
const app = getApp()
import {page} from "../../api/blog"
Page({
  data: {
    queryParam:{
      pageNum: 1,
      pageSize: 10
    }
  },
  getList(){
    page(this.data.queryParam).then(({data})=>{
      console.log(data);
    })
  },
  onLoad() {
    console.log('onLoad')
    this.getList();
  },
})

```

![image-20210821105240791](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\image-20210821105240791.png)

# 导入colorUI

下载地址https://github.com/weilanwl/ColorUI/

```
git clone https://github.com/weilanwl/ColorUI.git
```

![image-20210821105441637](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\image-20210821105441637.png)

将这三个文件拷到小程序项目中

![image-20210821105551222](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\image-20210821105551222.png)

修改app.wxss

```wxss
@import "./lib/colorUI/main.wxss";
@import "./lib/colorUI/icon.wxss";
@import "./lib/colorUI/animation.wxss";
#这里的路径 根据自己存放的位置自定义
```

# 导入Towxml

项目地址：https://github.com/sbfkcel/towxml

```shell
克隆项目到本地
git clone https://github.com/sbfkcel/towxml.git
安装构建依赖
npm install 或 yarn
编辑配置文件towxml/config.js
根据自行要求，仅保留你需要的功能即可（配置中有详细注释）
运行 npm run build 或 yarn run build即可
```

![image-20210821163923689](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\image-20210821163923689.png)

将dist文件夹下的内容拷到小程序项目中，最好放在根目录下towxml文件夹中，这样就可以直接使用，不然还要更改里面文件的引用，

![image-20210821164133787](E:\project\server\fgrapp-admin\fgrapp-admin-mini\images\image-20210821164133787.png)

修改app.js

```js
App({
  // 引入`towxml3.0`解析方法
	towxml:require('/towxml/index'),
...
})
```

要使用的页面的配置文件中加入组件引用

```json
{
  "usingComponents": {
    "towxml":"../../towxml/towxml"
      # 路径自定义
  }
}
```

js文件中对数据进行解析

```js
        let result = app.towxml(data.blog.content,'markdown',{
          //base:'https://xxx.com',				// 相对资源的base路径
          theme:'light',					// 主题，默认`light`
          events:{					// 为元素绑定的事件方法
            tap:(e)=>{
              console.log('tap',e);
            }
          }
        });
```

页面进行引用

```wxml
<towxml nodes="{{article}}"/>
```

```shell
app.towxml(str,type,options)有三个参数

str 必选，html或markdown字符串
type 必选，需要解析的内容类型html或markdown
options [object] 可选，可以该选项设置主题、绑定事件、设置base等
base [string] 可选，用于指定静态相对资源的base路径。例如：https://xx.com/static/
theme [string] 可选，默认:light，用于指定主题'light'或'dark'，或其它自定义
events [object] 可选，用于为元素绑定事件。key为事件名称，value则必须为一个函数。例如:{tap:e=>{console.log(e)}}
```

