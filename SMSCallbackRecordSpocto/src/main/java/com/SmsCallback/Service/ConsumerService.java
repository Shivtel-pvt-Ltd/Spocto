package com.SmsCallback.Service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.SmsCallback.InternalRestRequest.ClientProperties;
import com.SmsCallback.InternalRestRequest.RestRequestClass;
import com.SmsCallback.Model.callback_arch;
import com.SmsCallback.Repository.callbackRepository;
import com.SmsCallback.utility.CallbackPojo;

@Service
public class ConsumerService {

	@Autowired
	callbackRepository cbpRepository;

	@Autowired
	RestRequestClass requestClass;

	@Autowired
	ClientProperties properties;

	@Autowired
	RedisService redisService;

	Logger logger = LoggerFactory.getLogger(ConsumerService.class);

	@Async("apiCall")
	public void callToClientAndSaveToDatabase(CallbackPojo cbp) throws Exception {

		callback_arch cb = new callback_arch();
		cb.setCorelationid(cbp.getCorelationid());
		cb.setDeliverydt(cbp.getDeliverydt());
		cb.setDeliverystatus(cbp.getDeliverystatus());
		cb.setDescription(cbp.getDescription());
		cb.setFromk(cbp.getFromk());
		cb.setPdu(cbp.getPdu());
		cb.setText(cbp.getText());
		cb.setTok(cbp.getTok());
		cb.setTxid(cbp.getTxid());


		   CompletableFuture<String> responseFuture = getcall(cb).
				   thenApplyAsync(response -> {
		          saveCallbackData(cb, response);
		          return response;
		        }).exceptionally(ex -> {
		          this.logger.info("Exception during saving to arch table: " + ex.getLocalizedMessage());
		          saveCallbackData(cb, ex.getLocalizedMessage());
		          return null;
		        });		
	}

	
	public CompletableFuture<String> getcall(callback_arch cb) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return this.requestClass.getRequest(cb);
			} catch (Exception e) {
				logger.error("Exception during API call: {}", e.getLocalizedMessage(), e);
				return "API call failed: " + e.getMessage();
			}
		});

	}

	@Async("callbackInsert")
	public void saveCallbackData(callback_arch cb, String Response) {

		try {

			cb.setResponse(Response);
			cbpRepository.save(cb);
			logger.info("Insert to arch txid : {}, Response:{}, System.currentTimeMillisEnd : {} ", cb.getTxid(),
					Response, System.currentTimeMillis());

		}

		catch (Exception ex) {

			logger.error("Error During insertion into database : " + ex.getMessage());

			ex.printStackTrace();
		}

	}

}
