package com.stream.app.Vide_Streaming_Backend.Repository;

import com.stream.app.Vide_Streaming_Backend.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    Optional<Video> findByTitle(String title);
}
