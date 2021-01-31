package com.course.utils;

import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);
    //根据传入的枚举类型，拼接各接口最终的访问url
    public static String getUrl(InterfaceName name) {
        String address = bundle.getString("test.url");
        String uri = "";
        //最终的完整测试地址
        String testUrl;
        switch(name) {
            case GETUSERLIST:
                uri = bundle.getString("getUserList.uri");
                break;
            case LOGIN:
                uri = bundle.getString("login.uri");
                break;
            case ADDUSER:
                uri = bundle.getString("addUser.uri");
                break;
            case GETUSERINFO:
                uri = bundle.getString("getUserInfo.uri");
                break;
            case UPDATEUSERINFO:
                uri = bundle.getString("updateUserInfo.uri");
        }

        testUrl = address + uri;
        return testUrl;
    }
}
