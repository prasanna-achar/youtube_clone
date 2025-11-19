package com.nts.upload.Model.structure;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoLinkStructure {
    private String masterLink;
    private String _1080p;
    private String _720p;
    private String _480p;

}
