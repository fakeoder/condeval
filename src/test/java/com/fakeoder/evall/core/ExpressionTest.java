package com.fakeoder.evall.core;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ExpressionTest {

    static Map<String,Object> context;
    static {
        context = new HashMap<>();

        //a
        //--b
        Map<String,Object> context_in = new HashMap<>();
        context_in.put("b","1");

        //b
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);

        //c
        Object[] arr = ints.toArray();

        //d
        List d = new ArrayList(ints);
        d.add(1);

        //e
        String[] strs = new String[]{"1"};

        context.put("a",context_in);
        context.put("b",ints);
        context.put("c",arr);
        context.put("d",d);
        context.put("e",strs);
    }


    @Test
    public void test01(){
        Object result =  Expression.eval("(0+2*0)==10",null);
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
        Object result =  Expression.eval("equals(indexOf('abcd',${a.b}),0)",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.FALSE);
    }

    @Test
    public void test09(){
        Object result =  Expression.eval("equals(indexOf('abcd',${a.b}),'0')",context);
        assert Boolean.valueOf(result.toString()).equals(Boolean.FALSE);
    }

    @Test
    public void test10(){
        Object result =  Expression.eval("if(${a.b}==${a.b},'a','b')",context);
        assert result.toString().equals("a");
    }

    @Test
    public void test11(){
        Object result =  Expression.eval("filter(${b},#{${@}>3})",context);
        assert ((Collection)result).size()==2;
    }

    @Test
    public void test12(){
        Object result =  Expression.eval("asMap(${b},#{${@}},#{sqrt(${@}+1)})",context);
        assert ((Map)result).size()==5;
    }

    @Test
    public void test13(){
        Object result =  Expression.eval("map(${b},#{sqrt(${@}+1)})",context);
        assert ((Collection<Object>)result).size()==5;
    }

    @Test
    public void test14(){
        Object result =  Expression.eval("minOf(${b})",context);
        assert result.toString().equals("1");
    }

    @Test
    public void test15(){
        Object result =  Expression.eval("anyMatch(${b},#{${@}>3})",context);
        assert ((Boolean)result);
    }

    @Test
    public void test16(){
        Object result =  Expression.eval("allMatch(${b},#{${@}>0})",context);
        assert ((Boolean)result);
    }

    @Test
    public void test17(){
        Object result =  Expression.eval("sort(${b},#{${@}},'desc')",context);
        assert ((Collection)result).size()==5;
    }

    @Test
    public void test18(){
        Object result =  Expression.eval("asList(${c})",context);
        System.out.println(JSONObject.toJSONString(result));
        assert ((Collection)result).size()==5;
    }

    @Test
    public void test19(){
        Object result =  Expression.eval("sort(asList(${c}),#{${@}},'desc')",context);
        System.out.println(JSONObject.toJSONString(result));
        assert ((Collection)result).size()==5;
    }

    @Test
    public void test20(){
        Object result =  Expression.eval("distinct(${d})",context);
        System.out.println(JSONObject.toJSONString(result));
        assert ((Collection)result).size()==5;
    }

    @Test
    public void test21(){
        Expression.eval("#{level1.level2.level3}===1",context);
        System.out.println(JSONObject.toJSONString(context));
        assert context.containsKey("level1");
    }

    @Test
    public void test22(){
        Object result = Expression.eval("#{level1.level2.level3}=1;equals(${level1.level2.level3},1)",context);
        assert (Boolean)result;
    }


    @Test
    public void test23(){
        String expression = "#{level1.level2.level3}=1;distinct(${d});equals(${level1.level2.level3},1)";
        Object result = Expression.eval(expression,context);
        assert (Boolean)result;
    }


    @Test
    public void test24(){
        Object result = Expression.eval("concat(concat('','b'),'c')",context);
        assert result.toString().equals("bc");
    }

    @Test
    public void test25(){
        Object result = Expression.eval("limit(${d},2)",context);
        assert ((Collection)result).size() == 2;
    }


    @Test
    public void test26(){
        Expression.eval("#{.}===removeKey(${@},#{b});",context);
        System.out.println(JSONObject.toJSONString(context));
        assert !context.containsKey("b");
    }

    @Test
    public void test27(){
        Object result = Expression.eval("size(${d})",context);
        assert (int)result == 6;
    }

    @Test
    public void test28(){
        Object result = Expression.eval("new('java.lang.String','java.lang.String',${e},'true')",context);
        assert result.toString().equals("1");
    }

    @Test
    public void test29(){
        Object result = Expression.eval("int(123)",context);
        assert (int)result == 123;
    }

    @Test
    public void test30(){
        Object result = Expression.eval("bigDecimal(0.1)+bigDecimal(0.2)",context);
        assert result.toString().equals("0.3");
    }

    @Test
    public void test31(){
        Object result = Expression.eval("double(0.2)+double(0.1)",context);
        assert !result.toString().equals("0.3");
    }

    @Test
    public void test32(){
        Object result = Expression.eval("json(toString('{\"name\":${a},\"value\":${a}}'),${@})",context);
        assert result instanceof Map;

    }

    @Test
    public void test33(){
        Object result = Expression.eval("jsonArray(^('[{\"name\":${a},\"value\":${a}},{\"name\":${b},\"value\":${b}}]'),${@})",context);
        assert result instanceof List;
    }

    @Test
    public void test34(){
        Object result = Expression.eval("jsonArray(^('[${a},${b}]'),${@})",context);
        assert result instanceof List;
    }


    @Test
    public void test35(){
        Object result = Expression.eval("#{a b c}=${a};${@}",context);
        System.out.println(JSONObject.toJSONString(result));
        assert result instanceof List;
    }




}
