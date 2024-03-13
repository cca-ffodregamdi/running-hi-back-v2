package com.runninghi.runninghibackv2.post.domain.aggregate.entity;

import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;
import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.vo.GpxDataVO;
import com.runninghi.runninghibackv2.post.domain.service.CalculateGPX;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_POST")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    @Comment("작성자 계정 아이디")
    private Member member;

    @Column
    @Comment("게시글 제목")
    private String postTitle;

    @Column
    @Comment("게시글 내용")
    private String postContent;

    @Column
    @Comment("권한")
    private Role role;

    @Column
    @Comment("지역명")
    private String locationName;

    @Embedded
    private GpxDataVO gpxDataVO;

    private Post(PostBuilder builder) {
        this.member = builder.member;
        this.postTitle = builder.postTitle;
        this.postContent = builder.postContent;
        this.role = builder.role;
        this.locationName = builder.locationName;
        this.gpxDataVO = builder.gpxDataVO;
    }

    public static PostBuilder builder() {
        return new PostBuilder();
    }

    public static class PostBuilder {
        private Member member;
        private String postTitle;
        private String postContent;
        private Role role;
        private String locationName;
        private GpxDataVO gpxDataVO;

        public PostBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public PostBuilder postTitle(String postTitle) {
            this.postTitle = postTitle;
            return this;
        }

        public PostBuilder postContent(String postContent) {
            this.postContent = postContent;
            return this;
        }

        public PostBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public PostBuilder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public PostBuilder gpxDataVO(GpxDataVO gpxDataVO) {
            this.gpxDataVO = gpxDataVO;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

}
