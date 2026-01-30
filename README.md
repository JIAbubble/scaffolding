# 规划模型系统 - 使用指南

## 📋 项目简介

基于 Spring Boot 3.2.4 的规划模型系统，集成了 MyBatis-Plus、Swagger、Logback、LangChain4j 等常用组件。支持使用 LangChain4j 自动构建知识图谱功能。

## 🚀 快速开始

### 1. 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 5.7+ 或 8.0+
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Ollama** (可选): 用于本地LLM服务，推荐用于知识图谱功能

### 2. 数据库配置

#### 2.1 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS decisionmodel 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;
```

#### 2.2 创建用户表（示例）

```sql
USE decisionmodel;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(50) NOT NULL UNIQUE COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 2.3 创建知识图谱表

**重要提示**：请根据 `application-dev.yml` 中配置的数据库名称执行SQL脚本。

**开发环境**（默认使用 `performance` 数据库）：

```sql
USE performance;

CREATE TABLE IF NOT EXISTS knowledge_graph (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图谱ID',
    name VARCHAR(200) NOT NULL COMMENT '图谱名称',
    description VARCHAR(1000) COMMENT '图谱描述',
    graph_data TEXT COMMENT '图谱数据（JSON格式，包含节点和边）',
    node_count INT DEFAULT 0 COMMENT '节点数量',
    edge_count INT DEFAULT 0 COMMENT '边数量',
    build_time BIGINT COMMENT '构建耗时（毫秒）',
    status VARCHAR(20) DEFAULT 'SUCCESS' COMMENT '构建状态',
    message VARCHAR(500) COMMENT '构建消息',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_name (name),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识图谱表';
```

**或者直接执行SQL脚本文件**：

```bash
# 执行 docs/sql/knowledge_graph.sql 文件
# 注意：脚本中默认使用 performance 数据库，如需修改请编辑脚本中的 USE 语句
```

#### 2.4 修改数据库配置

**开发环境配置**（`src/main/resources/application-dev.yml`）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/performance?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root        # 修改为你的数据库用户名
    password: 123456789    # 修改为你的数据库密码
```

**注意**：
- 开发环境默认使用 `performance` 数据库
- 知识图谱表需要创建在配置的数据库中（即 `performance` 数据库）
- 如果使用其他数据库，请确保SQL脚本中的 `USE` 语句与配置一致

### 3. 启动项目

#### 方式一：使用 IDE 启动

1. 打开项目
2. 找到 `PlanningModelApplication.java`
3. 右键 → Run 'PlanningModelApplication'

#### 方式二：使用 Maven 命令

```bash
# 清理并编译
mvn clean install

# 启动项目
mvn spring-boot:run
```

#### 方式三：打包后运行

```bash
# 打包
mvn clean package

# 运行 JAR 文件
java -jar target/planningModel-0.0.1-SNAPSHOT.jar
```

### 4. 验证启动

启动成功后，控制台会显示：

```
Started PlanningModelApplication in X.XXX seconds
```

访问地址：
- **应用首页**: http://127.0.0.1:8090
- **Swagger API 文档**: http://127.0.0.1:8090/swagger-ui.html
- **API JSON 文档**: http://127.0.0.1:8090/v3/api-docs

## 📚 功能使用

### 1. Swagger API 文档

访问 `http://127.0.0.1:8090/swagger-ui.html` 可以：

- 查看所有 API 接口
- 在线测试 API
- 查看请求/响应示例
- 查看数据模型

**示例 API**：
- `GET /api/users` - 获取用户列表
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `POST /api/knowledge-graph/build` - 构建知识图谱
- `POST /api/knowledge-graph/build-async` - 异步构建知识图谱
- `GET /api/knowledge-graph/{graphId}` - 获取知识图谱

### 2. 日志功能

#### 2.1 日志配置

日志配置文件：`src/main/resources/logback-spring.xml`

#### 2.2 日志文件位置

- **应用日志**: `./logs/planning-model.log`
- **错误日志**: `./logs/error.log`

#### 2.3 在代码中使用日志

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    
    @GetMapping("/test")
    public String test() {
        log.debug("这是 DEBUG 日志");
        log.info("这是 INFO 日志");
        log.warn("这是 WARN 日志");
        log.error("这是 ERROR 日志");
        return "success";
    }
}
```

#### 2.4 日志级别配置

在 `application.yml` 中配置：

```yaml
logging:
  level:
    root: info                                    # 根日志级别
    com.example.planningmodel: debug      # 项目包日志级别
    com.example.planningmodel.dao: debug # SQL 日志级别
```

### 3. 前端页面

前端页面位于 `src/main/resources/static/html/`：

- **用户信息**: `/html/performan.html`
- **系统日志**: `/html/system-log.html`
- **历史数据管理**: `/html/EmergencyDrill/historical-data-management.html`
- **历史数据分析**: `/html/EmergencyDrill/historical-data.html`
- **数据服务配置**: `/html/systemManagement/data-service.html`
- **数据库配置**: `/html/systemManagement/database-info.html`

访问方式：`http://127.0.0.1:8090/html/文件名.html`

### 4. 知识图谱功能

#### 4.1 功能说明

使用 LangChain4j 自动从文本中提取实体和关系，构建知识图谱。支持 Ollama 和 OpenAI 两种 LLM 提供商。

#### 4.2 环境配置

**使用 Ollama（推荐）：**

```bash
# 安装并启动 Ollama
ollama serve

# 下载模型（例如 llama3）
ollama pull llama3
```

**使用 OpenAI：**

在 `application.yml` 中配置：

```yaml
langchain4j:
  model:
    provider: openai
    openai:
      api-key: your-api-key-here
      model-name: gpt-3.5-turbo
```

#### 4.3 使用示例

```bash
# 构建知识图谱
curl -X POST http://127.0.0.1:8090/api/knowledge-graph/build \
  -H "Content-Type: application/json" \
  -d '{
    "name": "飞机维修知识图谱",
    "description": "基于飞机维修手册构建的知识图谱",
    "inputText": "飞机维修是确保航空器安全运行的重要环节。维修人员需要具备专业的技能和知识。维修工具包括扳手、螺丝刀、万用表等。",
    "autoExtractEntities": true,
    "autoExtractRelations": true
  }'
```

#### 4.4 详细文档

更多使用说明请参考：[LangChain4j知识图谱使用说明.md](./docs/LangChain4j知识图谱使用说明.md)

### 5. API 调用示例

#### 使用 curl

```bash
# 获取用户列表
curl http://127.0.0.1:8090/api/users

# 创建用户
curl -X POST http://127.0.0.1:8090/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "role": "USER"
  }'

# 获取用户详情
curl http://127.0.0.1:8090/api/users/1

# 更新用户
curl -X PUT http://127.0.0.1:8090/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "new@example.com",
    "role": "ADMIN"
  }'

# 删除用户
curl -X DELETE http://127.0.0.1:8090/api/users/1

# 构建知识图谱
curl -X POST http://127.0.0.1:8090/api/knowledge-graph/build \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试图谱",
    "inputText": "飞机维修需要专业人员和专业工具。维修人员使用扳手、螺丝刀等工具进行维修。"
  }'

# 获取知识图谱
curl http://127.0.0.1:8090/api/knowledge-graph/1
```

#### 使用 Postman

1. 导入 Swagger 文档：访问 `http://127.0.0.1:8090/v3/api-docs`
2. 在 Postman 中导入该 JSON
3. 即可使用所有 API

## 🛠️ 开发指南

### 1. 项目结构

```
src/main/java/com/example/planningmodel/
├── config/          # 配置类
│   ├── CorsConfig.java           # 跨域配置
│   ├── MybatisPlusConfig.java   # MyBatis-Plus 配置
│   └── OpenApiConfig.java       # Swagger 配置
├── controller/      # 控制器层
│   ├── UserController.java
│   └── KnowledgeGraphController.java  # 知识图谱控制器
├── service/         # 服务层
│   ├── UserService.java
│   ├── KnowledgeGraphService.java      # 知识图谱服务接口
│   └── impl/
│       ├── UserServiceImpl.java
│       └── KnowledgeGraphServiceImpl.java  # 知识图谱服务实现
├── dao/            # 数据访问层
│   ├── UserMapper.java
│   └── KnowledgeGraphMapper.java      # 知识图谱Mapper
├── pojo/           # 实体类
│   ├── User.java
│   ├── ResponseResult.java
│   ├── ResultCode.java
│   ├── KnowledgeGraph.java            # 知识图谱实体
│   ├── KnowledgeGraphEntity.java      # 知识图谱数据库实体
│   └── KnowledgeGraphResult.java     # 知识图谱结果
├── form/           # 表单类
│   ├── UserCreateForm.java
│   └── KnowledgeGraphBuildForm.java  # 知识图谱构建表单
└── utils/          # 工具类
```

### 2. 创建新的 API

#### 步骤 1: 创建实体类

```java
@Data
@TableName("your_table")
public class YourEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    // ...
}
```

#### 步骤 2: 创建 Mapper

```java
@Mapper
public interface YourMapper extends BaseMapper<YourEntity> {
    // 自定义方法
}
```

#### 步骤 3: 创建 Service

```java
public interface YourService extends IService<YourEntity> {
    // 自定义业务方法
}
```

#### 步骤 4: 创建 Controller

```java
@RestController
@RequestMapping("/api/your-resource")
@RequiredArgsConstructor
public class YourController {
    private final YourService yourService;
    
    @GetMapping
    public List<YourEntity> list() {
        return yourService.list();
    }
    // ...
}
```

### 3. 常用配置

#### 修改端口

在 `application.yml` 中：

```yaml
server:
  port: 8080  # 修改为你想要的端口
```

#### 修改日志级别

```yaml
logging:
  level:
    com.example.planningmodel: debug
```

## 🔧 常见问题

### 1. 数据库连接失败

- 检查数据库是否启动
- 检查 `application.yml` 中的数据库配置
- 检查数据库用户权限

### 2. Swagger 404

- 确认访问地址：`http://127.0.0.1:8090/swagger-ui.html`
- 或尝试：`http://127.0.0.1:8090/swagger-ui/index.html`
- 检查是否有 Controller 类（至少需要一个）

### 3. 日志不输出

- 检查 `logback-spring.xml` 配置
- 检查日志文件目录权限
- 检查日志级别配置

### 4. 端口被占用

```bash
# Windows 查看端口占用
netstat -ano | findstr :8090

# Linux/Mac 查看端口占用
lsof -i :8090
```

修改 `application.yml` 中的端口号。

## 📝 注意事项

1. **数据库配置**：首次使用前必须配置数据库连接信息
2. **日志文件**：日志文件会自动创建在项目根目录的 `logs` 文件夹
3. **跨域配置**：已在 `CorsConfig` 中配置允许所有来源，生产环境请修改
4. **MyBatis-Plus**：已配置逻辑删除，删除操作不会真正删除数据

## 📚 相关文档

- [工具类总览](./docs/工具类总览.md) - 8个常用工具类使用说明
- [核心功能使用说明](./docs/核心功能使用说明.md) - 全局异常处理、统一响应等
- [LangChain4j知识图谱使用说明](./docs/LangChain4j知识图谱使用说明.md) - 知识图谱功能详细文档
- [新增功能使用说明](./docs/新增功能使用说明.md) - 认证授权、Redis等新功能

## 🎯 下一步

- 根据业务需求添加更多实体和 API
- 配置 Redis 缓存（如需要）
- 添加 JWT 认证（如需要）
- 配置生产环境配置
- 使用知识图谱功能构建业务知识库

---

**祝开发愉快！** 🎉

