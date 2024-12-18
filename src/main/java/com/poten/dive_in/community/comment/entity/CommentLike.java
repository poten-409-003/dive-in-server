package com.poten.dive_in.community.comment.entity;


import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.common.entity.CreateTimeEntity;
import com.poten.dive_in.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cmnt_like")
public class CommentLike extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cmnt_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmnt_id", nullable = false)
    private Comment comment;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}

