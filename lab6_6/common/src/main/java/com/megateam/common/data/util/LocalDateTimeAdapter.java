package com.megateam.common.data.util;

import com.megateam.common.exception.impl.LocalDateTimeParsingException;

import lombok.NonNull;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class provides an ability to marshal and unmarshal LocalDateTime Can be manually added as an
 * external adapter for JAXB
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    /** Specific date time marshalling and unmarshalling date format */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * This method converts LocalDateTime's string representation into a java object
     *
     * @param value string representation of the LocalDateTime
     * @return LocalDateTime instance
     * @throws LocalDateTimeParsingException if the LocalDateTime representation cannot be
     *     unmarshalled successfully
     */
    @Override
    public LocalDateTime unmarshal(@NonNull String value) throws LocalDateTimeParsingException {
        try {
            return LocalDateTime.parse(value, formatter);
        } catch (DateTimeParseException e) {
            throw new LocalDateTimeParsingException(
                    "Something went wrong during unmarshalling the LocalDateTime string representation");
        }
    }

    /**
     * This method converts LocalDateTime object into a string
     *
     * @param value LocalDateTime java object
     * @return string representation of the specified LocalDateTime java object
     * @throws LocalDateTimeParsingException if the LocalDateTime java object cannot ba marshalled
     *     successfully
     */
    @Override
    public String marshal(@NonNull LocalDateTime value) throws LocalDateTimeParsingException {
        try {
            return value.format(formatter);
        } catch (DateTimeException e) {
            throw new LocalDateTimeParsingException(
                    "Something went wrong during marshalling the LocalDateTime object");
        }
    }
}
