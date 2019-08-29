package com.example.demo.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 为所有的OSS文件操作提供OSSClient实例。
 * 所有涉及到文件操作的控制层，需要被纳入到此切面的范围内从而获取与OSS服务操作支持。
 *
 * @author zhou.xy
 * @since 2019/4/22
 */
@Service
public class OssFileAccessService {
    @Value("${oss.bucket}")
    public String defaultBucket;
    @Value("${oss.pc.accesskeyid}")
    private String pcAccessKeyID;
    @Value("${oss.pc.accesskeysecret}")
    private String pcAccessKeySecret;
    @Value("${oss.endpoint}")
    private String endpoint;

    public OSSClient getOSSClient() {
        return new OSSClient(endpoint, pcAccessKeyID, pcAccessKeySecret);
    }

    public String genFileUrl(String key) {
        return getDomain() + key;
    }

    public String getDomain() {
        return "https://" + defaultBucket + "." + endpoint + "/";
    }

    public void putObject(String key, InputStream input) {
        OSSClient client = getOSSClient();
        client.putObject(this.defaultBucket, key, input);
        client.shutdown();
    }

    public void putObject(String key, InputStream input, ObjectMetadata metadata) {
        OSSClient client = getOSSClient();
        client.putObject(this.defaultBucket, key, input, metadata);
        client.shutdown();
    }

    public void putObject(Map<String, InputStream> files) {
        OSSClient client = getOSSClient();
        for (Map.Entry<String, InputStream> entry : files.entrySet()) {
            client.putObject(this.defaultBucket, entry.getKey(), entry.getValue());
            try {
                entry.getValue().close();
            } catch (Exception e) {
            }
        }
        client.shutdown();
    }

    public byte[] getBytes(String key) {
        OSSClient client = getOSSClient();
        InputStream result = client.getObject(this.defaultBucket, key).getObjectContent();
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.shutdown();
        return bytes;
    }

    public boolean doesObjectExist(String key) {
        OSSClient client = getOSSClient();
        boolean result = client.doesObjectExist(defaultBucket, key);
        client.shutdown();
        return result;
    }

    public SimplifiedObjectMeta getSimplifiedObjectMeta(String key) {
        OSSClient client = getOSSClient();
        SimplifiedObjectMeta result = client.getSimplifiedObjectMeta(defaultBucket, key);
        client.shutdown();
        return result;
    }

    public ObjectListing listObjects() {
        OSSClient client = getOSSClient();
        ObjectListing result = client.listObjects(defaultBucket);
        client.shutdown();
        return result;
    }

    public ObjectListing listObjects(String prefix) {
        OSSClient client = getOSSClient();
        ObjectListing result = client.listObjects(defaultBucket, prefix);
        client.shutdown();
        return result;
    }
}
