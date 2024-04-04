package com.runninghi.runninghibackv2.post.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostKeyword is a Querydsl query type for PostKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostKeyword extends EntityPathBase<PostKeyword> {

    private static final long serialVersionUID = 867589711L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostKeyword postKeyword = new QPostKeyword("postKeyword");

    public final com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.QKeyword keyword;

    public final QPost post;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.vo.QPostKeywordId postKeywordId;

    public QPostKeyword(String variable) {
        this(PostKeyword.class, forVariable(variable), INITS);
    }

    public QPostKeyword(Path<? extends PostKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostKeyword(PathMetadata metadata, PathInits inits) {
        this(PostKeyword.class, metadata, inits);
    }

    public QPostKeyword(Class<? extends PostKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.keyword = inits.isInitialized("keyword") ? new com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.QKeyword(forProperty("keyword")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.postKeywordId = inits.isInitialized("postKeywordId") ? new com.runninghi.runninghibackv2.post.domain.aggregate.vo.QPostKeywordId(forProperty("postKeywordId")) : null;
    }

}

