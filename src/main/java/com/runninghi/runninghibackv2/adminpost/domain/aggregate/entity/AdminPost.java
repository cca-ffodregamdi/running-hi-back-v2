package com.runninghi.runninghibackv2.adminpost.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_ADMINPOST")
public class AdminPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminPostNo;

    @Column
    private String adminPostTitle;

    @Column
    private String adminPostContent;


}
