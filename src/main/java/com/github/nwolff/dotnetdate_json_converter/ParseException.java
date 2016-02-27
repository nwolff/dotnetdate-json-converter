package com.github.nwolff.dotnetdate_json_converter;

public class ParseException extends Exception {

    private static final long serialVersionUID = -6613238135387198070L;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
