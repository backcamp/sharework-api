package com.sharework.service;

import com.sharework.dao.LocationFavoriteDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.LocationFavorite;
import com.sharework.request.model.LocationFavoriteRequest;
import com.sharework.response.model.Coordinate;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.location.APIgetLoactionFavoriteList;
import com.sharework.response.model.location.Location;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationFavoriteService {

    @Autowired
    LocationFavoriteDao locationFavoriteDao;
    @Autowired
    TokenIdentification identification;


    public ResponseEntity getLocationFavoriteList(String accessToken) {
        ResponseEntity response = null;

        long userId = identification.getHeadertoken(accessToken);

        List<LocationFavorite> locationFavoriteList = locationFavoriteDao.findByUserId(userId);

        List<Location> locationList = new ArrayList<>();
        for (LocationFavorite locationFavorite : locationFavoriteList) {
            Coordinate coordinate = new Coordinate(locationFavorite.getLat(), locationFavorite.getLng());
            locationList.add(Location.builder().id(locationFavorite.getId()).title(locationFavorite.getLocationName()).coordinate(coordinate).
                    build());
        }

        APIgetLoactionFavoriteList.Payload payload = new APIgetLoactionFavoriteList.Payload(locationList);
        BasicMeta meta = new BasicMeta(true, "");

        APIgetLoactionFavoriteList apIgetLoactionFavoriteList = new APIgetLoactionFavoriteList(payload, meta);
        response = new ResponseEntity<>(apIgetLoactionFavoriteList, HttpStatus.OK);
        return response;
    }

    @Transactional
    public ResponseEntity insertLocationFavorite(LocationFavoriteRequest locationFavoriteRequest, String accessToken) {
        ResponseEntity response = null;
        BasicMeta meta = new BasicMeta(true, "즐겨찾기가 성공적으로 저장되었습니다.");
        SuccessResponse result = new SuccessResponse(meta);

        long userId = identification.getHeadertoken(accessToken);

        int itemCount = locationFavoriteRequest.getLocalInfomations().length;
        if (itemCount >= 5) {
            meta.setStatus(false);
            meta.setMessage("위치는 최대 5개까지 등록이 가능합니다.");
            result.setMeta(meta);
            response = new ResponseEntity<>(result, HttpStatus.OK);
            return response;
        }


        locationFavoriteDao.deleteByUserId(userId);

        List<LocationFavorite> locationFavoriteList = new ArrayList<>();
        for (LocationFavoriteRequest.LocalInfomation localInfomation : locationFavoriteRequest.getLocalInfomations()) {
            locationFavoriteList.add(new LocationFavorite(userId, localInfomation.title, localInfomation.coordinate.getLatitude(), localInfomation.coordinate.getLongitude()));
        }
        locationFavoriteDao.saveAll(locationFavoriteList);

//        locationFavoriteList.stream(value -> locationFavoriteDao.save(locaReview.builder().workerId(value.getUserId())
//                .comment(registerReview.getComment())
//                .starRating(registerReview.getStartRating())
//                .giverId(userId).reviewType("GIVER")
//                .build()));
        result.setMeta(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity deleteLocationFavorite(long id) {
        ResponseEntity response = null;
        BasicMeta meta = new BasicMeta(true, "즐겨찾기가 성공적으로 삭제되었습니다.");

        locationFavoriteDao.deleteById(id);

        SuccessResponse result = new SuccessResponse(meta);
        result.setMeta(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
}
