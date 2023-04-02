package com.ute.common.response;

public class ReviewResponse {

    private Integer id;
    private String comment;
    private String rating;
    private String reviewTime;
    private String updateReviewTime;
    private String customerName;
    private String customerPhoto;

    public ReviewResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getUpdateReviewTime() {
        return updateReviewTime;
    }

    public void setUpdateReviewTime(String updateReviewTime) {
        this.updateReviewTime = updateReviewTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(String customerPhoto) {
        this.customerPhoto = customerPhoto;
    }
}
