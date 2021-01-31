package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoginTest {

    @BeforeTest(groups = "loginTrue", description = "测试准备工作，获取HttpClient对象")
    public void beforeTest() {
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSER);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
        TestConfig.store = new BasicCookieStore();
        TestConfig.client = HttpClients.custom().setDefaultCookieStore(TestConfig.store).build();
    }

    @Test(groups = "loginTrue",description ="用户登录成功接口测试" )
    public void loginTrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        //获取用例数据
        //session.selectOne("SQLMapper.xml中的id",参数)
        LoginCase loginCase = session.selectOne("loginCase",1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //第一步是发送请求
        String result = getResult(loginCase);
        //第二步是验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }

    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        //设置头信息
        post.setHeader("Content-type","application/json");

        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        CloseableHttpResponse response = TestConfig.client.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }

    @Test(groups = "loginFalse",description = "用户登录失败接口测试")
    public void loginFalse() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase",2);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //第一步是发送请求
        String result = getResult(loginCase);
        //第二步是验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }

}
