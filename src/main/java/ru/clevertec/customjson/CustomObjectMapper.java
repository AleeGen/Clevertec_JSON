package ru.clevertec.customjson;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.customjson.entity.Child;
import ru.clevertec.customjson.entity.Detail;
import ru.clevertec.customjson.entity.Parent;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import java.util.*;

public class CustomObjectMapper {

    public static void main(String[] args) throws IllegalAccessException {
        List<Double> list1 = List.of(1.1);
        List<Double> list2 = List.of(2.1, 2.2);
        List<Double> list3 = List.of(3.1, 3.2, 3.3);
        List<String> list4 = List.of("4.1", "4.2", "4.3", "4.4");
        List<String> list5 = List.of("5.1", "5.2", "5.3", "5.4", "5.5");
        List<String> list6 = List.of("6.1", "6.2", "6.3", "6.4", "6.5", "7.6");
        Set<String> set1 = Set.of("a1", "b1", "c1");
        Set<String> set2 = Set.of("a2", "b2", "c2");
        Set<String> set3 = Set.of("a3", "b3", "c3");
        Detail detail1 = new Detail(1, "detail1", true, list1, set1);
        Detail detail2 = new Detail(2, "detail2", true, list2, set2);
        Detail detail3 = new Detail(3, "detail3", true, list3, set3);
        Date date = new Date();
        String[] array = new String[]{"stroka1", null, "stroka3"};
        Map<String, Detail> details = Map.of("d2", detail2, "d3", detail3);
        List<List<String>> listLists = List.of(list4, list5, list6);
        Child child = new Child(0, "parent", detail1, date, array, details, listLists, 's');


        try (FileWriter writer = new FileWriter("src/main/resources/data.txt")) {
            new ObjectMapper().writeValue(writer, child);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       /* try (FileReader reader = new FileReader("src/main/resources/data.txt")) {
            Human human1 = new ObjectMapper().readValue(reader, Human.class);
            System.out.println(human1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        String result = objectMapper.generate(child);
        System.out.println(result);

       /* String a = "asd";
        System.out.println(TypeClass.serialize(a));
        double b = 5.3;
        System.out.println(TypeClass.serialize(b));
        Date date = new Date();
        System.out.println(TypeClass.serialize(date));
        char c = 's';
        System.out.println(TypeClass.serialize(c));*/


    }

    public String generate(Object o) throws IllegalAccessException {
        Optional<String> optionalS = TypeClass.serialize(o);
        StringBuilder result = new StringBuilder();
        if (optionalS.isPresent()) { //проверка на примитивы, дату и строки
            return optionalS.get();
        }
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) { //проверка на массив
            result.append("[");
            for (Object element : ((Object[]) o)) {
                result.append(generate(element)).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            return result.append("]").toString();
        } else if (isExpectedClass(clazz, Collection.class)) { //проверка на коллекцию
            result.append("[");
            for (Object element : ((Collection<?>) o)) {
                result.append(generate(element)).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            return result.append("]").toString();
        } else if (isExpectedClass(clazz, Map.class)) { //проверка на мапу
            result.append("{");
            for (Map.Entry<?, ?> element : ((Map<?, ?>) o).entrySet()) {
                result.append(generate(element.getKey())).append(":")
                        .append(generate(element.getValue())).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            return result.append("}").toString();
        } else { //остальные объекты
            result.append("{");
            while (clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    result.append("\"").append(field.getName()).append("\":").append(generate(field.get(o))).append(",");
                }
                clazz = clazz.getSuperclass();
            }
            result.deleteCharAt(result.length() - 1);
            result.append("}");
        }
        return result.toString();
    }

    private boolean isExpectedClass(Class<?> actual, Class<?> expected) {
        if (actual == null || actual == Object.class) {
            return false;
        }
        if (actual == expected) {
            return true;
        }
        return Arrays.stream(actual.getInterfaces())
                .anyMatch(o -> isExpectedClass(o, expected)) || isExpectedClass(actual.getSuperclass(), expected);
    }

}