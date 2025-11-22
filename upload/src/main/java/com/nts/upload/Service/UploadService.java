package com.nts.upload.Service;

import com.nts.upload.ApiResponseBody.ApiFailResponse;
import com.nts.upload.ApiResponseBody.ResponseBody;
import com.nts.upload.DTO.Message.Message;
import com.nts.upload.DTO.RequestBody.UpdateVideoSchema;
import com.nts.upload.DTO.RequestBody.UploadVideoSchema;
import com.nts.upload.Model.Enums.VideoUploadingStatus;
import com.nts.upload.Model.Video;
import com.nts.upload.Repository.VideoRepository;
import com.nts.upload.Utils.CloudinaryUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.nts.upload.ApiResponseBody.ApiSuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UploadService {


    @Autowired
    private VideoRepository videoRepository;



    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.dir.upload_dir}")
    private String UPLOAD_DIR;


    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.video.process.key}")
    private String processRoutingKey;


    @Autowired
    CloudinaryUtils cloudinaryUtils;

    public UploadService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public ResponseBody processVideo(String userId, UploadVideoSchema uploadVideoSchema) throws IOException {
        if (uploadVideoSchema == null || uploadVideoSchema.getVideo() == null || uploadVideoSchema.getVideo().isEmpty()) {
            throw new IllegalArgumentException("Video file is required");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User id is required");
        }

        File videosDir = new File(UPLOAD_DIR + "/videos");
        File thumbnailsDir = new File(UPLOAD_DIR + "/thumbnail");

        if (!videosDir.exists()) videosDir.mkdirs();
        if (!thumbnailsDir.exists()) thumbnailsDir.mkdirs();

        MultipartFile videoFile = uploadVideoSchema.getVideo();
        String originalFileName = videoFile.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String fileName = userId + "_" + System.currentTimeMillis() + extension;
        Path videoPath = Paths.get(videosDir.getAbsolutePath(), fileName);

        Video video = new Video();
        video.setTitle(uploadVideoSchema.getTitle());
        video.setDescription(uploadVideoSchema.getDescription());
        video.setUserId(userId);
        video.setVideoUploadingStatus(VideoUploadingStatus.PENDING);
        Video savedVideo = videoRepository.save(video);

        String videoId = savedVideo.getToken();
        if (videoId == null || videoId.isBlank()) {
            videoId = userId + "_" + System.currentTimeMillis();
        }

        // save entry in DB as pending


        String thumbnailUrl = null;

        try {
            // Save video locally
            Files.write(videoPath, videoFile.getBytes());

            // Handle optional thumbnail
            if (uploadVideoSchema.getThumbnail() != null && !uploadVideoSchema.getThumbnail().isEmpty()) {
                MultipartFile thumb = uploadVideoSchema.getThumbnail();
                String thumbOriginal = thumb.getOriginalFilename();
                String thumbExt = "";

                if (thumbOriginal != null && thumbOriginal.contains(".")) {
                    thumbExt = thumbOriginal.substring(thumbOriginal.lastIndexOf("."));
                }

                String thumbName = userId + "_" + System.currentTimeMillis() + thumbExt;
                Path thumbPath = Paths.get(thumbnailsDir.getAbsolutePath(), thumbName);
                Files.write(thumbPath, thumb.getBytes());

                try {
                    thumbnailUrl = cloudinaryUtils.uploadImage(thumbPath.toString(), "users/" + userId + "/thumbnails");
                } catch (IOException e) {
                    System.out.println("Failed to upload thumbnail: " + e.getMessage());
                }
            }

            // Update DB to queued
            savedVideo.setThumbnailLink(thumbnailUrl);
            savedVideo.setVideoUploadingStatus(VideoUploadingStatus.PROCESSING);
            videoRepository.save(savedVideo);
            // Build message for RabbitMQ
            Map<String, String> map = new HashMap<>();
            map.put("video_path", videoPath.toString());
            map.put("user_id", userId);
            map.put("video_id", videoId);
            if (thumbnailUrl != null) {
                map.put("thumbnail_url", thumbnailUrl);
            }

            Message msg = new Message();
            msg.setMessage(map);

            rabbitTemplate.convertAndSend(exchangeName, processRoutingKey, msg);
            System.out.println("✅ Video queued for processing: " + fileName);

            return new ApiSuccessResponse<>(HttpStatus.OK, "Video uploaded and queued successfully", null);

        } catch (Exception e) {
            video.setVideoUploadingStatus(VideoUploadingStatus.FAILED);
            videoRepository.save(video);
            try { Files.deleteIfExists(videoPath); } catch (Exception ignore) {}
            throw new IOException("Failed to process video upload", e);
        }
    }

    public ResponseBody updateVideo(String userId, String videoId, UpdateVideoSchema updateVideoSchema){
        if(!videoRepository.existsById(videoId)){
            return new ApiFailResponse(HttpStatus.NOT_FOUND, "Video not found to update");
        }
        Video video = videoRepository.findById(videoId).get();
        if(!video.getId().equals(userId)){
            return  new ApiFailResponse(HttpStatus.UNAUTHORIZED, "You are not authorized to update this video");
        }

        if(updateVideoSchema.getTitle() == null || updateVideoSchema.getDescription() == null){
            return new ApiFailResponse(HttpStatus.NOT_ACCEPTABLE, "Title and description are supposed to be empty");
        }

        video.setTitle(updateVideoSchema.getTitle());
        video.setDescription(updateVideoSchema.getDescription());
        video = videoRepository.save(video);
//        VideoLink videoLink = videoLinkRepository.findById(video.getToken()).get();
        return new ApiSuccessResponse<>(HttpStatus.OK, "success", null);
    }


    public ResponseBody deleteById(String userId, String Id){
        if(!videoRepository.existsById(Id)){
            return new ApiFailResponse(HttpStatus.NOT_FOUND, "Video not found to delete");
        }
        Video video = videoRepository.findById(Id).get();
        if(!video.getUserId().equals(userId)){
            return new ApiFailResponse(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to delete this video");
        }

        videoRepository.deleteById(Id);
        return new ApiSuccessResponse<>("Video has been deleted successfully");
    }

    public ResponseBody getVideoById(String Id){
        if(!videoRepository.existsById(Id)){
            return new ApiFailResponse(HttpStatus.NOT_FOUND, "Video Not found");
        }

        return new ApiSuccessResponse<Video>(HttpStatus.ACCEPTED, "Success",
                videoRepository.findById(Id).get());
    }
    public ResponseBody getVideoByToken(String token){
        if(!videoRepository.existsByToken(token)){
            return new ApiFailResponse(HttpStatus.NOT_FOUND, "Video Not found");
        }


        return new ApiSuccessResponse<Video>(HttpStatus.ACCEPTED, "Success",
                videoRepository.findByToken(token).get()
        );
    }

    public ResponseBody getAllVideos(){

        List<Video> videos = videoRepository.findAll()
                .stream()
                .filter(v -> v.getVideoUploadingStatus() == VideoUploadingStatus.UPLOADED)
                .toList();


        return new ApiSuccessResponse<>(HttpStatus.OK,
                "All video have been successfully fetched",
                videos);
    }

    public void changeVideo(String Id, MultipartFile video){
        if(!videoRepository.existsById(Id)){
//            return new ApiFailResponse(HttpStatus.NOT_FOUND, "Video Not found");
            return;
        }
        Map<String , String> links = videoRepository
                .findById(Id)
                .get()
                .getVideoLinks();
        String masterUrl =  links.get("auto");

    }

    public ResponseBody getVideoByUserId(String userId){
        return new ApiSuccessResponse<>(HttpStatus.OK, "fetched all videos" , videoRepository.findAllByUserId(userId));
    }

}
