package com.sharework.service;

import com.sharework.dao.JobTagDao;
import com.sharework.dao.TagDao;
import com.sharework.dao.TagSubDao;
import com.sharework.model.JobTag;
import com.sharework.model.model.BaseTag;
import com.sharework.model.model.BaseTagSub;
import com.sharework.response.model.tag.giveTag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

	@Autowired
	TagDao tagDao;
	@Autowired
	TagSubDao tagSubDao;
	@Autowired
	JobTagDao jobTagDao;

	public List<giveTag> giveTagList() {
		List<BaseTag> baseTagList = tagDao.findAll();
		List<giveTag> tagList = new ArrayList<>();

		for (BaseTag tag : baseTagList) {
			List<BaseTagSub> tagSub = tagSubDao.getByBaseTagId(tag.getId());
			giveTag giveTag = new giveTag(tag.getId(), tag.getCategory(), tagSub);
			tagList.add(giveTag);
		}

		return tagList;
	}

	public List<giveTag> giveAllTagList() {
		List<BaseTagSub> baseTagListSub = tagSubDao.findAllByOrderById();
		List<giveTag> tagList = new ArrayList<>();

		giveTag giveTag = new giveTag(1, "전체", baseTagListSub);
		tagList.add(giveTag);

		return tagList;
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
