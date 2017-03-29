package com.zq.controller;

import com.zq.model.User;
import com.zq.service.AnalysisService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangqiang on 2016/12/12.
 *
 * SpringMVC的四个基本注解
 * @Controller 控制层，就是我们的action层
 * @Service 业务逻辑层，就是我们的service或者manager层
 * @Repository 持久层，就是我们常说的DAO层
 * @Component （字面意思就是组件），它在你确定不了事哪一个层的时候使用。
 * 其实，这四个注解的效果都是一样的，spring都会把它们当做需要注入的Bean加载在上下文中；
 * 但是在项目中，却建议你严格按照除Component的其余三个注解的含义使用在项目中。这对分层结构的web架构很有好处！！
 */

// a
// 负责注册一个bean到SpringContext
@Controller
// 为Controller指定URL请求路径
@RequestMapping("/mvc")
public class MyController {

    // 要使用@Autowired自动注入，必须是spring-mvc.xml文件中<context:component-scan base-package="com.zq"/>声明包或
    // 子包中的类才可以
    @Autowired
    private AnalysisService analysisService;

    // ajax请求此方法不会跳转，会将此页面"/WEB-INF/jsp/hello.jsp"的静态代码返回给成功回调函数中
    // form表单action提交可以跳转
    @RequestMapping("/toHello")
    public String hello() {
        // 会加上spring-mvc.xml文件中org.springframework.web.servlet.view.InternalResourceViewResolver
        // 配置的 prefix和suffix，最终拼接成 prefix +"hello" + suffix 生成可访问的url路径
        return "hello";
    }


    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    //此注解不能省略 否则ajax无法接受返回值
    @ResponseBody
    public User getUserById(@RequestBody JSONObject requestJson, HttpServletRequest request) {
        // @RequestBody注解只能用来标注一个参数，用来接收前台传递的json对象，而request可以接收url中的参数
        System.out.println("id from URL parameter = " + request.getParameter("id"));
        return analysisService.getUserById(requestJson.getInt("id"));
    }

    /**
     * 这种方式传递前台参数，方法的参数名要与前台json对象的key相同
     *
     * @param beginIndex
     * @param endIndex
     * @return
     */
    @RequestMapping(value = "/getSomeUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<User> getSomeUsers(Integer beginIndex, Integer endIndex) {
        return analysisService.getSomeUsers(beginIndex, endIndex);
    }
}
