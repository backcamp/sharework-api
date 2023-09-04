package com.sharework.service;

import java.util.ArrayList;
import java.util.List;
import com.sharework.model.JobTag;
import com.sharework.response.model.tag.giveTag;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sharework.model.model.BaseTag;
import com.sharework.model.model.BaseTagSub;
import com.sharework.dao.JobTagDao;
import com.sharework.dao.TagDao;
import com.sharework.dao.TagSubDao;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.GiveTagListPayload;
import com.sharework.response.model.GiveTagListResponse;

@Service
public class TagService {

	@Autowired
	TagDao tagDao;
	@Autowired
	TagSubDao tagSubDao;
	@Autowired
	JobTagDao jobTagDao;

	public ResponseEntity giveTagList() {
		ResponseEntity response = null;
		ErrorResponse error = null;
		List<BaseTag> baseTagList =  tagDao.findAll();
		List<giveTag> tagList = new ArrayList<giveTag>();

		for (BaseTag tag : baseTagList) {
			List<BaseTagSub> tagSub = tagSubDao.getByBaseTagId(tag.getId());
			giveTag giveTag = new giveTag(tag.getId(),tag.getCategory(),tagSub);
			tagList.add(giveTag);
		}

		BasicMeta meta = new BasicMeta(true,"태그리스트 제공이 완료되었습니다.");
		GiveTagListPayload giveTagListPayload = new GiveTagListPayload(tagList);
		GiveTagListResponse giveTagListResponse = new GiveTagListResponse( giveTagListPayload,meta);
		response = new ResponseEntity<>(giveTagListResponse, HttpStatus.OK);
		return response;
	}

	public ResponseEntity giveAllTagList() {
		ResponseEntity response = null;
		ErrorResponse error = null;
		List<BaseTagSub> baseTagListSub =  tagSubDao.findAllByOrderById();
		List<giveTag> tagList = new ArrayList<giveTag>();

		giveTag giveTag = new giveTag(1 , "전체", baseTagListSub);
		tagList.add(giveTag);

		BasicMeta meta = new BasicMeta(true,"전체 태그리스트 제공이 완료되었습니다.");
		GiveTagListPayload giveTagListPayload = new GiveTagListPayload(tagList);
		GiveTagListResponse giveTagListResponse = new GiveTagListResponse( giveTagListPayload,meta);
		response = new ResponseEntity<>(giveTagListResponse, HttpStatus.OK);
		return response;
	}

	public Boolean insertJobTag(String[] tagList, long jobId) {
		for (String a :tagList) {
			System.out.println(a);
		}
		for (String tagSub : tagList) {
			// tagSubId가 없다면
			jobTagDao.save(JobTag.builder().contents(tagSub).jobId(jobId).build());
		}
		return true;
	}

}
