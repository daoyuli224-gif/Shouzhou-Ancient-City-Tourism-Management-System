package com.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 跨域配置 + 静态资源映射
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${upload.root-path:upload}")
    private String uploadRootPath;

    /**
     * 启动时写入系统属性，供非 Spring 管理的类（如 Uploader）读取上传路径
     */
    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadRootPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String absolutePath = uploadDir.getAbsolutePath().replace("\\", "/");
        System.setProperty("upload.root.path", absolutePath);
    }

    /**
     * 跨域：允许前端直连后端 API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 静态资源：将 /upload/** URL 映射到磁盘上的 upload 目录
     * 这样前端通过 http://localhost:8088/upload/20260716/xxx.jpg 就能访问上传的图片
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File uploadDir = new File(uploadRootPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String absolutePath = uploadDir.getAbsolutePath().replace("\\", "/");
        if (!absolutePath.endsWith("/")) {
            absolutePath += "/";
        }
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + absolutePath);
    }
}
