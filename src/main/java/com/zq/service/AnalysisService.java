package com.zq.service;

import com.zq.dao.UserDao;
import com.zq.model.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangqiang on 2016/12/14.
 */

@Service
public class AnalysisService {

    // UserDao需要@Repository,将类注册到SpringContext中才可以被@Resource检测到
    // @Resource属于javax.annotation.Resource正常SpringMVC应该使用@Autowired
    @Resource
    private UserDao userDao;

    public User getUserById(Integer id) {
        User user = userDao.selectByPrimaryKey(id);
        return user;
    }

    public List<User> getSomeUsers(Integer beginIndex, Integer endIndex) {
        return userDao.getSomeUsers(beginIndex, endIndex);
    }
}
