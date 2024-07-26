package com.runninghi.runninghibackv2.domain.service;

import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
public class GpsCalculator {

    private static class TrackPoint {
        double lon;
        double lat;
        LocalDateTime time;

        public TrackPoint(double lon, double lat, LocalDateTime time) {
            this.lon = lon;
            this.lat = lat;
            this.time = time;
        }
    }

    private List<TrackPoint> trackPoints = new ArrayList<>();
    private float totalDistance = 0.0f;
    private int totalTimeInSeconds = 0;
    private static final double R = 6371;

    private void processJson(String gpsData) {
        JSONArray trkptList = new JSONArray(gpsData);

        for (int i = 0; i < trkptList.length(); i++) {
            JSONObject trkptElement = trkptList.getJSONObject(i);
            double lon = trkptElement.getDouble("lon");
            double lat = trkptElement.getDouble("lat");
            LocalDateTime time = LocalDateTime.parse(trkptElement.getString("time"), DateTimeFormatter.ISO_DATE_TIME);
            trackPoints.add(new TrackPoint(lon, lat, time));
        }
    }
//    public GpsDataVO getDataFromGpxFile(String gpxData) {
//
//        trackPoints.clear(); // trackPoints 리스트 초기화
//        totalDistance = 0.0f; // totalDistance 초기화
//        totalTimeInSeconds = 0; // totalTimeInMinutes 초기화
//
//        processJson(gpxData);
//
//        float distance = calculateDistance();
//        int time = calculateTimeInSeconds();
//        int kcal = calculateKcal();
//        int meanPace = calculateMeanPace();
//
//        return new GpsDataVO(distance, time, kcal, meanPace);
//    }

    private float getStartLatitude() {
        return (float) trackPoints.get(0).lat;
    }

    private float getStartLongitude() {
        return (float) trackPoints.get(0).lon;
    }

    private float getEndLatitude() {
        return (float) trackPoints.get(trackPoints.size() - 1).lat;
    }

    private float getEndLongitude() {
        return (float) trackPoints.get(trackPoints.size() - 1).lon;
    }


    private float calculateDistance() {

        for (int i = 0; i < trackPoints.size() - 1; i++) {
            TrackPoint p1 = trackPoints.get(i);
            TrackPoint p2 = trackPoints.get(i + 1);

            double lat1 = Math.toRadians(p1.lat);
            double lon1 = Math.toRadians(p1.lon);
            double lat2 = Math.toRadians(p2.lat);
            double lon2 = Math.toRadians(p2.lon);

            double x1 = R * Math.cos(lat1) * Math.cos(lon1);
            double y1 = R * Math.cos(lat1) * Math.sin(lon1);
            double x2 = R * Math.cos(lat2) * Math.cos(lon2);
            double y2 = R * Math.cos(lat2) * Math.sin(lon2);

            // 유클리드 거리 계산
            double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

            totalDistance += (float) distance;
        }

        return totalDistance;
    }


    private int calculateTimeInSeconds() {

        LocalDateTime startTime = trackPoints.get(0).time;
        LocalDateTime endTime = trackPoints.get(trackPoints.size() - 1).time;
        Duration duration = Duration.between(startTime, endTime);

        totalTimeInSeconds = (int) duration.getSeconds();

        return totalTimeInSeconds;
    }

    private int calculateKcal() {
        // 평균 1km 당 50kcal 소모 (키, 몸무게에 따라 변동 필요)
        return (int) (totalDistance * 50.0f);
    }

    private float calculateSpeed() {
        // 속도 = 거리 / 시간
        return totalDistance / totalTimeInSeconds / 60;
    }

    private int calculateMeanPace() {
        // 페이스 = 시간 / 거리
        // 1분당 평균 페이스로 반환
        return (int) (totalTimeInSeconds  / totalDistance);
    }

//    private float calculateMeanSlope() {
//        double totalElevationChange = 0.0;
//
//        // 경로의 각 위치점에 대해 반복
//        for (int i = 0; i < trackPoints.size() - 1; i++) {
//            TrackPoint p1 = trackPoints.get(i);
//            TrackPoint p2 = trackPoints.get(i + 1);
//
//            // p1에서 p2로의 고도 변화 계산 및 총 고도 변화에 더하기
//            totalElevationChange += p2.ele - p1.ele;
//        }
//
//        // 경로의 평균 고도 변화 계산
//        double meanElevationChange = totalElevationChange / (trackPoints.size() - 1);
//
//        // 경사도가 1% 미만일 시 0 리턴
//        if (Math.abs(meanElevationChange) < 1) {
//            return 0;
//        }
//
//        // 고도 변화를 백분율로 변환하여 반환
//        return (float) ((meanElevationChange / trackPoints.size()) * 100.0);
//    }

}
