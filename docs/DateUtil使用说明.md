# DateUtil 日期时间工具类使用说明

## 📋 概述

`DateUtil` 提供了常用的日期时间操作方法，基于 Java 8+ 的 `LocalDateTime` 和 `LocalDate` API。

## 🔧 常用方法

### 1. 获取当前时间

```java
// 获取当前日期时间字符串（格式：yyyy-MM-dd HH:mm:ss）
String now = DateUtil.now();
// 输出：2025-01-22 14:30:45

// 获取当前日期字符串（格式：yyyy-MM-dd）
String today = DateUtil.today();
// 输出：2025-01-22
```

### 2. 格式化日期时间

```java
LocalDateTime dateTime = LocalDateTime.now();

// 标准格式：yyyy-MM-dd HH:mm:ss
String formatted = DateUtil.format(dateTime);
// 输出：2025-01-22 14:30:45

// 自定义格式
String custom = DateUtil.format(dateTime, "yyyy年MM月dd日 HH时mm分ss秒");
// 输出：2025年01月22日 14时30分45秒

// 格式化日期
LocalDate date = LocalDate.now();
String dateStr = DateUtil.formatDate(date);
// 输出：2025-01-22
```

### 3. 解析日期时间字符串

```java
// 解析日期时间字符串
String dateTimeStr = "2025-01-22 14:30:45";
LocalDateTime dateTime = DateUtil.parse(dateTimeStr);

// 解析日期字符串
String dateStr = "2025-01-22";
LocalDate date = DateUtil.parseDate(dateStr);
```

### 4. Date 与 LocalDateTime 互转

```java
// LocalDateTime 转 Date
LocalDateTime localDateTime = LocalDateTime.now();
Date date = DateUtil.toDate(localDateTime);

// Date 转 LocalDateTime
Date date = new Date();
LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
```

### 5. 日期计算

```java
LocalDate start = LocalDate.of(2025, 1, 1);
LocalDate end = LocalDate.of(2025, 1, 31);

// 计算两个日期之间的天数差
long days = DateUtil.daysBetween(start, end);
// 输出：30

// 计算两个日期时间之间的小时差
LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 15, 30);
long hours = DateUtil.hoursBetween(startTime, endTime);
// 输出：5

// 计算分钟差
long minutes = DateUtil.minutesBetween(startTime, endTime);
// 输出：330
```

### 6. 日期加减

```java
LocalDate date = LocalDate.of(2025, 1, 22);

// 加天数
LocalDate future = DateUtil.plusDays(date, 7);
// 输出：2025-01-29

// 减天数
LocalDate past = DateUtil.minusDays(date, 7);
// 输出：2025-01-15
```

### 7. 日期判断

```java
LocalDate date = LocalDate.of(2025, 1, 22);
LocalDate start = LocalDate.of(2025, 1, 1);
LocalDate end = LocalDate.of(2025, 1, 31);

// 判断日期是否在指定范围内
boolean inRange = DateUtil.isBetween(date, start, end);
// 输出：true

// 判断是否为今天
boolean isToday = DateUtil.isToday(date);
```

### 8. 时间戳

```java
// 获取时间戳（秒）
long timestamp = DateUtil.getTimestamp();
// 输出：1705902645

// 获取时间戳（毫秒）
long timestampMillis = DateUtil.getTimestampMillis();
// 输出：1705902645123
```

## 💡 实际应用示例

### 示例1：在实体类中使用

```java
@Entity
public class Order {
    private LocalDateTime createdAt;
    
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
    
    public String getCreatedAtStr() {
        return DateUtil.format(this.createdAt);
    }
}
```

### 示例2：在 Service 中使用

```java
@Service
public class OrderService {
    
    public List<Order> getOrdersInRange(String startDate, String endDate) {
        LocalDate start = DateUtil.parseDate(startDate);
        LocalDate end = DateUtil.parseDate(endDate);
        
        // 查询指定日期范围内的订单
        // ...
    }
    
    public long calculateDaysOverdue(Order order) {
        LocalDate dueDate = order.getDueDate();
        LocalDate today = LocalDate.now();
        return DateUtil.daysBetween(today, dueDate);
    }
}
```

### 示例3：在 Controller 中使用

```java
@RestController
public class OrderController {
    
    @GetMapping("/orders")
    public ResponseResult<List<Order>> getOrders(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDate start = DateUtil.parseDate(startDate);
        LocalDate end = DateUtil.parseDate(endDate);
        
        // 查询订单
        // ...
    }
}
```

## 📚 常量定义

```java
// 标准日期时间格式
DateUtil.DATETIME_PATTERN  // "yyyy-MM-dd HH:mm:ss"

// 标准日期格式
DateUtil.DATE_PATTERN      // "yyyy-MM-dd"

// 标准时间格式
DateUtil.TIME_PATTERN      // "HH:mm:ss"
```

## ⚠️ 注意事项

1. **时区问题**：所有方法默认使用系统时区
2. **空值处理**：方法会自动处理 null 值，返回 null
3. **日期格式**：解析时格式必须匹配，否则会抛出异常
4. **性能考虑**：频繁调用建议缓存 `DateTimeFormatter` 对象

## 🔗 相关工具类

- `ValidationUtil.isDate()` - 验证日期格式
- `ValidationUtil.isDateTime()` - 验证日期时间格式

