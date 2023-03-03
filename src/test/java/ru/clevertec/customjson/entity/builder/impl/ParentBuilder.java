package ru.clevertec.customjson.entity.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.customjson.entity.Parent;
import ru.clevertec.customjson.entity.builder.EntityBuilder;


@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aParent")
public class ParentBuilder implements EntityBuilder<Parent> {
    private boolean aBoolean = false;
    private byte aByte = 1;
    private short aShort = 1;
    private int anInt = 1;
    private long aLong = 1;
    private double aDouble = 1;
    private float aFloat = 1;
    private char aChar = '.';

    @Override
    public Parent build() {
        final Parent parent = new Parent();
        parent.setABoolean(aBoolean);
        parent.setAByte(aByte);
        parent.setAShort(aShort);
        parent.setAnInt(anInt);
        parent.setALong(aLong);
        parent.setADouble(aDouble);
        parent.setAFloat(aFloat);
        parent.setAChar(aChar);
        return parent;
    }

}
