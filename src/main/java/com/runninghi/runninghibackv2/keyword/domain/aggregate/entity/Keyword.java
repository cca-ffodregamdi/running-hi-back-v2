package com.runninghi.runninghibackv2.keyword.domain.aggregate.entity;

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

    public Keyword(String keywordName) {
        this.keywordName = keywordName;
    }

}
