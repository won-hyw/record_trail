package com.example.record_trail;

public class ReviewItem {

    private byte[] image;
    private String date;
    private String review;

    public ReviewItem(byte[] image, String date, String review) {
        this.image = image;
        this.date = date;
        this.review = review;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getreview() {
        return review;
    }

    public void setreview(String review) {
        this.review = review;
    }

}
