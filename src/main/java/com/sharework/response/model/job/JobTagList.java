package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobTagList {

    private Long id;
    private String contents;

    @Builder
    public JobTagList(long id, String contents){
        this.id = id;
        this.contents = contents;
    }

    @Builder
    public JobTagList( String contents){
        this.contents = contents;
    }
}
