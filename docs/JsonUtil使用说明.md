# JsonUtil JSON工具类使用说明

## 📋 概述

`JsonUtil` 基于 FastJSON2 提供了 JSON 序列化和反序列化的便捷方法。

## 🔧 常用方法

### 1. 对象转JSON字符串

```java
User user = new User();
user.setId(1L);
user.setUsername("testuser");

// 标准格式
String json = JsonUtil.toJsonString(user);
// 输出：{"id":1,"username":"testuser"}

// 格式化输出（美化）
String prettyJson = JsonUtil.toJsonStringPretty(user);
// 输出：
// {
//   "id": 1,
//   "username": "testuser"
// }
```

### 2. JSON字符串转对象

```java
String json = "{\"id\":1,\"username\":\"testuser\"}";

// 转换为指定类型对象
User user = JsonUtil.parseObject(json, User.class);

// 转换为 List
String jsonArray = "[{\"id\":1},{\"id\":2}]";
List<User> users = JsonUtil.parseArray(jsonArray, User.class);
```

### 3. JSON字符串转JSONObject/JSONArray

```java
String json = "{\"id\":1,\"username\":\"testuser\"}";

// 转换为 JSONObject（可以动态访问）
JSONObject jsonObject = JsonUtil.parseObject(json);
Long id = jsonObject.getLong("id");
String username = jsonObject.getString("username");

// 转换为 JSONArray
String jsonArray = "[1,2,3]";
JSONArray array = JsonUtil.parseArray(jsonArray);
```

### 4. 对象与JSONObject互转

```java
User user = new User();
user.setId(1L);
user.setUsername("testuser");

// 对象转JSONObject
JSONObject jsonObject = JsonUtil.toJsonObject(user);

// JSONObject转对象
User user2 = JsonUtil.parseObject(jsonObject.toJSONString(), User.class);
```

### 5. Map与对象互转

```java
User user = new User();
user.setId(1L);
user.setUsername("testuser");

// 对象转Map
Map<String, Object> map = JsonUtil.objectToMap(user);
// map: {id=1, username="testuser"}

// Map转对象
User user2 = JsonUtil.mapToObject(map, User.class);
```

### 6. JSON验证

```java
String json = "{\"id\":1}";

// 判断是否为有效JSON
boolean valid = JsonUtil.isValidJson(json);  // true

// 判断是否为有效JSON对象
boolean validObj = JsonUtil.isValidJsonObject(json);  // true

// 判断是否为有效JSON数组
String jsonArray = "[1,2,3]";
boolean validArray = JsonUtil.isValidJsonArray(jsonArray);  // true
```

### 7. 深拷贝

```java
User user = new User();
user.setId(1L);
user.setUsername("testuser");

// 通过JSON序列化/反序列化实现深拷贝
User cloned = JsonUtil.deepClone(user, User.class);
```

## 💡 实际应用示例

### 示例1：在Controller中使用

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ResponseResult<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        
        // 如果需要返回JSON字符串
        String json = JsonUtil.toJsonString(user);
        return ResponseResult.success(user);
    }
    
    @PostMapping("/users")
    public ResponseResult<User> createUser(@RequestBody String jsonStr) {
        // 验证JSON格式
        if (!JsonUtil.isValidJsonObject(jsonStr)) {
            return ResponseResult.fail("无效的JSON格式");
        }
        
        // 解析JSON
        User user = JsonUtil.parseObject(jsonStr, User.class);
        userService.save(user);
        return ResponseResult.success(user);
    }
}
```

### 示例2：在Service中使用

```java
@Service
public class UserService {
    
    // 将对象转换为Map进行动态处理
    public Map<String, Object> getUserAsMap(Long id) {
        User user = this.getById(id);
        return JsonUtil.objectToMap(user);
    }
    
    // 从Map创建对象
    public User createUserFromMap(Map<String, Object> data) {
        return JsonUtil.mapToObject(data, User.class);
    }
}
```

### 示例3：处理复杂对象

```java
// 嵌套对象
public class Order {
    private Long id;
    private User user;  // 嵌套对象
    private List<OrderItem> items;  // 列表
}

// 序列化
Order order = new Order();
String json = JsonUtil.toJsonString(order);
// 输出：{"id":1,"user":{"id":1,"username":"test"},"items":[...]}

// 反序列化
Order order2 = JsonUtil.parseObject(json, Order.class);
```

### 示例4：API响应处理

```java
@RestController
public class ApiController {
    
    @GetMapping("/external-api")
    public ResponseResult<Map<String, Object>> callExternalApi() {
        // 调用外部API，返回JSON字符串
        String responseJson = httpClient.get("/api/data");
        
        // 验证JSON
        if (!JsonUtil.isValidJson(responseJson)) {
            return ResponseResult.fail("外部API返回格式错误");
        }
        
        // 解析为Map
        Map<String, Object> data = JsonUtil.objectToMap(
            JsonUtil.parseObject(responseJson)
        );
        
        return ResponseResult.success(data);
    }
}
```

### 示例5：对象深拷贝

```java
@Service
public class OrderService {
    
    public Order cloneOrder(Order original) {
        // 深拷贝订单对象
        Order cloned = JsonUtil.deepClone(original, Order.class);
        cloned.setId(null);  // 清除ID，用于创建新订单
        return cloned;
    }
}
```

## ⚠️ 注意事项

1. **性能考虑**：频繁序列化/反序列化时，FastJSON2 性能较好
2. **循环引用**：对象之间存在循环引用时，序列化可能失败
3. **日期格式**：日期字段会按照默认格式序列化，可通过注解自定义
4. **空值处理**：null 值在序列化时会被忽略（可通过配置修改）
5. **类型安全**：反序列化时确保类型匹配，否则可能抛出异常

## 🔧 高级用法

### 自定义序列化

```java
// 使用注解控制序列化
public class User {
    @JSONField(name = "user_id")
    private Long id;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JSONField(serialize = false)  // 不序列化
    private String password;
}
```

### 处理泛型

```java
// 处理泛型List
String json = "[{\"id\":1},{\"id\":2}]";
List<User> users = JsonUtil.parseArray(json, User.class);

// 处理Map
String mapJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
Map<String, String> map = JsonUtil.parseObject(mapJson, Map.class);
```

## 🔗 相关工具类

- `BeanUtil` - Bean对象转换
- `StringUtil` - 字符串处理

