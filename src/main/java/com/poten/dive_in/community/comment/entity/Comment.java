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
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "cmnt")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cmnt_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "cmnt_seq")
    private Integer commentSequence = 0;

    @Column(name = "group_nm")
    private Integer groupName; //댓글의Id를 groupName으로

    @Column(name = "cmnt_order")
    private Integer orderNumber; //댓글 순서 그룹별로 0부터 순차적으로

    @Column(name = "cmnt_class")
    private Integer cmntClass;// 0: 댓글, 1: 대댓글

    @Column(name = "cntn")
    private String content;

    @Column(name = "like_cnt")
    private Integer likeCount=0;

    @Column(name = "use_yn")
    private String isActive="Y";

    public void assignContent(String content) {
        this.content = content;
    }
    public void assignGroupName(Integer groupName) {
        this.groupName = groupName;
    }

    public void assignPost(Post post) { this.post = post;}
    public void adjustLikeCount(int increment) {
        this.likeCount += increment;
    }

}

