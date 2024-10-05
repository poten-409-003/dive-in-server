package com.poten.dive_in.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserInfoResponseDto {
    private Long id;

    private String email;

    private Role role;

    private String profileImageUrl;

    private String nickname;

    private String socialType;

    public static UserInfoResponseDto ofEntity(Member member){
        return UserInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .socialType(String.valueOf(member.getSocialType()))
                .role(member.getRole())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
