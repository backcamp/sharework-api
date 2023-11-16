package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HiredListResponse {
    public HiredListResponse(HiredListPayload payload, HiredListMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public HiredListPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public HiredListMeta meta;

    public static class HiredListPayload {
        @ApiModelProperty(value = "applications")
        public List<HiredListApplication> applications;
        
        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public HiredListPayload(List<HiredListApplication> applications, Pagination pagination) {
            this.applications = applications;
            this.pagination = pagination;
        }
    }

    public static class HiredListMeta extends BasicMeta {
        @ApiModelProperty(value = "totalPerson")
        public Integer totalPerson;

        public HiredListMeta(boolean status, String message, Integer totalPerson) {
            super(status, message);
            this.totalPerson = totalPerson;
        }
    }

    public static class HiredListApplication {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "status")
        public String status;

        @ApiModelProperty(value = "worker")
        public HiredListWorker worker;

        @ApiModelProperty(value = "checklist")
        public List<HiredListApplicationChecklist> checkList;

        public HiredListApplication(long id, String status, HiredListWorker worker, List<HiredListApplicationChecklist> checkList) {
            this.id = id;
            this.status = status;
            this.worker = worker;
            this.checkList = checkList;
        }
    }

    public static class HiredListWorker {
        public HiredListUser user;

        @Column(name = "experienceCount")
        public Integer experienceCount;

        @Column(name = "absenceCount")
        public Integer absenceCount;

        public HiredListWorker(HiredListUser user, Integer experienceCount, Integer absenceCount) {
            this.user = user;
            this.experienceCount = experienceCount;
            this.absenceCount = absenceCount;
        }
    }

    public static class HiredListUser {
        @Column(name = "id")
        public long id;

        @Column(name = "name")
        public String name;

        @Column(name = "profileImg")
        public String profileImg;

        public HiredListUser(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }

    public static class HiredListApplicationChecklist {
        @Column(name = "contents")
        public String contents;

        @Column(name = "isChecked")
        public Boolean isChecked;

        public HiredListApplicationChecklist(String contents, Boolean isChecked) {
            this.contents = contents;
            this.isChecked = isChecked;
        }
    }
}



