package com.example.productserviceproject.repository;

import com.example.productserviceproject.config.FirebaseConfig;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class FileService {

    @Autowired
    private FirebaseConfig firebaseConfig;

    public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("product-service-sop.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media")
                .setMetadata(Map.of("contentDisposition", "inline"))
                .build();

        // Decode the Base64-encoded private key
        byte[] decodedBytes = Base64.getMimeDecoder().decode(firebaseConfig.getPrivateKey()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""));

        // Convert the decoded bytes to a string
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println("Decoded String: " + decodedString);

        // Create GoogleCredentials from the decoded string
        Credentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(decodedString.getBytes())
        );

        // Use the credentials to create a Storage object
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        // Upload the file to Google Cloud Storage
        Blob created = storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // Delete the local file
        file.delete();

        // Return the media link of the uploaded file
        return created.getMediaLink();
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = new Date().getTime() + "-" + originalFilename.replace(" ", "-");

        File convertedFile = new File(Objects.requireNonNull(filename));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            System.out.println("File URL is empty or null.");
            return;
        }

        String objectName;

        // Extract the object name from the URL
        int index = fileUrl.indexOf("product-service-sop.appspot.com");

        if (index != -1) {
            objectName = fileUrl.substring(index + "product-service-sop.appspot.com".length() + 1);
            objectName = objectName.replaceAll("o/", "").replaceAll("\\?generation=\\d+&alt=media", "");
            System.out.println("Object name: " + objectName);
            try {
                Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("firebasePrivateKey.json"));
                Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                BlobId blobId = BlobId.of("product-service-sop.appspot.com", objectName);
                Blob blob = storage.get(blobId);
                if (blob != null) {
                    blob.delete();
                    System.out.println("File deleted successfully.");
                } else {
                    System.out.println("File not found.");
                }
            } catch (StorageException e) {
                System.err.println("Error deleting file from Google Cloud Storage: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }
}
