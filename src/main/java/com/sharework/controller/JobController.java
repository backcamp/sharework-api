package com.sharework.controller;

import com.sharework.request.model.AppliedList;
import com.sharework.request.model.JobDetail;
import com.sharework.request.model.JobLocation;
import com.sharework.request.model.RegisterJob;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.Response;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.GetMinimumWageResponse;
import com.sharework.response.model.job.*;
import com.sharework.service.JobService;
import com.sharework.service.JobStatusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = JobResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "/cluster/main", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "위도경도 MAX,MIN값을 받아 해당 지역 JOB정보 넘겨줌", notes = "insert screean lat,lng information ")
    public ResponseEntity<JobResponse> getJobList(JobLocation getJob) {
        JobResponse response = jobService.getJobList(getJob);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "POST", value = "일감 등록정보를 받아 DB에 저장", notes = "insert JOB information")
    public ResponseEntity<SuccessResponse> insertJob(@RequestBody @ApiParam(required = true) RegisterJob job,
                                    @RequestHeader("access-token") String accessToken) {
        SuccessResponse response = jobService.insertJob(accessToken, job);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = JobClusterDetailResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "/cluster", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "클러스터에 해당하는 jobId와 page,pageSize 및 userLat,Lng를 받아 유저와의 거리순으로 job 상세정보 제공", notes = "insert jobId,pageNum,userLat,userLng")
    public ResponseEntity<JobClusterDetailResponse> jobClusterDetail(JobDetail jobDetail) {
        JobClusterDetailResponse response = jobService.jobClusterDetail(jobDetail);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = JobDetailResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "info/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "jobId를 받아 job정보 제공.", notes = "공고 상세보기")
    public ResponseEntity<JobDetailResponse> jobDetail(@PathVariable long id, @RequestHeader("access-token") String accessToken) {
        JobDetailResponse response = jobService.jobDetail(id, accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = HiredListResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "hired/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "채택한 회원 리스트")
    public ResponseEntity<HiredListResponse> getHiredList(@PathVariable long id,
                                       @RequestHeader("access-token") String accessToken) {
        HiredListResponse response = jobService.getHiredList(id, accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = AppliedListResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "applied/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "지원한 회원 리스트")
    public ResponseEntity<AppliedListResponse> getAppliedList(@PathVariable long id,
                                         AppliedList appliedList,
                                         @RequestHeader("access-token") String accessToken) {
        AppliedListResponse response = jobService.getAppliedList(id, appliedList, accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = ReceiptGiverResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "receipt/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "기버 완료된 공고 영수증")
    public ResponseEntity<ReceiptGiverResponse> getReceiptGiver(@PathVariable long id) {
        ReceiptGiverResponse response = jobService.getReceiptGiver(id);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = PreviousJobResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "previous", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "이전공고 리스트")
    public ResponseEntity<PreviousJobResponse> getPreviousJobs(@RequestHeader("access-token") String accessToken) {
        PreviousJobResponse response = jobService.getPreviousJobs(accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = ProceedingListResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "proceeding", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "기버 진행중 리스트")
    public ResponseEntity<ProceedingListResponse> getProceedingList(@RequestHeader("access-token") String accessToken,
                                            @ApiParam(required = true) Integer page) {
        ProceedingListResponse response = jobService.getProceedingList(accessToken, page);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = CompletedListResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "completed", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "기버 완료 리스트")
    public ResponseEntity<CompletedListResponse> getCompletedList(@RequestHeader("access-token") String accessToken,
                                           @ApiParam(required = true) Integer page) {
        CompletedListResponse response = jobService.getCompletedList(accessToken, page);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PatchMapping(value = "closed/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "PATCH", value = "job status closed로 변경")
    public ResponseEntity<SuccessResponse> updateStatusClosed(@PathVariable long id) {
        SuccessResponse response = jobService.updateStatusClosed(id);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = GetMinimumWageResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "minwage", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "최저시급 정보 제공")
    public ResponseEntity<GetMinimumWageResponse> getMinimumWage () {
        GetMinimumWageResponse response = jobService.getMinimumWage();
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = JobHiredInfoResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "hired/info/{jobId}/approve/{applicationId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "일감 정보 확인")
    public ResponseEntity<JobHiredInfoResponse> getJobHIredInfo(@PathVariable long jobId, @PathVariable long applicationId){
        JobHiredInfoResponse response = jobService.getJobHiredInfo(jobId, applicationId);
        return ResponseEntity.ok(response);
    }

}
