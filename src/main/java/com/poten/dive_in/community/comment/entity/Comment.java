package com.poten.dive_in.community.comment.entity;


import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cmnt")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmnt_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "cmnt_seq")
    private Integer commentSequence;

    @Column(name = "group_nm")
    private Integer groupName;

    @Column(name = "order")
    private Integer orderNumber;

    @Column(name = "class")
    private Integer className;

    @Column(name = "cntn")
    private String content;

    @Column(name = "like_cnt")
    private Integer likeCount;

    @Column(name = "use_yn")
    private String isActive;
}

