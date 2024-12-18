package com.poten.dive_in.auth.entity;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.common.entity.BaseTimeEntity;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberRole role;

    @Email
    @Column(unique = true)
    private String email;

    @JoinColumn(name = "socl_lgn_cd", referencedColumnName = "cd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommonCode socialCode;

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
