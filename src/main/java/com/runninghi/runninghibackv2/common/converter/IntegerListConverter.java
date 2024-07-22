package com.runninghi.runninghibackv2.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        if (integers == null || integers.isEmpty()) {
            return null;
        }
        return integers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return Collections.emptyList();
        }
        return Stream.of(s.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
