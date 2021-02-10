package com.fakeoder.condeval.core;
import com.alibaba.fastjson.JSONObject;
import com.fakeoder.condeval.exception.ExpressionException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuo
 */

public enum Operator implements IOperator{

    ADD(2, "+", Number.class, 1){
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0])+Double.parseDouble(params[1]);
        }
    },
    MINUS(2, "-", Number.class, 2) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0])-Double.parseDouble(params[1]);
        }
    },
    TIME(2, "*", Number.class, 3) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0])*Double.parseDouble(params[1]);
        }
    },
    DIV(2, "/", Number.class, 3)  {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0])/Double.parseDouble(params[1]);
        }
    },
    SQRT(1, "sqrt", Number.class, 40)  {
        @Override
        public Object eval(String[] params) {
            return Math.sqrt(Double.parseDouble(params[0]));
        }

        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    BRACKET_LEFT(0, "(", null, 0)  {
        @Override
        public Object eval(String[] params) {
            return null;
        }
    },
    BRACKET_RIGHT(0, ")", null, 50)  {
        @Override
        public Object eval(String[] params) {
            return null;
        }
    },
    AND(2, "&&", Boolean.class, 31)  {
        @Override
        public Object eval(String[] params) {
            return Boolean.valueOf(params[0]) && Boolean.valueOf(params[1]);
        }
    },
    OR(2, "||", Boolean.class, 30)  {
        @Override
        public Object eval(String[] params) {
            return Boolean.valueOf(params[0]) || Boolean.valueOf(params[1]);
        }
    },
    GT(2, ">", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0]) > Double.parseDouble(params[1]);
        }
    },
    LT(2, "<", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0]) > Double.parseDouble(params[1]);
        }
    },
    GE(2, ">=", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0]) >= Double.parseDouble(params[1]);
        }
    },
    LE(2, "<=", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0]) <= Double.parseDouble(params[1]);
        }
    },
    EQUAL(2, "==", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return Double.parseDouble(params[0]) == Double.parseDouble(params[1]);
        }
    },
    INDEX(2, "indexOf", Integer.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].indexOf(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    CONTAINS(2, "contains", Boolean.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].contains(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    START_WITH(2, "startWith", Boolean.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].startsWith(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    END_WITH(2, "endWith", Boolean.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].endsWith(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    SUBSTRING(3, "subString", String.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].substring(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    CONCAT(2, "concat", String.class, 40) {
        @Override
        public Object eval(String[] params) {
            return String.join("", params);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    EQUALS(2, "equals", String.class, 40) {
        @Override
        public Object eval(String[] params) {
            return params[0].equals(params[1]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<String> params) {
            return doFindParameter(characters,idx,params);
        }
    };

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
    public void evalBefore(String... params){
        checkParameter(params);
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
    public Object evalAround(String... params) {
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
    public abstract Object eval(String[] params);

    @Override
    public int findParameter(char[] characters, int idx, List<String> params) {
        return idx;
    }

    @Override
    public boolean lessThan(IOperator other) {
        return this.priority < other.getPriority();
    }

    private static int doFindParameter(char[] characters, int idx, List<String> params){
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
}
