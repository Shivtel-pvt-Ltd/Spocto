package com.SmsCallback.InternalRestRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "")
public class HeaderProperties {
    
    private Map<String, String> header;

	public Map<String,String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

    
    
}
