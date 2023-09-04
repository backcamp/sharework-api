package com.sharework.response.model.tag;

import com.sharework.model.model.BaseTagSub;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApiModel
@Getter
@Setter
@RequiredArgsConstructor
public class giveTag {
    public giveTag(long id, String category,List<BaseTagSub> tags) {
        this.id = id;
        this.category = category;
        this.tags = tags;
    }

    private long id;
    private String category;
    private List<BaseTagSub> tags;
}

