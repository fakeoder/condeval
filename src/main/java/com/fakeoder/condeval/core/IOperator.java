package com.fakeoder.condeval.core;

import java.util.List;

/**
 * @author zhuo
 */
public interface IOperator{

    /**
     * ecak expression value by params
     * @param params
     * @return
     */
    Object eval(Object... params);

    /**
     * check if number of params is valid
     * @param params
     * @return
     */
    boolean checkParameter(Object... params);

    /**
     * find parameters, this method is for some string method
     * @param characters
     * @param idx
     * @param params
     */
    int findParameter(char[] characters, int idx, List<String> params);

    /**
     * get priority
     * @return
     */
    public int getPriority();

    /**
     * get parameters size
     * @return
     */
    public int getParamSize();

    /**
     * compare priority
     * @param o
     * @return
     */
    boolean lessThan(IOperator o);
}
