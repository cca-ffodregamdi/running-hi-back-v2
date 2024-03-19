package com.runninghi.runninghibackv2.post.domain.service;

import com.runninghi.runninghibackv2.post.domain.aggregate.vo.GpxDataVO;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class CalculateGPX {

    public GpxDataVO getDataFromGpxFile() {

        float startLatitude = getStartLatitude();
        float startLongitude = getStartLongitude();
        float endLatitude = getEndLatitude();
        float endLongitude = getEndLongitude();
        float distance = calculateDistance();
        float time = calculateTime();
        float kcal = calculateKcal();
        float speed = calculateSpeed();
        float meanPace = calculateMeanPace();
        float meanSlope = calculateMeanSlope();

        return new GpxDataVO(startLatitude, startLongitude, endLatitude, endLongitude, distance, time, kcal, speed, meanPace, meanSlope);
    }

    private float getStartLatitude() {

        //추가예정

        return 0.0f;
    }

    private float getStartLongitude() {

        //추가예정

        return 0.0f;
    }

    private float getEndLatitude() {

        //추가예정

        return 0.0f;
    }

    private float getEndLongitude() {

        //추가예정

        return 0.0f;
    }

    private float calculateDistance() {

        //추가예정

        return 0.0f;
    }

    private float calculateTime() {

        //추가예정

        return 0.0f;
    }

    private float calculateKcal() {

        //추가예정

        return 0.0f;
    }

    private float calculateSpeed() {

        //추가예정

        return 0.0f;
    }

    private float calculateMeanPace() {

        //추가예정

        return 0.0f;
    }

    private float calculateMeanSlope() {

        //추가예정

        return 0.0f;
    }

}
