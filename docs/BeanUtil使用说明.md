# BeanUtil Bean工具类使用说明

## 📋 概述

`BeanUtil` 封装了 Spring 的 `BeanUtils`，提供了对象属性复制和类型转换的便捷方法。

## 🔧 常用方法

### 1. 复制对象属性

```java
// 基本复制
User source = new User();
source.setId(1L);
source.setUsername("testuser");
source.setEmail("test@example.com");

User target = new User();
BeanUtil.copyProperties(source, target);

// target 现在拥有 source 的所有属性值
```

### 2. 复制属性（忽略指定字段）

```java
User source = new User();
source.setId(1L);
source.setUsername("testuser");
source.setPassword("password123");

User target = new User();
// 忽略 id 和 password 字段
BeanUtil.copyProperties(source, target, "id", "password");

// target 只有 username，没有 id 和 password
```

### 3. 复制属性（忽略null值）

```java
User source = new User();
source.setId(1L);
source.setUsername("testuser");
source.setEmail(null);  // null 值

User target = new User();
target.setEmail("existing@example.com");  // 已有值

// 只复制非null的属性，保留 target 的 email
BeanUtil.copyPropertiesIgnoreNull(source, target);
```

### 4. 对象类型转换

```java
User user = new User();
user.setId(1L);
user.setUsername("testuser");
user.setEmail("test@example.com");

// 转换为 UserDTO
UserDTO dto = BeanUtil.convert(user, UserDTO.class);

// 转换为 UserDTO（忽略null值）
UserDTO dto2 = BeanUtil.convertIgnoreNull(user, UserDTO.class);
```

### 5. List对象转换

```java
List<User> users = Arrays.asList(
    new User(1L, "user1", "user1@example.com"),
    new User(2L, "user2", "user2@example.com")
);

// 转换为 UserDTO 列表
List<UserDTO> dtos = BeanUtil.convertList(users, UserDTO.class);
```

### 6. 判断对象是否为空

```java
User user = new User();  // 所有属性都是 null

// 判断对象是否为空（所有属性都为null）
boolean isEmpty = BeanUtil.isEmpty(user);  // true

user.setUsername("testuser");
boolean isEmpty2 = BeanUtil.isEmpty(user);  // false
```

## 💡 实际应用示例

### 示例1：在Controller中使用（Entity转DTO）

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseResult<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseResult.fail("用户不存在");
        }
        
        // 转换为DTO
        UserDTO dto = BeanUtil.convert(user, UserDTO.class);
        
        return ResponseResult.success(dto);
    }
    
    @GetMapping
    public ResponseResult<List<UserDTO>> listUsers() {
        List<User> users = userService.list();
        
        // 转换为DTO列表
        List<UserDTO> dtos = BeanUtil.convertList(users, UserDTO.class);
        
        return ResponseResult.success(dtos);
    }
}
```

### 示例2：在Service中使用（Form转Entity）

```java
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    
    public void createUser(UserCreateForm form) {
        // Form 转 Entity
        User user = BeanUtil.convert(form, User.class);
        user.setCreatedAt(LocalDateTime.now());
        
        this.save(user);
    }
    
    public void updateUser(Long id, UserUpdateForm form) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 只更新非null的属性
        BeanUtil.copyPropertiesIgnoreNull(form, user);
        user.setUpdatedAt(LocalDateTime.now());
        
        this.updateById(user);
    }
}
```

### 示例3：更新操作（忽略敏感字段）

```java
@Service
public class UserService {
    
    public void updateUser(Long id, UserUpdateForm form) {
        User user = this.getById(id);
        
        // 更新时忽略 id、password、createdAt 等字段
        BeanUtil.copyProperties(form, user, 
            "id", "password", "createdAt", "deleted");
        
        user.setUpdatedAt(LocalDateTime.now());
        this.updateById(user);
    }
}
```

### 示例4：分页结果转换

```java
@RestController
public class UserController {
    
    @GetMapping("/users")
    public ResponseResult<PageResult<UserDTO>> listUsers(
            @RequestParam(required = false) Integer current,
            @RequestParam(required = false) Integer size) {
        
        Page<User> page = PageUtil.createPage(current, size);
        IPage<User> userPage = userService.page(page);
        
        // 转换为DTO分页结果
        PageResult<UserDTO> dtoPage = PageUtil.convert(userPage, user -> {
            return BeanUtil.convert(user, UserDTO.class);
        });
        
        return ResponseResult.success(dtoPage);
    }
}
```

### 示例5：对象合并

```java
@Service
public class UserService {
    
    public void mergeUserInfo(Long id, UserPartialUpdateForm form) {
        User existing = this.getById(id);
        
        // 只更新 form 中非null的属性，保留 existing 的其他属性
        BeanUtil.copyPropertiesIgnoreNull(form, existing);
        
        this.updateById(existing);
    }
}
```

### 示例6：DTO转Entity（用于更新）

```java
@Service
public class OrderService {
    
    public void updateOrder(Long id, OrderDTO dto) {
        Order order = this.getById(id);
        
        // 转换DTO到Entity，但忽略某些字段
        BeanUtil.copyProperties(dto, order, 
            "id", "orderNo", "createdAt", "status");
        
        this.updateById(order);
    }
}
```

## 🔄 完整示例

### Entity 和 DTO 定义

```java
// Entity
@Data
@TableName("sys_user")
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}

// DTO
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // 不包含 password 和 createdAt
}

// Form
@Data
public class UserCreateForm {
    private String username;
    private String email;
    private String password;
}
```

### 使用流程

```java
// 1. 创建用户（Form -> Entity）
UserCreateForm form = new UserCreateForm();
form.setUsername("testuser");
form.setEmail("test@example.com");
form.setPassword("password123");

User user = BeanUtil.convert(form, User.class);
user.setCreatedAt(LocalDateTime.now());
userService.save(user);

// 2. 查询用户（Entity -> DTO）
User user = userService.getById(1L);
UserDTO dto = BeanUtil.convert(user, UserDTO.class);

// 3. 更新用户（Form -> Entity，忽略null）
UserUpdateForm updateForm = new UserUpdateForm();
updateForm.setEmail("newemail@example.com");
// username 为 null，不会更新

User user = userService.getById(1L);
BeanUtil.copyPropertiesIgnoreNull(updateForm, user);
userService.updateById(user);
```

## ⚠️ 注意事项

1. **属性名匹配**：源对象和目标对象的属性名必须相同
2. **类型兼容**：属性类型必须兼容（可以自动转换）
3. **null值处理**：`copyPropertiesIgnoreNull` 会忽略null值，保留目标对象的原值
4. **性能考虑**：大量对象转换时，反射有一定性能开销
5. **深拷贝**：这是浅拷贝，嵌套对象不会递归复制

## 🔗 相关工具类

- `PageUtil` - 分页工具
- `JsonUtil` - JSON工具（可用于深拷贝）

