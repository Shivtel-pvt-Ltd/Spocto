package com.SmsCallback.utility;

import java.io.Serializable;

import lombok.Data;

@Data
public class CallbackPojo {

	private String corelationid;
	private String txid;
	private String tok;
	private String fromk;
	private String description;
	private String pdu;
	private String text;
	private String deliverystatus;
	private String deliverydt;
	private String errCode;
	private String errorDescription;
	private String accountName;
	
}