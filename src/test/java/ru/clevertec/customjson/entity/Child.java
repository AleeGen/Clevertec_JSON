package ru.clevertec.customjson.entity;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Child extends Parent {
    private String string;
    private Date date;
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
