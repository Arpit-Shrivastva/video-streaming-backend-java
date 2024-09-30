package com.stream.app.Vide_Streaming_Backend.Services;

import com.stream.app.Vide_Streaming_Backend.Models.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    Video save(Video video, MultipartFile file);

    Video get(String videoId);

    Video getByTitle(String title);

    List<Video> getAll();

    String processVideo(String videoId);
}
