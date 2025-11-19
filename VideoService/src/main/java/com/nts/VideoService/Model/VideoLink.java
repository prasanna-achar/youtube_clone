package com.nts.VideoService.Model;

import com.nts.VideoService.Model.Enums.VideoUploadingStatus;
import com.nts.VideoService.Model.structure.VideoLinkStructure;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoLink {
    @Id
    private String id;

    private VideoLinkStructure videoLinks;

    private String thumbnailLink;

    private VideoUploadingStatus videoUploadingStatus;
}
