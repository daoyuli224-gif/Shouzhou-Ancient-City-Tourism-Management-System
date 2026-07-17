package com.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 上传控制器
 * 文件保存到 uploadRootPath/yyyyMMdd/<UUID>.<ext>
 * 返回相对路径 upload/yyyyMMdd/<UUID>.<ext> 写入数据库
 * 前端通过 /upload/yyyyMMdd/<UUID>.<ext> 访问
 */
@Controller
public class UploadController extends BaseController {

    @Value("${upload.root-path:upload}")
    private String uploadRootPath;

    private File uploadRootDir;

    @PostConstruct
    public void init() {
        uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
    }

    /**
     * 上传文件
     */
    @RequestMapping(value = "/upload_re")
    public String Upload(MultipartFile fujian) throws Exception {
        String fileName = fujian.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffixName;

        // 按日期分子目录，如 upload/20260716/
        String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File targetDir = new File(uploadRootDir, dateFolder);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        try {
            File targetFile = new File(targetDir, fileName);
            fujian.transferTo(targetFile);

            // 写入数据库的是相对路径：upload/20260716/abc.jpg
            // 前端拼接 service_url 后访问：http://localhost:8088/upload/20260716/abc.jpg
            String relativePath = "upload/" + dateFolder + "/" + fileName;
            request.setAttribute("url", relativePath);

            if (isAjax()) {
                return jsonResult(request.getAttribute("url"));
            }
            return "upload";
        } catch (Exception e) {
            return showError(e.getMessage());
        }
    }
}
