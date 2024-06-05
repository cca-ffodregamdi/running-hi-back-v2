package com.runninghi.runninghibackv2.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class FloatListConverter implements AttributeConverter<List<Float>, String> {
    @Override
    public String convertToDatabaseColumn(List<Float> floats) {
        if (floats == null || floats.isEmpty()) {
            return null;
        }
        return floats.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    @Override
    public List<Float> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(this::parseFloat)
                .collect(Collectors.toList());
    }

    private Float parseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
