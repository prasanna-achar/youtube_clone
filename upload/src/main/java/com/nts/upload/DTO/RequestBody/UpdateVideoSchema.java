package com.nts.upload.DTO.RequestBody;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVideoSchema {
    private String title;
    private String description;
}
