# 代码生成模块 (code-generate)

## 简介

这是一个基于MyBatis Plus Generator的代码生成模块，可以快速生成Spring Boot风格的代码，包括：
- Entity实体类
- Mapper接口和XML文件
- Service接口及实现类
- Controller控制器

## 功能特点

1. 整合了SpringBoot推荐的MyBatis Plus代码生成框架
2. 模板配置通俗易懂，易于定制
3. 支持直接生成代码到指定模块
4. 通过运行main方法或调用REST API即可生成代码
5. 特别优化了对adminapi模块的代码生成支持

## 快速开始

### 1. 配置数据库连接

在 `application.yml` 中配置数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

### 2. 配置生成参数

在 `application.yml` 中配置代码生成参数：

```yaml
code:
  generate:
    # 默认生成到adminapi模块
    target-module: adminapi
    # 可选值: adminapi, admin, system, order, user, tool等
    module-options: adminapi,admin,system,order,user,tool
    # 输出路径前缀，默认为项目根目录
    output-base-path: ../
```

### 3. 运行代码生成

#### 方式一：命令行交互式生成

直接运行 [CodeGenerateApplication](file:///D:/code/springcloud-template/code-generate/src/main/java/com/company/codegenerate/CodeGenerateApplication.java) 的main方法，按照提示输入模块名和表名即可生成代码。

#### 方式二：REST API生成

启动应用后，可以通过以下API接口生成代码：

1. 生成单个表的代码：
   ```
   POST /code/generate/{moduleName}?tableName={tableName}
   ```

2. 批量生成代码：
   ```
   POST /code/generate/{moduleName}/batch
   Body: ["table1", "table2", "table3"]
   ```

3. 获取支持的模块列表：
   ```
   GET /code/generate/modules
   ```

### 4. 生成的代码结构

生成的代码将按照以下结构组织：

```
com.company.{module}
├── controller
│   └── {Entity}Controller.java
├── entity
│   └── {Entity}.java
├── mapper
│   ├── {Entity}Mapper.java
│   └── xml
│       └── {Entity}Mapper.xml
├── service
│   ├── {Entity}Service.java
│   └── impl
│       └── {Entity}ServiceImpl.java
```

## 定制模板

所有的代码模板都位于 `src/main/resources/templates` 目录下，采用Velocity模板语法编写，可以根据需要进行定制。

## 支持的模块

目前支持生成代码到以下模块：
- adminapi (运营管理后台API)
- admin (运营管理后台)
- system (系统模块)
- order (订单模块)
- user (用户模块)
- tool (工具模块)

## 注意事项

1. 请确保数据库连接正确并且有访问权限
2. 生成的代码会覆盖同名文件，请注意备份重要代码
3. 生成的代码可能需要根据具体业务需求进行调整