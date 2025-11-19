package com.nts.VideoService.Service;

import com.nts.VideoService.DTO.UpdateVideoToken;
import com.nts.VideoService.DTO.VideoSchema;
import com.nts.VideoService.Model.Video;
import com.nts.VideoService.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoDetailsService {

    @Autowired
    private VideoRepository videoRepository;

    public void createVideo(VideoSchema videoSchema) {
        Video video = new Video();
        video.setTitle(videoSchema.getTitle());
        video.setDescription(videoSchema.getDescription());
        video.setToken(videoSchema.getToken());
        videoRepository.save(video);
    }

    public void updateDetails(String id, VideoSchema videoSchema) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        video.setTitle(videoSchema.getTitle());
        video.setDescription(videoSchema.getDescription());
        videoRepository.save(video);
    }

    public void updateVideoToken(String id, UpdateVideoToken updateVideoToken) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        video.setToken(updateVideoToken.getToken());
        videoRepository.save(video);
    }

    public void deleteVideo(String id) {
        videoRepository.deleteById(id);
    }

    public Video getVideo(String id) {
        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}
