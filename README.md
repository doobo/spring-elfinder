# spring-elfinder

> 简单的文件管理工具,基于springboot2,主要方便集成到项目中去,方便浏览系统文件,如日志、系统定时生成静态页面等

关于权限问题，不在该项目的考虑范围内，自己拦截相关URL即可，也可用专门的权限框架实现。集成的md文档编辑器,方便编辑普通文本文件;
相关页面调用http接口时，如果localstorage有token或者authorization时，会自动加入的ajx的header上,添加了执行结束后广播，
方便在文件操作完成(如:文件上传、删除、压缩、下载)时，进行相关业务处理，继承CmdObserver抽象类，并暴露被bean容器即可。

## 如何添加
```
<dependency>
    <groupId>com.github.doobo</groupId>
    <artifactId>elfinder-api</artifactId>
    <version>1.3</version>
</dependency>
```

## 请求示例
* https://localhost:8080/fm.html
* https://localhost:8080/open.html
* https://localhost:8080/link.html
* https://localhost:8080/tinymce.html

## 文件列表
* http://localhost:8080/elfinder/connector?cmd=open&init=1&tree=1

## 保存文件
```
PUT:http://localhost:8080/elfinder/connector
传参数:
cmd:put
target:A_c3RhdGljL3NmMS9hcnRpY2xlLm1pbi5qcw_E_E
encoding:UTF-8
content:保存内容
```

## 基本配置
```yaml
#添加这个UTF-8对中文文件的支持
spring:
  http:
    encoding:
      charset: utf-8
      force: true
      enabled: true
  #大文件上传
  servlet:
    multipart:
      max-file-size: 10GB
      max-request-size: 30GB
      
file-manager:
  thumbnail:
    width: 80 # 缩略图宽
  volumes:
    - Node:
      source: fileSystem # 暂时只支持本地文件系统
      alias: 系统目录 # 目录别名
      path: data # 映射目录
      isDefault: true # 是否默认打开
      locale:
      constraint:
        locked: false # 文件夹是否锁定
        readable: true # 是否可读
        writable: true # 是否可写
#    - Node:
#      source: aliyunoss
#      alias: 阿里云 # 目录别名
#      path: moore-test1 #bucketName
#      isDefault: false # 是否默认打开
#      locale:
#      constraint:
#        locked: false # 文件夹是否锁定
#        readable: true # 是否可读
#        writable: true # 是否可写
#      aliyunDriverConfig:
#        endpoint: "oss-cn-beijing.aliyuncs.com"
#        #bindedDomain: "cdn-bk1.origocoffee.com"
#        accessKeyId: ENC(******)
#        accessKeySecret: ENC(******)
```

## JSON包排除
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.vaadin.external.google</groupId>
            <artifactId>android-json</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 获取文件上传的系统路径
```

public static VolumeHandler findTarget(ElfinderStorage elfinderStorage, String hash) {
    Target target = elfinderStorage.fromHash(hash);
    if (target == null) {
        return null;
    }
    return new VolumeHandler(target, elfinderStorage);
}
VolumeHandler handler = vo.getVolumeHandlerByHash(hash);
//文件存放的系统路径
String name = handler.getTarget().toString();
```

## 参考文献
该项目参考了:
1. [https://gitee.com/boyuan2000cn/springboot2-elfinder](https://gitee.com/boyuan2000cn/springboot2-elfinder)

