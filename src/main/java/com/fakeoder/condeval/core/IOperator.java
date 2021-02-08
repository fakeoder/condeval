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
    Object eval(String... params);

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
    int getPriority();

    /**
     * get parameters size
     * @return
     */
    int getParamSize();

    /**
     * if this operator need be pushed
     * @return
     */
    boolean isNeedPush();

    /**
     * compare priority
     * @param o
     * @return
     */
    boolean lessThan(IOperator o);
}
