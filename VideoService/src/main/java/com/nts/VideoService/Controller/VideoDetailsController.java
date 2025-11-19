package com.nts.VideoService.Controller;

import com.nts.VideoService.DTO.UpdateVideoToken;
import com.nts.VideoService.DTO.VideoSchema;
import com.nts.VideoService.ResponseBody.APIResponseBody;
import com.nts.VideoService.ResponseBody.APISuccessResponse;
import com.nts.VideoService.Service.VideoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
public class VideoDetailsController {

    @Autowired
    private VideoDetailsService videoDetailsService;

    @PostMapping
    public ResponseEntity<APIResponseBody> createVideo(@RequestBody VideoSchema videoSchema) {
        videoDetailsService.createVideo(videoSchema);
        return ResponseEntity.ok(new APISuccessResponse<>("Video created successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponseBody> updateDetails(@PathVariable String id, @RequestBody VideoSchema videoSchema) {
        videoDetailsService.updateDetails(id, videoSchema);
        return ResponseEntity.ok(new APISuccessResponse<>("Video details updated successfully"));
    }

    @PatchMapping("/token/{id}")
    public ResponseEntity<APIResponseBody> updateVideoToken(@PathVariable String id, @RequestBody UpdateVideoToken updateVideoToken) {
        videoDetailsService.updateVideoToken(id, updateVideoToken);
        return ResponseEntity.ok(new APISuccessResponse<>("Video token updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponseBody> deleteVideo(@PathVariable String id) {
        videoDetailsService.deleteVideo(id);
        return ResponseEntity.ok(new APISuccessResponse<>("Video deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponseBody> getVideo(@PathVariable String id) {
        return ResponseEntity.ok(new APISuccessResponse<>(videoDetailsService.getVideo(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponseBody> getAllVideos() {
        return ResponseEntity.ok(new APISuccessResponse<>(videoDetailsService.getAllVideos()));
    }

    @GetMapping("/health")
    public ResponseEntity<APIResponseBody> healthCheck() {
        APISuccessResponse<String> response = new APISuccessResponse<>("Server running successfully");
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
