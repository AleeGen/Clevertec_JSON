package ru.clevertec.customjson;

import ru.clevertec.customjson.entity.Child;

public class App {
    public static void main(String[] args) throws Exception {

        String json = "{\"id\":0,\"str\":\"parent\",\"detail\":{\"number\":1,\"str\":\"detail1\",\"flag\":true,\"list\":[1.1],\"set\":[\"c1\",\"b1\",\"a1\"]},\"date\":1677334686541,\"array\":[\"stroka1\",null,\"stroka3\"],\"map\":{\"d3\":{\"number\":3,\"str\":\"detail3\",\"flag\":true,\"list\":[3.1,3.2,3.3],\"set\":[\"c3\",\"b3\",\"a3\"]},\"d2\":{\"number\":2,\"str\":\"detail2\",\"flag\":true,\"list\":[2.1,2.2],\"set\":[\"c2\",\"b2\",\"a2\"]}},\"listLists\":[[\"4.1\",\"4.2\",\"4.3\",\"4.4\"],[\"5.1\",\"5.2\",\"5.3\",\"5.4\",\"5.5\"],[\"6.1\",\"6.2\",\"6.3\",\"6.4\",\"6.5\",\"7.6\"]],\"aChar\":\"s\"}";
        String json2 = "{\"id\":0,\"str\":\"parent\",\"detail\":{\"number\":1,\"str\":\"detail1\",\"flag\":true,\"list\":[1.1,1.2],\"set\":[\"b1\",\"c1\",\"a1\"]},\"date\":1677708617465,\"array\":[\"stroka1\",null,\"stroka3\"],\"map\":null,\"listLists\":[[\"4.1\",\"4.2\",\"4.3\",\"4.4\"],[\"5.1\",\"5.2\",\"5.3\",\"5.4\",\"5.5\"],[\"6.1\",\"6.2\",\"6.3\",\"6.4\",\"6.5\",\"7.6\"]],\"aChar\":\"s\"}";
        Child myChild = new Mapper().parseJson(json2, Child.class);


        /*List<Double> list1 = List.of(1.1);
        List<Double> list2 = List.of(2.1, 2.2);
        List<Double> list3 = List.of(3.1, 3.2, 3.3);
        Set<String> set1 = Set.of("a1", "b1", "c1");
        Set<String> set2 = Set.of("a2", "b2", "c2");
        Set<String> set3 = Set.of("a3", "b3", "c3");
        Detail detail1 = new Detail(1, "detail1", true, list1, set1);
        Detail detail2 = new Detail(2, "detail2", true, list2, set2);
        Detail detail3 = new Detail(3, "detail3", true, list3, set3);
        List<Detail> details = List.of(detail1, detail2, detail3);
        A a = new A(details);

        StringWriter stringWriter = new StringWriter();
        new ObjectMapper().writeValue(stringWriter, a);
        System.out.println(stringWriter);
        List<Detail> actual = new Mapper().parseJson(stringWriter.toString(), List.class);
        System.out.println(actual);*/


    }

}
