package ru.clevertec.customjson.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FactoryEntity {
    public static Child getEntity() {
        List<Double> list1 = List.of(1.1,1.2);
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
        Map<String, Detail> map = Map.of("d2", detail2, "d3", detail3);
        List<List<String>> listLists = List.of(list4, list5, list6);
        return new Child(0, "parent", detail1, date, array, null, listLists, 's');
    }
}
