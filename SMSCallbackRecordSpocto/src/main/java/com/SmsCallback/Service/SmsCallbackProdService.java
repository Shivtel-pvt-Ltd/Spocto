package com.SmsCallback.Service;

import com.SmsCallback.Config.QueueProducer;
import com.SmsCallback.Model.ErrorCodeDetails;
import com.SmsCallback.Model.callback;
import com.SmsCallback.Repository.CallBackRepositoryUniq;
import com.SmsCallback.Repository.ErrorCodeDetailsRepository;
import com.SmsCallback.utility.CallbackPojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SmsCallbackProdService {

	@Autowired
	QueueProducer queueProducer;

	@Autowired
	ErrorCodeDetailsRepository errorCodeDetailsRepository;
	
	@Autowired
	CallBackRepositoryUniq cRepositoryUniq;

	@Autowired
	RedisService redisService;

	Logger logger = LoggerFactory.getLogger(SmsCallbackProdService.class);

	public void SendCallBackToQueue(Map<String, String> callBackData) {
		try {

			logger.info("Preparing callback for Callback Queue");

			CallbackPojo cb = new CallbackPojo();

			Map<String, String> errorCodeAndDespRes = new HashMap<>();
			cb.setCorelationid(callBackData.get("corelationid"));
			
	
			cb.setDeliverystatus(callBackData.get("deliverystatus"));
			cb.setDescription(callBackData.get("description"));

			cb.setFromk(callBackData.get("from"));
			cb.setPdu(callBackData.get("pdu"));
			cb.setText(callBackData.get("text"));
			cb.setTok(callBackData.get("to"));
			cb.setTxid(callBackData.get("txid"));
			
			cb.setDeliverydt(callBackData.get("deliverydt"));
				
			
			queueProducer.sendMessage(cb);

		} catch (Exception e) {
			System.out.println("Exception While Sending TO RabbitMq : " + e.getMessage());
		}

	}
	
	
		public void DuplicateCheck(Map<String, String> callBackData) throws Exception{
			
			try {
				
				
				callback cb = new callback();
				cb.setCorelationid(callBackData.get("corelationid"));
				
				
				cb.setDeliverystatus(callBackData.get("deliverystatus"));
				cb.setDescription(callBackData.get("description"));

				cb.setFromk(callBackData.get("from"));
				cb.setPdu(callBackData.get("pdu"));
				cb.setText(callBackData.get("text"));
				cb.setTok(callBackData.get("to"));
				cb.setTxid(callBackData.get("txid"));
				
				cb.setDeliverydt(callBackData.get("deliverydt"));
				cRepositoryUniq.save(cb);
				
				logger.info("Unique transaction id : {} , Hence Save to database ",cb.getTxid());
				
				SendCallBackToQueue(callBackData);
				
			} catch (DataIntegrityViolationException e) {
				logger.info("Duplicate transaction id : {} with Exception : {}",callBackData.get("txid"),e.getLocalizedMessage());
			}catch (Exception e) {
				
				logger.info("Exception occur : "+ e.getLocalizedMessage());
				
				
				
			}
						
		}

	public Map<String, String> CustomErrorWithCode(String description) {

		Map<String, String> resultMap = new HashMap<>();

		String regex = "\\d+";

		String customErrorDescriptionWithCodeString = "";
		String errcodeString = "";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(description);

		List<Integer> numbers = new ArrayList<>();

		while (matcher.find()) {

			String errorCode = matcher.group();
			String errorDescriptionCus = redisService.getDesriptionByErrCode(errorCode);

			if (errorDescriptionCus == null) {
				Optional<List<ErrorCodeDetails>> errorCodeDetails = errorCodeDetailsRepository
						.findBySmpp_error_code(errorCode);

				if (errorCodeDetails.isPresent()){
					
					try {
						errorDescriptionCus = errorCodeDetails.get().get(0).getCustomised_description();
						redisService.setDescriptionByErrCode(errorCode, errorDescriptionCus);
						
					} catch (Exception e) {
						logger.error("Getting Error while fetch from database : {} ",e.getLocalizedMessage());
					}
				}
			}

			if (errorDescriptionCus != null) {
				customErrorDescriptionWithCodeString += errorDescriptionCus + " ";
				errcodeString += errorCode;
			}
			
			numbers.add(Integer.parseInt(matcher.group()));
		}

		if (!numbers.isEmpty() && !customErrorDescriptionWithCodeString.isEmpty()) {
			resultMap.put("errCode", errcodeString);
			resultMap.put("errDescription", customErrorDescriptionWithCodeString);
		}

		if (numbers.isEmpty()) {
			
			logger.info("Description contain error but not code Hence Send As it is.");
			customErrorDescriptionWithCodeString = "No error Code in description of operator";
			resultMap.put("errCode", customErrorDescriptionWithCodeString);
			resultMap.put("errDescription", customErrorDescriptionWithCodeString);

			return resultMap;
		}

		if (!numbers.isEmpty() && customErrorDescriptionWithCodeString.isEmpty()) {
			logger.info("operator Description Contain codes but these are not find in error code list. Hence Send As it is ");
			customErrorDescriptionWithCodeString = "No description avaliable for this errorcode";

			resultMap.put("errCode", customErrorDescriptionWithCodeString);
			resultMap.put("errDescription", customErrorDescriptionWithCodeString);

			return resultMap;
		}

		return resultMap;

	}

	public String changeDatetoUTCandISO(String deliverydt) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(deliverydt, inputFormatter);
		ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));

		ZonedDateTime zonedDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("UTC-05:30"));
		DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String isoDate = zonedDateTime.format(isoFormatter);
		return isoDate;

	}

}
