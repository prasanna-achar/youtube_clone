package com.nts.upload.Model;


import com.nts.upload.Model.Enums.VideoUploadingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;
    private String userId;

    @Column(nullable = false, length = 10, unique = true)
    private String token;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> videoLinks;

    private String thumbnailLink;

    @Enumerated(EnumType.STRING)
    private VideoUploadingStatus videoUploadingStatus;
    @PrePersist
    public void generateToken() {
        if (this.token == null) {
            this.token = randomToken(10);
        }
    }

    private String randomToken(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}
