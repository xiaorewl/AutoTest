package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class UpdateUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
    public void updateUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);
        //发送请求，获取结果
        int result = getResult(updateUserInfoCase);
       //Thread.sleep(8000);
        System.out.println("更新的数为" + result);
        //验证返回结果
        //接口是更新/删除指定UserId的用户的信息，所以是返回一条数据
        User user = session.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);
      //Thread.sleep(8000);
        System.out.println("user=" + user.toString());
        Assert.assertNotNull(user);

        //我认为应该根据判断是否返回0，确定是否更新成功，接口中默认是返回0的。
        Assert.assertNotEquals(0,result);
        //视频里老师判断是否返回Null
        //Assert.assertNotNull(result);
    }

    @Test(dependsOnGroups = "loginTrue",description = "删除用户信息")
    public void deleteUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",2);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        //发送请求，获取结果
        int result = getResult(updateUserInfoCase);
        //Thread.sleep(10000);
        System.out.println("更新的数为" + result);
        //验证返回结果
        //接口是更新/删除指定UserId的用户的信息，所以是返回一条数据
        User user = session.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);
        //Thread.sleep(10000);
        System.out.println("user=" + user.toString());
        Assert.assertNotNull(user);
        //我认为应该根据判断是否返回0，确定是否更新成功，接口中默认是返回0的。
        Assert.assertNotEquals(0,result);
        //视频里老师判断是否返回空
       //Assert.assertNotNull(result);
    }

    private int getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",updateUserInfoCase.getUserId());
        param.put("userName",updateUserInfoCase.getUserName());
        param.put("age",updateUserInfoCase.getAge());
        param.put("sex",updateUserInfoCase.getSex());
        param.put("permission",updateUserInfoCase.getPermission());
        param.put("isDelete",updateUserInfoCase.getIsDelete());
        post.setHeader("Content-type","application/json");
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        CloseableHttpResponse response = TestConfig.client.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return Integer.parseInt(result);

    }
}
