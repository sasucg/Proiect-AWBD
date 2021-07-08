package com.javaproject.storeapp.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewRequest {

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @NotBlank(message = "Comment cannot be null.")
    @Length(min = 2, max = 250)
    private String comment;

    @NotNull
    @Min(1)
    @Max(5)
    private int rating;

    public ReviewRequest(String comment, int rating) {//, Car car) {
        this.comment = comment;
        this.rating = rating;
    }
}
