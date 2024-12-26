package com.example.minio.service;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 上传文件到 MinIO
     * @param file MultipartFile 文件
     * @param path 自定义路径，可为空
     * @param contentType 文件类型，可为空
     * @return 上传成功信息
     */
    public String uploadFile(MultipartFile file, String path, String contentType) {
        try {
            // 确保桶存在
            ensureBucketExists();

            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            if (path != null && !path.isEmpty()) {
                fileName = path + "/" + fileName;
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType != null ? contentType : file.getContentType())
                            .build()
            );

            return "File uploaded successfully: " + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param objectName 文件名称
     * @return 文件流
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from MinIO: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param objectName 文件名称
     * @return 删除成功信息
     */
    public String deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return "File deleted successfully: " + objectName;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO: " + e.getMessage());
        }
    }

    /**
     * 检查桶是否存在，如果不存在则创建
     */
    private void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error ensuring bucket existence: " + e.getMessage());
        }
    }

    /**
     * 列出桶中的所有文件
     * @return 文件列表
     */
    public List<String> listFiles() {
        try {
            List<String> fileNames = new ArrayList<>();
            Iterable<Result<Item>> items = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );

            for (Result<Item> item : items) {
                fileNames.add(item.get().objectName());
            }
            return fileNames;
        } catch (Exception e) {
            throw new RuntimeException("Error listing files in bucket: " + e.getMessage());
        }
    }
}
