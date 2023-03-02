package ru.clevertec.customjson.parser;

import ru.clevertec.customjson.exception.ParserException;
import ru.clevertec.customjson.util.TypeClass;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.clevertec.customjson.util.TypeClass.getTreeClasses;
import static ru.clevertec.customjson.util.TypeClass.isImplementInterface;
import static ru.clevertec.customjson.exception.Message.FAIL_FILL_FIELD;

public class ParserToObject {
    private static final String REGEX_FIELD = "\"%s\"[:]+((?=\\[)\\[[^]]*\\]|(?=\\{)\\{[^\\}]*\\}|\\\"[^\"]*\"|(?=\\d)\\d*.*?\\d*|(?=\\w)\\w*)";
    private static final String REGEX_ARRAY = "[\\[\\{].+[\\]\\}]";

    public <T> T parse(String json, Class<T> clazz) throws ParserException {
        T object = null;
        if (TypeClass.getType(clazz).isEmpty()) {
            if (clazz.isArray()) {
                return (T) recursionArray(json, clazz);
            } else if (isImplementInterface(clazz, List.class)) {
                return (T) recursionList(json, clazz);
            } else if (isImplementInterface(clazz, Set.class)) {
                return (T) recursionList(json, clazz).stream().collect(Collectors.toSet());
            } else if (isImplementInterface(clazz, Map.class)) {
                //// TODO
            } else {
                try {
                    object = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new ParserException(String.format("Error %s when create object %s", e.getMessage(), clazz.getName()));
                }
                List<Field> fields = getTreeClasses(clazz).stream()
                        .flatMap(c -> Arrays.stream(c.getDeclaredFields())).toList();
                for (Field field : fields) {
                    Matcher matcher = Pattern.compile(String.format(REGEX_FIELD, field.getName())).matcher(json);
                    if (matcher.find()) {
                        String group = matcher.group();
                        if (TypeClass.getType(field.getType()).isEmpty()) {
                            if (!"null".equals(matcher.group(1))) {
                                group = json.substring(json.indexOf(group), getLastIndexField(json));
                            }
                            json = json.replace(group, "");
                            try {
                                fillField(field, object, group);
                            } catch (IllegalAccessException e) {
                                throw new ParserException(String.format(FAIL_FILL_FIELD, e.getMessage(), field.getName(), object));
                            }
                        } else {
                            json = json.substring(json.indexOf(group) + group.length());
                            String value = matcher.group(1);
                            if (value.endsWith("\"") && value.startsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            Object ob = TypeClass.deserialize(value, field.getType());
                            field.setAccessible(true);
                            try {
                                field.set(object, ob);
                            } catch (IllegalAccessException e) {
                                throw new ParserException(String.format(FAIL_FILL_FIELD, e.getMessage(), field.getName(), object));
                            }
                        }
                    }
                }
            }
        }
        return object;
    }

    private void fillField(Field field, Object o, String json) throws IllegalAccessException, ParserException {
        Matcher matcher = Pattern.compile(REGEX_ARRAY).matcher(json);
        if(matcher.find()) {
            json = matcher.group(0);
        }
        Object result = null;
        field.setAccessible(true);
        if (field.getType().isArray()) {
            result = recursionArray(json, field.getType());
        } else if (isImplementInterface(field.getType(), List.class)) {
            result = recursionList(json, field.getGenericType());
        } else if (isImplementInterface(field.getType(), Set.class)) {
            result = recursionList(json, field.getGenericType()).stream().collect(Collectors.toSet());
        } else if (isImplementInterface(field.getType(), Map.class)) {
            //// TODO
        } else {
            field.set(o, parse(json, field.getType()));
            return;
        }
        field.set(o, result);
    }

    private Collection recursionList(String json, Type type) {
        Collection a = null;
        Type newType = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (!(newType instanceof Class)) {
            a = fillMultiList(json, type);
        } else {
            a = fillSingleList(json, type);
        }
        return a;
    }

    private Object recursionArray(String json, Class clazz) {
        Object a = null;
        clazz = clazz.getComponentType();
        if (clazz != null && clazz.isArray()) {
            a = fillMultiArray(json, clazz);
        } else {
            a = fillSingleArray(json, clazz);
        }
        return a;
    }

    private Collection fillMultiList(String json, Type type) {
        Collection a = null;
        json = json.substring(1, json.length() - 1);
        List<String> values = new ArrayList();
        int index = 0;
        Type newType = ((ParameterizedType) type).getActualTypeArguments()[0];
        do {
            index = getLastIndexField(json);
            String group = json.substring(0, index);
            json = json.replace(group, "");
            if (json.startsWith(",")) {
                json = json.substring(1);
            }
            values.add(group);
        } while (json.length() > 0);
        int size = values.size();
        a = Arrays.stream(((Object[]) Array.newInstance(Collection.class, size))).toList();
        Object[] b = a.toArray();
        for (int i = 0; i < size; i++) {
            Array.set(b, i, recursionList(values.get(i), newType));
        }
        a = Arrays.stream(b).toList();
        return a;
    }

    private Collection fillSingleList(String json, Type type) {
        Collection a = null;
        Class<?> generic = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        List<String> values = new ArrayList<>(Arrays.asList(json.split(",")));
        values.set(0, values.get(0).substring(1));
        values.set(values.size() - 1, values.get(values.size() - 1).substring(0, values.get(values.size() - 1).length() - 1));
        int size = values.size();
        a = Arrays.stream(((Object[]) Array.newInstance(generic, size))).toList();
        int i = 0;
        Object[] b = a.toArray();
        for (String value : values) {
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1);
            }
            if (value.endsWith("\"") && value.startsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            Array.set(b, i, TypeClass.deserialize(value, generic));
            i++;
        }
        a = Arrays.stream(b).toList();
        return a;
    }

    private Object fillMultiArray(String json, Class clazz) {
        Object a;
        json = json.substring(1, json.length() - 1);
        List<String> values = new ArrayList();
        int index = 0;
        do {
            index = getLastIndexField(json);
            String group = json.substring(0, index);
            json = json.replace(group, "");
            if (json.startsWith(",")) {
                json = json.substring(1);
            }
            values.add(group);
        } while (json.length() > 0);
        int size = values.size();
        a = Array.newInstance(clazz, size);
        for (int i = 0; i < size; i++) {
            Array.set(a, i, recursionArray(values.get(i), clazz));
        }
        return a;
    }

    private Object fillSingleArray(String json, Class clazz) {
        Object a;
        List<String> values = new ArrayList<>(Arrays.asList(json.split(",")));
        values.set(0, values.get(0).substring(1));
        values.set(values.size() - 1, values.get(values.size() - 1).substring(0, values.get(values.size() - 1).length() - 1));
        int size = values.size();
        if (clazz == null) {
            clazz = Object.class;
        }
        a = Array.newInstance(clazz, size);
        int i = 0;
        for (String value : values) {
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1);
            }
            if (value.endsWith("\"") && value.startsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            Array.set(a, i, TypeClass.deserialize(value, clazz));
            i++;
        }
        return a;
    }

    private int getLastIndexField(String json) {
        int count = -1;
        char[] charArray = json.toCharArray();
        char symbol;
        int i = 0;
        while (count != 0) {
            symbol = charArray[i];
            if (symbol == '{' || symbol == '[') {
                if (count == -1) {
                    count = 0;
                }
                count++;
            } else if (symbol == '}' || symbol == ']') {
                count--;
            }
            i++;
        }
        return i;
    }

}