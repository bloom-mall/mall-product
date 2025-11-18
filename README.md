# Mall Product

Mall Product 是一个用于管理商品信息的微服务，提供商品的创建、更新、删除和查询功能。

## 技术架构

## 技术组件

| 技术栈          | 版本     | 说明          |
|--------------|--------|-------------|
| Spring Boot  | 3.2.4  | 基础框架        |
| Mybatis-Flex | 1.10.9 | 持久层框架       |
| Nacos        | 2.5.1  | 配置中心、服务注册发现 |
| MySQL        | 8.0    | 数据库         |
| Redis        |        | 缓存          |
| MongoDB      |        | 文档数据库       |

## 关键设计

- 数据模型
- 开发评审
- 安全方案

## 开发环境

### 环境要求

- JDK 17+
- Docker

### 本地启动

MacOS/Linux:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

### 本地check

```bash
./gradlew check
```

### 本地单测和集成测试

```bash
./gradlew test
```
