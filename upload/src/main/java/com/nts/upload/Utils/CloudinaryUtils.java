package com.nts.upload.Utils;


import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public final class CloudinaryUtils {
    private Cloudinary cloudinary;


    public CloudinaryUtils(
            @Value("${cloudinary.name}")
            String cloud_name,
            @Value("${cloudinary.apikey}")
            String api_key,
            @Value("${cloudinary.apisecret}")
            String api_secret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloud_name,
                "api_key", api_key,
                "api_secret", api_secret,
                "secure", true));
    }



    private String getFolder(String masterUrl){
        return masterUrl.substring(60, masterUrl.lastIndexOf('/'));
    }

    public Map<String, String> uploadHlsFolder(String folderPath, String cloudFolder) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("Invalid HLS folder path: " + folderPath);
        }

        Map<String, String> uploadedFiles = new HashMap<>();

        // Upload .ts and .m3u8 files only
        var validFiles = Files.walk(folder.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".ts") || path.toString().endsWith(".m3u8"))
                .collect(Collectors.toList());

        for (Path filePath : validFiles) {
            String relativePath = folder.toPath().relativize(filePath).toString().replace("\\", "/");

            try {
                var uploadResult = cloudinary.uploader().upload(
                        filePath.toFile(),
                        ObjectUtils.asMap(
                                "resource_type", "raw",
                                "folder", cloudFolder + "/" + folder.getName(),
                                "public_id", relativePath.replace(".ts", "").replace(".m3u8", "")
                        )
                );

                uploadedFiles.put(relativePath, uploadResult.get("secure_url").toString());
                System.out.println("✅ Uploaded: " + relativePath);

            } catch (Exception e) {
                System.out.println("⚠️ Skipped invalid file: " + relativePath + " | " + e.getMessage());
            }
        }

        return uploadedFiles;
    }


    public String uploadImage(String localPath, String cloudFolder) throws IOException {
        Path imagePath = Paths.get(localPath);

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("Image file not found at: " + localPath);
        }

        String fileName = imagePath.getFileName().toString();
        String publicId = cloudFolder + "/" + fileName.substring(0, fileName.lastIndexOf('.'));

        Map uploadRes = cloudinary.uploader().upload(
                imagePath.toFile(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "resource_type", "image",
                        "overwrite", true
                )
        );

        return (String) uploadRes.get("secure_url");
    }

    public boolean deleteVideo(String masterUrl)  {

        try {
            cloudinary
                    .api()
                    .deleteAllResources(ObjectUtils.emptyMap(

                    ));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
