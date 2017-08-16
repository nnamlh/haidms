package com.congtyhai.model.app;

/**
 * Created by HAI on 8/16/2017.
 */

public class CheckInFunctionInfo {
    private int icon;
    private String title;
    private String time;

    public CheckInFunctionInfo(int icon, String title, String time) {
        this.setIcon(icon);
        this.setTitle(title);
        this.setTime(time);
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
}
