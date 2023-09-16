package com.sharework.response.model.tag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public interface JobTagRank {
    String getContents();
    Long getId();

    int getCount();

}
