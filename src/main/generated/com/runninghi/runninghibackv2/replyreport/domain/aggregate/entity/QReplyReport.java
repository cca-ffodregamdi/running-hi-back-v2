package com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyReport is a Querydsl query type for ReplyReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyReport extends EntityPathBase<ReplyReport> {

    private static final long serialVersionUID = -1311195946L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyReport replyReport = new QReplyReport("replyReport");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ReportCategory> category = createEnum("category", com.runninghi.runninghibackv2.common.enumtype.ReportCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final BooleanPath isReplyDeleted = createBoolean("isReplyDeleted");

    public final StringPath replyContent = createString("replyContent");

    public final NumberPath<Long> replyReportNo = createNumber("replyReportNo", Long.class);

    public final com.runninghi.runninghibackv2.reply.domain.aggregate.entity.QReply reportedReply;

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember reporter;

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus> status = createEnum("status", com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QReplyReport(String variable) {
        this(ReplyReport.class, forVariable(variable), INITS);
    }

    public QReplyReport(Path<? extends ReplyReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyReport(PathMetadata metadata, PathInits inits) {
        this(ReplyReport.class, metadata, inits);
    }

    public QReplyReport(Class<? extends ReplyReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedReply = inits.isInitialized("reportedReply") ? new com.runninghi.runninghibackv2.reply.domain.aggregate.entity.QReply(forProperty("reportedReply"), inits.get("reportedReply")) : null;
        this.reporter = inits.isInitialized("reporter") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("reporter")) : null;
    }

}

