package com.runninghi.runninghibackv2.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_image")
public class Image extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_no")
    private Long id;

    @Column(name = "image_url", nullable = false)
    @Comment(value = "이미지 URL")
    private String imageUrl;

    @Column
    @Comment(value = "관련 게시글")
    private Long postNo;


    @Builder
    public Image(Long id, String imageUrl, Long postNo) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.postNo = postNo;
    }

    public void updatePostNo(Long postNo) {
        this.postNo = postNo;
    }
}
