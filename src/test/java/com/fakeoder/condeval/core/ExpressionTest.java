package com.fakeoder.condeval.core;

import org.junit.Test;

public class ExpressionTest {


    @Test
    public void test01(){
        Object result =  Expression.eval("(0+2*0)>0",null);
        System.out.println(result);
    }

    @Test
    public void test02(){
        Object result =  Expression.eval("sqrt(4)>0",null);
        System.out.println(result);
    }
}
