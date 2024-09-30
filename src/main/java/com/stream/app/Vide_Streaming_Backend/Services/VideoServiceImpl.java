package com.stream.app.Vide_Streaming_Backend.Services;

import com.stream.app.Vide_Streaming_Backend.Models.Video;
import com.stream.app.Vide_Streaming_Backend.Repository.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Value("${files.video}")
    String DIR;

    @Value("${file.video.hsl}")
    String HLS_DIR;

    @PostConstruct
    public void init() throws IOException {
        File file = new File(DIR);

        try {
            Files.createDirectories(Paths.get(HLS_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder created");
        } else {
            System.out.println("FOlder already created");
        }
    }


    @Override
    public Video save(Video video, MultipartFile file) {

        //original file name
        try {
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();


            //file path
            String cleanFileName = StringUtils.cleanPath(filename);

            //folder path : create
            String cleanFolder = StringUtils.cleanPath(DIR);

            //folder path with filename
            Path path = Paths.get(cleanFolder, cleanFileName);
            System.out.println(path);

            //copy file to folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            //video metadata
            video.setContentType(contentType);
            video.setFilePath(path.toString());


            videoRepository.save(video);


            //processing video
            processVideo(video.getVideoId());


            //delete actual video file if exception occurs


            //metadata save
            return video;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Video get(String videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @Override
    public String processVideo(String videoId) {

        Video video = this.get(videoId);
        String filePath = video.getFilePath();

        Path videoPath = Paths.get(filePath);

//        String output360p = HLS_DIR + videoId + "/360p/";
//
//        String output720p = HLS_DIR + videoId + "/720p/";
//
//        String output1080p = HLS_DIR + videoId + "/1080p/";

        try {

//            Files.createDirectories(Paths.get(output360p));
//            Files.createDirectories(Paths.get(output720p));
//            Files.createDirectories(Paths.get(output1080p));

            //ffmpeg command
//        StringBuilder ffmpegCmd = new StringBuilder();
//        ffmpegCmd.append("ffmpeg -i")
//                .append(videoPath.toString())
//                .append("")
//                .append("-map 0:v -map 0:a -s:v:0 640*360 -b:v:0 800k ")
//                .append("-map 0:v -map 0:a -s:v:1 1280*720 -b:v:1 2800k ")
//                .append("-map 0:v -map 0:a -s:v:2 1920*1080 -b:v:2 5000k ")
//                .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
//                .append("-master_pl_name ").append(HLS_DIR).append(videoId).append("/master.m3u8 ")
//                .append("-f hls_time 10 -hls_list_size 0 ")
//                .append("-hls_segment_filename \"").append(HLS_DIR).append(videoId)
//                .append("\"").append(HLS_DIR).append(videoId).append("/v%v/prog_index,m3u8\"");

            Path outputPath = Paths.get(HLS_DIR, videoId);

            Files.createDirectories(outputPath);

            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strice -2 -f hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%03d.ts\" \"%s/master.m3u8\" ", videoPath, outputPath, outputPath
            );

            System.out.println(ffmpegCmd);
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0){
                throw new RuntimeException("video processing failed");
            }


        } catch (IOException e) {
            throw new RuntimeException("Video processing failed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
