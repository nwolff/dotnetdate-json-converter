package com.github.nwolff.dotnetdate_json_converter;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts between java.time.OffsetDateTime objects and the .net DateTime Wire Format: https://msdn.microsoft.com/en-us/library/bb412170%28v=vs.110%29.aspx
 * <p>
 * Excludes:
 * - We do not accept nor generate dates without an offset (which in the microsoft spec should be interpreted as a 'local date'
 * <p>
 * <p>
 * References:
 * -
 * - http://stackoverflow.com/questions/10286204/the-right-json-date-format
 * - http://stackoverflow.com/questions/9127529/how-to-parse-net-datetime-received-as-json-string-into-javas-date-object
 */
public class DotNetDateJsonConverter {

    private static Pattern RE = Pattern.compile("^/Date\\((\\d+)([+-])(\\d{2})(\\d{2})\\)/$");

    /**
     * Returns a json representation of the given offsetDateTime.
     *
     * @param offsetDateTime the date to represent in json
     * @return a json representation in .net wire format
     */
    public static String toJson(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        // http://programmers.stackexchange.com/questions/225343/why-are-the-java-8-java-time-classes-missing-a-getmillis-method
        long millis = localDateTime.toEpochSecond(ZoneOffset.UTC) * 1_000 + localDateTime.getNano() / 1_000_000;

        int offsetSeconds = offsetDateTime.getOffset().getTotalSeconds();
        int offsetHours = offsetSeconds / 3_600;
        int offsetMinutes = (offsetSeconds / 60) % 60;

        StringBuilder sb = new StringBuilder();
        sb.append("/Date(");
        sb.append(millis);
        if (offsetHours < 0) {
            sb.append("-");
        } else {
            sb.append("+");
        }
        sb.append(String.format("%02d", Math.abs(offsetHours)));
        sb.append(String.format("%02d", Math.abs(offsetMinutes)));
        sb.append(")/");

        return sb.toString();
    }

    /**
     * Builds an OffsetDateTime given the json representation of a date in .net wire format,
     * or throws ParseException if the given string is not a date in .net wire format
     *
     * @param json a json representation of a date in .net wire format
     * @return the OffsetDateTime that was represented in the json string
     * @throws ParseException if the given string is not a date in .net wire format
     */
    public static OffsetDateTime fromJson(String json) throws ParseException {
        if (json == null) {
            return null;
        }

        Matcher matcher = RE.matcher(json);
        if (!matcher.find()) {
            throw new ParseException("Cannot parse " + json);
        }
        String millisStr = matcher.group(1);
        String offsetSignStr = matcher.group(2);
        String offsetHoursStr = matcher.group(3);
        String offsetMinutesStr = matcher.group(4);
        try {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(millisStr)), ZoneOffset.UTC);

            int offsetHours = Integer.valueOf(offsetHoursStr);
            int offsetMinutes = Integer.valueOf(offsetMinutesStr);
            if (offsetSignStr.equals("-")) {
                offsetHours = -offsetHours;
                offsetMinutes = -offsetMinutes;
            }
            ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(offsetHours, offsetMinutes);

            return OffsetDateTime.of(localDateTime, zoneOffset);
        } catch (NumberFormatException e) {
            throw new ParseException("Cannot parse " + json, e);
        }
    }
}
