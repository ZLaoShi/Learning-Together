package com.example.controller;

import com.example.annotation.SysLogger;
import com.example.entity.RestBean;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {


    @PostMapping("/upload")
    @SysLogger("上传图片")
    public RestBean<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 获取原文件后缀
            String originalFilename = file.getOriginalFilename();
            if(originalFilename == null) {
                return RestBean.failure(400, "文件名不能为空");
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 生成新文件名
            String newFileName = UUID.randomUUID().toString() + suffix;
            
            // 使用File类直接操作文件系统
            String projectPath = System.getProperty("user.dir");
            String uploadPath = projectPath + "/backend/src/main/resources/static/uploads/images/";
            File saveDir = new File(uploadPath);
            
            // 确保目录存在
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            
            // 保存文件
            File dest = new File(saveDir, newFileName);
            file.transferTo(dest);
            
            // 返回访问路径
            return RestBean.success("uploads/images/" + newFileName);
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return RestBean.failure(400, "上传图片失败");
        }
    }
}