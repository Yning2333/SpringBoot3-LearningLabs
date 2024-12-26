package com.example.minio.controller;

import com.example.minio.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    /**
     * 上传文件
     * @param file 文件
     * @param path 自定义路径
     * @param contentType 文件类型
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "path", required = false) String path,
            @RequestParam(value = "contentType", required = false) String contentType) {

        try {
            String result = minioService.uploadFile(file, path, contentType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param objectName 文件名
     * @return 文件流
     */
    @GetMapping("/download/{objectName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("objectName") String objectName) {
        try {
            InputStream fileStream = minioService.downloadFile(objectName);
            byte[] fileBytes = fileStream.readAllBytes(); // 将 InputStream 转换为字节数组

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + objectName) // 设置文件名
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM) // 设置 Content-Type
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * 删除文件
     * @param objectName 文件名
     * @return 删除结果
     */
    @DeleteMapping("/delete/{objectName}")
    public ResponseEntity<String> deleteFile(@PathVariable("objectName") String objectName) {
        try {
            String result = minioService.deleteFile(objectName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }

    /**
     * 列出所有文件
     * @return 文件列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> files = minioService.listFiles();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
