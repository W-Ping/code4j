# code4j
#### `JAVA`模板代码生成工具。
### 源码地址：[码云](https://gitee.com/LW_Ping/code4j) [GitHub](https://github.com/W-Ping/code4j)
![gif](https://user-images.githubusercontent.com/42802329/174940893-1f512f9d-3ef4-44ed-9515-0f41a8493ece.gif)

## 环境资源
* Windows 系统
* JDK8+ 
* MySQL数据库
* PostgreSQL数据库

## 工具栏说明
* 连接：配置数据源
* 自定义配置：自定义代码模板配置
* 帮助文档：查看工具文档

## 支持插件模板
* lombok
* swagger
* mybatis-plus
## 代码模板说明
* Entity 配置：此对象与数据库表结构对应（字段配置）。

* VO 配置：显示层对象（字段配置）。

* Mapper 配置：数据访问层接口（不选mybatis-plus可配置）。

* Xml 配置：Mybatis Sql 文件（不选mybatis-plus可配置）。

* Service Api配置：业务层接口。

* Service Impl配置：业务层实现类接口。

* Controller 配置：应用层方法(可配置)。

名称：代码（类名）或文件名称；例如：UserInfo.java  UserMapper.xml

包名：执代码所属的包路径。例如：com.xxx.UserInfo 中只需要填写 com.xxx

路径：项目中代码所属文件夹。（默认 src/main/java 无需改动）

父类：生成代码继承的父类。（必须是全路径 例如：com.xxx.BaseVo）

字段配置：可配置字段类型和选择字段否需要生成规则（注意：如果继承了父类某些字段可以不用生成了）

Mapper 配置：可选择自定义的接口。（默认不生成）

Xml 配置：可选择自定义的接口。（默认不生成）

Controller 配置：可选自定义的方法（默认不生成）。

响应：专指Controller层方法统一响应类。

## 使用教程

1. 拉取代码到本地打成jar包 （mvn package）双击运行jar包。

2. 配置MySQL/PostgreSQL数据源。

3. 选择左边栏数据库表。

4. 根据实际需要配置(自定义)

5. 点击生成代码按钮，确定后即可生成代码。

## 补充说明

![image](https://user-images.githubusercontent.com/42802329/174946492-b9aa05b9-6051-4bf9-8735-d17051f68a5a.png)