package ru.clevertec.customjson.entity;

import java.util.*;

public class Child extends Parent {
    private List<List<String>> listLists;
    private char aChar;

    public Child() {
    }

    public Child(int id, String str, Detail detail, Date date, String[] array, Map<String, Detail> map, List<List<String>> listLists, char aChar) {
        super(id, str, detail, date, array, map);
        this.listLists = listLists;
        this.aChar = aChar;
    }

    public List<List<String>> getListLists() {
        return listLists;
    }

    public void setListLists(List<List<String>> listLists) {
        this.listLists = listLists;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Child child = (Child) o;
        return aChar == child.aChar && listLists.equals(child.listLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listLists, aChar);
    }

    @Override
    public String toString() {
        return "Child{" +
                "listLists=" + listLists +
                ", aChar=" + aChar +
                super.toString() +
                '}';
    }
}
