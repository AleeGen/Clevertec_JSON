package ru.clevertec.customjson;

import java.lang.constant.Constable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

enum TypeClass {
    //Можно добавлять неудобные классы, чтобы реализовать их сериализацию/десериализацию
    DATE(Date.class, o1 -> String.valueOf(((Date) o1).getTime())),
    NUMBER(Number.class, Object::toString),
    BOOLEAN(Boolean.class, Object::toString),
    CHARACTER(Character.class, o -> "\"" + o.toString() + "\""),
    CHAR_SEQUENCE(CharSequence.class, o -> "\"" + o.toString() + "\"");
    private final Class<?> clazz;
    private final Function<Object, String> serializeFunction;
    //private Function<Object, String> deserializeFunction;

    TypeClass(Class<?> clazz, Function<Object, String> function) {
        this.clazz = clazz;
        this.serializeFunction = function;
    }

    public static Optional<String> serialize(Object o) {
        if (o == null) {
            return Optional.of("null");
        }
        Optional<TypeClass> optional = Arrays.stream(TypeClass.values())
                .filter(typeClass -> typeClass.clazz == o.getClass() ||
                        Arrays.stream(o.getClass().getInterfaces()).anyMatch(t -> t == typeClass.clazz) ||
                        typeClass.clazz == o.getClass().getSuperclass()).findFirst();
        return optional.map(typeClass -> typeClass.serializeFunction.apply(o));
    }
}
