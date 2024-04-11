package com.runninghi.runninghibackv2.post.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.runninghi.runninghibackv2.domain.enumtype.Role;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 552419898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.vo.QGpxDataVO gpxDataVO;

    public final StringPath locationName = createString("locationName");

    public final com.runninghi.runninghibackv2.domain.entity.QMember member;

    public final StringPath postContent = createString("postContent");

    public final NumberPath<Long> postNo = createNumber("postNo", Long.class);

    public final StringPath postTitle = createString("postTitle");

    public final NumberPath<Integer> reportCnt = createNumber("reportCnt", Integer.class);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gpxDataVO = inits.isInitialized("gpxDataVO") ? new com.runninghi.runninghibackv2.post.domain.aggregate.vo.QGpxDataVO(forProperty("gpxDataVO")) : null;
        this.member = inits.isInitialized("member") ? new com.runninghi.runninghibackv2.domain.entity.QMember(forProperty("member")) : null;
    }

}

