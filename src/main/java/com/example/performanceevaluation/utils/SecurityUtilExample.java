package com.example.performanceevaluation.utils;

import com.example.performanceevaluation.pojo.User;

/**
 * SecurityUtil.verifyPassword 使用示例
 * 
 * 这个方法用于验证用户输入的密码是否正确
 * 
 * 使用场景：
 * 1. 用户注册时：生成盐值，加密密码并存储
 * 2. 用户登录时：使用存储的盐值和加密密码验证用户输入的密码
 */
public class SecurityUtilExample {

    /**
     * 示例1：用户注册流程
     * 
     * 步骤：
     * 1. 生成随机盐值（通常16-32位）
     * 2. 使用 hashPassword 方法加密密码（密码 + 盐值）
     * 3. 将盐值和加密后的密码存储到数据库
     */
    public static void registerExample() {
        // 用户输入的原始密码
        String userPassword = "myPassword123";
        
        // 1. 生成随机盐值（建议16-32位）
        String salt = SecurityUtil.generateRandomString(16);
        System.out.println("生成的盐值: " + salt);
        
        // 2. 使用盐值加密密码
        String hashedPassword = SecurityUtil.hashPassword(userPassword, salt);
        System.out.println("加密后的密码: " + hashedPassword);
        
        // 3. 将 salt 和 hashedPassword 存储到数据库
        // 例如：
        // User user = new User();
        // user.setPassword(hashedPassword);
        // user.setSalt(salt);
        // userService.save(user);
    }

    /**
     * 示例2：用户登录验证流程
     * 
     * 步骤：
     * 1. 从数据库获取用户的盐值和加密密码
     * 2. 使用 verifyPassword 方法验证用户输入的密码
     * 3. 返回验证结果
     */
    public static void loginExample() {
        // 用户登录时输入的密码
        String inputPassword = "myPassword123";
        
        // 从数据库获取的用户信息（模拟）
        String storedSalt = "abc123xyz456";  // 数据库中存储的盐值
        String storedHashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99";  // 数据库中存储的加密密码
        
        // 使用 verifyPassword 验证密码
        boolean isValid = SecurityUtil.verifyPassword(inputPassword, storedSalt, storedHashedPassword);
        
        if (isValid) {
            System.out.println("密码正确，登录成功！");
        } else {
            System.out.println("密码错误，登录失败！");
        }
    }

/*    *//**
     * 示例3：在 Service 中的实际使用
     *//*
    public static class UserServiceExample {
        
        *//**
         * 注册用户
         *//*
        public void register(String username, String password) {
            // 1. 生成盐值
            String salt = SecurityUtil.generateRandomString(16);
            
            // 2. 加密密码
            String hashedPassword = SecurityUtil.hashPassword(password, salt);
            
          //  3. 创建用户对象
            User user = User.builder()
                .username(username)
                .password(hashedPassword)  // 存储加密后的密码
                .salt(salt)                 // 存储盐值
                .build();
            
         //   4. 保存到数据库
            userService.save(user);
        }
        
        *//**
         * 登录验证
         *//*
        public boolean login(String username, String password) {
            // 1. 从数据库查询用户
            // User user = userService.getOne(
            //     new LambdaQueryWrapper<User>()
            //         .eq(User::getUsername, username)
            // );
            
            // 2. 验证密码
            // if (user != null) {
            //     boolean isValid = SecurityUtil.verifyPassword(
            //         password,                    // 用户输入的原始密码
            //         user.getSalt(),              // 数据库中存储的盐值
            //         user.getPassword()          // 数据库中存储的加密密码
            //     );
            //     return isValid;
            // }
            
            // return false;
            return false; // 示例代码
        }
    }*/

    /**
     * 示例4：完整的使用流程演示
     */
    public static void completeExample() {
        System.out.println("=== 用户注册流程 ===");
        
        // 用户注册
        String originalPassword = "user123456";
        String salt = SecurityUtil.generateRandomString(16);
        String hashedPassword = SecurityUtil.hashPassword(originalPassword, salt);
        
        System.out.println("原始密码: " + originalPassword);
        System.out.println("盐值: " + salt);
        System.out.println("加密密码: " + hashedPassword);
        System.out.println();
        
        System.out.println("=== 用户登录验证 ===");
        
        // 用户登录（输入正确的密码）
        String loginPassword1 = "user123456";
        boolean result1 = SecurityUtil.verifyPassword(loginPassword1, salt, hashedPassword);
        System.out.println("输入密码: " + loginPassword1);
        System.out.println("验证结果: " + (result1 ? "✓ 密码正确" : "✗ 密码错误"));
        System.out.println();
        
        // 用户登录（输入错误的密码）
        String loginPassword2 = "wrongPassword";
        boolean result2 = SecurityUtil.verifyPassword(loginPassword2, salt, hashedPassword);
        System.out.println("输入密码: " + loginPassword2);
        System.out.println("验证结果: " + (result2 ? "✓ 密码正确" : "✗ 密码错误"));
    }

    /**
     * 主方法：运行示例
     */
    public static void main(String[] args) {
        completeExample();
    }
}

