package com.sharework.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sharework.common.ApplicationTypeEnum;
import com.sharework.common.JobTypeEnum;
import com.sharework.dao.*;
import com.sharework.manager.CreateJwt;
import com.sharework.manager.HashidsManager;
import com.sharework.manager.JwtManager;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.request.model.APIUpdateUser;
import com.sharework.request.model.LoginObj;
import com.sharework.request.model.SignInRequestPw;
import com.sharework.request.model.SignupRequestPw;
import com.sharework.response.model.*;
import com.sharework.response.model.job.JobTagList;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.tag.APIGetTagRank;
import com.sharework.response.model.tag.JobTagRank;
import com.sharework.response.model.tag.TagRank;
import com.sharework.response.model.user.APIGetUser;
import com.sharework.response.model.user.Giver;
import com.sharework.response.model.user.Profile;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserDao userDao;

    private final JobDao jobDao;

    private final UserRateDao userRateDao;

    private final JobTagDao jobTagDao;

    private final ApplicationDao applicationDao;

    @Autowired
    JwtManager jwtManager;

    @Autowired
    HashidsManager hashidsManager;

    @Autowired
    TokenIdentification identification;

    @Autowired
    CreateJwt createJwt;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${profile.title}")
    private String saveTitle;
    @Value("${profile.subTitle}")
    private String saveSubTitle;
    private final AmazonS3 amazonS3;

//    public ResponseEntity signUp(SignupRequest request, BindingResult bindingResult) {
//
//        ResponseEntity response = null;
//        ErrorResponse error = null;
//        String errorMsg = null;
//        BasicMeta meta;
//
//        // vaild가 틀렸을 경우
//        if (bindingResult.hasErrors()) {
//            errorMsg = "정확한 정보를 기입해주세요.";
//            meta = new BasicMeta(false, errorMsg);
//            error = new ErrorResponse(meta);
//            response = new ResponseEntity<>(error, HttpStatus.OK);
//            return response;
//        } else if (userDao.getUserByEmail(request.getEmail()).isPresent()) {
//            errorMsg = "이메일이 중복됩니다.";
//            meta = new BasicMeta(false, errorMsg);
//            error = new ErrorResponse(meta);
//            response = new ResponseEntity<>(error, HttpStatus.OK);
//            return response;
//        } else if (userDao.findUserByPhoneNumber(request.getPhoneNumber()).isPresent()) {
//            errorMsg = "번호가 중복됩니다.";
//            meta = new BasicMeta(false, errorMsg);
//            error = new ErrorResponse(meta);
//            response = new ResponseEntity<>(error, HttpStatus.OK);
//            return response;
//        }
//        ResidentNumberJsonb residentNumber = new ResidentNumberJsonb(request.getResidentNumberFront(),
//                request.getResidentNumberRear());
//
//        String refreshToken = createJwt.createRefreshToken();
//
//        userDao.save(
//                User.builder().email(request.getEmail()).name(request.getName()).phoneNumber(request.getPhoneNumber())
//                        .residentNumber(residentNumber).userType("worker").refreshToken(refreshToken).build());
//
//        String accessToken = createJwt.createAccessToken(userDao.getUserByPhoneNumber(request.getPhoneNumber()));
//
//        SignUpPayload signupPayload = new SignUpPayload(accessToken, refreshToken);
//        meta = new BasicMeta(true, "회원가입이 완료되었습니다.");
//        final SignUpResponse result = new SignUpResponse(meta, signupPayload);
//        response = new ResponseEntity<>(result, HttpStatus.OK);
//        return response;
//    }

    public ResponseEntity signUpPw(SignupRequestPw request, BindingResult bindingResult) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        String errorMsg = null;
        BasicMeta meta;

        // vaild가 틀렸을 경우
        if (bindingResult.hasErrors()) {
            errorMsg = "정확한 정보를 기입해주세요.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        } else if (userDao.existsByEmailAndDeleteYn(request.getEmail(), "N")) {
            errorMsg = "이메일이 중복됩니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        } else if (userDao.existsByPhoneNumberAndDeleteYn(request.getPhoneNumber(), "N")) {
            errorMsg = "번호가 중복됩니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        } else if (userDao.existsByNameAndDeleteYn(request.getName(), "N")) {
            errorMsg = "닉네임이 중복됩니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        ResidentNumberJsonb residentNumber = null;

        if (request.getResidentNumberFront() != null && request.getResidentNumberRear() != null) {
            System.out.println(123);
            residentNumber = new ResidentNumberJsonb(request.getResidentNumberFront(),
                    request.getResidentNumberRear());
        }
        String refreshToken = createJwt.createRefreshToken();

        userDao.save(
                User.builder().email(request.getEmail()).name(request.getName()).phoneNumber(request.getPhoneNumber())
                        .residentNumber(residentNumber).userType("worker").password(request.getPassword()).refreshToken(refreshToken).build());

        meta = new BasicMeta(true, "회원가입이 완료되었습니다.");
        SuccessResponse successResponse = new SuccessResponse(meta);
        response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        return response;
    }

    public ResponseEntity login(SignInRequestPw request) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        String errorMsg = null;
        BasicMeta meta;


        Optional<User> user = userDao.findUserByPhoneNumberAndPasswordAndDeleteYn(request.getPhoneNumber(), request.getPassword(), "N");
        LoginObj loginObj = new LoginObj();

        user.ifPresentOrElse(selectUser -> {
            User userToken = userDao.findUserByPhoneNumberAndDeleteYn(selectUser.getPhoneNumber(), "N").orElseThrow();
            String accessToken = createJwt.createAccessToken(userToken);
            String refreshToken = selectUser.getJwt();
            String userType = selectUser.getUserType();
            loginObj.setFlag(true);
            loginObj.setAccessToken(accessToken);
            loginObj.setRefreshToken(refreshToken);
            loginObj.setUserType(userType);
        }, () -> {
            loginObj.setFlag(false);
        });

        if (loginObj.getFlag()) {
            VerifiedPayload verifiedPayload = new VerifiedPayload(loginObj.getAccessToken(), loginObj.getRefreshToken(), loginObj.getUserType());
            meta = new BasicMeta(true, "로그인 성공");
            VerifiedResponse verifiedResponse = new VerifiedResponse(verifiedPayload, meta);
            response = new ResponseEntity<>(verifiedResponse, HttpStatus.OK);
        } else {
            errorMsg = "로그인 정보가 일치하지 않습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
        }
        return response;
    }

    public ResponseEntity checkNickname(String nickname) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        String errorMsg = null;
        BasicMeta meta;


        if (!Pattern.matches("^[가-힣a-zA-Z0-9]{1,6}$", nickname)) {
            errorMsg = "사용 불가능한 닉네임입니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        if (userDao.existsByNameAndDeleteYn(nickname, "N")) {
            errorMsg = "닉네임이 존재합니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        meta = new BasicMeta(true, "사용 가능한 닉네임입니다.");
        SuccessResponse successResponse = new SuccessResponse(meta);
        response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    public ResponseEntity getUser(String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));

        Giver responseUser = new Giver(user.getId(), user.getName(), user.getProfileImg());
        int jobCount = jobDao.countByUserId(user.getId());
        Optional<UserRate> userRate = userRateDao.findByUserTypeAndUserId(user.getUserType().toUpperCase(), user.getId());

        final Profile profile = new Profile(responseUser, jobCount, 0, user.getComment());
        userRate.ifPresent(userRating -> {
            profile.setRate(userRating.getRate());
        });
        BasicMeta meta = new BasicMeta(true, "");
        APIGetUser apiGetUser = new APIGetUser(new APIGetUser.Payload(profile), meta);
        response = new ResponseEntity<>(apiGetUser, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateUser(String accessToken, APIUpdateUser request) {

        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();

        if(request.getName() != null)
           user.setName(request.getName());
        if(request.getComment() != null)
           user.setComment(request.getComment());

        BasicMeta meta = new BasicMeta(true, "정보가 수정되었습니다.");
        SuccessResponse successResponse = new SuccessResponse(meta);
        response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getTagRank(String accessToken) {

        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();

        List<TagRank> tagRankList = new ArrayList<>();
        List<Long> jobIdList = new ArrayList<>();
        List<JobTagRank> jobTagRankList = null;

        //giver라면
        if (user.getUserType().equals("giver")) {
            List<Job> jobList = jobDao.getByUserIdAndStatus(userId, JobTypeEnum.COMPLETED.name());

            jobList.forEach(job -> {
                jobIdList.add(job.getId());
            });

            jobTagRankList = jobTagDao.findByJobIdCountContents(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getContents()), jobTagRank.getCount()));
            });
        } else {
            List<Application> applicationList = applicationDao.getByUserIdAndStatus(userId, ApplicationTypeEnum.COMPLETED.name());

            applicationList.forEach(application -> {
                jobIdList.add(application.getJobId());
            });

            jobTagRankList = jobTagDao.findByJobIdCountContents(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                int hour = applicationDao.countByUserIdAndTagContents(userId, jobTagRank.getContents());
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getContents()), jobTagRank.getCount(), hour));
            });
        }

        BasicMeta meta = new BasicMeta(true, "");
        APIGetTagRank apiGetTagRank = new APIGetTagRank(new APIGetTagRank.Payload(tagRankList), meta);
        response = new ResponseEntity<>(apiGetTagRank, HttpStatus.OK);
        return response;
    }

    public ResponseEntity insertImg(String accessToken, MultipartFile multipartFile) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);
        userDao.findByIdAndDeleteYn(userId, "N").ifPresent(selectUser -> {
            if (selectUser.getProfileImg() != null)
                deleteImg(accessToken);
        });

        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            default:
                contentType = "image/jpg";
                break;
        }

        String title = saveTitle + "_" + saveSubTitle + "_" + userId + '.' + ext;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, title, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //user 프로필 url 저장.
        final BasicMeta meta = new BasicMeta();
        userDao.findByIdAndDeleteYn(userId, "N").ifPresentOrElse(user -> {
            String imagePath = amazonS3.getUrl(bucket, title).toString();
            user.setProfileImg(imagePath);
            user.setImgTitle(title);
            userDao.save(user);
            meta.setStatus(true);
            meta.setMessage("이미지가 성공적으로 저장되었습니다.");
        }, () -> {
            meta.setStatus(false);
            meta.setMessage("유저 정보가 존재하지 않습니다.");
        });

        SuccessResponse result = new SuccessResponse(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity withDrawal(String accessToken, String refreshToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();
        String deleteYn = user.getDeleteYn();

        if (deleteYn.equals("Y")) {
            String errorMsg = "삭제된 회원입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        BasicMeta meta = new BasicMeta();

        boolean flag = false;
        // 일감 오픈 및 진행중이라면 탈퇴불가
        ArrayList<String> jobStatusList = new ArrayList<>();
        jobStatusList.add(JobTypeEnum.OPEN.name());
        jobStatusList.add(JobTypeEnum.CLOSED.name());
        jobStatusList.add(JobTypeEnum.STARTED.name());
        if (jobDao.findByUserIdAndStatusIn(userId, jobStatusList).size() > 0) {
            String errorMsg = "진행중인 일감 삭제 후 탈퇴진행해 주시기 바랍니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        // hired_request,,hired_approved 일 마감하라는 에러 메시지 출력.
        ArrayList<String> applicationFailedStatusList = new ArrayList<>();
        applicationFailedStatusList.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        applicationFailedStatusList.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        if (applicationDao.countByUserIdAndStatusIn(userId, applicationFailedStatusList) > 0) {
            String errorMsg = "진행중인 일 처리 후 탈퇴진행해 주시기 바랍니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        ArrayList<String> applicationSuccessStatusList = new ArrayList<>();
        applicationSuccessStatusList.add(ApplicationTypeEnum.APPLIED.name());
        applicationSuccessStatusList.add(ApplicationTypeEnum.HIRED.name());

        applicationDao.getByUserIdAndStatusIn(userId, applicationSuccessStatusList).forEach(applicationDao::delete);

        userDao.findByIdAndDeleteYn(userId, "N").ifPresent(users -> {
            users.setDeleteYn("Y");
            userDao.save(users);
        });

        meta = new BasicMeta(true, "회원탈퇴가 성공하였습니다.");
        SuccessResponse result = new SuccessResponse(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getImg(String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        final ImgPayload imgPayload = new ImgPayload();
        final BasicMeta meta = new BasicMeta();
        userDao.findByIdAndDeleteYn(userId, "N").ifPresentOrElse(profileUser -> {
            imgPayload.setProfileImg(profileUser.getProfileImg());
            meta.setStatus(true);
            meta.setMessage("");
        }, () -> {
            meta.setStatus(false);
            meta.setMessage("유저 정보가 존재하지 않습니다.");
        });

        ImgResponse imgResponse = new ImgResponse(meta, imgPayload);
        response = new ResponseEntity<>(imgResponse, HttpStatus.OK);
        return response;
    }

    public ResponseEntity deleteImg(String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        final BasicMeta meta = new BasicMeta();
        userDao.findByIdAndDeleteYn(userId, "N").ifPresentOrElse(profileUser -> {
            String fileName = profileUser.getImgTitle();

            //이미지가 존재하지 않을 경우.
            if (fileName == null) {
                meta.setStatus(false);
                meta.setMessage("이미지가 존재하지 않습니다.");
                return;
            }

            amazonS3.deleteObject(bucket, fileName);

            //user profleImg null로 변경
            profileUser.setProfileImg(null);
            profileUser.setImgTitle(null);
            userDao.save(profileUser);
            meta.setStatus(true);
            meta.setMessage("성공적으로 삭제하였습니다.");
        }, () -> {
            meta.setStatus(false);
            meta.setMessage("유저 정보가 존재하지 않습니다.");
        });

        SuccessResponse successResponse = new SuccessResponse(meta);
        response = new ResponseEntity<>(successResponse, HttpStatus.OK);
        return response;
    }
}
