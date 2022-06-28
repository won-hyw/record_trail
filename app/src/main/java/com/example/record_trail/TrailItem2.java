package com.example.record_trail;

public class TrailItem2 {

    private String title;
    private String time;
    private String length;
    // 시작지점도로명주소
    private String startingPointAddress;
    // 시작지점소재지지번주소
    private String startingPointSupportAddr;
    // 도착지점도로명주소
    private String endingPointAddress;
    // 도착지점소재지지번주소
    private String endingPointSupportAddr;
    
    public TrailItem2(String title, String time, String length, String startingPointAddress, String startingPointSupportAddr, String endingPointAddress, String endingPointSupportAddr) {
        this.title = title;
        this.time = time;
        this.length = length;
        this.startingPointAddress = startingPointAddress;
        this.startingPointSupportAddr = startingPointSupportAddr;
        this.endingPointAddress = endingPointAddress;
        this.endingPointSupportAddr = endingPointSupportAddr;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getStartingPointAddress() {
        return startingPointAddress;
    }

    public void setStartingPointAddress(String startingPointAddress) {
        this.startingPointAddress = startingPointAddress;
    }

    public String getStartingPointSupportAddr() {
        return startingPointSupportAddr;
    }

    public void setStartingPointSupportAddr(String startingPointSupportAddr) {
        this.startingPointSupportAddr = startingPointSupportAddr;
    }

    public String getEndingPointAddress() {
        return endingPointAddress;
    }

    public void setEndingPointAddress(String endingPointAddress) {
        this.endingPointAddress = endingPointAddress;
    }

    public String getEndingPointSupportAddr() {
        return endingPointSupportAddr;
    }

    public void setEndingPointSupportAddr(String endingPointSupportAddr) {
        this.endingPointSupportAddr = endingPointSupportAddr;
    }
}
