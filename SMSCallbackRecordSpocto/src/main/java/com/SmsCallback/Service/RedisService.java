package com.SmsCallback.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.SmsCallback.Model.callback_arch;
import com.SmsCallback.Repository.ErrorCodeDetailsRepository;
import com.SmsCallback.Repository.callbackRepository;

@Service
public class RedisService {

	Logger logger = LoggerFactory.getLogger(RedisService.class);

	@Autowired
	ErrorCodeDetailsRepository errorCodeDetailsRepository;
	
	@Autowired
	callbackRepository cRepository;

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	

	public String getDesriptionByErrCode(String Errcode) {

		try {
			String description = (String) redisTemplate.opsForHash().get("smpp_error", Errcode);
			logger.info("From Redis ErrorCode : {} , ErrorDescription : {} ", Errcode, description);
			return description;

		} catch (Exception e) {
			logger.error("Exception While Get Error Codes from Redis : {} ", e.getLocalizedMessage());
			return null;
		}

	}

	public void setDescriptionByErrCode(String Errcode, String ErrorDescription) {
		try {
			logger.info("Going to Save to  Redis ErrorCode : {} , ErrorDescription : {} ", Errcode, ErrorDescription);
			redisTemplate.opsForHash().put("smpp_error", Errcode, ErrorDescription);
		} catch (Exception e) {
			logger.error("Exception While Setting Error Codes to Redis : {} ", e.getLocalizedMessage());
		}

	}
	

}
