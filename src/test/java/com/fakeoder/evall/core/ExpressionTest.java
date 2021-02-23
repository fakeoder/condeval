package com.fakeoder.evall.core;

import org.junit.Test;

import java.util.*;

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
        Object result =  Expression.eval("(sqrt(4)==3)||equals('1','1')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test04(){
        Object result =  Expression.eval("startWith('abc','a')||endWith('false','e')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test05(){
        Object result =  Expression.eval("startWith('abc','a')||contains('false','als')",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }


    @Test
    public void test06(){
        Object result =  Expression.eval("startWith('abc','a')||(indexOf('abc','a')==1)",null);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test07(){
        Object result =  Expression.eval("subString('abcdef',2,2)",null);
        assert result.toString().isEmpty();
    }

    @Test
    public void test08(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","a");
        context.put("a",context_in);
        Object result =  Expression.eval("equals(indexOf('abcd',${a.b}),0)",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test09(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","a");
        context.put("a",context_in);
        Object result =  Expression.eval("equals(indexOf('abcd',${a.b}),'0')",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.TRUE);
    }

    @Test
    public void test10(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        context.put("a",context_in);
        Object result =  Expression.eval("if(${a.b}==${a.b},'a','b')",context);
        assert result.toString().equals("a");
    }

    @Test
    public void test11(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("filter(${b},#{${@}>3})",context);
        assert ((Collection)result).size()==2;
    }

    @Test
    public void test12(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(1);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("beMap(${b},#{${@}},#{sqrt(${@}+1)})",context);
        assert ((Map)result).size()==4;
    }

    @Test
    public void test13(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("map(${b},#{sqrt(${@}+1)})",context);
        assert ((Collection<Object>)result).size()==5;
    }

    @Test
    public void test14(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("minOf(${b})",context);
        assert result.toString().equals("1");
    }

    @Test
    public void test15(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("anyMatch(${b},#{${@}>3})",context);
        assert ((Boolean)result);
    }

    @Test
    public void test16(){
        Map<String,Object> context = new HashMap<>();
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        context.put("a",context_in);
        context.put("b",ints);
        Object result =  Expression.eval("anyMatch(${b},#{${@}>0})",context);
        assert ((Boolean)result);
    }

}
