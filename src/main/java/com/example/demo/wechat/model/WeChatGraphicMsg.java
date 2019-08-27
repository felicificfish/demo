/**
 *
 */
package com.example.demo.wechat.model;

import lombok.Data;

import java.util.List;

/**
 * 微信图文消息实体
 *
 * @author zhou.xy
 * @since 2019/4/18
 */
@Data
public class WeChatGraphicMsg extends WeChatBaseMsg {
    /**
     * 图文消息个数，限制为10条以内
     */
    private int ArticleCount;
    /**
     * 多条图文消息信息，默认第一个item为大图
     */
    private List<WeChatArticleMsg> Articles;
}
