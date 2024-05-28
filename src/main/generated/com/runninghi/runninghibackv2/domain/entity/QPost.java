package com.runninghi.runninghibackv2.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 2063752679L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final EnumPath<com.runninghi.runninghibackv2.domain.enumtype.Difficulty> difficulty = createEnum("difficulty", com.runninghi.runninghibackv2.domain.enumtype.Difficulty.class);

    public final com.runninghi.runninghibackv2.domain.entity.vo.QGpsDataVO gpsDataVO;

    public final StringPath gpxUrl = createString("gpxUrl");

    public final ListPath<Keyword, QKeyword> keywordList = this.<Keyword, QKeyword>createList("keywordList", Keyword.class, QKeyword.class, PathInits.DIRECT2);

    public final StringPath locationName = createString("locationName");

    public final QMember member;

    public final StringPath postContent = createString("postContent");

    public final NumberPath<Long> postNo = createNumber("postNo", Long.class);

    public final NumberPath<Integer> reportCnt = createNumber("reportCnt", Integer.class);

    public final EnumPath<com.runninghi.runninghibackv2.domain.enumtype.Role> role = createEnum("role", com.runninghi.runninghibackv2.domain.enumtype.Role.class);

    public final BooleanPath status = createBoolean("status");

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
        this.gpsDataVO = inits.isInitialized("gpsDataVO") ? new com.runninghi.runninghibackv2.domain.entity.vo.QGpsDataVO(forProperty("gpsDataVO")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

