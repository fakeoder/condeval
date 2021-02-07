package com.fakeoder.condeval.core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Operator implements IOperator{

    ADD(2, "+", Number.class, 1){
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    MINUS(2, "-", Number.class, 2) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    TIME(2, "*", Number.class, 3) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    DIV(2, "/", Number.class, 3)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    SQRT(1, "sqrt", Number.class, 40)  {
        @Override
        public Object eval(Object[] params) {
            return null;
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
    AND(2, "&", Boolean.class, 31)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    OR(2, "|", Boolean.class, 30)  {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    GT(2, ">", Comparable.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    LT(2, "<", Comparable.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    GE(2, ">=", Comparable.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    LE(2, "<=", Comparable.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    EQUAL(2, "==", Comparable.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    INDEX(2, "indexOf", Integer.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    CONTAINS(2, "contains", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    START_WITH(2, "startWith", Boolean.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    END_WITH(2, "endWith", Boolean.class, 18) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    SUBSTRING(3, "substring", String.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
        }
    },
    CONCAT(2, "concat", String.class, 20) {
        @Override
        public Object eval(Object[] params) {
            return null;
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
