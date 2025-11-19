package com.nts.upload.Consumers;

import com.nts.upload.DTO.Message.Message;
import com.nts.upload.Model.Enums.VideoUploadingStatus;
import com.nts.upload.Model.Video;
import com.nts.upload.Model.structure.VideoLinkStructure;
import com.nts.upload.Repository.VideoRepository;
import com.nts.upload.Utils.CloudinaryUtils;
import com.nts.upload.Utils.VideoProcessingUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RabbitMQConsumer {


    @Autowired
    VideoRepository videoRepository;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.video.upload.key}")
    private String uploadRoutingKey;

    @Autowired
    VideoProcessingUtils videoProcessingUtils;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CloudinaryUtils cloudinaryUtils;


    @RabbitListener(queues = "${rabbitmq.queue.video.process.name}")
    public void videoProcessingConsumer(Message mes) {
        System.out.println("🎞️ Received video processing message: " + mes.getMessage());

        String videoId = mes.getMessage().get("video_id");
        updateVideoStatus(videoId, VideoUploadingStatus.PROCESSING);

        String videoPath = mes.getMessage().get("video_path");


        Map<String, String> msg = videoProcessingUtils.transcodeVideo(videoPath);
        String outputDir = msg.getOrDefault("outputDir", mes.getMessage().get("video_path") + "_hls");
        msg.put("hls_path", outputDir);
        if (videoId != null) {
            msg.put("video_id", videoId);
        }
        String userId = mes.getMessage().get("user_id");
        if (userId != null) {
            msg.put("user_id", userId);
        }
        String thumbnailUrl = mes.getMessage().get("thumbnail_url");
        if (thumbnailUrl != null) {
            msg.put("thumbnail_url", thumbnailUrl);
        }
        mes.setMessage(msg);
        try{
            Files.deleteIfExists(Paths.get(videoPath));
        }catch(Exception  e){
            System.out.println("Video didn't get deleted after processing");
            System.out.println(e.getMessage());
        }
        rabbitTemplate.convertAndSend(exchangeName, uploadRoutingKey, mes);
        System.out.println("📤 Sent upload job to video.upload queue");
    }

    @RabbitListener(queues = "${rabbitmq.queue.video.upload.name}")
    public void videoUploadingConsumer(Message mes) {
        String folderPath = mes.getMessage().get("hls_path");
        String userId = mes.getMessage().get("user_id");
        String videoId = mes.getMessage().get("video_id");
        if (folderPath == null || folderPath.isBlank()) {
            System.out.println("⚠️ Missing hls_path in upload message, skipping URL update.");
            return;
        }
        if (videoId == null || videoId.isBlank()) {
            System.out.println("⚠️ Missing video_id in upload message, skipping URL update.");
            return;
        }

        updateVideoStatus(videoId, VideoUploadingStatus.UPLOADING);

        Map<String, String> uploaded;
        VideoLinkStructure videoLinkStructure = new VideoLinkStructure();
        try{

           String cloudFolder = userId != null && !userId.isBlank()
                   ? "users/" + userId + "/videos/" + videoId
                   : "videos/" + videoId;
           uploaded  = cloudinaryUtils.uploadHlsFolder(folderPath, cloudFolder);
        }catch(IOException exc){
            throw new RuntimeException();
        }
        uploaded.put("video_id", videoId);
        if (userId != null) {
            uploaded.put("user_id", userId);
        }
        String thumbnailUrl = mes.getMessage().get("thumbnail_url");
        if (thumbnailUrl != null) {
            uploaded.put("thumbnail_url", thumbnailUrl);
        }
        String masterUrl = uploaded.get("master.m3u8");
        if (masterUrl != null) {
            uploaded.put("master_url", masterUrl);
            videoLinkStructure.setMasterLink(uploaded.get("master_url"));
        }

        Map<String, String> links = new LinkedHashMap<>();
        links.put("auto", masterUrl);
        links.put("1080p",uploaded.getOrDefault("1080/index.m3u8",null));
        links.put("720p",uploaded.getOrDefault("720/index.m3u8",null));
        links.put("480p",uploaded.getOrDefault("480/index.m3u8",null));
        Video videoLink = videoRepository.findByToken(videoId).get();

        videoLink.setVideoLinks(links);

        videoLink.setVideoUploadingStatus(VideoUploadingStatus.UPLOADED);

        videoRepository.save(videoLink);
        mes.setMessage(uploaded);

        try{
            File folder = new File(folderPath);
            var validFiles = Files.walk(folder.toPath())
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".ts") || path.toString().endsWith(".m3u8"))
                    .collect(Collectors.toList());

            var folderPaths = Files.walk(folder.toPath())
                    .filter(Files::isDirectory)
                    .collect(Collectors.toList());
            for(Path file : validFiles){
                Files.deleteIfExists(file);
            }

            for(Path foldr: folderPaths){
                Files.deleteIfExists(foldr);
            }

            Files.deleteIfExists(Paths.get(folderPath));

        }catch(DirectoryNotEmptyException ex){
            System.err.println("Empty Directory can't be deleted");
        }catch(FileNotFoundException e){
            System.out.println("File not found!!!");
            List<StackTraceElement> err = Arrays.stream(e.getStackTrace()).toList();
        }

        catch(Exception e){
            System.err.println("Exception occurred while deleting from local storage. !!!!");
            System.err.println(e.getStackTrace());
        }

        System.out.println(mes.getMessage());

//        rabbitTemplate.convertAndSend(exchangeName, "video.urlupdate.key", mes);
    }



    private void updateVideoStatus(String videoId, VideoUploadingStatus status) {
        if (videoId == null || videoId.isBlank()) {
            return;
        }
        videoRepository.findByToken(videoId).ifPresent(videoLink -> {
            videoLink.setVideoUploadingStatus(status);
            videoRepository.save(videoLink);
        });
    }
}
