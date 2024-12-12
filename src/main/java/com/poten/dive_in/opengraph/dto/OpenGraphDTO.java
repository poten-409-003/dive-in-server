package com.poten.dive_in.opengraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenGraphDTO {
    private String title;
    private String description;
    private String image;
    private String url;
    private String error; // 오류 메시지를 담기 위한 필드
}

