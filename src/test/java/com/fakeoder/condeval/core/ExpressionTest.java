package com.fakeoder.condeval.core;

import org.junit.Test;

public class ExpressionTest {


    @Test
    public void test01(){
        Object result =  Expression.eval("(0+2*0)>0",null);
        System.out.println(result);
        assert Boolean.valueOf(result.toString()).equals(Boolean.FALSE);
    }

    @Test
    public void test02(){
        Object result =  Expression.eval("sqrt(4)>0",null);
        System.out.println(result);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test03(){
        Object result =  Expression.eval("(sqrt(4)==2)||\"true\"",null);
        System.out.println(result);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test04(){
        Object result =  Expression.eval("startWith(abc,a)||\"false\"",null);
        System.out.println(result);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test05(){
        Object result =  Expression.eval("startWith(abc,a)||\"false\"",null);
        System.out.println(result);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }
}
