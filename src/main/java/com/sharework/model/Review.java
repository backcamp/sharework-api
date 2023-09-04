package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "worker_id")
    private long workerId;

    @NotNull
    @Column(name = "giver_id")
    private long giverId;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "star_rating")
    private double starRating;

    @Column(name = "review_type")
    private String reviewType;

    @Column(name = "job_id")
    @NotNull
    private long jobId;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;

    @Builder
    public Review(long workerId, long giverId, String comment, double starRating, String reviewType, long jobId) {
        LocalDateTime now = LocalDateTime.now();
        this.workerId = workerId;
        this.giverId = giverId;
        this.comment = comment;
        this.starRating = starRating;
        this.reviewType = reviewType;
        this.jobId = jobId;
        this.createdAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());
    }
}
