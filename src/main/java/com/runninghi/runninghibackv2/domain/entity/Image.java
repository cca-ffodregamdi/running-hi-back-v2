package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.application.dto.image.response.ImageTarget;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_image")
public class Image{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_no")
    private Long id;

    @Column(name = "image_url", nullable = false)
    @Comment(value = "이미지 URL")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_target")
    @Comment(value = "관련 엔티티")
    private ImageTarget imageTarget;

    @Column(name = "target_id")
    @Comment(value = "관련 엔티티 식별 값")
    private Long targetNo;

    @Column(name = "create_date", updatable = false)
    @Comment(value = "이미지 생성일")
    private LocalDateTime createDate;


    @Builder
    public Image(Long id, String imageUrl, Long targetNo) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.targetNo = targetNo;
        this.createDate = LocalDateTime.now();
    }

    public void updateTargetNo(Long targetNo) {
        this.targetNo = targetNo;
    }

    public void updateImageTarget(ImageTarget imageTarget) {
        this.imageTarget = imageTarget;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
