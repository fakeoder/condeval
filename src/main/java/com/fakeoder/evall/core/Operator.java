package com.fakeoder.evall.core;
import com.alibaba.fastjson.JSONObject;
import com.fakeoder.evall.exception.ExpressionException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuo
 */

public enum Operator implements IOperator{

    ADD(2, "+", Number.class, 1){
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() + ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString())+Double.parseDouble(params[1].toString());
        }
    },
    MINUS(2, "-", Number.class, 2) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() - ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) - Double.parseDouble(params[1].toString());
        }
    },
    TIME(2, "*", Number.class, 3) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() * ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) * Double.parseDouble(params[1].toString());
        }
    },
    DIV(2, "/", Number.class, 3)  {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() / ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) / Double.parseDouble(params[1].toString());
        }
    },
    SQRT(1, "sqrt", Number.class, 40)  {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number){
                return Math.sqrt(((Number)params[0]).doubleValue());
            }
            return Math.sqrt(Double.parseDouble(params[0].toString()));
        }

        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    BRACKET_LEFT(0, "(", null, 0)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    BRACKET_RIGHT(0, ")", null, 50)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    //for variable calculate
    DOLLAR(0, "$", null, 40, false)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }

        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameterVariable(characters,idx,params);
        }
    },
    //some late calculate method ( example : map/filter )
    SHARP(0, "#", null, 40, false)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }

        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameterVariable(characters,idx,params);
        }
    },
    AND(2, "&&", Boolean.class, 31)  {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Boolean && params[1] instanceof Boolean) {
                return Boolean.valueOf(params[0].toString()) && Boolean.valueOf(params[1].toString());
            }
            throw new ExpressionException("expect boolean, but not");
        }
    },
    OR(2, "||", Boolean.class, 30)  {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Boolean && params[1] instanceof Boolean) {
                return Boolean.valueOf(params[0].toString()) || Boolean.valueOf(params[1].toString());
            }
            throw new ExpressionException("expect boolean, but not");
        }
    },
    GT(2, ">", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() > ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) > Double.parseDouble(params[1].toString());
        }
    },
    LT(2, "<", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() < ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) < Double.parseDouble(params[1].toString());
        }
    },
    GE(2, ">=", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() >= ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) >= Double.parseDouble(params[1].toString());
        }
    },
    LE(2, "<=", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() <= ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) <= Double.parseDouble(params[1].toString());
        }
    },
    EQUAL(2, "==", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return ((Number)params[0]).doubleValue() == ((Number)params[1]).doubleValue();
            }
            return Double.parseDouble(params[0].toString()) == Double.parseDouble(params[1].toString());
        }
    },
    INDEX(2, "indexOf", Integer.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].toString().indexOf(params[1].toString());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    MAX(2, "max", Number.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Number && params[1] instanceof Number){
                return Math.max(((Number)params[0]).doubleValue(), ((Number)params[1]).doubleValue());
            }
            return Double.parseDouble(params[0].toString()) / Double.parseDouble(params[1].toString());
        }
    },
    MIN(2, "min", Number.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return Math.min(((Number)params[0]).doubleValue(), ((Number)params[1]).doubleValue());
        }
    },
    CONTAINS(2, "contains", Boolean.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].toString().contains(params[1].toString());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    START_WITH(2, "startWith", Boolean.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].toString().startsWith(params[1].toString());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    END_WITH(2, "endWith", Boolean.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].toString().endsWith(params[1].toString());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    SUBSTRING(3, "subString", String.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].toString().substring(Integer.parseInt(params[1].toString()), Integer.parseInt(params[2].toString()));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    CONCAT(2, "concat", String.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return objectsJoin(params, "");
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    EQUALS(2, "equals", String.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return params[0].equals(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    /******************seq*********************/
    MAX_SEQ(1, "maxOf", Object.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return Collections.max((Collection) params[0]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    MIN_SEQ(1, "minOf", Object.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return Collections.max((Collection) params[0]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    IF(3, "if", Object.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Boolean){
                return (Boolean)params[0]?params[1]:params[2];
            }
            throw new ExpressionException("expect first param is boolean, but not");
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //args:[0:collection,1:pass expression]
    FILTER(2, "filter", Collection.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return ((Collection)params[0]).stream().filter(param->{
                Map context = object2Map(param);
                Object rsl = Expression.eval(unpack(params[1].toString()), context);
                if(rsl instanceof Boolean){
                    return (boolean) rsl;
                }
                return false;
            }).collect(Collectors.toList());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //args:[0:collection,1:key expression,2:value expression]
    BE_MAP(3, "beMap", Map.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Collection){
                Map<String,Object> rsl = new HashMap<>();
                for(Object param : (Collection)params[0]){
                    Map context = object2Map(param);
                    rsl.put(Expression.eval(unpack(params[1].toString()),context).toString(),Expression.eval(unpack(params[2].toString()),context));
                }
                return rsl;
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    MAP(2, "map", Collection.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Collection){
                return ((Collection)params[0]).stream().map(param->{
                    Map context = object2Map(param);
                    return Expression.eval(unpack(params[1].toString()),context);
                }).collect(Collectors.toList());
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    ANY_MATCH(2, "anyMatch", Collection.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Collection){
                return ((Collection)params[0]).stream().anyMatch(param->{
                    Map context = object2Map(param);
                    return (boolean) Expression.eval(unpack(params[1].toString()),context);
                });
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    ALL_MATCH(2, "anyMatch", Collection.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Collection){
                return ((Collection)params[0]).stream().allMatch(param->{
                    Map context = object2Map(param);
                    return (boolean) Expression.eval(unpack(params[1].toString()),context);
                });
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },

    //TODO other operators

    ;

    protected String unpack(String param){
        return param.substring(2,param.length()-1);
    }


    private static final Map<String, Operator> SYMBOL_MAP = new HashMap<>(32);
    static{
        for(Operator operator : Operator.values()){
            SYMBOL_MAP.put(operator.symbol, operator);
        }
    }

    Operator(int paramSize,String symbol, Class resultType, int priority){
        this.paramSize = paramSize;
        this.symbol = symbol;
        this.resultType = resultType;
        this.priority = priority;
    }

    Operator(int paramSize,String symbol, Class resultType, int priority, boolean isNeedPush){
        this.paramSize = paramSize;
        this.symbol = symbol;
        this.resultType = resultType;
        this.priority = priority;
        this.isNeedPush = isNeedPush;
    }



    private int paramSize;
    private String symbol;
    private Class resultType;
    private int priority;
    private boolean isNeedPush = true;

    private static Logger log = Logger.getLogger(Operator.class);




    public static Operator valueOfSymbol(String symbol){
        return SYMBOL_MAP.get(symbol);
    }

    public static boolean maybeOperator(String symbol){
        return SYMBOL_MAP.keySet().stream().anyMatch((key)->key.startsWith(symbol));
    }

    public String getSymbol(){
        return this.symbol;
    }

    @Override
    public int getPriority(){
        return priority;
    }



    @Override
    public int getParamSize(){
        return paramSize;
    }

    @Override
    public boolean isNeedPush() {
        return isNeedPush;
    }

    /**
     * check if number of params is valid
     * @param params
     * @return
     */
    public void checkParameter(Object... params) {
        if(params.length!=this.paramSize){
            throw new ExpressionException(String.format("params count is not match![%s] expect %s but params is %s",this.symbol,this.getParamSize(),JSONObject.toJSONString(params)));
        }
    }

    /**
     * eval before
     * @param params
     */
    public void evalBefore(Object... params){
//        checkParameter(params);
        if(log.isDebugEnabled()){
            log.debug(String.format("[IN][%s][PARAM]=%s",this,JSONObject.toJSONString(params)));
        }
    }

    /**
     * eval after
     * @param result
     */
    public void evalAfter(String result){
        if(log.isDebugEnabled()){
            log.debug(String.format("[OUT][%s][RESULT]=[%s]",this,result));
        }
    }


    @Override
    public Object evalAround(Object... params) {
        Object result = null;
        try{
            evalBefore(params);
            result = eval(params);
        }catch (ExpressionException e){
            evalAfter(JSONObject.toJSONString(e));
        }
        evalAfter(result==null?"null":result.toString());
        return result;
    }

    /**
     * eval expression value by params
     * @param params
     * @return
     */
    public abstract Object eval(Object[] params);

    @Override
    public int findParameter(char[] characters, int idx, List<Object> params) {
        return idx;
    }

    @Override
    public boolean lessThan(IOperator other) {
        return this.priority < other.getPriority();
    }

    private static int doFindParameter(char[] characters, int idx, List<Object> params){
        String storage = "";
        idx++;
        int right = 0;
        for(;idx<characters.length;idx++){
            char character = characters[idx];
            switch (character){
                case '(':
                    right--;
                    storage+=character;
                    break;
                case ')':
                    right++;
                    if(right>0){
                        params.add(storage);
                        return idx+1;
                    }else {
                        storage+=character;
                    }
                    break;
                case ',':
                    if(right==0) {
                        params.add(storage);
                        storage = "";
                    }else{
                        storage+=character;
                    }
                    break;
                default:
                    storage+=character;
            }
        }
        throw new ExpressionException(String.format("find parameters error! idx:%s",idx));
    }

    protected int doFindParameterVariable(char[] characters, int idx, List<Object> params){
        String storage = "";
        idx--;
        int right = 0;
        for(;idx<characters.length;idx++){
            char character = characters[idx];
            switch (character){
                case '{':
                    right--;
                    storage+=character;
                    break;
                case '}':
                    right++;
                    if(right==0){
                        storage+=character;
                        params.add(storage);
                        return idx+1;
                    }else {
                        storage+=character;
                    }
                    break;
                default:
                    storage+=character;
            }
        }
        throw new ExpressionException(String.format("find parameters error! idx:%s",idx));
    }

    static String objectsJoin(Object[] params, String delimiter){
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object cs: params) {
            joiner.add((CharSequence) cs);
        }
        return joiner.toString();
    }

    static Map object2Map(Object param){
        Map context = new HashMap();
        try {
            context = JSONObject.parseObject(JSONObject.toJSONString(param));
        }catch (Exception e){
            context.put("@",param);
        }
        return context;
    }
}
