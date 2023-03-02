package ru.clevertec.customjson.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Detail {
    private int number;
    private String str;
    private boolean flag;
    private List<Double> list;
    private Set<String> set;

    public Detail() {
    }

    public Detail(int number, String str, boolean flag, List<Double> list, Set<String> set) {
        this.number = number;
        this.str = str;
        this.flag = flag;
        this.list = list;
        this.set = set;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<Double> getList() {
        return list;
    }

    public void setList(List<Double> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return number == detail.number && flag == detail.flag && str.equals(detail.str) && list.equals(detail.list) && set.equals(detail.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, str, flag, list, set);
    }

    @Override
    public String toString() {
        return "Detail{" +
                "number=" + number +
                ", str='" + str + '\'' +
                ", flag=" + flag +
                ", list=" + list +
                ", set=" + set +
                '}';
    }
}
