package com.example.demo.controller;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 通用服务
 *
 * @author zhou.xy
 * @date 2019/8/29
 * @since 1.0
 */
@RestController
public class CommonController {

    @Autowired
    private FileService fileService;

    @PostMapping("/file/upload")
    public Map<String, Object> upload(@RequestParam MultipartFile file) {
        if (null == file) {
            throw new ValidateException("请上传附件");
        }
        return fileService.uploadFile(file);
    }
}
