package com.example.demo.responsibility;

/**
 * TODO
 *
 * @author zhou.xy
 * @since 2019/5/27
 */
public class Demo {
    public static void main(String[] args) {
        String input = "3";
        CaseChain caseChain = new CaseChain();
        caseChain.addBaseCase(new FirstCase()).addBaseCase(new SecondCase()).addBaseCase(new DefaultCase());
        caseChain.doSomething(input, caseChain);
    }
}
