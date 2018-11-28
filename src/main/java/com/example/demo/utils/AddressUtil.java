package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.AddressDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据IP地址获取详细的地域信息
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
public class AddressUtil {
    /**
     * @param ip 请求IP
     * @return AddressDO
     */
    public static AddressDO getAddresses(String ip) {
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = getResult(urlStr, "ip=" + ip, "UTF-8");
        AddressDO addressDO = new AddressDO();
        if (StringUtils.hasText(returnStr)) {
            JSONObject object = JSON.parseObject(returnStr);
            Integer code = object.getInteger("code");
            // 0：成功，1：失败
            if (code == 0) {
                JSONObject data = object.getJSONObject("data");
                if (!data.isEmpty()) {
                    addressDO.setIp(data.getString("ip"));
                    addressDO.setCountry(data.getString("country"));
                    addressDO.setCountryId(data.getString("country_id"));
                    addressDO.setArea(data.getString("area"));
                    addressDO.setAreaId(data.getString("area_id"));
                    addressDO.setRegion(data.getString("region"));
                    addressDO.setRegionId(data.getString("region_id"));
                    addressDO.setCity(data.getString("city"));
                    addressDO.setCityId(data.getString("city_id"));
                    addressDO.setCounty(data.getString("county"));
                    addressDO.setCountyId(data.getString("county_id"));
                    addressDO.setIsp(data.getString("isp"));
                    addressDO.setIspId(data.getString("isp_id"));
                }
            }
        }
        return addressDO;
    }

    /**
     * @param urlStr   请求的地址
     * @param content  请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private static String getResult(String urlStr, String content, String encoding) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            // 新建连接实例
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间，单位毫秒
            connection.setConnectTimeout(2000);
            // 设置读取数据超时时间，单位毫秒
            connection.setReadTimeout(2000);
            // 是否打开输出流 true|false
            connection.setDoOutput(true);
            // 是否打开输入流true|false
            connection.setDoInput(true);
            // 提交方法POST|GET
            connection.setRequestMethod("POST");
            // 是否缓存true|false
            connection.setUseCaches(false);
            // 打开连接端口
            connection.connect();
            // 打开输出流往对端服务器写数据
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.writeBytes(content);
            out.flush();
            out.close();
            // 往对端写完数据对端服务器返回数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String ip = "202.99.166.6";
        AddressDO address = AddressUtil.getAddresses(ip);
        log.info(address);
    }
}
