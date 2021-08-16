import request from '@/utils/request'

export function upload(file) {
  // 此时可以自行将文件上传至服务器
  let fileName = file.name;  // 保存文件名
  let split = fileName.split('.');  // 对文件名进行切割
  let suffix = split[split.length - 1] //文件后缀名
  //var renameFile =new File([原文件], 新文件名,文件类型);
  var renameFile =new File([file], new Date().getTime() + '.' + suffix,{type:file.type});
  //修改文件名
  var formdata = new FormData();
  formdata.append('file', renameFile);
  return request({
    url: '/func/file',
    method: 'post',
    data: formdata,
    headers:{
      "Content-Type": "multipart/form-data;"
    }
  })
}
