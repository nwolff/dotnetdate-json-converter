package com.github.nwolff.gson_dotnetdate_adapter;

import com.github.nwolff.dotnetdate_json_converter.ParseException;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.github.nwolff.dotnetdate_json_converter.DotNetDateJsonConverter.fromJson;
import static com.github.nwolff.dotnetdate_json_converter.DotNetDateJsonConverter.toJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DotNetDateJsonConverterTest {

    @Test
    public void UTC() throws ParseException {
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(10, 10, 10), ZoneOffset.UTC);
        String s = "/Date(1420107010000+0000)/";
        assertEquals(s, toJson(d));
        assertEquals(d, fromJson(s));
    }

    @Test
    // We received events with timestamps that were before the opening time of the system, so we knew there was a problem.
    public void exampleFromProdution() throws ParseException {
        String s = "/Date(1512889962000+0100)/";
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(2017, 12, 10), LocalTime.of(8, 12, 42), ZoneOffset.ofHoursMinutes(1, 00));
        assertEquals(d, fromJson(s));
        assertEquals(s, toJson(d));
    }

    @Test
    // https://stackoverflow.com/questions/33224540/use-json-net-to-parse-json-date-of-format-dateepochtime-offset/33228106#33228106
    public void exampleFromStackOverflow() throws ParseException {
        String s = "/Date(1445301615000-0700)/";
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(2015, 10, 19), LocalTime.of(17, 40, 15), ZoneOffset.ofHoursMinutes(-7, 00));
        assertEquals(d, fromJson(s));
        assertEquals(s, toJson(d));
    }

    @Test
    public void negativeMillis() throws ParseException {
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0, 0, 0), ZoneOffset.ofHoursMinutes(1, 00));
        String s = "/Date(-3600000+0100)/";
        assertEquals(d, fromJson(s));
        assertEquals(s, toJson(d));
    }

    @Test
    public void testNull() throws ParseException {
        assertNull(toJson(null));
        assertNull(fromJson(null));
    }

    @Test(expected = ParseException.class)
    public void unmarshalNotADate() throws ParseException {
        String s = "/Date(pizza)/";
        fromJson(s);
    }

    @Test(expected = ParseException.class)
    public void unmarshalWithLeadingCharacters() throws ParseException {
        String s = " /Date(1420107010000+0000)/";
        fromJson(s);
    }

    @Test(expected = ParseException.class)
    public void unmarshalWithTrailingCharacters() throws ParseException {
        String s = "/Date(1420107010000+0000)/ ";
        fromJson(s);
    }

}

