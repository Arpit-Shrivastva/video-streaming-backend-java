package com.stream.app.Vide_Streaming_Backend.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Video {
    @Id
    private String videoId;
    private String title;
    private String description;
    private String contentType;
    private String filePath;
//    @ManyToOne
//    private Course course;
}
