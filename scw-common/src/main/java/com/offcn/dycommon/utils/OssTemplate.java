package com.offcn.dycommon.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@ToString
@Data
public class OssTemplate {

    private String endpoint;
    private String bucketDomain;// "https://"+endpoint
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(InputStream inputStream,String filename){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String newFileName = UUID.randomUUID().toString().replace("-","")+"_"+filename;

        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        ossClient.putObject(bucketName,"pic/"+date+"/"+newFileName,inputStream);

        try {
            inputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        ossClient.shutdown();
        String url= "https://"+bucketDomain+"/pic/"+date+"/"+newFileName;
        System.out.println("上传文件访问路径:"+url);
        return url;

    }
}
