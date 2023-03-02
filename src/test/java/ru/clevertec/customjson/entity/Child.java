package ru.clevertec.customjson.entity;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(staticName = "aChild")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Child extends Parent {
    private String string;
    private Double[] doubles;
    private List<String> stringList;
    private Set<Integer> integerSet;

    public void setParent(Parent parent) {
        setABoolean(parent.isABoolean());
        setAByte(parent.getAByte());
        setAShort(parent.getAShort());
        setAnInt(parent.getAnInt());
        setALong(parent.getALong());
        setADouble(parent.getADouble());
        setAFloat(parent.getAFloat());
        setAChar(parent.getAChar());
    }
}
