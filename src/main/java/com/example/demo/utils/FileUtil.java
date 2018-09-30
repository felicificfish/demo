package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author zhou.xy
 * @date 2018/9/19 18:43
 * @since 1.0
 */
@Log4j2
public class FileUtil {
    public static List<String> readFile2(String fileName) {
        List<String> list = new ArrayList<>();
        BufferedReader reader = null;
        FileInputStream fis = null;
        try {
            File file = new File(fileName);
            if (file.isFile() && file.exists()) {
                fis = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                String line;
                while (null != (line = reader.readLine())) {
                    if (!StringUtils.isEmpty(line)) {
                        list.add(line);
                    }
                }
            }
        } catch (Exception e) {
            log.error("readFile", e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("InputStream关闭异常", e);
            }
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error("FileInputStream关闭异常", e);
            }
        }
        return list;
    }


    /**
     * 读取文件内容
     *
     * @param fileName 文件路径
     * @return 文本集合
     */
    public static List<String> readFile(String fileName) {
        List<String> list = new ArrayList<>();

        try (
                FileInputStream fis = new FileInputStream(new File(fileName));
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))
        ) {
            String line;
            while (null != (line = reader.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            log.error("readFile", e);
        }
        return list;
    }

    /**
     * 将内容写入文件
     *
     * @param content  内容
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 写入文件是否成功 true|false
     */
    public static boolean writeFile(String content, String filePath, String fileName) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file, false);
            fos.write((content + "\n").getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return true;
    }

    public static String getDownLoadFileName(String fileName, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String name = null;

        try {
            name = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("%28", "(").replace("%29", ")");
        } catch (UnsupportedEncodingException var6) {
            name = fileName;
        }

        if (!StringUtils.isEmpty(userAgent)) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.indexOf("opera") != -1) {
                name = "filename*=UTF-8''" + name;
            } else if (userAgent.indexOf("msie") != -1) {
                name = "filename=\"" + name + "\"";
            } else if (userAgent.indexOf("mozilla") != -1 && userAgent.indexOf("firefox") != -1) {
                name = "filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"";
            } else if (userAgent.indexOf("mozilla") != -1) {
                name = "filename=\"" + name + "\"";
            } else {
                name = "\"filename=" + name + "\"";
            }
        } else {
            name = "\"filename=" + name + "\"";
        }

        return name;
    }

    public static void response(HttpServletRequest request, HttpServletResponse response, byte[] buffer, String fileName) throws IOException {
        Assert.notNull(buffer, "buffer is null.");
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buffer));
        OutputStream rOut = response.getOutputStream();
        BufferedOutputStream brOut = new BufferedOutputStream(rOut);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;" + getDownLoadFileName(fileName, request));
        response.addHeader("Content-Length", String.valueOf(buffer.length));
        byte[] buf = new byte[10240];

        int length;
        while((length = bis.read(buf)) != -1) {
            brOut.write(buf, 0, length);
        }

        brOut.flush();
        bis.close();
        rOut.close();
        brOut.close();
    }
}
