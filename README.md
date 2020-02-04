# reine

使用Vertx、Web Component实现的简易报表引擎


### 模板代码
``` xml
<?xml version="1.0" encoding="utf-8" ?>
<reine xmlns="http://schema.eviao.cn/reine">
    <datasource>
        <dataset name="platform" url="jdbc:mysql://localhost/platform" driver="com.mysql.cj.jdbc.Driver" username="root" password="root">
            <query name="roles">SELECT * FROM role</query>
            <query name="user" mode="single">SELECT * FROM user WHERE id = {{userid}}</query>
        </dataset>
    </datasource>
    <layout>
        <![CDATA[
            <hello-world value="{{platform.user.name}}"></hello-world>
        ]]>
    </layout>
</reine>
```


### 视图
``` html
hello: administrator!
```
