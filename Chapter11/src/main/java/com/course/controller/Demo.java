package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Log4j2
@RestController
@Api(value = "v1")
@RequestMapping("v1")
public class Demo {
    //首先获取一个执行sql语句的对象

    //@Autowired  用这个出现警告，autowired field injection is  not recommended，换成了@Resource
    @Resource
    private SqlSessionTemplate template;

    @GetMapping("/getUserCount")
    @ApiOperation(value = "可以获取到用户数",httpMethod = "GET")
    public int getUserCount() {
        return template.selectOne("getUserCount");
    }

    @PostMapping("/addUser")
    public int addUser(@RequestBody User user) {
        //return template.insert("addUser",user);
        int result = template.insert("addUser",user);
        return result;
    }
    @PostMapping("/updateUser")
    public int updateUser(@RequestBody User user) {
        return template.update("updateUser",user);
    }

    @PostMapping("/deleteUser")
    public int delUser(@RequestParam int id) {
        return template.delete("deleteUser",id);
    }

}
