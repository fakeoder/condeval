package com.fakeoder.evall.core;

import com.fakeoder.evall.exception.ExpressionException;
import java.util.HashMap;
import java.util.Iterator;
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
    public static boolean isVariable(Object variable){
        try {
            if(variable instanceof String) {
                VariableMatcher.match(variable.toString());
            }else{
                return true;
            }
        }catch (ExpressionException e){
            //todo check
            return false;
        }
        return true;

    }

    public static Object realVariable(Object variable, Map<String, Object> local, Map<String,Object> global) {
        return VariableMatcher.findRealValue(variable,local, global);
    }

    public static void putMapWithKeyNValue(Map<String,Object> context, Iterator<String> keys, Object value){
        if(keys==null||!keys.hasNext()){
            context.clear();
            context.putAll((Map) value);
            return;
        }
        String key = keys.next();
        if(context.containsKey(key)){
            Object inner = context.get(key);
            if(inner instanceof Map){
                putMapWithKeyNValue((Map<String, Object>) inner, keys, value);
            }else{
                //has sub level
                if(keys.hasNext()){
                    Map<String,Object> sub = new HashMap<>(2);
                    context.put(key, sub);
                    putMapWithKeyNValue(sub, keys, value);
                }else{
                    context.put(key, value);
                }
            }
        }else{
            //has sub level
            if(keys.hasNext()){
                Map<String,Object> sub = new HashMap<>(2);
                context.put(key, sub);
                putMapWithKeyNValue(sub, keys, value);
            }else{
                context.put(key, value);
            }
        }
    }

    public static void removeKey(Map context, Iterator<String> keys) {
        Map localContext = context;
        if(keys==null||!keys.hasNext()){
            return;
        }
        while(keys.hasNext()){
            String key = keys.next();
            if(!keys.hasNext()){
                localContext.remove(key);
            }else{
                Object value = localContext.get(key);
                if(value instanceof Map) {
                    localContext = (Map) value;
                }else{
                    return;
                }
            }
        }
    }

    enum InnerConstantVariables{
        //TODO add some variables like osVersion, jdkVersion, e tc


    }

    enum VariableMatcher{

        PATTERN_DIGITAL{
            @Override
            Number realValue(String variable, Map<String,Object> context) {
                if(variable.indexOf(DOT)==-1){
                    return Integer.parseInt(variable);
                }
                return Double.parseDouble(variable);
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_DIGITAL.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_DIGITAL = Pattern.compile("-?[0-9]+\\.?[0-9]*");
            private final String DOT = ".";

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
                if(result instanceof String){
                    return Operator.packString(result.toString());
                }
                return result;
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_CONTEXT.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_CONTEXT = Pattern.compile("\\$\\{[a-zA-Z0-9_\\-\\@\\.]+\\}");


        },PATTERN_STRING{
            @Override
            String realValue(String variable, Map<String,Object> context) {
                return variable;
            }

            @Override
            boolean doMatch(String variable) {
                return VARIABLE_PATTERN_STRING.matcher(variable).matches();
            }

            private final Pattern VARIABLE_PATTERN_STRING = Pattern.compile("(\"[\\s\\S]*\")|('[\\s\\S]*')");

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
         * @param local
         * @param global
         * @return
         */
        public static Object findRealValue(Object variable, Map<String,Object> local, Map<String,Object> global){
            if(variable instanceof String){
                Map<String,Object> context = new HashMap<>();
                context.putAll(global);
                context.putAll(local);
                return match(variable.toString()).realValue(variable.toString(), context);
            }
            return variable;
        }

    }
}
