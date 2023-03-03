package ru.clevertec.customjson.exception;

public final class Message {
    private Message() {
    }

    public static final String FAIL_READ_JSON = "Error \"%s\" converting json to object";
    public static final String FAIL_WRITE_JSON = "Error \"%s\" converting object to json";

}
