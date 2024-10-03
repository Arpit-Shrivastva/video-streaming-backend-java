package com.stream.app.Vide_Streaming_Backend.Payload;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomMessage {
    private String message;
    private boolean success = false;
}
