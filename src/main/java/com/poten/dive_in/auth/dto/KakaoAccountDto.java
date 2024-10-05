package com.poten.dive_in.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class KakaoAccountDto {

    private String email;

    @Getter
    @NoArgsConstructor
    public static class Profile {
        private String nickname;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }

    private Profile profile;

}
