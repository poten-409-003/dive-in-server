package com.poten.dive_in.community.post.entity;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.community.comment.entity.Comment;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@NamedEntityGraph(
        name = "Post.detail",
        attributeNodes = {
                @NamedAttributeNode("member"),
                @NamedAttributeNode("categoryCode"),
                @NamedAttributeNode("comments"),
                @NamedAttributeNode("images"),
                @NamedAttributeNode("likes")
        }
)
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "ttl")
    private String title;

    @JoinColumn(name = "post_ctgr_cd", referencedColumnName = "cd", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CommonCode categoryCode;

    @Column(name = "cntn")
    private String content;

    @Column(name = "cmnt_cnt")
    private Integer commentCount;

    @Column(name = "like_cnt")
    private Integer likeCount;

    @Column(name = "chc_cnt")
    private Integer viewCount;

    @Column(name = "use_yn")
    private String isActive;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes;

    public void adjustLikeCount(int increment) {
        this.likeCount += increment;
    }
}
