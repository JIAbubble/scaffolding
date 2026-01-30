# SecurityUtil.verifyPassword 使用说明

## 📋 方法签名

```java
public static boolean verifyPassword(String password, String salt, String hashedPassword);
```

## 📝 参数说明

| 参数 | 类型 | 说明 |
|------|------|------|
| `password` | String | 用户输入的**原始密码**（未加密） |
| `salt` | String | **盐值**（注册时生成的随机字符串，存储在数据库中） |
| `hashedPassword` | String | **加密后的密码**（注册时生成的，存储在数据库中） |

## 🔄 返回值

- `true` - 密码验证通过
- `false` - 密码验证失败

## 🎯 使用场景

### 场景1：用户注册

```java
// 1. 用户注册时，生成盐值并加密密码
String userPassword = "myPassword123";

// 生成随机盐值（建议16-32位）
String salt = SecurityUtil.generateRandomString(16);

// 使用盐值加密密码
String hashedPassword = SecurityUtil.hashPassword(userPassword, salt);

// 保存到数据库
User user = new User();
user.setUsername("testuser");
user.setPassword(hashedPassword);  // 存储加密后的密码
user.setSalt(salt);                 // 存储盐值
userService.save(user);
```

### 场景2：用户登录验证

```java
// 2. 用户登录时，验证密码
String inputPassword = "myPassword123";  // 用户输入的原始密码

// 从数据库获取用户信息
User user = userService.getOne(
    new LambdaQueryWrapper<User>()
        .eq(User::getUsername, "testuser")
);

if (user != null) {
    // 使用 verifyPassword 验证密码
    boolean isValid = SecurityUtil.verifyPassword(
        inputPassword,           // 用户输入的原始密码
        user.getSalt(),          // 数据库中存储的盐值
        user.getPassword()       // 数据库中存储的加密密码
    );
    
/*
    if (isValid) {
        // 密码正确，登录成功
        return "登录成功";
    } else {
        // 密码错误
        return "密码错误";
    }
}*/

```

## 💡 完整示例

### 在 UserService 中使用

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    /**
     * 用户注册
     */
    public void register(String username, String password) {
        // 1. 检查用户名是否已存在
        User existing = this.getOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 2. 生成盐值
        String salt = SecurityUtil.generateRandomString(16);
        
        // 3. 加密密码
        String hashedPassword = SecurityUtil.hashPassword(password, salt);
        
        // 4. 创建用户
        User user = User.builder()
            .username(username)
            .password(hashedPassword)
            .salt(salt)
            .createdAt(LocalDateTime.now())
            .build();
        
        // 5. 保存到数据库
        this.save(user);
    }
    
    /**
     * 用户登录
     */
    public boolean login(String username, String password) {
        // 1. 查询用户
        User user = this.getOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
        
        if (user == null) {
            return false;  // 用户不存在
        }
        
        // 2. 验证密码
        return SecurityUtil.verifyPassword(
            password,           // 用户输入的原始密码
            user.getSalt(),     // 数据库中存储的盐值
            user.getPassword()  // 数据库中存储的加密密码
        );
    }
}
```

### 在 Controller 中使用

```java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody RegisterForm form) {
        try {
            userService.register(form.getUsername(), form.getPassword());
            return ResponseResult.success("注册成功");
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginForm form) {
        boolean isValid = userService.login(form.getUsername(), form.getPassword());
        if (isValid) {
            // 生成token等操作...
            return ResponseResult.success("登录成功");
        } else {
            return ResponseResult.fail("用户名或密码错误");
        }
    }
}
```

## ⚠️ 注意事项

1. **盐值必须唯一**：每个用户都应该有唯一的盐值，不能共享
2. **盐值长度**：建议使用16-32位的随机字符串作为盐值
3. **盐值存储**：盐值必须和加密密码一起存储在数据库中
4. **密码强度**：建议在注册时验证密码强度（可使用 `ValidationUtil.isStrongPassword()`）
5. **安全性**：MD5 加密相对较弱，生产环境建议使用更安全的加密方式（如 BCrypt）

## 🔐 安全建议

如果需要更高的安全性，可以考虑：

1. **使用 BCrypt**（推荐）：
```java
// 需要添加依赖：org.springframework.security:spring-security-crypto
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(password);  // 自动包含盐值
boolean isValid = encoder.matches(password, hashedPassword);  // 验证
```

2. **使用 SHA-256 + 盐值**（比 MD5 更安全）：
```java
// 可以修改 SecurityUtil.hashPassword 方法使用 SHA-256
String hash = SecurityUtil.sha256(password + salt);
```

## 📚 相关方法

- `SecurityUtil.hashPassword()` - 密码加密
- `SecurityUtil.generateRandomString()` - 生成随机盐值
- `SecurityUtil.md5()` - MD5加密
- `SecurityUtil.sha256()` - SHA256加密

