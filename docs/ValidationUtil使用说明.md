# ValidationUtil 验证工具类使用说明

## 📋 概述

`ValidationUtil` 提供了常用的数据验证方法，包括邮箱、手机号、身份证、URL等格式验证。

## 🔧 常用方法

### 1. 邮箱验证

```java
// 验证邮箱格式
boolean valid1 = ValidationUtil.isEmail("test@example.com");      // true
boolean valid2 = ValidationUtil.isEmail("invalid-email");         // false
boolean valid3 = ValidationUtil.isEmail("test@example");          // false
```

### 2. 手机号验证

```java
// 验证手机号格式（11位数字，1开头）
boolean valid1 = ValidationUtil.isPhone("13812345678");    // true
boolean valid2 = ValidationUtil.isPhone("1381234567");    // false（位数不对）
boolean valid3 = ValidationUtil.isPhone("23812345678");   // false（不是1开头）
```

### 3. 身份证号验证

```java
// 验证身份证号格式（18位或15位）
boolean valid1 = ValidationUtil.isIdCard("110101199001011234");  // true（18位）
boolean valid2 = ValidationUtil.isIdCard("110101900101123");    // true（15位）
boolean valid3 = ValidationUtil.isIdCard("123456789");          // false
```

### 4. URL验证

```java
// 验证URL格式
boolean valid1 = ValidationUtil.isUrl("https://www.example.com");     // true
boolean valid2 = ValidationUtil.isUrl("http://example.com/path");     // true
boolean valid3 = ValidationUtil.isUrl("not-a-url");                   // false
```

### 5. IP地址验证

```java
// 验证IP地址格式
boolean valid1 = ValidationUtil.isIp("192.168.1.1");      // true
boolean valid2 = ValidationUtil.isIp("127.0.0.1");        // true
boolean valid3 = ValidationUtil.isIp("256.1.1.1");        // false（超出范围）
```

### 6. 中文验证

```java
// 验证是否为中文
boolean valid1 = ValidationUtil.isChinese("中文");        // true
boolean valid2 = ValidationUtil.isChinese("hello");       // false
boolean valid3 = ValidationUtil.isChinese("中文123");     // false（包含数字）
```

### 7. 数字验证

```java
// 验证是否为数字
boolean valid1 = ValidationUtil.isNumber("123");          // true
boolean valid2 = ValidationUtil.isNumber("123.45");       // false（包含小数点）
boolean valid3 = ValidationUtil.isNumber("abc");          // false

// 验证是否为整数（包括负数）
boolean valid4 = ValidationUtil.isInteger("123");         // true
boolean valid5 = ValidationUtil.isInteger("-123");       // true
boolean valid6 = ValidationUtil.isInteger("123.45");     // false

// 验证是否为浮点数
boolean valid7 = ValidationUtil.isDecimal("123.45");      // true
boolean valid8 = ValidationUtil.isDecimal("-123.45");    // true
boolean valid9 = ValidationUtil.isDecimal("123");        // true（整数也算浮点数）
```

### 8. 字符串长度验证

```java
// 验证字符串长度范围
boolean valid1 = ValidationUtil.isLengthValid("hello", 3, 10);   // true
boolean valid2 = ValidationUtil.isLengthValid("hi", 3, 10);      // false（太短）
boolean valid3 = ValidationUtil.isLengthValid("hello world", 3, 10);  // false（太长）

// 验证字符串固定长度
boolean valid4 = ValidationUtil.isLengthValid("12345", 5);       // true
boolean valid5 = ValidationUtil.isLengthValid("1234", 5);        // false
```

### 9. 密码强度验证

```java
// 验证密码强度（至少包含字母和数字，长度6-20）
boolean valid1 = ValidationUtil.isStrongPassword("Pass123");     // true
boolean valid2 = ValidationUtil.isStrongPassword("password");    // false（只有字母）
boolean valid3 = ValidationUtil.isStrongPassword("123456");      // false（只有数字）
boolean valid4 = ValidationUtil.isStrongPassword("Pass");        // false（太短）
```

### 10. 用户名验证

```java
// 验证用户名格式（字母、数字、下划线，3-20位）
boolean valid1 = ValidationUtil.isUsername("user123");          // true
boolean valid2 = ValidationUtil.isUsername("user_name");        // true
boolean valid3 = ValidationUtil.isUsername("user-name");        // false（包含连字符）
boolean valid4 = ValidationUtil.isUsername("ab");                // false（太短）
```

### 11. 日期格式验证

```java
// 验证日期格式（yyyy-MM-dd）
boolean valid1 = ValidationUtil.isDate("2025-01-22");           // true
boolean valid2 = ValidationUtil.isDate("2025/01/22");            // false（格式不对）
boolean valid3 = ValidationUtil.isDate("2025-13-22");            // false（月份无效）

// 验证日期时间格式（yyyy-MM-dd HH:mm:ss）
boolean valid4 = ValidationUtil.isDateTime("2025-01-22 14:30:45");  // true
boolean valid5 = ValidationUtil.isDateTime("2025-01-22");          // false（缺少时间）
```

## 💡 实际应用示例

### 示例1：在Controller中使用

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public ResponseResult<String> createUser(@RequestBody UserForm form) {
        // 验证邮箱
        if (!ValidationUtil.isEmail(form.getEmail())) {
            return ResponseResult.fail("邮箱格式不正确");
        }
        
        // 验证手机号
        if (StringUtil.isNotBlank(form.getPhone()) 
            && !ValidationUtil.isPhone(form.getPhone())) {
            return ResponseResult.fail("手机号格式不正确");
        }
        
        // 验证密码强度
        if (!ValidationUtil.isStrongPassword(form.getPassword())) {
            return ResponseResult.fail("密码必须包含字母和数字，长度6-20位");
        }
        
        // 创建用户
        // ...
    }
}
```

### 示例2：在Service中使用

```java
@Service
public class UserService {
    
    public void validateUserInfo(User user) {
        // 验证用户名
        if (!ValidationUtil.isUsername(user.getUsername())) {
            throw new RuntimeException("用户名格式不正确");
        }
        
        // 验证邮箱
        if (!ValidationUtil.isEmail(user.getEmail())) {
            throw new RuntimeException("邮箱格式不正确");
        }
        
        // 验证手机号
        if (StringUtil.isNotBlank(user.getPhone()) 
            && !ValidationUtil.isPhone(user.getPhone())) {
            throw new RuntimeException("手机号格式不正确");
        }
    }
}
```

### 示例3：表单验证

```java
@Component
public class FormValidator {
    
    public ResponseResult<String> validateRegisterForm(RegisterForm form) {
        // 用户名验证
        if (StringUtil.isBlank(form.getUsername())) {
            return ResponseResult.fail("用户名不能为空");
        }
        if (!ValidationUtil.isUsername(form.getUsername())) {
            return ResponseResult.fail("用户名格式不正确（3-20位字母、数字、下划线）");
        }
        
        // 邮箱验证
        if (StringUtil.isBlank(form.getEmail())) {
            return ResponseResult.fail("邮箱不能为空");
        }
        if (!ValidationUtil.isEmail(form.getEmail())) {
            return ResponseResult.fail("邮箱格式不正确");
        }
        
        // 手机号验证（可选）
        if (StringUtil.isNotBlank(form.getPhone()) 
            && !ValidationUtil.isPhone(form.getPhone())) {
            return ResponseResult.fail("手机号格式不正确");
        }
        
        // 密码验证
        if (StringUtil.isBlank(form.getPassword())) {
            return ResponseResult.fail("密码不能为空");
        }
        if (!ValidationUtil.isStrongPassword(form.getPassword())) {
            return ResponseResult.fail("密码必须包含字母和数字，长度6-20位");
        }
        
        return ResponseResult.success("验证通过");
    }
}
```

### 示例4：数据导入验证

```java
@Service
public class DataImportService {
    
    public void validateImportedData(List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            // 验证邮箱
            String email = (String) row.get("email");
            if (!ValidationUtil.isEmail(email)) {
                throw new RuntimeException("无效的邮箱: " + email);
            }
            
            // 验证手机号
            String phone = (String) row.get("phone");
            if (StringUtil.isNotBlank(phone) 
                && !ValidationUtil.isPhone(phone)) {
                throw new RuntimeException("无效的手机号: " + phone);
            }
            
            // 验证日期
            String dateStr = (String) row.get("birthDate");
            if (!ValidationUtil.isDate(dateStr)) {
                throw new RuntimeException("无效的日期格式: " + dateStr);
            }
        }
    }
}
```

### 示例5：配置验证

```java
@Service
public class ConfigService {
    
    public void validateConfig(Config config) {
        // 验证URL
        if (StringUtil.isNotBlank(config.getApiUrl()) 
            && !ValidationUtil.isUrl(config.getApiUrl())) {
            throw new RuntimeException("API地址格式不正确");
        }
        
        // 验证IP地址
        if (StringUtil.isNotBlank(config.getServerIp()) 
            && !ValidationUtil.isIp(config.getServerIp())) {
            throw new RuntimeException("服务器IP地址格式不正确");
        }
    }
}
```

## ⚠️ 注意事项

1. **格式验证**：只验证格式，不验证真实性（如邮箱是否存在）
2. **空值处理**：null 值会返回 false，建议先使用 `StringUtil.isBlank()` 判断
3. **性能考虑**：正则表达式验证有一定性能开销，大量数据时注意优化
4. **国际化**：部分验证规则（如手机号）针对中国，需要根据实际情况调整

## 🔗 相关工具类

- `StringUtil` - 字符串处理
- `SecurityUtil` - 密码加密

