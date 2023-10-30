package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppliedListResponse {
    public AppliedListResponse(AppliedListPayload payload, AppliedListMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public AppliedListPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public AppliedListMeta meta;

    public static class AppliedListPayload {
        @ApiModelProperty(value = "applications")
        public List<AppliedListApplication> applications;

        @ApiModelProperty(value = "pagination")
        public AppliedListPagination pagination;

        public AppliedListPayload(List<AppliedListApplication> applications, AppliedListPagination pagination) {
            this.applications = applications;
            this.pagination = pagination;
        }
    }

    public static class AppliedListMeta extends BasicMeta {

        public AppliedListMeta(boolean status, String message) {
            super(status, message);
        }
    }

    public static class AppliedListApplication {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "status")
        public String status;

        @ApiModelProperty(value = "worker")
        public AppliedListWorker worker;

        @ApiModelProperty(value = "checklist")
        public List<AppliedListApplicationChecklist> checkList;

        public AppliedListApplication(long id, String status, AppliedListWorker worker, List<AppliedListApplicationChecklist> checkList) {
            this.id = id;
            this.status = status;
            this.worker = worker;
            this.checkList = checkList;
        }
    }

    public static class AppliedListWorker {
        public AppliedListUser user;

        @Column(name = "experienceCount")
        public Integer experienceCount;

        @Column(name = "absenceCount")
        public Integer absenceCount;

        public AppliedListWorker(AppliedListUser user, Integer experienceCount, Integer absenceCount) {
            this.user = user;
            this.experienceCount = experienceCount;
            this.absenceCount = absenceCount;
        }
    }

    public static class AppliedListUser {
        @Column(name = "id")
        public long id;

        @Column(name = "name")
        public String name;

        @Column(name = "profileImg")
        public String profileImg;

        public AppliedListUser(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }

    public static class AppliedListApplicationChecklist {
        @Column(name = "contents")
        public String contents;

        @Column(name = "isChecked")
        public Boolean isChecked;

        public AppliedListApplicationChecklist(String contents, Boolean isChecked) {
            this.contents = contents;
            this.isChecked = isChecked;
        }
    }

    public static class AppliedListPagination {
        @ApiModelProperty(value = "last")
        public boolean last;

        @ApiModelProperty(value = "totalPage")
        public int totalPage;

        @ApiModelProperty(value = "page")
        public int nextPage;

        @Builder
        public AppliedListPagination(boolean last, int totalPage, int page) {
            this.last = last;
            this.totalPage = totalPage;
            this.nextPage = page;
        }
    }
}



