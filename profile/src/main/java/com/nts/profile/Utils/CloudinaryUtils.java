package com.nts.profile.Utils;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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






    public String uploadImage(Path file, String cloudFolder, String name) throws IOException {



        String publicId = cloudFolder + "/"+ name;

        Map uploadRes = cloudinary.uploader().upload(
                file.toFile(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "resource_type", "image",
                        "overwrite", true
                )
        );

        return (String) uploadRes.get("secure_url");
    }


}
