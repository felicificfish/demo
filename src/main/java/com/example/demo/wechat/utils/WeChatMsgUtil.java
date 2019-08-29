package com.example.demo.wechat.utils;

import com.example.demo.wechat.model.WeChatArticleMsg;
import com.example.demo.wechat.model.WeChatGraphicMsg;
import com.example.demo.wechat.model.WeChatInputMsg;
import com.example.demo.wechat.model.WeChatTextMsg;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

/**
 * 微信消息处理类
 *
 * @author zhou.xy
 * @since 2019/8/27
 */
public class WeChatMsgUtil {
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";


    /**
     * 请求消息类型：小视频
     */
    public static final String REQ_MESSAGE_TYPE_SHORT_VIDEO = "shortvideo";

    /**
     * 请求消息类型：视频
     */
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 事件类型：SCAN（用户已关注事件）
     */
    public static final String EVENT_TYPE_SCAN = "SCAN";

    /**
     * 事件类型：LOCATION（上报地理位置事件）
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";

    /**
     * 事件类型：VIEW（点击菜单跳转链接事件）
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";

    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public PrettyPrintWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                boolean cdata = false;

                @SuppressWarnings("rawtypes")
                @Override
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                    if (clazz.equals(String.class)) {
                        cdata = true;
                    } else {
                        cdata = false;
                    }
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 将request 消息 转换成 请求消息对象
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static WeChatInputMsg parseXml(InputStream inputStream) throws Exception {
        WeChatInputMsg msgReq = new WeChatInputMsg();

        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();

        // 遍历节点，封装成对象
        for (Element e : elementList) {
            String name = e.getName();
            String text = e.getText();
            if ("MsgType".equals(name)) {
                msgReq.setMsgType(text);
            } else if ("MsgId".equals(name)) {
                msgReq.setMsgId(Long.parseLong(text));
            } else if ("MsgId".equals(name)) {
                msgReq.setMsgId(Long.parseLong(text));
            } else if ("FromUserName".equals(name)) {
                msgReq.setFromUserName(text);
            } else if ("ToUserName".equals(name)) {
                msgReq.setToUserName(text);
            } else if ("CreateTime".equals(name)) {
                msgReq.setCreateTime(Long.parseLong(text));

            } else if ("Content".equals(name)) {
                // 文本消息
                msgReq.setContent(text);
            } else if ("PicUrl".equals(name)) {
                // 图片消息
                msgReq.setPicUrl(text);
            } else if ("Location_X".equals(name)) {
                // 地理位置消息
                msgReq.setLocation_X(text);
            } else if ("Location_Y".equals(name)) {
                msgReq.setLocation_Y(text);
            } else if ("Scale".equals(name)) {
                msgReq.setScale(Long.parseLong(text));
            } else if ("Label".equals(name)) {
                msgReq.setLabel(text);
            } else if ("Event".equals(name)) {
                // 事件消息
                msgReq.setEvent(text);
            } else if ("EventKey".equals(name)) {
                msgReq.setEventKey(text);
            }
        }

        inputStream.close();
        return msgReq;
    }

    /**
     * 将response 消息 转换成 返回消息对象
     *
     * @param respXml
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    public static WeChatInputMsg xmlToObject(String respXml) throws UnsupportedEncodingException {
        WeChatInputMsg responseMessage = new WeChatInputMsg();
        SAXReader reader = new SAXReader();
        InputStream inputStream = new ByteArrayInputStream(respXml.getBytes("UTF-8"));
        Document document;
        try {
            document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();

            // 遍历节点，封装成对象
            for (Element e : elementList) {
                String name = e.getName();
                String text = e.getText();

                if ("MsgType".equals(name)) {
                    responseMessage.setMsgType(text);
                } else if ("MsgId".equals(name)) {
                    responseMessage.setMsgId(Long.parseLong(text));
                } else if ("MsgId".equals(name)) {
                    responseMessage.setMsgId(Long.parseLong(text));
                } else if ("FromUserName".equals(name)) {
                    responseMessage.setFromUserName(text);
                } else if ("ToUserName".equals(name)) {
                    responseMessage.setToUserName(text);
                } else if ("CreateTime".equals(name)) {
                    responseMessage.setCreateTime(Long.parseLong(text));

                } else if ("Content".equals(name)) {//文本消息
                    responseMessage.setContent(text);
                } else if ("PicUrl".equals(name)) {//图片消息
                    responseMessage.setPicUrl(text);
                } else if ("Location_X".equals(name)) {//地理位置消息
                    responseMessage.setLocation_X(text);
                } else if ("Location_Y".equals(name)) {
                    responseMessage.setLocation_Y(text);
                } else if ("Scale".equals(name)) {
                    responseMessage.setScale(Long.parseLong(text));
                } else if ("Label".equals(name)) {
                    responseMessage.setLabel(text);
                } else if ("Event".equals(name)) {//事件消息
                    responseMessage.setEvent(text);
                } else if ("EventKey".equals(name)) {
                    responseMessage.setEventKey(text);
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(WeChatTextMsg textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(WeChatGraphicMsg newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new WeChatArticleMsg().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 输入xml转换成WeChatInputMsgDO对象
     *
     * @param xml
     * @return
     */
    public static WeChatInputMsg xmlToWXInputMessage(String xml) {
        xstream.alias("xml", WeChatInputMsg.class);
        return (WeChatInputMsg) xstream.fromXML(xml);
    }
}
