package com.example.demo.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 二维码工具类...
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class QRCodeUtil {

    private static final int DEFAULT_WITH = 300;

    private static final int DEFAULT_HEIGHT = 300;

    private static final String DEFAULT_FORMAT = "png";

    private static final String DEFAULT_UTF_8 = "UTF-8";

    private static final QRCodeDefaultConfig DEFAULT_CONFIG = new QRCodeDefaultConfig();

    /**
     * <p>根据给定数据生成二维码对象</p>
     *
     * @param qrCodeContent 要写入二维码内容(文字或URL路径，如果是文字扫码后直接展示，如果是URL则扫码后直接跳转)
     * @param width         二维码照片宽度
     * @param height        二维码照片高度
     * @return
     */
    public static BitMatrix createBitMatrix(String qrCodeContent, int width, int height) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>(5);
        /**设置字符编码**/
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_UTF_8);
        /**指定纠错等级(级别越高，占用空间越大，存储的数据就越小，扫码速度也会相应越慢)*/
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        /**指定二维码周围空白间距(二维码边界空白大小 1,2,3,4 (4为默认,最大))*/
        hints.put(EncodeHintType.MARGIN, 1);
        return new MultiFormatWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, width, height, hints);

    }

    /**
     * <p>根据给定数据生成二维码对象</p>
     *
     * @param qrCodeContent 要写入二维码内容(文字或URL路径，如果是文字扫码后直接展示，如果是URL则扫码后直接跳转)
     * @return
     */
    public static BitMatrix createBitMatrix(String qrCodeContent) throws Exception {
        return createBitMatrix(qrCodeContent, DEFAULT_WITH, DEFAULT_HEIGHT);
    }

    /**
     * <p>创建二维码(不带logo图片)</p>
     *
     * @param qrCodeContent 要写入二维码内容(文字或URL路径，如果是文字扫码后直接展示，如果是URL则扫码后直接跳转)
     * @param qrCodePath    二维码输出路径
     * @throws IOException
     */
    public static void createQRCode(String qrCodeContent, String qrCodePath) throws Exception {
        MatrixToImageWriter.writeToPath(createBitMatrix(qrCodeContent), DEFAULT_FORMAT, new File(qrCodePath).toPath(), new MatrixToImageConfig());
    }

    /**
     * <p> 创建二维码、以及将照片logo写入二维码中 </p>
     *
     * @param qrCodeContent 要写入二维码内容(文字或URL路径，如果是文字扫码后直接展示，如果是URL则扫码后直接跳转)
     * @param qrCodePath    二维码输出路径
     * @param logoPath      logo路径
     * @throws Exception
     */
    public static void createQRCode(String qrCodeContent, String qrCodePath, String logoPath) throws Exception {
        createQRCode(qrCodeContent, qrCodePath, logoPath, DEFAULT_CONFIG);
    }

    /**
     * <p> 创建二维码、以及将照片logo写入二维码中</p>
     *
     * @param qrCodeContent 要写入二维码内容(文字或URL路径，如果是文字扫码后直接展示，如果是URL则扫码后直接跳转)
     * @param qrCodePath    二维码输出路径
     * @param logoPath      logo 路径
     * @param logoConfig    logo配置对象
     * @throws Exception
     */
    public static void createQRCode(String qrCodeContent, String qrCodePath, String logoPath, QRCodeDefaultConfig logoConfig) throws Exception {
        /**
         * @1.先把二维码写到指定文件中
         * @2.再读取已生成的二维码，进行绘制，把logo绘制到二维码中
         * @3.再写出
         * */
        MatrixToImageWriter.writeToPath(createBitMatrix(qrCodeContent), DEFAULT_FORMAT, new File(qrCodePath).toPath(), new MatrixToImageConfig());
        //添加logo图片, 此处一定需要重新进行读取，而不能直接使用二维码的BufferedImage 对象
        BufferedImage img = ImageIO.read(new File(qrCodePath));
        appendLogoToQRCode(img, qrCodePath, logoPath, logoConfig);
    }


    /**
     * 将照片logo添加到二维码中间
     *
     * @param image     生成的二维码照片对象
     * @param imagePath 照片保存路径
     * @param logoPath  logo照片路径
     */
    public static void appendLogoToQRCode(BufferedImage image, String imagePath, String logoPath, QRCodeDefaultConfig logoConfig) {
        try {
            BufferedImage logo = ImageIO.read(new File(logoPath));
            Graphics2D g = image.createGraphics();
            /**考虑到logo照片贴到二维码中，建议大小不要超过二维码的1/5;*/
            int width = image.getWidth() / logoConfig.getLogoPart();
            int height = image.getHeight() / logoConfig.getLogoPart();
            /**logo起始位置，此目的是为logo居中显示*/
            int x = (image.getWidth() - width) / 2;
            int y = (image.getHeight() - height) / 2;
            /**绘制图*/
            g.drawImage(logo, x, y, width, height, null);
            /**
             * <p>给logo画边框</p>
             * <p>构造一个具有指定线条宽度以及 cap 和 join 风格的默认值的实心 BasicStroke</p>
             * **/
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, width, height);
            g.dispose();
            /**写入logo照片到二维码**/
            ImageIO.write(image, DEFAULT_FORMAT, new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
//            createQRCode("你好", "D:\\Administrator\\Documents\\WeChat Files\\FF_FelicificFish\\FileStorage\\File\\2019-05\\a.png");
            createQRCode("您好",
                    "D:\\Administrator\\Documents\\WeChat Files\\FF_FelicificFish\\FileStorage\\File\\2019-05\\a.png",
                    "D:\\Administrator\\Documents\\WeChat Files\\FF_FelicificFish\\FileStorage\\File\\2019-05\\槐米.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
