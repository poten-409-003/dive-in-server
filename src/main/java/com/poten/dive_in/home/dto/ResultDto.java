package com.poten.dive_in.home.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultDto {
    private String keyword;
    private String content;
    private String categoryName;
    private String address;
}
