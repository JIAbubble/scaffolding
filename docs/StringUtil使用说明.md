# StringUtil 字符串工具类使用说明

## 📋 概述

`StringUtil` 提供了常用的字符串操作方法，包括空值判断、字符串转换、格式化等功能。

## 🔧 常用方法

### 1. 空值判断

```java
// 判断字符串是否为空（null、空字符串、只包含空白字符）
boolean blank1 = StringUtil.isBlank(null);      // true
boolean blank2 = StringUtil.isBlank("");        // true
boolean blank3 = StringUtil.isBlank("   ");     // true
boolean blank4 = StringUtil.isBlank("hello");   // false

// 判断字符串是否不为空
boolean notBlank = StringUtil.isNotBlank("hello");  // true

// 判断字符串是否为空（null 或空字符串，不包括空白字符）
boolean empty1 = StringUtil.isEmpty(null);       // true
boolean empty2 = StringUtil.isEmpty("");        // true
boolean empty3 = StringUtil.isEmpty("   ");     // false
boolean empty4 = StringUtil.isEmpty("hello");   // false

// 判断字符串是否不为空
boolean notEmpty = StringUtil.isNotEmpty("hello");  // true
```

### 2. 默认值处理

```java
// 如果字符串为空，返回默认值
String result1 = StringUtil.defaultIfBlank(null, "default");     // "default"
String result2 = StringUtil.defaultIfBlank("", "default");         // "default"
String result3 = StringUtil.defaultIfBlank("value", "default");   // "value"

// 如果字符串为null，返回空字符串
String result = StringUtil.nullToEmpty(null);   // ""
String result = StringUtil.nullToEmpty("text"); // "text"
```

### 3. 字符串处理

```java
// 去除首尾空白字符
String trimmed = StringUtil.trim("  hello  ");  // "hello"

// 去除所有空白字符
String noSpace = StringUtil.removeWhitespace("hello world");  // "helloworld"

// 首字母大写
String capitalized = StringUtil.capitalize("hello");  // "Hello"

// 首字母小写
String uncapitalized = StringUtil.uncapitalize("Hello");  // "hello"
```

### 4. 命名转换

```java
// 驼峰命名转下划线命名
String underscore = StringUtil.camelToUnderscore("userName");      // "user_name"
String underscore2 = StringUtil.camelToUnderscore("userNameId");  // "user_name_id"

// 下划线命名转驼峰命名
String camel = StringUtil.underscoreToCamel("user_name");      // "userName"
String camel2 = StringUtil.underscoreToCamel("user_name_id"); // "userNameId"
```

### 5. 字符串截取

```java
String str = "Hello World";

// 安全截取（不会越界）
String sub1 = StringUtil.substring(str, 0, 5);   // "Hello"
String sub2 = StringUtil.substring(str, 6, 11);   // "World"
String sub3 = StringUtil.substring(str, 0, 100);   // "Hello World"（自动截取到末尾）

// 从指定位置开始截取
String sub4 = StringUtil.substring(str, 6);       // "World"
```

### 6. 字符串填充

```java
// 左填充（左侧补0）
String leftPadded = StringUtil.leftPad("123", 5, '0');  // "00123"

// 右填充（右侧补空格）
String rightPadded = StringUtil.rightPad("123", 5, ' ');  // "123  "

// 重复字符
String repeated = StringUtil.repeat('*', 5);  // "*****"
```

### 7. 字符串连接

```java
List<String> list = Arrays.asList("a", "b", "c");

// 使用指定分隔符连接
String joined = StringUtil.join(list, ",");  // "a,b,c"
String joined2 = StringUtil.join(list, " | ");  // "a | b | c"

// 连接数组
String[] array = {"a", "b", "c"};
String joined3 = StringUtil.join(array, "-");  // "a-b-c"
```

### 8. 字符串匹配（忽略大小写）

```java
String str = "Hello World";

// 判断是否包含指定子串（忽略大小写）
boolean contains = StringUtil.containsIgnoreCase(str, "hello");  // true
boolean contains2 = StringUtil.containsIgnoreCase(str, "HELLO");  // true

// 判断是否以指定前缀开头（忽略大小写）
boolean starts = StringUtil.startsWithIgnoreCase(str, "hello");  // true

// 判断是否以指定后缀结尾（忽略大小写）
boolean ends = StringUtil.endsWithIgnoreCase(str, "world");  // true
```

### 9. 敏感信息脱敏

```java
// 隐藏手机号中间4位
String phone = "13812345678";
String masked = StringUtil.maskPhone(phone);  // "138****5678"

// 隐藏邮箱中间部分
String email = "test@example.com";
String maskedEmail = StringUtil.maskEmail(email);  // "te***@example.com"
```

## 💡 实际应用示例

### 示例1：参数验证

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public ResponseResult<String> createUser(@RequestBody UserForm form) {
        // 验证用户名
        if (StringUtil.isBlank(form.getUsername())) {
            return ResponseResult.fail("用户名不能为空");
        }
        
        // 使用默认值
        String nickname = StringUtil.defaultIfBlank(form.getNickname(), "未设置昵称");
        
        // 创建用户
        // ...
    }
}
```

### 示例2：数据库字段转换

```java
@Service
public class UserService {
    
    // 将 Java 驼峰命名转换为数据库下划线命名
    public void saveUser(User user) {
        // 如果数据库字段是 user_name，Java 属性是 userName
        // 可以使用 camelToUnderscore 进行转换
        String dbField = StringUtil.camelToUnderscore("userName");  // "user_name"
    }
}
```

### 示例3：日志脱敏

```java
@Service
public class LogService {
    
    public void logUserInfo(User user) {
        // 脱敏处理
        String maskedPhone = StringUtil.maskPhone(user.getPhone());
        String maskedEmail = StringUtil.maskEmail(user.getEmail());
        
        log.info("用户信息 - 手机号: {}, 邮箱: {}", maskedPhone, maskedEmail);
    }
}
```

### 示例4：字符串格式化

```java
// 生成订单号（左填充0）
public String generateOrderNo(Long id) {
    String orderNo = StringUtil.leftPad(String.valueOf(id), 10, '0');
    return "ORD" + orderNo;  // "ORD0000000123"
}

// 格式化显示
public String formatList(List<String> items) {
    return StringUtil.join(items, "、");  // "项目1、项目2、项目3"
}
```

## ⚠️ 注意事项

1. **空值安全**：所有方法都进行了空值检查，不会抛出 `NullPointerException`
2. **性能考虑**：频繁操作大量字符串时，建议使用 `StringBuilder`
3. **编码问题**：所有方法默认使用 UTF-8 编码
4. **不可变性**：字符串是不可变的，所有方法都返回新字符串

## 🔗 相关工具类

- `ValidationUtil` - 字符串格式验证
- `SecurityUtil` - 字符串加密

