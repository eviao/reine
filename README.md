# reine

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


**result**
``` html
hello: administrator!
```
