package com.example.demo.responsibility;

/**
 * TODO
 *
 * @author zhou.xy
 * @since 2019/5/27
 */
public class DefaultCase implements BaseCase {
    @Override
    public void doSomething(String input, BaseCase baseCase) {
        System.out.println(getClass().getName());
    }
}
