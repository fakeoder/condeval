package com.fakeoder.evall.core;

import org.apache.log4j.Logger;

import java.util.Stack;

/**
 * @author zhuo
 */
public class CustomStack extends Stack {

    private static Logger log = Logger.getLogger(CustomStack.class);
    private String name;

    public CustomStack(String name){
        this.name = name;
    }

    @Override
    public Object push(Object item) {
        Object object = super.push(item);
        if(log.isDebugEnabled()) {
            log.debug("[PUSH]" + this.toString());
        }
        return object;
    }

    @Override
    public synchronized Object pop() {
        Object object = super.pop();
        if(log.isDebugEnabled()) {
            log.debug("[POP]" + this.toString());
        }
        return object;
    }

    @Override
    public synchronized String toString() {
        return String.format("[%s]=%s\t(hashcode=%s)",this.name,super.toString(),this.hashCode());
    }
}


