package com.example.demo.responsibility;

public interface BaseCase {
    /**
     * 所有case 处理的逻辑
     *
     * @param input
     * @param baseCase
     */
    void doSomething(String input, BaseCase baseCase);
}
