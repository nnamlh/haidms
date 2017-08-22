package com.congtyhai.model.app;

/**
 * Created by HAI on 8/16/2017.
 */

public class CheckInFunctionInfo {
    private int icon;
    private String title;
    private String time;
    private int code;

    public CheckInFunctionInfo(int code, int icon, String title, String time) {
        this.setIcon(icon);
        this.setTitle(title);
        this.setTime(time);
        this.setCode(code);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
