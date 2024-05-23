package com.runninghi.runninghibackv2.domain.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class GpxCoordinateExtractor {

    public List<double[]> extractCoordinates(InputStream inputStream) throws IOException {
        List<double[]> lonLatList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(new String(inputStream.readAllBytes()));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            double lon = jsonObject.getDouble("lon");
            double lat = jsonObject.getDouble("lat");

            double[] coordinate = {lon, lat};
            lonLatList.add(coordinate);
        }

        return lonLatList;
    }
}
