package com.course.httpclient.cookies;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
    private String url;
    private ResourceBundle bundle;
    //用来存储cookies信息的变量
    private CookieStore store;

    @BeforeTest
    public void beforeTest() {
        bundle = ResourceBundle.getBundle("application",Locale.CHINA);
        url = bundle.getString("test.url");
    }

    //测试返回cookie信息的get接口
    @Test
    public void testGetCookies() throws IOException {
        String result;
        //从配置文件中拼接测试的url
        String uri = bundle.getString("test.getCookies.uri");
        //测试逻辑代码
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        //如果不获取cookie，用这一条也能正确返回
        //CloseableHttpClient client = HttpClients.createDefault();
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

    //测试必须携带cookies信息才能访问的get接口
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testGetWithCookies() throws IOException {
        String uri = bundle.getString("test.get.with.cookies.uri");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        //同时设置cookies信息
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.store).build();
        CloseableHttpResponse response = client.execute(get);
        //获取响应的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        if(statusCode == 200) {
            String result;
            result = EntityUtils.toString(response.getEntity(),"utf-8");
            System.out.println(result);
        }
    }
}
