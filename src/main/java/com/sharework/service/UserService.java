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
import com.sharework.global.NotFoundException;
import com.sharework.manager.CreateJwt;
import com.sharework.manager.HashidsManager;
import com.sharework.manager.JwtManager;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.request.model.APIUpdateUser;
import com.sharework.request.model.LoginObj;
import com.sharework.request.model.SignInRequestPw;
import com.sharework.request.model.SignupRequestPw;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.VerifiedPayload;
import com.sharework.response.model.VerifiedResponse;
import com.sharework.response.model.job.JobTagList;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.tag.JobTagRank;
import com.sharework.response.model.tag.TagRank;
import com.sharework.response.model.user.Giver;
import com.sharework.response.model.user.Profile;
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

import javax.transaction.Transactional;
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

    public SuccessResponse signupPw(SignupRequestPw request, BindingResult bindingResult) {// vaild가 틀렸을 경우
        if (bindingResult.hasErrors()) {
            return new SuccessResponse(new BasicMeta(false, "정확한 정보를 기입해주세요."));
        } else if (userDao.existsByEmailAndDeleteYn(request.getEmail(), "N")) {
            return new SuccessResponse(new BasicMeta(false, "이메일이 중복됩니다."));
        } else if (userDao.existsByPhoneNumberAndDeleteYn(request.getPhoneNumber(), "N")) {
            return new SuccessResponse(new BasicMeta(false, "번호가 중복됩니다."));
        } else if (userDao.existsByNameAndDeleteYn(request.getName(), "N")) {
            return new SuccessResponse(new BasicMeta(false, "이미 사용중인 닉네임 입니다. 다른 닉네임을 입력해 주세요."));
        }

        ResidentNumberJsonb residentNumber = null;

        if (request.getResidentNumberFront() != null && request.getResidentNumberRear() != null) {
            residentNumber = new ResidentNumberJsonb(request.getResidentNumberFront(),
                    request.getResidentNumberRear());
        }
        String refreshToken = createJwt.createRefreshToken();

        userDao.save(
                User.builder().email(request.getEmail()).name(request.getName()).phoneNumber(request.getPhoneNumber())
                        .residentNumber(residentNumber).userType("worker").password(request.getPassword()).refreshToken(refreshToken).build());
        return new SuccessResponse(new BasicMeta(true, "회원가입이 완료되었습니다."));
    }

    public VerifiedResponse login(SignInRequestPw request) {
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
            BasicMeta meta = new BasicMeta(true, "로그인 성공");

            return new VerifiedResponse(verifiedPayload, meta);
        } else {
            throw new NotFoundException("로그인 정보가 일치하지 않습니다.");
        }
    }

    public SuccessResponse checkNickname(String nickname) {
        if (!Pattern.matches("^[가-힣a-zA-Z0-9]{1,6}$", nickname)) {
            return new SuccessResponse(new BasicMeta(false, "사용할 수 없는 닉네임입니다. 한글, 영문, 숫자로 구성된 6자이내 닉네임을 입력해 주세요"));
        }

        if (userDao.existsByNameAndDeleteYn(nickname, "N")) {
            return new SuccessResponse(new BasicMeta(false, "이미 사용중인 닉네임 입니다. 다른 닉네임을 입력해 주세요."));
        }

        return new SuccessResponse(new BasicMeta(true, "사용 가능한 닉네임입니다."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    public Profile getUser(String accessToken) {
        long id = identification.getHeadertoken(accessToken);
        return getUserInfo(id);
    }

    public Profile getUserById(Long id) {
        return getUserInfo(id);
    }

    public Profile getUserInfo(Long id) {
        User user = userDao.findByIdAndDeleteYn(id, "N").orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));

        Giver responseUser = new Giver(user.getId(), user.getName(), user.getProfileImg());
        int jobCount = jobDao.countByUserId(user.getId());
        Optional<UserRate> userRate = userRateDao.findByUserTypeAndUserId(user.getUserType().toUpperCase(), user.getId());
        List<TagRank> tagRanks = getTagRank(user);

        final Profile profile = new Profile(responseUser, jobCount, 0, user.getComment(), tagRanks);
        userRate.ifPresent(userRating -> {
            profile.setRate(userRating.getRate());
        });

        return profile;
    }

    public List<TagRank> getTagRank(User user) {
        List<TagRank> tagRankList = new ArrayList<>();
        List<Long> jobIdList = new ArrayList<>();
        List<JobTagRank> jobTagRankList = null;
        long userId = user.getId();

        //giver라면
        if (user.getUserType().equals("givers")) {
            List<Job> jobList = jobDao.getByUserIdAndStatus(userId, JobTypeEnum.COMPLETED.name());

            jobList.forEach(job -> {
                jobIdList.add(job.getId());
            });

            jobTagRankList = jobTagDao.findByJobIdCountContentsId(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getId(), jobTagRank.getContents()), jobTagRank.getCount()));
            });
        } else {
            List<Application> applicationList = applicationDao.getByUserIdAndStatus(userId, ApplicationTypeEnum.COMPLETED.name());

            applicationList.forEach(application -> {
                jobIdList.add(application.getJobId());
            });

            jobTagRankList = jobTagDao.findByJobIdCountContentsId(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                int hour = applicationDao.countByUserIdAndTagContents(userId, jobTagRank.getContents());
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getId(), jobTagRank.getContents()), jobTagRank.getCount(), hour));
            });
        }
        return tagRankList;
    }

    @Transactional
    public void updateUser(String accessToken, APIUpdateUser request) {
        long userId = identification.getHeadertoken(accessToken);

        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();

        if (request.getName() != null)
            user.setName(request.getName());
        if (request.getComment() != null)
            user.setComment(request.getComment());
    }

    public List<TagRank> getTagRank(String accessToken) {
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

            jobTagRankList = jobTagDao.findByJobIdCountContentsId(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getId(), jobTagRank.getContents()), jobTagRank.getCount()));
            });
        } else {
            List<Application> applicationList = applicationDao.getByUserIdAndStatus(userId, ApplicationTypeEnum.COMPLETED.name());

            applicationList.forEach(application -> {
                jobIdList.add(application.getJobId());
            });

            jobTagRankList = jobTagDao.findByJobIdCountContentsId(jobIdList);

            jobTagRankList.forEach(jobTagRank -> {
                int hour = applicationDao.countByUserIdAndTagContents(userId, jobTagRank.getContents());
                tagRankList.add(new TagRank(new JobTagList(jobTagRank.getId(), jobTagRank.getContents()), jobTagRank.getCount(), hour));
            });
        }

        return tagRankList;
    }

    public boolean insertImg(String accessToken, MultipartFile multipartFile) {
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
        Optional<User> userOptional = userDao.findByIdAndDeleteYn(userId, "N");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String imagePath = amazonS3.getUrl(bucket, title).toString();
            user.setProfileImg(imagePath);
            user.setImgTitle(title);
            userDao.save(user);
            return true;
        } else {
            return false;
        }
    }

    public SuccessResponse withdrawal(String accessToken, String refreshToken) {
        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();
        String deleteYn = user.getDeleteYn(); // FIXME: already an exception above.

        if (deleteYn.equals("Y")) {
            return new SuccessResponse(new BasicMeta(false, "삭제된 회원입니다."));
        }

        // 일감 오픈 및 진행중이라면 탈퇴불가
        ArrayList<String> jobStatusList = new ArrayList<>();
        jobStatusList.add(JobTypeEnum.OPEN.name());
        jobStatusList.add(JobTypeEnum.CLOSED.name());
        jobStatusList.add(JobTypeEnum.STARTED.name());
        if (jobDao.findByUserIdAndStatusIn(userId, jobStatusList).size() > 0) {
            return new SuccessResponse(new BasicMeta(false, "진행중인 일감 삭제 후 탈퇴진행해 주시기 바랍니다."));
        }

        // hired_request,,hired_approved 일 마감하라는 에러 메시지 출력.
        ArrayList<String> applicationFailedStatusList = new ArrayList<>();
        applicationFailedStatusList.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        applicationFailedStatusList.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        if (applicationDao.countByUserIdAndStatusIn(userId, applicationFailedStatusList) > 0) {
            return new SuccessResponse(new BasicMeta(false, "진행중인 일 처리 후 탈퇴진행해 주시기 바랍니다."));
        }

        ArrayList<String> applicationSuccessStatusList = new ArrayList<>();
        applicationSuccessStatusList.add(ApplicationTypeEnum.APPLIED.name());
        applicationSuccessStatusList.add(ApplicationTypeEnum.HIRED.name());

        applicationDao.getByUserIdAndStatusIn(userId, applicationSuccessStatusList).forEach(applicationDao::delete);

        userDao.findByIdAndDeleteYn(userId, "N").ifPresent(users -> {
            users.setDeleteYn("Y");
            userDao.save(users);
        });

        return new SuccessResponse(new BasicMeta(true, "회원탈퇴가 성공하였습니다."));
    }

    public String getImg(String accessToken) {
        long userId = identification.getHeadertoken(accessToken);

        Optional<User> userOptional = userDao.findByIdAndDeleteYn(userId, "N");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getProfileImg();
        } else {
            return "";
        }
    }

    public boolean deleteImg(String accessToken) {
        long userId = identification.getHeadertoken(accessToken);

        Optional<User> userOptional = userDao.findByIdAndDeleteYn(userId, "N");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String fileName = user.getImgTitle();

            //이미지가 존재하지 않을 경우.
            if (fileName == null) {
                return false;
            }

            amazonS3.deleteObject(bucket, fileName);

            //user profleImg null로 변경
            user.setProfileImg(null);
            user.setImgTitle(null);
            userDao.save(user);
            return true;
        } else {
            return false;
        }
    }
}
