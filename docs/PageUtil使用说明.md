# PageUtil 分页工具类使用说明

## 📋 概述

`PageUtil` 提供了 MyBatis-Plus 分页操作的便捷方法，简化分页查询和结果处理。

## 🔧 常用方法

### 1. 创建分页对象

```java
// 方式1：使用 long 类型
Page<User> page1 = PageUtil.createPage(1, 10);  // 第1页，每页10条

// 方式2：使用 Integer 类型（带默认值和限制）
Page<User> page2 = PageUtil.createPage(null, null);      // 默认：第1页，每页10条
Page<User> page3 = PageUtil.createPage(2, 20);          // 第2页，每页20条
Page<User> page4 = PageUtil.createPage(1, 200);         // 自动限制为100条（最大限制）
```

### 2. 分页结果转换

```java
// 将 MyBatis-Plus 的 IPage 转换为自定义分页结果
IPage<User> iPage = userService.page(PageUtil.createPage(1, 10));
PageResult<User> result = PageUtil.toPageResult(iPage);

// 使用结果
List<User> records = result.getRecords();  // 数据列表
Long total = result.getTotal();            // 总记录数
Long current = result.getCurrent();        // 当前页
Long size = result.getSize();              // 每页大小
Long pages = result.getPages();            // 总页数
```

### 3. 内存分页

```java
// 将 List 转换为分页结果（内存分页）
List<User> allUsers = userService.list();  // 获取所有数据
PageResult<User> result = PageUtil.toPageResult(allUsers, 1, 10);  // 第1页，每页10条
```

### 4. 分页结果转换（实体类型转换）

```java
// 将分页结果中的实体类型转换为DTO
IPage<User> userPage = userService.page(PageUtil.createPage(1, 10));

// 转换为 UserDTO
PageResult<UserDTO> dtoPage = PageUtil.convert(userPage, user -> {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    return dto;
});
```

## 💡 实际应用示例

### 示例1：在Controller中使用

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseResult<PageResult<User>> list(
            @RequestParam(required = false) Integer current,
            @RequestParam(required = false) Integer size) {
        
        // 创建分页对象
        Page<User> page = PageUtil.createPage(current, size);
        
        // 执行分页查询
        IPage<User> iPage = userService.page(page);
        
        // 转换为分页结果
        PageResult<User> result = PageUtil.toPageResult(iPage);
        
        return ResponseResult.success(result);
    }
}
```

### 示例2：带条件的分页查询

```java
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    
    public PageResult<User> searchUsers(String keyword, Integer current, Integer size) {
        // 创建分页对象
        Page<User> page = PageUtil.createPage(current, size);
        
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(keyword)) {
            wrapper.like(User::getUsername, keyword)
                   .or()
                   .like(User::getEmail, keyword);
        }
        
        // 执行分页查询
        IPage<User> iPage = this.page(page, wrapper);
        
        // 转换为分页结果
        return PageUtil.toPageResult(iPage);
    }
}
```

### 示例3：返回DTO的分页查询

```java
@RestController
public class UserController {
    
    @GetMapping("/users")
    public ResponseResult<PageResult<UserDTO>> listUsers(
            @RequestParam(required = false) Integer current,
            @RequestParam(required = false) Integer size) {
        
        // 创建分页对象
        Page<User> page = PageUtil.createPage(current, size);
        
        // 查询用户
        IPage<User> userPage = userService.page(page);
        
        // 转换为DTO
        PageResult<UserDTO> dtoPage = PageUtil.convert(userPage, user -> {
            UserDTO dto = new UserDTO();
            BeanUtil.copyProperties(user, dto);
            // 脱敏处理
            dto.setPhone(StringUtil.maskPhone(user.getPhone()));
            dto.setEmail(StringUtil.maskEmail(user.getEmail()));
            return dto;
        });
        
        return ResponseResult.success(dtoPage);
    }
}
```

### 示例4：内存分页（适用于小数据量）

```java
@Service
public class ReportService {
    
    public PageResult<Report> getReports(Integer current, Integer size) {
        // 获取所有数据（数据量不大时）
        List<Report> allReports = this.list();
        
        // 内存分页
        PageResult<Report> result = PageUtil.toPageResult(allReports, current, size);
        
        return result;
    }
}
```

### 示例5：分页结果的使用

```java
@RestController
public class UserController {
    
    @GetMapping("/users")
    public ResponseResult<PageResult<User>> listUsers(
            @RequestParam(required = false) Integer current,
            @RequestParam(required = false) Integer size) {
        
        Page<User> page = PageUtil.createPage(current, size);
        IPage<User> iPage = userService.page(page);
        PageResult<User> result = PageUtil.toPageResult(iPage);
        
        // 使用分页结果
        log.info("总记录数: {}", result.getTotal());
        log.info("当前页: {}", result.getCurrent());
        log.info("每页大小: {}", result.getSize());
        log.info("总页数: {}", result.getPages());
        log.info("是否有上一页: {}", result.hasPrevious());
        log.info("是否有下一页: {}", result.hasNext());
        
        return ResponseResult.success(result);
    }
}
```

## 📊 PageResult 类说明

```java
public class PageResult<T> {
    private List<T> records;    // 数据列表
    private Long total;          // 总记录数
    private Long current;        // 当前页
    private Long size;           // 每页大小
    private Long pages;          // 总页数
    
    // 便捷方法
    public boolean hasPrevious();  // 是否有上一页
    public boolean hasNext();     // 是否有下一页
}
```

## 🔄 完整示例

### 前端请求格式

```javascript
// GET /api/users?current=1&size=10
```

### 后端响应格式

```json
{
  "code": 200,
  "status": true,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "user1",
        "email": "user1@example.com"
      }
    ],
    "total": 100,
    "current": 1,
    "size": 10,
    "pages": 10
  }
}
```

## ⚠️ 注意事项

1. **默认值**：如果 current 或 size 为 null，会自动设置为默认值（1 和 10）
2. **最大限制**：每页大小最大限制为 100，超过会自动调整为 100
3. **性能考虑**：内存分页适用于小数据量，大数据量建议使用数据库分页
4. **空值处理**：所有方法都进行了空值检查，不会抛出异常

## 🔗 相关工具类

- `BeanUtil` - 对象转换
- `StringUtil` - 字符串处理

