package com.runninghi.runninghibackv2.domain.service;

import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GpxCalculator {

    private final List<TrackPoint> trackPoints = new ArrayList<>();
    private float totalDistance = 0.0f;
    private float totalTimeInMinutes = 0.0f;

    static class TrackPoint {
        double lon;
        double lat;
        double ele;
        LocalDateTime time;

        public TrackPoint(double lon, double lat, double ele, LocalDateTime time) {
            this.lon = lon;
            this.lat = lat;
            this.ele = ele;
            this.time = time;
        }
    }

    private void documentBuilder (Resource gpxFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc;

        try (InputStream inputStream = gpxFile.getInputStream()) {
            doc = dBuilder.parse(inputStream);
        }

        doc.getDocumentElement().normalize();

        NodeList trkptList = doc.getElementsByTagName("trkpt");
        int length = trkptList.getLength();

        for (int i = 0; i < length; i++) {
            Element trkptElement = (Element) trkptList.item(i);
            double lon = Double.parseDouble(trkptElement.getAttribute("lon"));
            double lat = Double.parseDouble(trkptElement.getAttribute("lat"));
            double ele = Double.parseDouble(trkptElement.getElementsByTagName("ele").item(0).getTextContent());
            LocalDateTime time = LocalDateTime.parse(trkptElement.getElementsByTagName("time").item(0).getTextContent(),
                    DateTimeFormatter.ISO_DATE_TIME);
            trackPoints.add(new TrackPoint(lon, lat, ele, time));
        }

    }

    public GpxDataVO getDataFromGpxFile(Resource gpxFile) throws ParserConfigurationException, IOException, SAXException {

        documentBuilder(gpxFile);

        float startLatitude = getStartLatitude();
        float startLongitude = getStartLongitude();
        float endLatitude = getEndLatitude();
        float endLongitude = getEndLongitude();
        float distance = calculateDistance();
        float time = calculateTimeInMinutes();
        float kcal = calculateKcal();
        float speed = calculateSpeed();
        float meanPace = calculateMeanPace();
        float meanSlope = calculateMeanSlope();

        return new GpxDataVO(startLatitude, startLongitude, endLatitude, endLongitude, distance, time, kcal, speed, meanPace, meanSlope);
    }

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

            // 하버사인 공식
            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = 6371 * c;

            totalDistance += (float) distance;
        }

        return totalDistance;
    }

    private float calculateTimeInMinutes() {

        LocalDateTime startTime = trackPoints.get(0).time;
        LocalDateTime endTime = trackPoints.get(trackPoints.size() - 1).time;
        Duration duration = Duration.between(startTime, endTime);

        totalTimeInMinutes = (float) duration.getSeconds() / 60;

        return totalTimeInMinutes;
    }

    private float calculateKcal() {
        // 평균 1km 당 50kcal 소모 (키, 몸무게에 따라 변동 필요)
        return totalDistance * 50.0f;
    }

    private float calculateSpeed() {
        // 속도 = 거리 / 시간
        return totalDistance / totalTimeInMinutes;
    }

    private float calculateMeanPace() {
        // 페이스 = 시간 / 거리
        // 1분당 평균 페이스로 반환
        return totalTimeInMinutes / totalDistance;
    }

    private float calculateMeanSlope() {
        double totalElevationChange = 0.0;

        // 경로의 각 위치점에 대해 반복
        for (int i = 0; i < trackPoints.size() - 1; i++) {
            TrackPoint p1 = trackPoints.get(i);
            TrackPoint p2 = trackPoints.get(i + 1);

            // p1에서 p2로의 고도 변화 계산 및 총 고도 변화에 더하기
            totalElevationChange += p2.ele - p1.ele;
        }

        // 경로의 평균 고도 변화 계산
        double meanElevationChange = totalElevationChange / (trackPoints.size() - 1);

        // 경사도가 1% 미만일 시 0 리턴
        if (Math.abs(meanElevationChange) < 1) {
            return 0;
        }

        // 고도 변화를 백분율로 변환하여 반환
        return (float) ((meanElevationChange / trackPoints.size()) * 100.0);
    }

}
