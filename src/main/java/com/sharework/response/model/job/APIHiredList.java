package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.application.APIApplicationHistory;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIHiredList {
    public APIHiredList(Payload payload, Meta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public Meta meta;

    public class Payload {
        @ApiModelProperty(value = "applications")
        public List<Application> applications;
        
        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public Payload(List<Application> applications, Pagination pagination) {
            this.applications = applications;
            this.pagination = pagination;
        }
    }

    public static class Meta extends BasicMeta {
        @ApiModelProperty(value = "totalPerson")
        public Integer totalPerson;

        public Meta(boolean status, String message, Integer totalPerson) {
            super(status, message);
            this.totalPerson = totalPerson;
        }
    }

    public static class Application {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "status")
        public String status;

        @ApiModelProperty(value = "worker")
        public Worker worker;

        @ApiModelProperty(value = "checklist")
        public List<ApplicationChecklist> checkList;

        public Application(long id, String status, Worker worker, List<ApplicationChecklist> checkList) {
            this.id = id;
            this.status = status;
            this.worker = worker;
            this.checkList = checkList;
        }
    }

    public static class Worker {
        public User user;

        @Column(name = "experienceCount")
        public Integer experienceCount;

        @Column(name = "absenceCount")
        public Integer absenceCount;

        public Worker(User user, Integer experienceCount, Integer absenceCount) {
            this.user = user;
            this.experienceCount = experienceCount;
            this.absenceCount = absenceCount;
        }
    }

    public static class User {
        @Column(name = "id")
        public long id;

        @Column(name = "name")
        public String name;

        @Column(name = "profileImg")
        public String profileImg;

        public User(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }

    public static class ApplicationChecklist {
        @Column(name = "contents")
        public String contents;

        @Column(name = "isChecked")
        public Boolean isChecked;

        public ApplicationChecklist(String contents, Boolean isChecked) {
            this.contents = contents;
            this.isChecked = isChecked;
        }
    }
}



