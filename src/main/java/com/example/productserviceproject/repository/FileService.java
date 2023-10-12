package com.example.productserviceproject.repository;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Objects;

@Service
public class FileService {
    public String uploadFile(File file, String fileName) throws IOException {

        BlobId blobId = BlobId.of("product-service-sop.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").setMetadata(ImmutableMap.of("contentDisposition", "inline")).build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/Users/aikaze/Documents/coding/SOP/ProductServiceProject/firebasePrivateKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob created = storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        file.delete();
        return created.getMediaLink();
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();                        // to get original file name
        String filename = new Date().getTime() + "-" + originalFilename.replace(" ", "-");

        File convertedFile = new File(Objects.requireNonNull(filename));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
}
