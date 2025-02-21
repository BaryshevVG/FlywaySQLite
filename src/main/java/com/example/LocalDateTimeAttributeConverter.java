package com.example;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (localDateTime != null) ? localDateTime.format(FORMATTER) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null || databaseValue.isEmpty()) {
            return null;
        }
        if (databaseValue.length() == 10) { // Если дата без времени
            databaseValue += " 00:00:00.000"; // Добавляем время по умолчанию
        }
        return LocalDateTime.parse(databaseValue, FORMATTER);
    }
}
