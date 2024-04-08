package com.example.greengrove.Model;

public class Review {
    private float averageRating;
    private int numberOfReviews;

    public Review(float averageRating, int numberOfReviews) {
        this.averageRating = averageRating;
        this.numberOfReviews = numberOfReviews;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    @Override
    public String toString() {
        return "Review{" +
                "averageRating=" + averageRating +
                ", numberOfReviews=" + numberOfReviews +
                '}';
    }
}
