package com.example.demo.responsibility;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链管理类，管理所有 case
 *
 * @author zhou.xy
 * @since 2019/5/27
 */
public class CaseChain implements BaseCase {
    /**
     * 所有case列表
     */
    List<BaseCase> caseList = new ArrayList<>();
    /**
     * 索引，用于遍历所有case列表
     */
    private int index = 0;

    /**
     * 添加 case
     *
     * @param baseCase
     * @return
     */
    public CaseChain addBaseCase(BaseCase baseCase) {
        caseList.add(baseCase);
        return this;
    }

    @Override
    public void doSomething(String input, BaseCase baseCase) {
        if (index == caseList.size()) {
            return;
        }
        // 获取当前case
        BaseCase currentCase = caseList.get(index);
        // 修改索引值，以便下次回调获取下个节点，达到遍历效果
        index++;
        // 调用当前case处理方法
        currentCase.doSomething(input, this);
    }
}
