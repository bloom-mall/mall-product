# Template Spring Cloud

Spring Cloud 微服务模板项目，用于快速创建新的Spring Cloud项目.

## 初始化修改

### 1. 执行初始化脚本

对项目进行初始化，包括修改项目名称、包名等, 脚本传入一个必填参数作为新项目名称，必须使用中划线分隔.

```bash
chmod +x ./rename-project.sh
./rename-project.sh <new-project-name>
```
  
### 2. 删除初始化脚本

```bash
rm ./rename-project.sh
```

### 3. 按需修改`build.gradle`

- 包括项目的依赖、版本号以及描述信息等.

### 4. 在Github中添加 Repository secrets:

- `DOCKER_USERNAME`
- `DOCKER_ACCESS_TOKEN`
  
### 5. 在Nacos中添加项目配置

- 配置 ***DataId*** 为`<new-project-name>-config.yaml`
- 配置的 ***group*** 为`BASE`


----------------------------删除以上内容，并按需修改如下README.md-----------------------------

# 项目名称

项目简介.

## 技术架构

## 技术组件

| 技术组件        | 版本     | 说明 |
|-------------|--------|------|
| Spring Boot | 3.5.4  | 基础框架 |
| MySQL       | 8.0    | 数据存储 |

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
