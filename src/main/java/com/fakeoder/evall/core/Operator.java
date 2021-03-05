package com.fakeoder.evall.core;
import com.alibaba.fastjson.JSONObject;
import com.fakeoder.evall.exception.ExpressionException;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * operators for expression
 * it define many operators and how to eval it
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
    DOLLAR(0, "$", null, 40, false, true)  {
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
    SHARP(0, "#", null, 40, false, true)  {
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
                return Boolean.parseBoolean(params[0].toString()) && Boolean.parseBoolean(params[1].toString());
            }
            throw new ExpressionException("expect boolean, but not");
        }
    },
    OR(2, "||", Boolean.class, 30)  {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Boolean && params[1] instanceof Boolean) {
                return Boolean.parseBoolean(params[0].toString()) || Boolean.parseBoolean(params[1].toString());
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
            return params[0].toString().equals(params[1].toString());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    TO_STRING(1, "toString", String.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return "'"+params[0]+"'";
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
            return Collections.min((Collection) params[0]);
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //for list
    SUM(1, "sum", Object.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return ((Collection) params[0]).stream().reduce(0,(acc, item)-> Double.parseDouble(acc.toString())+Double.parseDouble(item.toString()));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //for list
    CONCAT_SEQ(1, "concatFrom", Object.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return ((Collection) params[0]).stream().reduce(0,(acc, item)-> (acc.toString()+item.toString()));
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
    REMOVE_KEY(2, "removeKey", Void.class, 40) {
        @Override
        public Object eval(Object[] params) {
            Map context = (Map) params[0];
            String key = unpack(params[1].toString());
            Iterator<String> keys = Arrays.stream(key.split(DOT)).iterator();
            Variable.removeKey(context, keys);
            return context;
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
        private final static String DOT = "\\.";
    },
    //for list
    DISTINCT(1, "distinct", Collection.class, 40) {
        @Override
        public Object eval(Object[] params) {
            return ((Collection)params[0]).stream().distinct().collect(Collectors.toList());
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    SIZE(1, "size", Integer.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Collection) {
                return ((Collection) params[0]).size();
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //for set and array
    AS_LIST(1, "asList", Map.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0].getClass().isArray()){
                return Arrays.stream((Object[])params[0]).collect(Collectors.toList());
            }
            if(params[0] instanceof Set){
                return ((Set)params[0]).stream().collect(Collectors.toList());
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //for List args:[0:list,1:sort column,2:asc/desc]
    SORT(3, "sort", Map.class, 40) {
        private final static String ASC = "asc";
        private final static String DESC = "desc";
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof List){
                ((List) params[0]).sort((left, right) -> {
                    Object leftV = Expression.eval(unpack(params[1].toString()), object2Map(left));
                    Object rightV = Expression.eval(unpack(params[1].toString()), object2Map(right));
                    String type = params[2].toString();
                    switch (type) {
                        case ASC:
                            return compare(leftV, rightV);
                        case DESC:
                            return compare(rightV, leftV);
                        default:
                            throw new ExpressionException("");
                    }
                });
                return params[0];
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    LIMIT(2, "limit", List.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof List){
                long limit = (int)params[1];
                return ((List) params[0]).stream().limit(limit).collect(Collectors.toList());
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //args:[0:collection,1:key expression,2:value expression]
    AS_MAP(3, "asMap", Map.class, 40) {
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
    //for map
    KEY_SET(3, "keySet", Map.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Map){
                return ((Map)params[0]).keySet();
            }
            throw new ExpressionException("params type disMatch:"+JSONObject.toJSONString(params));
        }
        @Override
        public int findParameter(char[] characters, int idx, List<Object> params) {
            return doFindParameter(characters,idx,params);
        }
    },
    //for map
    VALUES(3, "values", Map.class, 40) {
        @Override
        public Object eval(Object[] params) {
            if(params[0] instanceof Map){
                return ((Map)params[0]).values();
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
    ALL_MATCH(2, "allMatch", Collection.class, 40) {
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

    /************ support multiline code ************/
    ASSIGN(3, "=", Void.class, 0, true, false) {
        @Override
        public Object eval(Object[] params) {
            //param[0]:#{a.b} key:a.b
            String key = unpack(params[0].toString());
            Object value = params[2];
            Map<String,Object> context = (Map<String, Object>) params[1];
            Variable.putMapWithKeyNValue(context, Arrays.stream(key.split(DOT)).iterator(), value);
            return null;
        }
        private final static String DOT = "\\.";
    },
    ASSIGN_OUTER(3, "===", Void.class, 0, true, false) {
        @Override
        public Object eval(Object[] params) {
            //param[0]:#{a.b} key:a.b
            String key = unpack(params[0].toString());
            Object value = params[2];
            Map<String,Object> context = (Map<String, Object>) params[1];
            Variable.putMapWithKeyNValue(context, Arrays.stream(key.split(DOT)).iterator(), value);
            return null;
        }
        private final static String DOT = "\\.";
    },
    LINEFEED(0, ";", Void.class, -1, false, false) {
        @Override
        public Object eval(Object[] params) {
            return null;
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

    Operator(int paramSize,String symbol, Class resultType, int priority, boolean isNeedPush, boolean isNeedPushValue){
        this.paramSize = paramSize;
        this.symbol = symbol;
        this.resultType = resultType;
        this.priority = priority;
        this.isNeedPush = isNeedPush;
        this.isNeedPushValue = isNeedPushValue;
    }



    private final int paramSize;
    private final String symbol;
    private final Class resultType;
    private final int priority;
    private boolean isNeedPush = true;
    private boolean isNeedPushValue = true;

    private static final Logger log = Logger.getLogger(Operator.class);




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
    @Override
    public boolean isNeedPushValue() {
        return isNeedPushValue;
    }

    /**
     * check if number of params is valid
     * @param params params
     */
    public void checkParameter(Object... params) {
        if(params.length!=this.paramSize){
            throw new ExpressionException(String.format("params count is not match![%s] expect %s but params is %s",this.symbol,this.getParamSize(),JSONObject.toJSONString(params)));
        }
    }

    /**
     * eval before
     * @param params params
     */
    public void evalBefore(Object... params){
//        checkParameter(params);
        if(log.isDebugEnabled()){
            log.debug(String.format("[IN][%s][PARAM]=%s",this,JSONObject.toJSONString(params)));
        }
    }

    /**
     * eval after
     * @param result eval result
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
     * @param params eval params
     * @return eval result
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

    /**
     * method parameter find
     * @param characters expression characters
     * @param idx index
     * @param params operator params
     * @return the new index
     */
    private static int doFindParameter(char[] characters, int idx, List<Object> params){
        StringBuilder storage = new StringBuilder();
        idx++;
        int right = 0;
        for(;idx<characters.length;idx++){
            char character = characters[idx];
            switch (character){
                case '(':
                    right--;
                    storage.append(character);
                    break;
                case ')':
                    right++;
                    if(right>0){
                        params.add(storage.toString());
                        return idx+1;
                    }else {
                        storage.append(character);
                    }
                    break;
                case ',':
                    if(right==0) {
                        params.add(storage.toString());
                        storage = new StringBuilder();
                    }else{
                        storage.append(character);
                    }
                    break;
                default:
                    storage.append(character);
            }
        }
        throw new ExpressionException(String.format("find parameters error! idx:%s",idx));
    }

    /**
     * find parameters, for variables in context find
     * @param characters expression characters
     * @param idx index
     * @param params operator params
     * @return new index
     */
    protected int doFindParameterVariable(char[] characters, int idx, List<Object> params){
        StringBuilder storage = new StringBuilder();
        idx--;
        int right = 0;
        for(;idx<characters.length;idx++){
            char character = characters[idx];
            switch (character){
                case '{':
                    right--;
                    storage.append(character);
                    break;
                case '}':
                    right++;
                    storage.append(character);
                    if(right==0){
                        params.add(storage.toString());
                        return idx+1;
                    }
                    break;
                default:
                    storage.append(character);
            }
        }
        throw new ExpressionException(String.format("find parameters error! idx:%s",idx));
    }

    /**
     * join params
     * @param params object array
     * @param delimiter join delimiter
     * @return new joined string
     */
    static String objectsJoin(Object[] params, String delimiter){
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object cs: params) {
            joiner.add((CharSequence) cs);
        }
        return joiner.toString();
    }

    /**
     * object transform to map
     * if map -> map
     * else map with {"@":param}
     * @param param object
     * @return map
     */
    static Map object2Map(Object param){
        Map context = new HashMap();
        try {
            context = JSONObject.parseObject(JSONObject.toJSONString(param));
        }catch (Exception e){
            context.put("@",param);
        }
        return context;
    }

    /**
     * compare two comparable args
     * @param leftV left variable
     * @param rightV right variable
     * @return compare result
     */
    static int compare(Object leftV, Object rightV){
        if(leftV instanceof Comparable && rightV instanceof Comparable){
            return ((Comparable) leftV).compareTo(rightV);
        }
        throw new ExpressionException("some variables is not ");
    }
}
