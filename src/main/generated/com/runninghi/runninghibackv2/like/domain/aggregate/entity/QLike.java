package com.runninghi.runninghibackv2.like.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLike is a Querydsl query type for Like
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLike extends EntityPathBase<Like> {

    private static final long serialVersionUID = 1191614874L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLike like = new QLike("like1");

    public final com.runninghi.runninghibackv2.like.domain.aggregate.vo.QLikeId likeId;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost post;

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember writer;

    public QLike(String variable) {
        this(Like.class, forVariable(variable), INITS);
    }

    public QLike(Path<? extends Like> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLike(PathMetadata metadata, PathInits inits) {
        this(Like.class, metadata, inits);
    }

    public QLike(Class<? extends Like> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.likeId = inits.isInitialized("likeId") ? new com.runninghi.runninghibackv2.like.domain.aggregate.vo.QLikeId(forProperty("likeId")) : null;
        this.post = inits.isInitialized("post") ? new com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.writer = inits.isInitialized("writer") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("writer")) : null;
    }

}

