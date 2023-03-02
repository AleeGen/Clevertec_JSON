package ru.clevertec.customjson.entity;


import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(staticName = "aComplex")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Complex {
    private List<List<String>> listList;
    private Boolean[][] booleans;
    private Set<Set<Character>> sets;
    private Child child;
}
