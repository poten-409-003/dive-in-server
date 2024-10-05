package com.poten.dive_in.auth.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.auth.enums.Role;
import com.poten.dive_in.auth.enums.SocialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter @Builder @NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Email
    @Column(unique = true)
    private String email;

    private String nickname;

    private String profileImageUrl;


    public void updateMember(String nickname, String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }
}
