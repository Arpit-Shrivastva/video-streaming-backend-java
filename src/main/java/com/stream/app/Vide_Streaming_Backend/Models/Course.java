package com.stream.app.Vide_Streaming_Backend.Models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Course {

    @Id
    private String id;

    private String title;

//    @OneToMany(mappedBy = "course")
//    private List<Video> list = new ArrayList<>();
}
