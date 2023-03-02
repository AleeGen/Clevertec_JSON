package ru.clevertec.customjson.entity;

import java.util.*;

public class Parent {
    private int id;
    private String str;
    private Detail detail;
    private Date date;
    private String[] array;
    private Map<String, Detail> map;

    public Parent() {
    }

    public Parent(int id, String str, Detail detail, Date date, String[] array, Map<String, Detail> map) {
        this.id = id;
        this.str = str;
        this.detail = detail;
        this.date = date;
        this.array = array;
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public Map<String, Detail> getMap() {
        return map;
    }

    public void setMap(Map<String, Detail> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return id == parent.id && str.equals(parent.str) && detail.equals(parent.detail) && date.equals(parent.date) && Arrays.equals(array, parent.array) && map.equals(parent.map);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, str, detail, date, map);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", str='" + str + '\'' +
                ", detail=" + detail +
                ", date=" + date +
                ", array=" + Arrays.toString(array) +
                ", map=" + map +
                '}';
    }
}
