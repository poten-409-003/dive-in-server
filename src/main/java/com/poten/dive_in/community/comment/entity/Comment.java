package com.poten.dive_in.community.comment.entity;


import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
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

    @Column(name = "cmnt_order")
    private Integer orderNumber;

    @Column(name = "cmnt_class")
    private Integer cmntClass;// 0: 댓글, 1: 대댓글

    @Column(name = "cntn")
    private String content;

    @Column(name = "like_cnt")
    private Integer likeCount;

    @Column(name = "use_yn")
    private String isActive;

    public void assignContent(String content) {
        this.content = content;
    }
}

