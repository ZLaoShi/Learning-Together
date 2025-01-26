package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Post;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
