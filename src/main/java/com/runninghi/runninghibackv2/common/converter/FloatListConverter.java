package com.runninghi.runninghibackv2.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            return Collections.emptyList();
        }
        return Stream.of(dbData.split(","))
                .map(String::trim)
                .map(Float::parseFloat)
                .collect(Collectors.toList());
    }
}