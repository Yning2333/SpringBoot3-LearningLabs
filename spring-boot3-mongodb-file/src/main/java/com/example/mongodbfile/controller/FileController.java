package com.example.mongodbfile.controller;

import com.example.mongodbfile.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * URL_ADDRESS
     * POST http://localhost:8080/files/upload
     * Form-Data:
     *   Key: file
     *   Value: [选择一个文件]
     *
     *
     * 文件源数据可以通过 db.fs.files.find()命令查看。
     * 文件的分块数据可以通过 db.fs.chunks.find() 命令查看。
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileStorageService.storeFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to MongoDB: " + e.getMessage());
        }
    }

    /**
     * URL_ADDRESS
     * GET http://localhost:8080/files/download/{id}
     * @param id
     * @return
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        try {
            // 获取文件信息和输入流
            FileStorageService.FileInfo fileInfo = fileStorageService.getFile(id);
            InputStream inputStream = fileInfo.getInputStream();
            byte[] content = inputStream.readAllBytes();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileInfo.getContentType()));
            headers.setContentDispositionFormData("attachment", fileInfo.getFilename());

            // 返回响应
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(content);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading file from MongoDB: " + e.getMessage());
        }
    }
}