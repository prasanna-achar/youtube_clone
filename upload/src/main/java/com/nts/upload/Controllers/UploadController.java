package com.nts.upload.Controllers;

import com.nts.upload.ApiResponseBody.ResponseBody;
import com.nts.upload.DTO.RequestBody.UploadVideoSchema;
import com.nts.upload.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping("/video")
@RestController
public class UploadController {

    @Autowired
    UploadService uploadService;



//    @PostMapping("/image")
//    public ResponseEntity<ResponseBody> uploadImage(@RequestHeader("X-User-Id") String userId, @RequestBody MultipartFile file){return null;}

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseBody> uploadVideo(@RequestHeader("X-User-Id") String userId,@ModelAttribute UploadVideoSchema uploadVideoSchema) throws IOException {
        ResponseBody responseBody = uploadService.processVideo(userId, uploadVideoSchema);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }


//    @PostMapping("/file")
//    public ResponseEntity<ResponseBody> uploadFile(@RequestHeader("X-User-Id") String userId, @RequestBody MultipartFile file){return null;}


    @DeleteMapping("/delete-by-token/{token}")
    public ResponseEntity<ResponseBody> deleteVideoByToken(@PathVariable String token){
        ResponseBody responseBody = uploadService.getVideoByToken(token);

        return  new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseBody> deleteVideoById(@RequestHeader("X-User-Id") String userId,@PathVariable String id){
        ResponseBody responseBody = uploadService.deleteById(userId, id);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());

    }

    @GetMapping("/{Id}")
    public ResponseEntity<ResponseBody> getVideoById(@PathVariable String Id){
        ResponseBody responseBody = uploadService.getVideoById(Id);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }
    @GetMapping("/get-by-token/{token}")
    public ResponseEntity<ResponseBody> getVideoByToken(@PathVariable String token){
        ResponseBody responseBody = uploadService.getVideoByToken(token);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @GetMapping
    public ResponseEntity<ResponseBody> getALlVideos(){
        ResponseBody responseBody = uploadService.getAllVideos();
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());

    }
}
