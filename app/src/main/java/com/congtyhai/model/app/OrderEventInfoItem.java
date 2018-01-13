package com.congtyhai.model.app;

/**
 * Created by HAI on 10/13/2017.
 */

public class OrderEventInfoItem {

    private String event;
    private String time;
    private String eventId;
    private String award;
    private String awardImg;
    private String point;
    private String hasPoint;
    private String describe;
    private int type;
    // 0: header
    // 1: content

    public OrderEventInfoItem(int type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getAwardImg() {
        return awardImg;
    }

    public void setAwardImg(String awardImg) {
        this.awardImg = awardImg;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(String hasPoint) {
        this.hasPoint = hasPoint;
    }
}
