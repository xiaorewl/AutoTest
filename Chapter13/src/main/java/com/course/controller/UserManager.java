package com.course.controller;


import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@Api(value = "v1")
@RequestMapping("v1")
public class UserManager {

    @Resource  //视频中是@Autowired，但显示波浪线错误提示，改成了@Resource
    private SqlSessionTemplate template;

    @ApiOperation(value = "登陆接口",httpMethod = "POST")
    @PostMapping(value = "/login") //相当于@RequestMapping(value="/login",httpMethod="POST")
    public Boolean login(HttpServletResponse response,@RequestBody User user) {
        int i = template.selectOne("login",user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查询到的结果是：" + i);
        if(i==1) {
            log.info("登陆的用户是：" + user.getUserName());
            return true;
        }
        return false;
    }
    @ApiOperation(value = "添加用户接口",httpMethod = "POST")
    @PostMapping("/addUser")
    public Boolean addUser(HttpServletRequest request,@RequestBody User user) {
        Boolean x = verifyCookies(request);
        int result=0;
        //视频里是“if(x!=null) { ”我感觉不对啊
        if(x==true) {
            result = template.insert("addUser",user);
        }
        if(result > 0) {
            log.info("添加用户的数量是：" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户信息接口",httpMethod = "POST")
    @PostMapping("/getUserInfo")
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user) {
        Boolean x = verifyCookies(request);
        if(x==true) {
            List<User> users = template.selectList("getUserInfo",user);
            log.info("getUserInfo获取到的用户数量是：" + users.size());
            return users;
        }else {
            return null;
        }
    }

    @ApiOperation(value = "更新/删除用户信息",httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    public int updateUser(HttpServletRequest request,@RequestBody User user) {
        Boolean x = verifyCookies(request);
        int i = 0;
        if(x==true) {
            i = template.update("updateUserInfo",user);
        }
        log.info("更新数据的条目数为：" + i);
        return i;
    }

    //验证cookies信息
    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)) {
            log.info("cookies为空。");
            return false;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("login") &&
                    cookie.getValue().equals("true")) {
                log.info("cookies验证通过。");
                return true;
            }
        }
        return false;
    }
}
