package com.fakeoder.condeval.core;
import com.fakeoder.condeval.exception.ExpressionException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String storage = "";
            for(;idx<characters.length;idx++){
                char character = characters[idx];
                switch (character){
                    case '(':
                        break;
                    case ')':
                        params.add(storage);
                        return idx;
                    default:
                        storage+=character;
                }
            }
            throw new ExpressionException(String.format("find parameters error! idx:%s",idx));
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
    AND(2, "&", Boolean.class, 31)  {
        @Override
        public Object eval(String[] params) {
            return Boolean.valueOf(params[0]) && Boolean.valueOf(params[1]);
        }
    },
    OR(2, "|", Boolean.class, 30)  {
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
    INDEX(2, "indexOf", Integer.class, 20) {
        @Override
        public Object eval(String[] params) {
            return params[0].indexOf(params[1]);
        }
    },
    CONTAINS(2, "contains", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return params[0].contains(params[1]);
        }
    },
    START_WITH(2, "startWith", Boolean.class, 20) {
        @Override
        public Object eval(String[] params) {
            return params[0].startsWith(params[1]);
        }
    },
    END_WITH(2, "endWith", Boolean.class, 18) {
        @Override
        public Object eval(String[] params) {
            return params[0].endsWith(params[1]);
        }
    },
    SUBSTRING(3, "substring", String.class, 20) {
        @Override
        public Object eval(String[] params) {
            return params[0].substring(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
        }
    },
    CONCAT(2, "concat", String.class, 20) {
        @Override
        public Object eval(String[] params) {
            return String.join("", params);
        }
    },
    QUOTATION_SINGLE(0, "'", null, 60) {
        @Override
        public Object eval(String[] params) {
            return String.join("", params);
        }
    },
    QUOTATION_DOUBLE(0, "\"", null, 60) {
        @Override
        public Object eval(String[] params) {
            return String.join("", params);
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



    private int paramSize;
    private String symbol;
    private Class resultType;
    private int priority;




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
    public boolean checkParameter(Object... params) {
        return params.length==this.paramSize;
    }

    @Override
    public int findParameter(char[] characters, int idx, List<String> params) {
        return idx;
    }

    @Override
    public boolean lessThan(IOperator other) {
        return this.priority < other.getPriority();
    }


}
