package ru.clevertec.customjson.entity;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Parent {
    private boolean aBoolean;
    private byte aByte;
    private short aShort;
    private int anInt;
    private long aLong;
    private double aDouble;
    private float aFloat;
    private char aChar;
}
