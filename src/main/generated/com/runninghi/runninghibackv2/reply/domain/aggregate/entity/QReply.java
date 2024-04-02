package com.runninghi.runninghibackv2.reply.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = -1080945202L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    public final ListPath<Reply, QReply> children = this.<Reply, QReply>createList("children", Reply.class, QReply.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QReply parent;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost post;

    public final StringPath replyContent = createString("replyContent");

    public final NumberPath<Long> replyNo = createNumber("replyNo", Long.class);

    public final NumberPath<Integer> reportedCount = createNumber("reportedCount", Integer.class);

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus> reportStatus = createEnum("reportStatus", com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember writer;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QReply(forProperty("parent"), inits.get("parent")) : null;
        this.post = inits.isInitialized("post") ? new com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.writer = inits.isInitialized("writer") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("writer")) : null;
    }

}

