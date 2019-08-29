package com.example.demo.service;

import com.aliyun.oss.model.ObjectMetadata;
import com.example.demo.configs.exception.ValidateException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务
 *
 * @author zhou.xy
 * @date 2019/8/29
 * @since 1.0
 */
@Service
public class FileService {
    @Autowired
    private OssFileAccessService ossFileAccessService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */
    public Map<String, Object> uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileSuffix = resolveSuffix(fileName);
        try {
            byte[] bytes = file.getBytes();
            String fileKey = RandomStringUtils.random(32, true, true) + "." + fileSuffix;
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition("attachment;filename=\"" + fileName + "\"");
            ossFileAccessService.putObject(fileKey, is, metadata);
            String fileUrl = ossFileAccessService.genFileUrl(fileKey);

            Map<String, Object> map = new HashMap<>(2);
            map.put("fileUrl", fileUrl);
            map.put("fileName", fileName);
            return map;
        } catch (IOException e) {
            throw new ValidateException(e.getMessage());
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return String 文件后缀
     */
    private String resolveSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        } else {
            int index = fileName.lastIndexOf(".");
            return index == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        }
    }
}
