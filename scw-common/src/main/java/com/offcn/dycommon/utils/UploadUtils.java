package com.offcn.dycommon.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadUtils {
    public static void main(String[] args) throws FileNotFoundException {
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI5tHzugCdzPnX1MLbigoZ";
        String accessKeySecret = "nwdIkQ8Jhel15YZSbhMJ68qwTOTOqU";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        InputStream inputStream = new FileInputStream(new File("C:\\Users\\King_Zzzz\\Desktop\\111.png"));
        ossClient.putObject("afa-bucket", "img/008.jpg", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();

        System.out.println("测试完成");
    }

}
