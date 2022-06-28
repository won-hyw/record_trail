package com.example.record_trail;

public class CommentsItem {

    private int image;
    private float rating;
    private String comments;

    public CommentsItem(int image, float rating, String comments) {
        this.image = image;
        this.rating = rating;
        this.comments = comments;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
