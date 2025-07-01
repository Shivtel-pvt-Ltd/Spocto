package com.SmsCallback.InternalRestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("")
public class ClientProperties {

	private Map<String, String> ClientUrl = new HashMap<>();
	private Map<String, String> ClientMethod = new HashMap<>();
	private Map<String, String> ClientBody = new HashMap<>();
	public Map<String, String> getClientUrl() {
		return ClientUrl;
	}

	public void setClientUrl(Map<String, String> clientUrl) {
		ClientUrl = clientUrl;
	}

	public Map<String, String> getClientMethod() {
		return ClientMethod;
	}

	public void setClientMethod(Map<String, String> clientMethod) {
		ClientMethod = clientMethod;
	}

	public Map<String, String> getClientBody() {
		return ClientBody;
	}

	public void setClientBody(Map<String, String> clientBody) {
		ClientBody = clientBody;
	}
	
}
