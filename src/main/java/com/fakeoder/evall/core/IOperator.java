package com.fakeoder.evall.core;

import java.util.List;

/**
 * @author zhuo
 */
public interface IOperator{

    /**
     * eval expression value by params
     * @param params
     * @return
     */
    Object evalAround(Object... params);


    /**
     * find parameters, this method is for some string method
     * @param characters
     * @param idx
     * @param params
     */
    int findParameter(char[] characters, int idx, List<Object> params);

    /**
     * get priority
     * @return
     */
    int getPriority();

    /**
     * get parameters size
     * @return
     */
    int getParamSize();

    /**
     * set parameters size
     * @param paramSize
     * @return
     */
    void setParamSize(int paramSize);

    /**
     * if this operator need be pushed
     * @return
     */
    boolean isNeedPush();

    /**
     * if this value need be pushed
     * @return
     */
    boolean isNeedPushValue();

    /**
     * compare priority
     * @param o
     * @return
     */
    boolean lessThan(IOperator o);
}
