package com.sharework.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;


@Entity
@Getter
@Setter
@Table(name = "user_checklist")
@NoArgsConstructor
@TypeDef(name = "string-Array", typeClass = StringArrayType.class)
public class UserChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Column(name = "id")
    private long id;

    @ApiModelProperty(value = "userId", example = "30")
    @NotNull
    @Column(name = "user_id")
    private long userId;

    @NotNull
    @Column(name = "contents")
    @Type(type = "string-Array")
    private String[] contents;

    @Builder
    public UserChecklist(long userId, String[] contents) {
        this.userId = userId;
        this.contents = contents;
    }
}
