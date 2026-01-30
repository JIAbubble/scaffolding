package com.example.performanceevaluation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.performanceevaluation.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

