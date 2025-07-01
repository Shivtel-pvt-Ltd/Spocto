package com.SmsCallback.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Data
@Table(name = "callback_spocto_arch", uniqueConstraints = {@UniqueConstraint(columnNames = {"txid"})})
public class callback_arch {
	 @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private long id;
	  
	 @Column(columnDefinition = "TEXT")
	  private String corelationid;
	  
	  private String txid;
	  
	  private String tok;
	  
	  private String fromk;
	  
	  private String description;
	  
	  private String pdu;
	  
	  @Column(columnDefinition = "TEXT")
	  private String text;
	  
	  private String deliverystatus;
	  
	  private String deliverydt;
	  
	  private String response;
	  
	  private String created_date;
	  
	  @PrePersist
	  protected void onCreate() {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        created_date = LocalDateTime.now().format(formatter);
	  }
}