package com.poten.dive_in.community.post.entity;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.pool.entity.PoolImage;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer commentCount=0;

    @Column(name = "like_cnt")
    private Integer likeCount=0;

    @Column(name = "chc_cnt")
    private Integer viewCount=0;

    @Column(name = "use_yn")
    private String isActive="Y";

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostImage> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes;

    public void adjustViewCount() {
        this.viewCount += 1;
    }
    public void adjustLikeCount(int increment) {
        this.likeCount += increment;
    }
    public void addImage(Set<PostImage> poolImageList) {
        this.images = poolImageList;
    }

    public void replaceImageList(Set<PostImage> newPoolImageList){
        if (this.images != null) {
            this.images.clear();
            this.images.addAll(newPoolImageList);
        } else{
            this.images = new HashSet<>(newPoolImageList);
        }
    }
}
