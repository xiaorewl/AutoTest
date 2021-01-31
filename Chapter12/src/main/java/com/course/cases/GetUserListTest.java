package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
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
import java.util.Arrays;
import java.util.List;

public class GetUserListTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取性别为男的用户信息")
    public void getUserList() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        //获取用例数据
        GetUserListCase getUserListCase = session.selectOne("getUserListCase",1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);

        //发送请求，获取结果
        JSONArray resultJson = getJsonResult(getUserListCase);
        //验证返回结果
        List<User> userList = session.selectList(getUserListCase.getExpected(),getUserListCase);
        for(User u : userList) {
            System.out.println("获取的user：" + u.toString());
        }
        JSONArray userListJson = new JSONArray(userList);
        //首先判断用户个数是否一致
        Assert.assertEquals(userListJson.length(),resultJson.length());

        //再比较每个用户内容是否一致,actual：实际结果，expect：预期结果
        for(int i=0; i<resultJson.length(); i++) {
            JSONObject actual = (JSONObject) resultJson.get(i);
            JSONObject expect = (JSONObject) userListJson.get(i);
            Assert.assertEquals(expect.toString(),actual.toString());
        }
    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        param.put("userName",getUserListCase.getUserName());
        param.put("age",getUserListCase.getAge());
        param.put("sex",getUserListCase.getSex());

        post.setHeader("Content-type","application/json");
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        CloseableHttpResponse response = TestConfig.client.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("result=" + result);
        //List resultList = Arrays.asList(result);
        JSONArray jsonArray =new JSONArray(result);

        return jsonArray;
    }
}
