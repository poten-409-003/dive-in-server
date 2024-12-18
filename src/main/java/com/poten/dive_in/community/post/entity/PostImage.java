package com.poten.dive_in.community.post.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "imageUrl")
@Entity
@Table(name = "post_img")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "rprs_img_yn")
    private String isRepresentative;

    @Column(name = "img_url")
    private String imageUrl;
}
