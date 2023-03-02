package ru.clevertec.customjson;

import ru.clevertec.customjson.exception.ParserException;
import ru.clevertec.customjson.parser.ParserToObject;
import ru.clevertec.customjson.parser.ParserToJson;

public class Mapper {

    public <T> T parseJson(String json, Class<T> clazz) throws ParserException {
        return new ParserToObject().parse(json, clazz);
    }

    public String parseObject(Object o) throws ParserException {
        return new ParserToJson().parse(o);
    }
}
