package com.example.demo.utils;

import com.github.jarlakxen.embedphantomjs.ExecutionTimeout;
import com.github.jarlakxen.embedphantomjs.PhantomJSReference;
import com.github.jarlakxen.embedphantomjs.executor.PhantomJSFileExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 页面转图片工具，windows版需要将phantomjs放在目录：C:\Users\Administrator\.embedphantomjs\2.1.1下
 *
 * @author zhou.xy
 * @since 2019/5/28
 */
public class PhantomJsUtil {
    /**
     * @param binaryPath 可执行路径；phantomjs安装路径bin目录
     * @param url        html请求路径；如：http://127.0.0.1:8089/snapshoot/20170426/2017042600013.html
     * @param pngPath    图片保存路径；如：D:\\data\\snapshoot\\20170426\\abc.png
     * @return
     */
    public static boolean html2Png(String binaryPath, String url, String pngPath) {
        StringBuffer command = new StringBuffer();
        command.append("'use strict';");
        command.append("var system = require('system');");
        command.append("var page = require('webpage').create();");
        command.append("var url = system.args[1];");
        command.append("var fileName = system.args[2];");
//        command.append("console.log('url:' + url);");
//        command.append("console.log('fileName:' + fileName);");
        command.append("page.open(url, function (status) {");
        command.append("console.log('Status: ' + status);");
        command.append("if (status === 'success') {");
        command.append("page.render(fileName);");
        command.append("}");
        command.append("phantom.exit();");
        command.append("});");

//		File jsFile = new File("F:\\study\\phantomjs.org\\phantomjs-2.1.1-windows\\examples\\html2image.js");
//		try {
//			jsFile.createNewFile();
//			FileUtils.write(jsFile, command.toString(), Charset.defaultCharset());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        PhantomJSReference.PhantomJSReferenceBuilder builder = PhantomJSReference.create();
        PhantomJSReference phantomJSRef = builder.build();
//        phantomJSRef.setBinaryPath(binaryPath);
        phantomJSRef.ensureBinary();// 确定可执行程序

        PhantomJSFileExecutor exf = new PhantomJSFileExecutor(phantomJSRef, new ExecutionTimeout(60, TimeUnit.SECONDS));
        try {
//			String out = exf.execute(jsFile, url, pngPath).get();
//			System.out.println(out);
            String out2 = exf.execute(command.toString(), url, pngPath).get();
            System.out.println(out2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
//        html2Png("F:\\study\\phantomjs.org\\phantomjs-2.1.1-windows\\bin\\phantomjs",
//                "https://blog.csdn.net/qq_39630314/article/details/80520309",
//                "D:\\data\\snapshoot\\i.png");

        html2Png(null,
                "https://blog.csdn.net/qq_39630314/article/details/80520309",
                "D:\\data\\snapshoot\\j.png");
    }
}
