package com.runninghi.runninghibackv2.domain.service;

import com.runninghi.runninghibackv2.application.dto.post.response.GpxDataResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class GpxCoordinateExtractor {

    public List<double[]> extractCoordinates(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        NodeList trackPoints = document.getElementsByTagName("trkpt");

        List<double[]> lonLatList = new ArrayList<>();

        for (int i = 0; i < trackPoints.getLength(); i++) {
            Element trkpt = (Element) trackPoints.item(i);
            double lon = Double.parseDouble(trkpt.getAttribute("lon"));
            double lat = Double.parseDouble(trkpt.getAttribute("lat"));

            double[] coordinate = {lon, lat};
            lonLatList.add(coordinate);
        }

        return lonLatList;
    }
}
