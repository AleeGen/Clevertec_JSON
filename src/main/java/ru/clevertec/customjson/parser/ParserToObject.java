package ru.clevertec.customjson.parser;

import ru.clevertec.customjson.exception.ParserException;
import ru.clevertec.customjson.util.TypeClass;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.clevertec.customjson.exception.Message.*;
import static ru.clevertec.customjson.util.TypeClass.getTreeClasses;
import static ru.clevertec.customjson.util.TypeClass.isImplementInterface;

public class ParserToObject {
    private static final String REGEX_FIELD = "\"%s\"[:]+((?=\\[)\\[[^]]*\\]|(?=\\{)\\{[^\\}]*\\}|\\\"[^\"]*\"|(?=\\d)\\d*.*?\\d*,|(?=\\w)\\w*)";
    private static final String REGEX_ARRAY = "[\\[\\{].+[\\]\\}]";
    private static final String END_OBJECT = "}";
    private static final String BEGIN_OBJECT = "{";
    private static final String FIELD_SEPARATOR = ",";
    private static final String QUOTE = "\"";
    private static final String NULL = "null";

    public <T> T parse(String json, Class<T> clazz) throws ParserException {
        List<Field> fields = getTreeClasses(clazz).stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredFields())).toList();
        try {
            T object = clazz.getDeclaredConstructor().newInstance();
            return (T) fillField(fields, json, object);
        } catch (ReflectiveOperationException e) {
            throw new ParserException(String.format(FAIL_READ_JSON, e.getMessage()));
        }
    }

    private Object fillField(List<Field> fields, String json, Object object) throws ParserException, ReflectiveOperationException {
        json = json.startsWith(BEGIN_OBJECT) && json.endsWith(END_OBJECT) ? json.substring(1, json.length() - 1) : json;
        for (Field field : fields) {
            Matcher matcher = Pattern.compile(String.format(REGEX_FIELD, field.getName().toLowerCase())).matcher(json);
            if (matcher.find()) {
                String group = matcher.group();
                group = group.endsWith(FIELD_SEPARATOR) ? group.substring(0, group.length() - 1) : group;
                if (TypeClass.getType(field.getType()).isEmpty()) {
                    group = !NULL.equals(matcher.group(1)) ? json.substring(json.indexOf(group), getLastIndexField(json)) : group;
                    json = json.replace(group, "");
                    fillComplexType(field, object, group);
                } else {
                    json = json.substring(0, json.indexOf(group)) + json.substring(json.indexOf(group) + group.length());
                    String value = matcher.group(1);
                    value = value.endsWith(FIELD_SEPARATOR) ? value.substring(0, value.length() - 1) : value;
                    value = value.endsWith(QUOTE) && value.startsWith(QUOTE) ? value.substring(1, value.length() - 1) : value;
                    Object ob = TypeClass.deserialize(value, field.getType());
                    field.setAccessible(true);
                    field.set(object, ob);
                }
            }
        }
        return object;
    }

    private void fillComplexType(Field field, Object o, String json) throws ParserException, ReflectiveOperationException {
        Matcher matcher = Pattern.compile(REGEX_ARRAY).matcher(json);
        json = matcher.find() ? matcher.group(0) : json;
        field.setAccessible(true);
        if (field.getType().isArray()) {
            field.set(o, recursionArray(json, field.getType()));
        } else if (isImplementInterface(field.getType(), Collection.class)) {
            field.set(o, recursionList(json, field.getGenericType()));
        } else {
            field.set(o, parse(json, field.getType()));
        }
    }

    private Collection<?> recursionList(String json, Type type) {
        Collection<?> a = null;
        Type newType = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (!(newType instanceof Class)) {
            List<String> values = disassembleArray(json);
            Object[] o = (Object[]) Array.newInstance(Collection.class, values.size());
            AtomicInteger i = new AtomicInteger(0);
            values.forEach(value -> Array.set(o, i.getAndIncrement(), recursionList(value, newType)));
            a = Arrays.stream(o).toList();
        } else {
            Class<?> generic = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
            a = Arrays.stream((Object[]) fillArray(json, generic)).toList();
        }
        a = isImplementInterface((Class<?>) ((ParameterizedType) type).getRawType(), Set.class) ? new HashSet<>(a) : a;
        return a;
    }

    private Object recursionArray(String json, Class<?> clazz) {
        Class<?> c = clazz.getComponentType();
        if (c != null && c.isArray()) {
            List<String> values = disassembleArray(json);
            Object o = Array.newInstance(c, values.size());
            AtomicInteger i = new AtomicInteger(0);
            values.forEach(value -> Array.set(o, i.getAndIncrement(), recursionArray(value, c)));
            return o;
        } else {
            return fillArray(json, c == null ? Object.class : c);
        }
    }

    private Object fillArray(String json, Class<?> clazz) {
        List<String> values = new ArrayList<>(Arrays.asList(json.split(FIELD_SEPARATOR)));
        values.set(0, values.get(0).substring(1));
        values.set(values.size() - 1, values.get(values.size() - 1).substring(0, values.get(values.size() - 1).length() - 1));
        Object result = Array.newInstance(clazz, values.size());
        AtomicInteger i = new AtomicInteger(0);
        values.forEach(v -> {
            v = v.endsWith(FIELD_SEPARATOR) ? v.substring(0, v.length() - 1) : v;
            v = v.endsWith(QUOTE) && v.startsWith(QUOTE) ? v.substring(1, v.length() - 1) : v;
            Array.set(result, i.getAndIncrement(), TypeClass.deserialize(v, clazz));
        });
        return result;
    }

    private List<String> disassembleArray(String array) {
        List<String> values = new ArrayList<>();
        array = array.substring(1, array.length() - 1);
        do {
            String group = array.substring(0, getLastIndexField(array));
            array = array.replace(group, "");
            array = array.startsWith(FIELD_SEPARATOR) ? array.substring(1) : array;
            values.add(group);
        } while (array.length() > 0);
        return values;
    }

    private int getLastIndexField(String json) {
        int count = -1;
        char[] charArray = json.toCharArray();
        int i = 0;
        while (count != 0) {
            if (charArray[i] == '{' || charArray[i] == '[') {
                if (count == -1) {
                    count = 0;
                }
                count++;
            } else if (charArray[i] == '}' || charArray[i] == ']') {
                count--;
            }
            i++;
        }
        return i;
    }

}