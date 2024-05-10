package com.runninghi.runninghibackv2.domain.entity.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGpxDataVO is a Querydsl query type for GpxDataVO
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGpxDataVO extends BeanPath<GpxDataVO> {

    private static final long serialVersionUID = -207151484L;

    public static final QGpxDataVO gpxDataVO = new QGpxDataVO("gpxDataVO");

    public final NumberPath<Float> distance = createNumber("distance", Float.class);

    public final NumberPath<Float> endLatitude = createNumber("endLatitude", Float.class);

    public final NumberPath<Float> endLongitude = createNumber("endLongitude", Float.class);

    public final NumberPath<Float> kcal = createNumber("kcal", Float.class);

    public final NumberPath<Float> meanPace = createNumber("meanPace", Float.class);

    public final NumberPath<Float> speed = createNumber("speed", Float.class);

    public final NumberPath<Float> startLatitude = createNumber("startLatitude", Float.class);

    public final NumberPath<Float> startLongitude = createNumber("startLongitude", Float.class);

    public final NumberPath<Float> time = createNumber("time", Float.class);

    public QGpxDataVO(String variable) {
        super(GpxDataVO.class, forVariable(variable));
    }

    public QGpxDataVO(Path<? extends GpxDataVO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGpxDataVO(PathMetadata metadata) {
        super(GpxDataVO.class, metadata);
    }

}

