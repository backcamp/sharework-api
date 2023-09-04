package com.sharework.model.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ReqParamsSendSms {
	private final String sender = "01021253653";
	private String receiver;
	private String content;
}
