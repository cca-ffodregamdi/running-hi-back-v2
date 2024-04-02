package com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentReport is a Querydsl query type for CommentReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentReport extends EntityPathBase<CommentReport> {

    private static final long serialVersionUID = -729692533L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentReport commentReport = new QCommentReport("commentReport");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ReportCategory> category = createEnum("category", com.runninghi.runninghibackv2.common.enumtype.ReportCategory.class);

    public final NumberPath<Long> commentReportNo = createNumber("commentReportNo", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final BooleanPath reportedCommentDeleted = createBoolean("reportedCommentDeleted");

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember reportedMember;

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember reporter;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QCommentReport(String variable) {
        this(CommentReport.class, forVariable(variable), INITS);
    }

    public QCommentReport(Path<? extends CommentReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentReport(PathMetadata metadata, PathInits inits) {
        this(CommentReport.class, metadata, inits);
    }

    public QCommentReport(Class<? extends CommentReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedMember = inits.isInitialized("reportedMember") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("reportedMember")) : null;
        this.reporter = inits.isInitialized("reporter") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("reporter")) : null;
    }

}

