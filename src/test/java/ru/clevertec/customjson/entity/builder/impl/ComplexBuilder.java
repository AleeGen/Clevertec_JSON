package ru.clevertec.customjson.entity.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.assertj.core.util.Arrays;
import ru.clevertec.customjson.entity.Child;
import ru.clevertec.customjson.entity.Complex;
import ru.clevertec.customjson.entity.builder.EntityBuilder;

import java.util.List;
import java.util.Set;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComplex")
public class ComplexBuilder implements EntityBuilder<Complex> {
    private List<List<String>> listList = List.of(List.of("str_1.1", "str_1.2"), List.of("str_2.1", "str_2.2"));
    private Boolean[][] booleans = Arrays.array(Arrays.array(false, false), Arrays.array(true, true));
    private Set<Set<Character>> sets = Set.of(Set.of('a', 'b'), Set.of('c', 'd'));
    private List<Set<String>> listSet = List.of(Set.of("a", "b"), Set.of("c", "d"));
    private Child child = ChildBuilder.aChild().build();

    @Override
    public Complex build() {
        final Complex complex = new Complex();
        complex.setListList(listList);
        complex.setBooleans(booleans);
        complex.setSets(sets);
        complex.setListSet(listSet);
        complex.setChild(child);
        return complex;
    }
}
