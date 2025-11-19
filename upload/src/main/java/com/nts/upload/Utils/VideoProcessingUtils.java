package com.nts.upload.Utils;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public final class VideoProcessingUtils {

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public VideoProcessingUtils() throws IOException {
        this.ffmpeg = new FFmpeg("ffmpeg");
        this.ffprobe = new FFprobe("ffprobe");
    }

    private int[] getResolution(String videoPath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(videoPath);
        int width = probeResult.getStreams().get(0).width;
        int height = probeResult.getStreams().get(0).height;
        return new int[]{width, height};
    }

    private void transcodeVariant(String inputPath, int height, int width, int bitrateKbps, String outputDir, String variantName) {
        try {
            String variantDir = outputDir + "/" + variantName;
            new File(variantDir).mkdirs();

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputPath)
                    .overrideOutputFiles(true)
                    .addOutput(variantDir + "/index.m3u8")
                    .addExtraArgs(
                            "-profile:v", "baseline",
                            "-level", "3.0",
                            "-start_number", "0",
                            "-hls_time", "2",
                            "-hls_list_size", "0",
                            "-hls_segment_filename", variantDir + "/segment%d.ts",
                            "-f", "hls"
                    )
                    .setVideoCodec("libx264")
                    .setVideoBitRate(bitrateKbps * 1000)
                    .setVideoResolution(width, height)
                    .setAudioCodec("aac")
                    .setAudioBitRate(128_000)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            System.out.println("✅ Transcoded " + variantName + "p variant successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateMasterPlaylist(String outputDir, List<String> variants) throws IOException {
        File masterFile = new File(outputDir, "master.m3u8");
        try (FileWriter writer = new FileWriter(masterFile)) {
            writer.write("#EXTM3U\n");
            for (String v : variants) {
                int height = Integer.parseInt(v);
                int bandwidth = switch (height) {
                    case 1080 -> 5000000;
                    case 720 -> 2800000;
                    case 480 -> 1200000;
                    default -> 800000;
                };
                writer.write("#EXT-X-STREAM-INF:BANDWIDTH=" + bandwidth + ",RESOLUTION=" + getResolutionString(height) + "\n");
                writer.write(v + "/index.m3u8\n");
            }
        }
        System.out.println("🎯 Master playlist created: " + masterFile.getAbsolutePath());
    }

    private String getResolutionString(int height) {
        return switch (height) {
            case 1080 -> "1920x1080";
            case 720 -> "1280x720";
            case 480 -> "854x480";
            default -> "640x360";
        };
    }

    public Map<String, String> transcodeVideo(String inputPath) {
        try {
            int[] res = getResolution(inputPath);
            int width = res[0];
            int height = res[1];

            String outputDir = inputPath.replace(".mp4", "_hls");
            new File(outputDir).mkdirs();

            List<String> generated = new ArrayList<>();

            if (height >= 1080) {
                transcodeVariant(inputPath, 1080, 1920, 5000, outputDir, "1080");
                transcodeVariant(inputPath, 720, 1280, 2800, outputDir, "720");
                transcodeVariant(inputPath, 480, 854, 1200, outputDir, "480");
                generated.add("1080");
                generated.add("720");
                generated.add("480");
            } else if (height >= 720) {
                transcodeVariant(inputPath, 720, 1280, 2800, outputDir, "720");
                transcodeVariant(inputPath, 480, 854, 1200, outputDir, "480");
                generated.add("720");
                generated.add("480");
            } else {
                transcodeVariant(inputPath, 480, 854, 1200, outputDir, "480");
                generated.add("480");
            }

            generateMasterPlaylist(outputDir, generated);
            System.out.println("✅ All variants created successfully in: " + outputDir);


            Map<String, String> msg = new HashMap<>();
            msg.put("outputDir", outputDir);
            msg.put("videoPath", inputPath);

            return msg;

            // TODO:: Video Upload service should be called here
            // IMPORTANT:: Can't store in local dir
//            new Thread(() ->{
//                try{
//
//                }catch (Exception ex){
//                    throw new RuntimeException(ex);
//                }
//            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
