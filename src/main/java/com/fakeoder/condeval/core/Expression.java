package com.fakeoder.condeval.core;

import com.fakeoder.condeval.exception.ExpressionException;

import java.util.*;

/**
 * @author zhuo
 */
public class Expression {

    private Map<String,Object> context = new HashMap<>();

    /**
     * operator stack
     */
    private Stack<IOperator> operatorStack = new Stack<>();
    private Stack<String> variableStack = new Stack<>();

    /**
     * operator number stack
     */
    private Stack<String> variables = new Stack<>();

    /**
     * finally result
     */
    private Object result;

    /**
     * load express from expression string
     * @param expressionStr
     * @return
     */
    public static Object eval(String expressionStr, Map<String,Object> context){
        if(expressionStr==null||expressionStr.isEmpty()){
            throw new ExpressionException("expression is empty!");
        }

        Expression expression = new Expression(context);
        //todo valid this expression

        char[] characters = expressionStr.toCharArray();
        String storage = "";
        boolean preIsOperator = false;
        Operator preOperator = null;
        for(int idx = 0; idx < characters.length; idx++){

            if(Operator.maybeOperator(storage)){
                if(preIsOperator){
                    Operator operator = Operator.valueOfSymbol(storage+=characters[idx]);
                    if(operator == null){
                        if(preOperator != null){
                            idx = expression.pushOperator(preOperator.getSymbol(),characters,idx);
                            preOperator = null;
                            storage = characters[idx]+"";
                        }else{
                            storage+=characters[idx];
                        }
                    }else{
                        preOperator = operator;
                        storage+=characters[idx];
                    }
                }else{
                    //not first time
                    if(!storage.isEmpty()){
                        expression.pushVariable(storage+characters[idx]);
                    }
                    storage = characters[idx]+"";
                }
                preIsOperator = true;
            }else{
                if(preIsOperator){
                    idx = expression.pushOperator(storage+characters[idx],characters,idx);
                    preOperator = null;
                    storage = characters[idx]+"";
                }else{
                    storage+=characters[idx];
                }
                preIsOperator = false;
            }
        }
        return expression.result;
    }

    /**
     *
     * @param variable
     */
    private void pushVariable(String variable) {
        variableStack.push(variable);
    }

    /**
     * eval and push
     * @param operatorStr
     * @param characters
     * @param idx the first param parameter index
     * @return
     */
    private int pushOperator(String operatorStr, char[] characters, int idx) {
        //valid
        Operator operator = Operator.valueOfSymbol(operatorStr);
        if(operator==null){
            throw new ExpressionException(String.format("not valid operator:%s", operatorStr));
        }

        IOperator operatorPre = operatorStack.peek();
        if(operator.lessThan(operatorPre)){
            List<String> params = new ArrayList<>(8);
            for(int paramIdx = 0; paramIdx < operatorPre.getParamSize(); paramIdx++){
                params.add(variableStack.pop());
            }
            //todo check logic
            Object result = operatorPre.eval(params);
            pushVariable(result.toString());
        }

        operatorStack.push(operator);
        List<String> params = new ArrayList<>();
        //todo this is important, need check.
        idx = operator.findParameter(characters,idx, params);
        for(String param : params){
            pushVariable(param);
        }
        //return next index
        return idx;
    }

    /**
     * private
     */
    private Expression(Map<String,Object> context) {
        this.context = context;
    }
}
