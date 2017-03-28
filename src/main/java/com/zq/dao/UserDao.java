package com.zq.dao;

import com.zq.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Component
@Repository
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 传递多个参数的时候需要用@Param注解方式，或者传递一个Map类型的参数
    List<User> getSomeUsers(@Param("beginIndex")Integer beginIndex, @Param("endIndex")Integer endIndex);
}