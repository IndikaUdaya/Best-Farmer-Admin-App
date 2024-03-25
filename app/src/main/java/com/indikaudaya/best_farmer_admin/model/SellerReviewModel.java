package com.indikaudaya.best_farmer_admin.model;

import java.io.Serializable;

public class SellerReviewModel implements Serializable {
    private String firstName;
    private double score;
    private String reviewComment;
    private String reviewDate;
    private String profileImageUrl;


    public SellerReviewModel(String firstName, double score, String reviewComment, String reviewDate, String profileImageUrl) {
        this.firstName = firstName;
        this.score = score;
        this.reviewComment = reviewComment;
        this.reviewDate = reviewDate;
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String commentedDate) {
        this.reviewDate = commentedDate;
    }
}
