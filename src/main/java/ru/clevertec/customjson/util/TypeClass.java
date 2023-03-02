package ru.clevertec.customjson.util;

import java.util.*;
import java.util.function.Function;

public enum TypeClass {
    //Можно добавлять другие неделимые типы, чтобы реализовать их сериализацию/десериализацию
    BYTE(byte.class, Object::toString, Byte::parseByte),
    SHORT(short.class, Object::toString, Short::parseShort),
    INT(int.class, Object::toString, Integer::parseInt),
    LONG(long.class, Object::toString, Long::parseLong),
    FLOAT(float.class, Object::toString, Float::parseFloat),
    DOUBLE(double.class, Object::toString, Double::parseDouble),
    CHAR(char.class, Object::toString, o -> o.charAt(0)),
    BOOLEAN(boolean.class, Object::toString, Boolean::parseBoolean),
    BYTE_OBJECT(Byte.class, Object::toString, Byte::parseByte),
    SHORT_OBJECT(Short.class, Object::toString, Short::parseShort),
    INT_OBJECT(Integer.class, Object::toString, Integer::parseInt),
    LONG_OBJECT(Long.class, Object::toString, Long::parseLong),
    FLOAT_OBJECT(Float.class, Object::toString, Float::parseFloat),
    DOUBLE_OBJECT(Double.class, Object::toString, Double::parseDouble),
    BOOLEAN_OBJECT(Boolean.class, Object::toString, Boolean::parseBoolean),
    CHAR_OBJECT(Character.class, o -> "\"" + o.toString() + "\"", o -> o.charAt(0)),
    STRING(String.class, o -> "\"" + o.toString() + "\"", o -> o),
    DATE(Date.class, o -> String.valueOf(((Date) o).getTime()), o -> new Date(Long.parseLong(o)));

    private final Class<?> clazz;
    private final Function<Object, String> serializeFunction;
    private final Function<String, Object> deserializeFunction;

    TypeClass(Class<?> clazz, Function<Object, String> sFunction, Function<String, Object> deserializeFunction) {
        this.clazz = clazz;
        this.serializeFunction = sFunction;
        this.deserializeFunction = deserializeFunction;
    }

    public static Optional<String> serialize(Object o) {
        if (o == null) {
            return Optional.of("null");
        }
        return getType(o.getClass()).map(typeClass -> typeClass.serializeFunction.apply(o));
    }

    public static Object deserialize(String str, Class<?> clazz) {
        return getType(clazz).map(typeClass -> typeClass.deserializeFunction.apply(str)).get();
    }

    public static boolean isImplementInterface(Class<?> actual, Class<?> expected) {
        if (actual == null || actual == Object.class) {
            return false;
        }
        if (actual == expected) {
            return true;
        }
        return Arrays.stream(actual.getInterfaces())
                .anyMatch(o -> isImplementInterface(o, expected)) || isImplementInterface(actual.getSuperclass(), expected);
    }

    public static List<Class<?>> getTreeClasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        if (clazz.isPrimitive()) {
            classes.add(clazz);
            return classes;
        }
        while (clazz != Object.class) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                break;
            }
        }
        Collections.reverse(classes);
        return classes;
    }

    public static Optional<TypeClass> getType(Class<?> o) {
        return Arrays.stream(TypeClass.values())
                .filter(typeClass -> getTreeClasses(o).contains(typeClass.clazz) ||
                        isImplementInterface(o, typeClass.clazz)).findFirst();
    }
}