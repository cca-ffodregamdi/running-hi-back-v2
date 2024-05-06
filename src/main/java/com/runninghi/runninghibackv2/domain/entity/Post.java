package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.application.dto.post.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_POST")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("작성자 계정 아이디")
    private Member member;

    @Column
    @Nullable
    @Comment("게시글 제목")
    private String postTitle;

    @Column
    @Nullable
    @Comment("게시글 내용")
    private String postContent;

    @Column
    @Comment("신고 횟수")
    private int reportCnt;

    @Column
    @Comment("권한")
    private Role role;

    @Column
    @Comment("지역명")
    private String locationName;

    @Column
    @Comment("게시글 공유 여부")
    private Boolean status;

    @Embedded
    private GpxDataVO gpxDataVO;

    @Column
    @Comment("gpx 파일 url")
    private String gpxUrl;

    @OneToMany(mappedBy = "keywordNo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Keyword> keywordList;

    @Builder
    public Post(Member member, @Nullable String postTitle, @Nullable String postContent, Role role, String locationName, Boolean status, String gpxUrl, GpxDataVO gpxDataVO) {
        this.member = member;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.reportCnt = 0;
        this.role = role;
        this.locationName = locationName;
        this.status = status;
        this.gpxUrl = gpxUrl;
        this.gpxDataVO = gpxDataVO;
    }

    public void update(UpdatePostRequest request) {
        this.postTitle = request.postTitle();
        this.postContent = request.postContent();
    }

    public void addReportedCount() {
        this.reportCnt += 1;
    }

    public void resetReportedCount() {
        this.reportCnt = 0;
    }
}
