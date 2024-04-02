package com.runninghi.runninghibackv2.postreport.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostReport is a Querydsl query type for PostReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostReport extends EntityPathBase<PostReport> {

    private static final long serialVersionUID = -1825649958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostReport postReport = new QPostReport("postReport");

    public final com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity _super = new com.runninghi.runninghibackv2.common.entity.QBaseTimeEntity(this);

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ReportCategory> category = createEnum("category", com.runninghi.runninghibackv2.common.enumtype.ReportCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> postReportNo = createNumber("postReportNo", Long.class);

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember reportedMember;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost reportedPost;

    public final BooleanPath reportedPostDeleted = createBoolean("reportedPostDeleted");

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember reporter;

    public final EnumPath<com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus> status = createEnum("status", com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QPostReport(String variable) {
        this(PostReport.class, forVariable(variable), INITS);
    }

    public QPostReport(Path<? extends PostReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostReport(PathMetadata metadata, PathInits inits) {
        this(PostReport.class, metadata, inits);
    }

    public QPostReport(Class<? extends PostReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedMember = inits.isInitialized("reportedMember") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("reportedMember")) : null;
        this.reportedPost = inits.isInitialized("reportedPost") ? new com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost(forProperty("reportedPost"), inits.get("reportedPost")) : null;
        this.reporter = inits.isInitialized("reporter") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("reporter")) : null;
    }

}

