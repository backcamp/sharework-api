package com.sharework.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "job_tag")
@ToString
public class JobTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "job_id")
    @NotNull
    private long jobId;


    @Column(name = "contents")
    @NotNull
    private String contents;

    @Builder
    public JobTag(long id, long jobId, String contents) {
        this.id = id;
        this.jobId = jobId;
        this.contents = contents;
    }
}
