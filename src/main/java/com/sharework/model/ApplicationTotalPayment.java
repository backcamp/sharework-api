package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "application_total_payment")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationTotalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", position = 1, example = "1", required = true)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "application_id")
    private Long applicationId;

    @NotNull
    @Column(name = "job_id")
    private Long jobId;

    @NotNull
    @Column(name = "total_payment")
    private int totalPayment;

    @Builder
    public ApplicationTotalPayment(long id, long applicationId, long jobId, int totalPayment) {

        this.id = id;
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.totalPayment = totalPayment;
    }
}
