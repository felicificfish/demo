package com.example.demo.utils;

import lombok.Getter;

import java.awt.*;

/**
 * 二维码默认配置项...
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class QRCodeDefaultConfig {

    /**
     * <p>logo默认边框颜色</p>
     */
    public static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

    /**
     * <p>logo默认边框宽度</p>
     */
    public static final int DEFAULT_BORDER_WIDTH = 2;

    /**
     * <p> logo大小默认为照片的1/5 </p>
     **/
    public static final int DEFAULT_LOGO_PART = 5;

    @Getter
    private final int border = DEFAULT_BORDER_WIDTH;
    @Getter
    private final Color borderColor;
    @Getter
    private final int logoPart;

    public QRCodeDefaultConfig() {
        this(DEFAULT_BORDER_COLOR, DEFAULT_LOGO_PART);
    }

    /**
     * @param borderColor
     * @param logoPart    值越大，logo越小
     */
    public QRCodeDefaultConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

}
