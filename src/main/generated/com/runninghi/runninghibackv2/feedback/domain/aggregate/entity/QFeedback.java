package com.runninghi.runninghibackv2.feedback.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedback is a Querydsl query type for Feedback
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedback extends EntityPathBase<Feedback> {

    private static final long serialVersionUID = -432211750L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedback feedback = new QFeedback("feedback");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    public final EnumPath<FeedbackCategory> category = createEnum("category", FeedbackCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> feedbackNo = createNumber("feedbackNo", Long.class);

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember feedbackWriter;

    public final BooleanPath hasReply = createBoolean("hasReply");

    public final StringPath reply = createString("reply");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QFeedback(String variable) {
        this(Feedback.class, forVariable(variable), INITS);
    }

    public QFeedback(Path<? extends Feedback> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedback(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedback(PathMetadata metadata, PathInits inits) {
        this(Feedback.class, metadata, inits);
    }

    public QFeedback(Class<? extends Feedback> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feedbackWriter = inits.isInitialized("feedbackWriter") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("feedbackWriter")) : null;
    }

}

