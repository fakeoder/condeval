package com.fakeoder.evall.core;

import com.fakeoder.evall.exception.ExpressionException;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author zhuo
 */
public class Expression {

    private static Logger log = Logger.getLogger(Expression.class);

    private final Map<String,Object> global;

    private final Map<String,Object> local = new HashMap<>();

    /**
     * operator stack
     */
    private final Stack<IOperator> operatorStack = new CustomStack("OPERATOR");

    /**
     * operator number stack
     */
    private final Stack<Object> variableStack = new CustomStack("VARIABLE");


    private final static String USELESS_CHARACTER = "\\s*";

    /**
     * finally result
     */
    private Object result;

    /**
     * load express from expression string
     * @param expressionStr
     * @return
     */
    public static Object eval(String expressionStr, Map<String,Object> global){
        if(expressionStr==null||expressionStr.isEmpty()){
            log.error("expression is empty!");
            return null;
        }
        if(global==null){
            global = new HashMap<>(0);
        }

        Expression expression = new Expression(global);
        //todo valid this expression

        char[] characters = expressionStr.replaceAll(USELESS_CHARACTER,"").toCharArray();
        String storage = "";
        boolean preIsOperator = false;
        boolean hasLeft = true;
        int idx = 0;
        for(; idx < characters.length; idx++){
            if(preIsOperator){
                //if current match, try to find the max length operator
                if(Operator.maybeOperator(storage+characters[idx])) {
                    storage += characters[idx];
                    preIsOperator = true;
                }else{
                    idx = expression.pushOperator(storage, characters, idx);
                    if(idx>characters.length-1){
                        hasLeft = false;
                        break;
                    }
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
        }

        if(hasLeft) {
            if (!preIsOperator) {
                expression.pushVariable(storage);
            } else {
                //must be right bracket
                switch (Operator.valueOfSymbol(storage)){
                    case BRACKET_RIGHT:
                        expression.rightBracketDeal();
                        break;
                    default:
                        //do nothing
                        break;
                }
            }
        }


        while(!expression.operatorStack.isEmpty()) {
            IOperator operator = expression.operatorStack.pop();
            expression.operatorEval(operator);
        }
        return expression.variableStack.isEmpty() ? null : expression.variableStack.pop();
    }


    /**
     *
     * @param variable
     */
    private void pushVariable(Object variable) {
        variableStack.push(variable);
    }

    /**
     *
     * @param params
     */
    private void pushVariables(List<Object> params) {
        for (Object param : params) {
            pushVariable(param);
        }
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
            if(operator.isNeedPush()) {
                operatorStack.push(operator);
            }
            List<Object> params = new ArrayList<>();
            //todo this is important, need check.
            idx = operator.findParameter(characters,idx, params);
            if(operator==Operator.DOLLAR){
                Object variable = Variable.VariableMatcher.findRealValue(params.get(0).toString(),local, global);
                pushVariable(variable);
            }else {
                pushVariables(params);

                //for assign
                if(operator==Operator.ASSIGN){
                    pushVariable(local);
                }
                if(operator==Operator.ASSIGN_OUTER){
                    pushVariable(global);
                }
            }
        }else {
            IOperator operatorPre = operatorStack.peek();
            if (operator.lessThan(operatorPre)) {
                if(operator!=Operator.BRACKET_LEFT) {
                    operatorStack.pop();
                    operatorEval(operatorPre);
                }
                if(operator.isNeedPush()) {
                    operatorStack.push(operator);
                }

                //for assign
                if(operator==Operator.ASSIGN){
                    pushVariable(local);
                }
                if(operator==Operator.ASSIGN_OUTER){
                    pushVariable(global);
                }

            }else {
                if(operator==Operator.BRACKET_RIGHT){
                    rightBracketDeal();
                } else{
                    List<Object> params = new ArrayList<>();
                    if(operator.isNeedPush()) {
                        operatorStack.push(operator);
                    }
                    //todo this is important, need check.
                    idx = operator.findParameter(characters, idx, params);
                    if(operator==Operator.DOLLAR){
                        Object variable = Variable.VariableMatcher.findRealValue(params.get(0).toString(), local, global);
                        pushVariable(variable);
                    }else {
                        pushVariables(params);
                    }
                }
            }
        }
        //return next index
        return idx;
    }

    /**
     * visit right bracket, should back, eval and push value
     */
    public void rightBracketDeal(){
        IOperator backOperator;
        while((backOperator=operatorStack.pop())!=Operator.BRACKET_LEFT) {
            Object[] params = new Object[backOperator.getParamSize()];
            for (int paramIdx = backOperator.getParamSize() - 1; paramIdx >= 0; paramIdx--) {
                params[paramIdx] = variableStack.pop();
                if(Variable.isVariable(params[paramIdx])) {
                    params[paramIdx] = Variable.realVariable(params[paramIdx], local, global);
                }else{
                    Object res = Expression.eval(params[paramIdx].toString(), global);
                    params[paramIdx] = res;
                }
            }
            Object result = backOperator.evalAround(params);
            if(backOperator.isNeedPushValue()) {
                pushVariable(result);
            }
        }
    }

    /**
     * eval operator
     * @param operator
     */
    public void operatorEval(IOperator operator){
        Object[] params = new Object[operator.getParamSize()];
        for (int paramIdx = operator.getParamSize() - 1; paramIdx >= 0; paramIdx--) {
            if(variableStack.isEmpty()){
                throw new ExpressionException("params stack is empty! please check expression!");
            }
            Object val = variableStack.pop();
            //todo variables?(${name},"abc",123):expression(a+1)
            if(Variable.isVariable(val)) {
                params[paramIdx] = Variable.realVariable(val, local, global);
            }else{
                Object res = Expression.eval(val.toString(), global);
                params[paramIdx] = res;
            }

        }
        //todo check logic
        Object result = operator.evalAround(params);
        if(operator.isNeedPushValue()) {
            pushVariable(result);
        }
    }


    /**
     * private
     */
    private Expression(Map<String,Object> global) {
        this.global = global;
    }


}
