package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author zhou.xy
 * @date 2018/9/19 18:43
 * @since  1.0
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
}
