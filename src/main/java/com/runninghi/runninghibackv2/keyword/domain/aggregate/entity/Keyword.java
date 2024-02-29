package com.runninghi.runninghibackv2.keyword.domain.aggregate.entity;

import com.runninghi.runninghibackv2.adminpost.domain.aggregate.entity.AdminPost;
import com.runninghi.runninghibackv2.memberpost.domain.aggregate.entity.MemberPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_KEYWORD")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordNo;

    @Column
    private String keywordName;

    @ManyToOne
    @JoinColumn(name = "memberPostNo")
    private MemberPost memberPost;

    @ManyToOne
    @JoinColumn(name = "adminPostNo")
    private AdminPost adminPost;

    public Keyword(String keywordName, MemberPost memberPost, AdminPost adminPost) {
        this.keywordName = keywordName;
        this.memberPost = memberPost;
        this.adminPost = adminPost;
    }

    public static class Builder {
        private String keywordName;
        private MemberPost memberPost;
        private AdminPost adminPost;

        public Builder keywordName(String keywordName) {
            this.keywordName = keywordName;
            return this;
        }

        public Builder memberPost(MemberPost memberPost) {
            this.memberPost = memberPost;
            return this;
        }

        public Builder adminPost(AdminPost adminPost) {
            this.adminPost = adminPost;
            return this;
        }

        public Keyword build() {
            return new Keyword(keywordName, memberPost, adminPost);
        }
    }
}
