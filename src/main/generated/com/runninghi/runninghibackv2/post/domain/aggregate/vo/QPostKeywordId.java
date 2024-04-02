package com.runninghi.runninghibackv2.post.domain.aggregate.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostKeywordId is a Querydsl query type for PostKeywordId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPostKeywordId extends BeanPath<PostKeywordId> {

    private static final long serialVersionUID = 840137888L;

    public static final QPostKeywordId postKeywordId = new QPostKeywordId("postKeywordId");

    public final NumberPath<Long> keywordNo = createNumber("keywordNo", Long.class);

    public final NumberPath<Long> postNo = createNumber("postNo", Long.class);

    public QPostKeywordId(String variable) {
        super(PostKeywordId.class, forVariable(variable));
    }

    public QPostKeywordId(Path<? extends PostKeywordId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostKeywordId(PathMetadata metadata) {
        super(PostKeywordId.class, metadata);
    }

}

