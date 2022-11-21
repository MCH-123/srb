package com.atguigu.srb.oss.service;

import java.io.InputStream;

/**
 * @ClassName FileService
 * @Description TODO
 * @Author mch
 * @Date 2022/11/16
 * @Version 1.0
 */
public interface FileService {
    //文件上传到阿里云
    String upload(InputStream inputStream, String module, String fileName);
    //从阿里云删除文件
    void removeFile(String url);
}
