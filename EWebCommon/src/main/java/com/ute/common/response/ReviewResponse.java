package com.ute.common.response;

import java.util.Date;

public class ReviewResponse {

    private Integer id;
    private String comment;
    private String rating;
    private Date reviewTime;
    private Date updateReviewTime;
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

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getUpdateReviewTime() {
        return updateReviewTime;
    }

    public void setUpdateReviewTime(Date updateReviewTime) {
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
