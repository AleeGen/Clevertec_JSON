package ru.clevertec.customjson.entity.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.assertj.core.util.Arrays;
import ru.clevertec.customjson.entity.Child;
import ru.clevertec.customjson.entity.builder.EntityBuilder;

import java.util.Date;
import java.util.List;
import java.util.Set;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aChild")
public class ChildBuilder implements EntityBuilder<Child> {

    private String string = "string";
    private Date date = new Date(1);
    private Double[] doubles = Arrays.array(1.0, 2.0, 3.0, 4.0, 5.0);
    private List<String> stringList = List.of("sting1", "string2");
    private Set<Integer> integerSet = Set.of(1, 2, 3, 4, 5);

    @Override
    public Child build() {
        final Child child = new Child();
        child.setParent(ParentBuilder.aParent().build());
        child.setString(string);
        child.setDate(date);
        child.setDoubles(doubles);
        child.setStringList(stringList);
        child.setIntegerSet(integerSet);
        return child;
    }
}
