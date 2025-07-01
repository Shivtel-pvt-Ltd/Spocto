package com.SmsCallback.controller;

import com.SmsCallback.InternalRestRequest.HeaderProperties;
import com.SmsCallback.Model.callback_arch;
import com.SmsCallback.Repository.callbackRepository;
import com.SmsCallback.Service.RedisService;
import com.SmsCallback.Service.SmsCallbackProdService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SmsCallbacks/Spocto")
public class SmsCallbackController {
	@Autowired
	SmsCallbackProdService smscbService;

	@Autowired
	callbackRepository cbpRepository;
	

	@Autowired
	HeaderProperties headerProperties;

	@Autowired
	RedisService redisService;

	Logger logger = LoggerFactory.getLogger(SmsCallbackController.class);

	@GetMapping({ "/webhook" })
	public void CallBackController(@RequestParam Map<String, String> requestdata) throws Exception {

		logger.info("Data from Operator : " + requestdata.toString());

		this.smscbService.DuplicateCheck(requestdata);
		
		
	}
	
	@GetMapping("/save")
	public void savetest() throws Exception {
		
		logger.info("Testing the Callbacks");
		
		for (int i = 0; i < 100000; i++) {

			Map<String, String > calbackPractoDataMap = new HashMap<>();
			
	
			calbackPractoDataMap.put("corelationid", "9999999890.1200000000.1654002362.20220531.0.wzrk_default.-1");
			calbackPractoDataMap.put("txid", UUID.randomUUID().getMostSignificantBits() + "");
			calbackPractoDataMap.put("to", "919999778080");
			calbackPractoDataMap.put("from", "Spocto");
			calbackPractoDataMap.put("description", "Message delivered successfully");
			calbackPractoDataMap.put("pdu", "1");
			calbackPractoDataMap.put("text", "Hi, your followup visit at Excellence Esthetics is due on 1st Oct. - Practo");
			calbackPractoDataMap.put("deliverystatus", "DELIVERY_SUCCESS");
			calbackPractoDataMap.put("deliverydt", "2024-08-01 03:30:46");
			
			this.smscbService.DuplicateCheck(calbackPractoDataMap);
			
		}
		
		
		
	}
	

}