package com.fakeoder.evall.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ExpressionTest {


    @Test
    public void test01(){
        Object result =  Expression.eval("(0+2*0)>0",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.FALSE);
    }

    @Test
    public void test02(){
        Object result =  Expression.eval("sqrt(4)>0",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test03(){
        Object result =  Expression.eval("(sqrt(4)==3)||concat(true,'')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test04(){
        Object result =  Expression.eval("startWith(abc,a)||concat(false,'')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test05(){
        Object result =  Expression.eval("startWith(abc,a)||concat(false,'')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }


    @Test
    public void test06(){
        Object result =  Expression.eval("startWith(abc,a)||concat(false,'')||(indexOf(abc,a)==1)",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test07(){
        Object result =  Expression.eval("substring(abcdef,2,2)",null);
        assert result.toString().isEmpty();
    }

    @Test
    public void test08(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","a");
        context.put("a",context_in);
        Object result =  Expression.eval("equals(indexOf(abcd,${a.b}),0)",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test09(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","a");
        context.put("a",context_in);
        Object result =  Expression.eval("equals(indexOf(abcd,${a.b}),0)",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

}
