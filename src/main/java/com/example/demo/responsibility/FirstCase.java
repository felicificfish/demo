package com.example.demo.responsibility;

/**
 * TODO
 *
 * @author zhou.xy
 * @since 2019/5/27
 */
public class FirstCase implements BaseCase {
    @Override
    public void doSomething(String input, BaseCase baseCase) {
        if ("1".equals(input)) {
            System.out.println(getClass().getName());
            return;
        }
        // 当前无法处理，让下一个处理
        baseCase.doSomething(input, baseCase);
    }
}
