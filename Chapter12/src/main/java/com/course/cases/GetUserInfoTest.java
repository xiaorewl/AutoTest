package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import com.mongodb.util.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取userId为2的用户信息")
    public void getUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",2);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        //发送请求，获取结果
        JSONArray resultJson = getJsonResult(getUserInfoCase);
        //验证返回结果
        User user = session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        //user放在List中，才能变为JSONArray
        List userList =new ArrayList();
        userList.add(user);
        JSONArray jsonArray = new JSONArray(userList);
        JSONArray jsonArray1 = new JSONArray(resultJson.getString(0));
        Assert.assertEquals(jsonArray.toString(),jsonArray1.toString());
        Assert.assertEquals(jsonArray,jsonArray);
    }

    private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        post.setHeader("Content-type","application/json");
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        CloseableHttpResponse response = TestConfig.client.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList = Arrays.asList(result);
        JSONArray jsonArray = new JSONArray(resultList);
        return jsonArray;
    }
}
