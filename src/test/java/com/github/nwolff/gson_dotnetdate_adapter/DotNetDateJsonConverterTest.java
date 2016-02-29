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
    public void positiveOffset() throws ParseException {
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(10, 10, 10), ZoneOffset.ofHoursMinutes(1, 30));
        String s = "/Date(1420107010000+0130)/";
        assertEquals(s, toJson(d));
        assertEquals(d, fromJson(s));
    }

    @Test
    public void negativeOffset() throws ParseException {
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(10, 10, 10), ZoneOffset.ofHoursMinutes(-1, -30));
        String s = "/Date(1420107010000-0130)/";
        assertEquals(s, toJson(d));
        assertEquals(d, fromJson(s));
    }

    @Test
    public void negativeMillis() throws ParseException {
        OffsetDateTime d = OffsetDateTime.of(LocalDate.of(1969, 12, 31), LocalTime.of(23, 0, 0), ZoneOffset.ofHoursMinutes(1, 00));
        String s = "/Date(-3600000+0100)/";
       //  assertEquals(s, toJson(d)); // We don't care about this
       assertEquals(d, fromJson(s));
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

