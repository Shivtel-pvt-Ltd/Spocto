package com.SmsCallback.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@Entity
@Table(name = "error_code_details_curefit", uniqueConstraints = {@UniqueConstraint(columnNames = {"smpp_error_code"})})
public class ErrorCodeDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	String smpp_error_code;
	String customised_description;
}
