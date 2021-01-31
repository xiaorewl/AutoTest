package com.course.testng;


import org.testng.annotations.*;

public class BasicAnnotation {
    //最基本的注解，用来把方法标记为测试的一部分
    @Test
    public void testCase1() {
        System.out.println("这是测试用例1。Test最基本的注解，用来把方法标记为测试的一部分。");
        System.out.printf("----------Thread Id : %s%n",Thread.currentThread().getId());
    }

    @Test
    public void testCase2(){
       System.out.println("Test这是测试用例2。");
       System.out.printf("----------Thread Id : %s%n",Thread.currentThread().getId());
    }
    @BeforeMethod
    public void beforeMethod() {
        System.out.println("BeforeMethod这是在测试方法之前运行的。");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("AfterMethod这是在测试方法之后运行的。");
    }
    @BeforeClass
    public void beforeClass(){
        System.out.println("beforeClass这是在类运行之前运行的方法。");
    }
    @AfterClass
    public void afterClass() {
        System.out.println("afterClass这是在类运行之后运行的方法。");
    }
}


