package com.runninghi.runninghibackv2.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Table(name = "TBL_LOCATION")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long locationId;

    @Column
    private String locationName;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point geography;

}
