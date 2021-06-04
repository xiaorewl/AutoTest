package com.course.testng.multiThread;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MultiTreadTest {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    @BeforeClass
    public void beforeClass(){
        System.out.println("Start Time: " + df.format(new Date()));
    }

    @Test(enabled=true, dataProvider="testdp", threadPoolSize=2, invocationCount=5)
    public void test(String dpNumber) throws InterruptedException{
        System.out.println("Current Thread Id: " + Thread.currentThread().getId() + ". Dataprovider number: "+ dpNumber);
        Thread.sleep(5000);
    }

    @DataProvider(name = "testdp", parallel = true)
    public static Object[][]testdp(){
        return new Object[][]{
                {"1"},
                {"2"}
        };
    }

    @AfterClass
    public void afterClass(){
        System.out.println("End Time: " + df.format(new Date()));
    }
}
