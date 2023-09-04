package com.sharework.manager;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HashidsManager {
	@Value("${security.hashids-salt}")
	String SALT;

	public String toEncode(int number) {
		Hashids hashIds = new Hashids(SALT);
		return hashIds.encode(number);
	}

	public long[] toDecodeArray(String code) {
		Hashids hashIds = new Hashids(SALT);
		return hashIds.decode(code);
	}
}
