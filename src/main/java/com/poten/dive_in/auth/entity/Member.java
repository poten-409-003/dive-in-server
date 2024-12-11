package com.poten.dive_in.auth.entity;

import com.poten.dive_in.cmmncode.entity.CmmnCd;
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



    @JoinColumn(name = "role_id")
    @Column(name = "role_id")
    @OneToOne(fetch = FetchType.LAZY)
    private MemberRole role;

    @Email
    @Column(unique = true)
    private String email;

    @JoinColumn(name = "cd_id")
    @Column(name = "socl_lgn_cd")
    @OneToOne(fetch = FetchType.LAZY)
    private CmmnCd socialCode;

    @Column(name = "nckn")
    private String nickname;

    @Column(name = "prfl_img_url")
    private String profileImageUrl;

    private Character useYn;

//    @OneToMany(mappedBy = "member")
//    private List<Post> posts;
//
//    @OneToMany(mappedBy = "member")
//    private List<Cmnt> cmnts;
//
//    @OneToMany(mappedBy = "member")
//    private List<CmntLike> cmntLikes;
//
//    @OneToMany(mappedBy = "member")
//    private List<PostLike> postLikes;

    public void updateMember(String nickname, String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
    }

}
