package com.congtyhai.model.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarAgencyInfo {

    private String deputy;
    private String code;
    private String name;
    private int check;
    private List<Integer> dayChoose;
    private String rank;
    private String group;
    private  String type;

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public CalendarAgencyInfo(String deputy, String code, String name, int check, String type) {
        this.deputy = deputy;
        this.code = code;
        this.name = name;
        this.check = check;
        this.type = type;
    }

    public List<Integer> getDayChoose() {
        return dayChoose;
    }

    public void setDayChoose(List<Integer> dayChoose) {
        this.dayChoose = dayChoose;
    }

    public void addDayChoose(int daySelect) {
        if (dayChoose == null)
            dayChoose = new ArrayList<>();

        if (!dayChoose.contains(daySelect))
            dayChoose.add(daySelect);
    }

    public void removeDayChoose(int daySelect) {
        if (dayChoose == null)
            dayChoose = new ArrayList<>();

        int postion = dayChoose.indexOf(daySelect);
       if (postion != -1) {
        dayChoose.remove(postion);
       }
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
