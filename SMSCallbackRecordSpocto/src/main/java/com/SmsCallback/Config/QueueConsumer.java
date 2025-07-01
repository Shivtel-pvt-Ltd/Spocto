package com.SmsCallback.Config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SmsCallback.Service.ConsumerService;
import com.SmsCallback.utility.CallbackPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class QueueConsumer {

	
	@Autowired
	ConsumerService smsCallbackService ;
	
	Logger logger = LoggerFactory.getLogger(QueueConsumer.class);
	
	@RabbitListener(id = "Spocto",queues = "${queueName}" , concurrency = "1" )
	public void recieveMessage(CallbackPojo message) throws Exception{

		
		logger.info("Consuming callback data from  Spocto.callback.queue Queue : "+ message);
		smsCallbackService.callToClientAndSaveToDatabase(message);

		
	}	
	
}
