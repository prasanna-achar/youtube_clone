package com.nts.upload.DTO.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoSchema {
    private String title;
    private String description;
    private MultipartFile video;
    private MultipartFile thumbnail;
}
