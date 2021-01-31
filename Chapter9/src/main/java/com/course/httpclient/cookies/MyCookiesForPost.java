package com.course.httpclient.cookies;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
    private String url;
    private ResourceBundle bundle;
    //用来存储cookies信息的变量
    private CookieStore store;

    @BeforeTest
    public void beforeTest() {
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }
    //测试返回cookie信息的get接口
    @Test
    public void testGetCookies() throws IOException {
        String result;
        //从配置文件中拼接测试的url
        String uri = bundle.getString("test.getCookies.uri");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        this.store = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.store).build();
        CloseableHttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

        //获取cookies信息
        List<Cookie> cookieList = store.getCookies();
        for(Cookie cookie:cookieList) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookie name = " + name + "; cookie value = " + value);
        }
    }
    //测试必须携带cookies信息才能访问的post接口
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testPostWithCookies() throws IOException {
        String uri = bundle.getString("test.post.with.cookies.uri");
        //拼接最终的测试地址
        String testUrl = this.url + uri;
        //声明一个client对象，用来进行方法的执行；设置cookies信息
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.store).build();
        //声明一个方法，这个方法就是post方法
        HttpPost post = new HttpPost(testUrl);
        //添加参数
        JSONObject param = new JSONObject();
        param.put("name","huhansan");
        param.put("age","18");

        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        CloseableHttpResponse response = client.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        //处理结果，就是判断返回结果是否符合预期
        //--将返回的响应结果字符串转化成为Json对象
        JSONObject resultJson = new JSONObject(result);
        //--获取到结果值
        String success = (String)resultJson.get("huhansan");
        String status = (String)resultJson.get("status");
        //--具体的判断返回结果的值
        Assert.assertEquals("success",success);
        Assert.assertEquals("1",status);

    }
}
