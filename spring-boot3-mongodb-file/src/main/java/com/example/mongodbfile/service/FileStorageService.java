package com.example.mongodbfile.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileStorageService {

    @Autowired
    private GridFSBucket gridFSBucket;

    public String storeFile(MultipartFile file) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new org.bson.Document("contentType", file.getContentType()));

        ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), file.getInputStream(), options);

        return fileId.toString();
    }

    public FileInfo getFile(String id) {
        try {
            GridFSFile gridFSFile = gridFSBucket.find(new org.bson.Document("_id", new ObjectId(id))).first();
            if (gridFSFile == null) {
                throw new RuntimeException("File not found");
            }
            InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            String contentType = gridFSFile.getMetadata().getString("contentType");
            return new FileInfo(gridFSFile.getFilename(), contentType, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file from MongoDB: " + e.getMessage());
        }
    }

    public static class FileInfo {
        private String filename;
        private String contentType;
        private InputStream inputStream;

        public FileInfo(String filename, String contentType, InputStream inputStream) {
            this.filename = filename;
            this.contentType = contentType;
            this.inputStream = inputStream;
        }

        public String getFilename() {
            return filename;
        }

        public String getContentType() {
            return contentType;
        }

        public InputStream getInputStream() {
            return inputStream;
        }
    }
}