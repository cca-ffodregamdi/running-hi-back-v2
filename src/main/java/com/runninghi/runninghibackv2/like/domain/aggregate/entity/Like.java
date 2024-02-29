package com.runninghi.runninghibackv2.like.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_LIKE")
public class Like {

//@Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_KEY")
//    private Member memberKey;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JointColumn(name = "MEMBER_POST_NO")
//    private MEMBERPOST memberPostNo;

}
