package com.nts.VideoService.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private String token;

    public Video(String title, String description, String token) {
        this.title = title;
        this.description = description;
        this.token = token;
    }
}
