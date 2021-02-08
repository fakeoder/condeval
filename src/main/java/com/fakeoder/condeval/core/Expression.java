package com.fakeoder.condeval.core;

import com.fakeoder.condeval.exception.ExpressionException;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.*;

/**
 * @author zhuo
 */
public class Expression {

    private Map<String,Object> context;

    /**
     * operator stack
     */
    private Stack<IOperator> operatorStack = new Stack<>();

    /**
     * operator number stack
     */
    private Stack<String> variableStack = new Stack<>();


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
        int idx = 0;
        for(; idx < characters.length; idx++){
            if(preIsOperator){
                if(Operator.maybeOperator(storage+characters[idx])) {
                    Operator operator = Operator.valueOfSymbol(storage + characters[idx]);
                    if (operator == null) {
                        if (preOperator != null) {
                            idx = expression.pushOperator(preOperator.getSymbol(), characters, idx);
                            preOperator = null;
                            storage = characters[idx] + "";
                        } else {
                            storage += characters[idx];
                        }
                    } else {
                        preOperator = operator;
                        storage += characters[idx];
                    }
                    preIsOperator = true;
                }else{
                    idx = expression.pushOperator(storage, characters, idx);
                    if(idx>characters.length-1){
                        printStack(expression);
                        break;
                    }
                    preOperator = null;
                    storage = characters[idx] + "";
                    preIsOperator = Operator.maybeOperator(characters[idx]+"");
                }
            }else {
                if(Operator.maybeOperator(characters[idx]+"")) {
                    //not first time
                    if(!storage.isEmpty()){
                        expression.pushVariable(storage);
                    }
                    storage = characters[idx]+"";
                    preIsOperator = true;
                }else{
                    storage += characters[idx];
                    preIsOperator = false;
                }
            }

            printStack(expression);
        }

        if(!preIsOperator){
            expression.pushVariable(storage);
            printStack(expression);
        }

        while(!expression.operatorStack.isEmpty()) {
            IOperator operator = expression.operatorStack.pop();
            String[] params = new String[3];
            for (int paramIdx = operator.getParamSize() - 1; paramIdx >= 0; paramIdx--) {
                params[paramIdx] = expression.variableStack.pop();
            }
            Object result = operator.eval(params);
            expression.pushVariable(result.toString());

            printStack(expression);
        }

        System.out.println("finally:");
        printStack(expression);
        return expression.variableStack.pop();
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

        if(operatorStack.isEmpty()){
            operatorStack.push(operator);
            List<String> params = new ArrayList<>();
            //todo this is important, need check.
            idx = operator.findParameter(characters,idx, params);
            for(String param : params){
                pushVariable(param);
            }
        }else {
            IOperator operatorPre = operatorStack.peek();
            if (operator.lessThan(operatorPre)) {
                operatorStack.pop();
                String[] params = new String[3];
                for (int paramIdx = operatorPre.getParamSize() - 1; paramIdx >= 0; paramIdx--) {
                    params[paramIdx] = variableStack.pop();
                }
                //todo check logic
                Object result = operatorPre.eval(params);
                pushVariable(result.toString());
                operatorStack.push(operator);
            }else {
                if(operator==Operator.BRACKET_RIGHT){
                    IOperator backOperator = null;
                    while((backOperator=operatorStack.pop())!=Operator.BRACKET_LEFT) {
                        String[] params = new String[3];
                        for (int paramIdx = backOperator.getParamSize() - 1; paramIdx >= 0; paramIdx--) {
                            params[paramIdx] = variableStack.pop();
                        }
                        Object result = backOperator.eval(params);
                        pushVariable(result.toString());
                    }
                }else {
                    List<String> params = new ArrayList<>();
                    if(operator.isNeedPush()) {
                        operatorStack.push(operator);
                    }
                    //todo this is important, need check.
                    idx = operator.findParameter(characters, idx, params);
                    for (String param : params) {
                        pushVariable(param);
                    }
                }
            }
        }
        //return next index
        return idx;
    }

    public static void printStack(Expression expression){
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(expression.operatorStack);
        System.out.println(expression.variableStack);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println();
    }

    /**
     * private
     */
    private Expression(Map<String,Object> context) {
        this.context = context;
    }


}
