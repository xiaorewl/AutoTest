package com.course.server;

import com.course.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@Api(value = "/")
@RequestMapping("v1")
public class MyPostMethod {
    //这个变量是用来装我们的cookies信息的
    private static Cookie cookie;

    /**
     * 登陆接口
     * @param response
     * @param userName
     * @param password
     * @return
     */
    //用户登录成功获取到cookies，然后再访问其他接口获取到列表
    //@RequestParam(value = "userName",required = true) 说明：
    // value：前端显示名称，required=true：true表示这个参数是必传参数
    @PostMapping(value = "/login")
    @ApiOperation(value = "登录接口，成功后获取cookies信息",httpMethod = "POST")
    public String login(HttpServletResponse response,
                        @RequestParam(value = "userName",required = true) String userName,
                        @RequestParam(value = "password",required = true) String password) {
        if(userName.equals("zhangsan") && password.equals("123456")) {
            cookie = new Cookie("login","true");
            response.addCookie(cookie);
            return "恭喜你登陆成功了！";
        }
        return "用户名或者密码错误！";

    }

    /**
     * 登陆成功后获取用户列表接口
     * @param request
     * @param u
     * @return
     */
    @PostMapping("/getUserList")
    @ApiOperation(value = "获取用户列表",httpMethod = "POST")
    public String getUserList(HttpServletRequest request,
                            @RequestBody User u) {
        User user;
        //获取cookies
        Cookie[] cookies = request.getCookies();
       if(Objects.isNull(cookies)) {
            return "未带cookies";
        }
        for(Cookie c : cookies) {
            if(c.getName().equals("login")
                    && c.getValue().equals("true")
                    && u.getUserName().equals("zhangsan")
                    && u.getPassword().equals("123456")) {
                user = new User();
                user.setName("lisi");
                user.setAge("18");
                user.setSex("man");
                return user.toString();
            }
        }
        return "参数不合法。";
    }
}
