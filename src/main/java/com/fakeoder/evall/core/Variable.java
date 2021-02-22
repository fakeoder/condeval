package com.fakeoder.evall.core;


import com.fakeoder.evall.exception.ExpressionException;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * ${variable} to real value
 * @author zhuo
 */
public class Variable {


    /**
     * judge if variable
     * @param variable
     * @return
     */
    public static boolean isVariable(String variable){
        try {
            VariableMatcher.match(variable);
        }catch (ExpressionException e){
            //todo check
            return false;
        }
        return true;

    }

    public static Object realVariable(String variable, Map<String, Object> context) {
        return VariableMatcher.findRealValue(variable,context);
    }

    static enum VariableMatcher{

        PATTERN_DIGITAL{
            @Override
            Number realValue(String variable, Map<String,Object> context) {
                return Double.parseDouble(variable);
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_DIGITAL.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_DIGITAL = Pattern.compile("-?[0-9]+\\.?[0-9]*");

        },PATTERN_CONTEXT{
            @Override
            Object realValue(String variable, Map<String,Object> context) {
                Object result = "";
                String unpack = variable.substring(2,variable.length()-1);
                if(unpack.equals(SELF_SIGN)){
                    if(context.containsKey(SELF_SIGN)){
                        return context.get(SELF_SIGN);
                    }
                    return context;
                }
                String[] packs = unpack.split(REGEX_DOT);
                for(String pack : packs){
                    if(context==null){
                        throw new ExpressionException(String.format("no such variables in context![%s]",unpack));
                    }
                    Object object = context.get(pack);
                    if(object instanceof Map){
                        context = (Map)object;
                        result = object;
                    }else{
                        result = object;
                        context = null;
                    }
                }
                return result;
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_CONTEXT.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_CONTEXT = Pattern.compile("\\$\\{[a-zA-Z0-9_\\@\\.]+\\}");

        },PATTERN_STRING{
            @Override
            String realValue(String variable, Map<String,Object> context) {
                return variable.replaceAll("\"","").replaceAll("'","");
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_STRING.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_STRING = Pattern.compile("(\".*\")|('.*')|([a-zA-Z0-9_\\.]+)");

        };

        private static final String REGEX_DOT = "\\.";
        private static final String SELF_SIGN = "@";
        /**
         * value transform
         * @param variable
         * @param context
         * @return
         */
        abstract Object realValue(String variable, Map<String,Object> context);

        /**
         * @param variable
         * @return
         */
        abstract boolean doMatch(String variable);

        public static VariableMatcher match(String variable)  {
            for(VariableMatcher variableMatcher : VariableMatcher.values()){
                if(variableMatcher.doMatch(variable)){
                    return variableMatcher;
                }
            }
            throw new ExpressionException(String.format("no match variables matcher![%s]",variable));
        }

        /**
         *
         * @param variable
         * @param context
         * @return
         */
        public static Object findRealValue(String variable, Map<String,Object> context){
            return match(variable).realValue(variable, context);
        }


    }
}
