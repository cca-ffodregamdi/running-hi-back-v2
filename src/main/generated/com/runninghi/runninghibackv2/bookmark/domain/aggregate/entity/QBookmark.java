package com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import com.runninghi.runninghibackv2.domain.aggregate.entity.Bookmark;


/**
 * QBookmark is a Querydsl query type for Bookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookmark extends EntityPathBase<Bookmark> {

    private static final long serialVersionUID = 963322L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookmark bookmark = new QBookmark("bookmark");

    public final com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.QBookmarkId bookmarkId;

    public final com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember member;

    public final com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost post;

    public QBookmark(String variable) {
        this(Bookmark.class, forVariable(variable), INITS);
    }

    public QBookmark(Path<? extends Bookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookmark(PathMetadata metadata, PathInits inits) {
        this(Bookmark.class, metadata, inits);
    }

    public QBookmark(Class<? extends Bookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bookmarkId = inits.isInitialized("bookmarkId") ? new com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.QBookmarkId(forProperty("bookmarkId")) : null;
        this.member = inits.isInitialized("member") ? new com.runninghi.runninghibackv2.member.domain.aggregate.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new com.runninghi.runninghibackv2.post.domain.aggregate.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

