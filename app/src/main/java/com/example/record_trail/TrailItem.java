package com.example.record_trail;

public class TrailItem {

    private String location; // 화면에 지도 마커를 찍기 위한 변수
    private String title;
    private String time;
    private String calorie;
    private boolean bookmark = false;

    public TrailItem(String title, String time, String calorie) {
        this.title = title;
        this.time = time;
        this.calorie = calorie;
    }

    public TrailItem(String title, String time, String calorie, boolean bookmark) {
        this.title = title;
        this.time = time;
        this.calorie = calorie;
        this.bookmark = bookmark;
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

    public String getcalorie() {
        return calorie;
    }

    public void setcalorie(String calorie) {
        this.calorie = calorie;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
