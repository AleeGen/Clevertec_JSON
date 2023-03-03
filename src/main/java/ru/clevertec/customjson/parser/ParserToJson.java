package ru.clevertec.customjson.parser;

import ru.clevertec.customjson.exception.ParserException;
import ru.clevertec.customjson.util.TypeClass;

import java.lang.reflect.*;
import java.util.*;

import static ru.clevertec.customjson.exception.Message.FAIL_WRITE_JSON;
import static ru.clevertec.customjson.util.TypeClass.getTreeClasses;
import static ru.clevertec.customjson.util.TypeClass.isImplementInterface;

public class ParserToJson {
    public String parse(Object o) throws ParserException {
        Optional<String> optionalS = TypeClass.serialize(o);
        StringBuilder result = new StringBuilder();
        if (optionalS.isPresent()) {
            return optionalS.get();
        }
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            result.append("[");
            for (Object element : ((Object[]) o)) {
                result.append(parse(element)).append(",");
            }
            result.deleteCharAt(result.length() - 1).append("]");
        } else if (isImplementInterface(clazz, Collection.class)) {
            result.append("[");
            for (Object element : ((Collection<?>) o)) {
                result.append(parse(element)).append(",");
            }
            result.deleteCharAt(result.length() - 1).append("]");
        } else if (isImplementInterface(clazz, Map.class)) {
            result.append("{");
            for (Map.Entry<?, ?> element : ((Map<?, ?>) o).entrySet()) {
                result.append(parse(element.getKey())).append(":")
                        .append(parse(element.getValue())).append(",");
            }
            result.deleteCharAt(result.length() - 1).append("}");
        } else {
            try {
                result.append(writeField(result.toString(), clazz, o));
            } catch (IllegalAccessException e) {
                throw new ParserException(String.format(FAIL_WRITE_JSON, e.getMessage()));
            }
        }
        return result.toString();
    }

    private String writeField(String json, Class<?> clazz, Object o) throws IllegalAccessException, ParserException {
        StringBuilder result = new StringBuilder(json).append("{");
        for (Class<?> c : getTreeClasses(clazz)) {
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                result.append("\"").append(field.getName().toLowerCase())
                        .append("\":").append(parse(field.get(o))).append(",");
            }
        }
        result.deleteCharAt(result.length() - 1);
        return result.append("}").toString();
    }

}