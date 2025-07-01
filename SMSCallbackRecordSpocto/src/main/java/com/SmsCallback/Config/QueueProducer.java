package com.SmsCallback.Config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.SmsCallback.utility.CallbackPojo;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class QueueProducer {

	
	Logger logger = LoggerFactory.getLogger(QueueProducer.class);

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	
	@Value("${exchangeName}") 
	private String exchangeName;
	
	
	@Value("${rountingKey}") 
	private String rountingKey;


	   public void sendMessage(CallbackPojo message) throws Exception
	    {  
		   
		   logger.info("Key :: {} Value :: {}",LocalDate.now(),new ObjectMapper().writeValueAsString(message));
		   logger.info("Sending to RabbitMq : exchangeName "+ exchangeName + " rountingKey : " + rountingKey);
	        rabbitTemplate.convertAndSend( 
	        		exchangeName, rountingKey, message); 
	        	        
	    } 
	   
}
