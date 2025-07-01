package com.SmsCallback.InternalRestRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.SmsCallback.Model.callback_arch;
import com.SmsCallback.Repository.callbackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestRequestClass {
	Logger logger = LoggerFactory.getLogger(com.SmsCallback.InternalRestRequest.RestRequestClass.class);

//	@Autowired
//	ClientProperties properties;

	@Value("${UrlString}")
	String clientUrl;

	@Autowired
	HeaderProperties headerProperties;

	@Autowired
	callbackRepository cbpRepository;

	RestTemplate restTemplate = new RestTemplate();

	public ResponseEntity<String> getRequest1( String txid ,String error, String Description) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		
		headers.add("Content-Type", "application/json");
		
		logger.info("header : " + headers);
		
		String clienturlString = "https://app.flash49.com/AlertManagmentSystem/save?appName=SpoctoCallback"+"&error="+error+"&time="+LocalDateTime.now()+"&description="+Description+"&priority=low";

		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);


		logger.info("Get call To  : " + clienturlString);

		logger.info("Api call to Slack Start Time : {} of txid : {} ", System.currentTimeMillis(), txid);
		ResponseEntity<String> responseEntity = this.restTemplate.exchange(clienturlString, HttpMethod.GET, httpEntity,
				String.class);
		logger.info("Api call End Time : {} of txid : {} ", System.currentTimeMillis(),txid);
		logger.info("Response from client api  : {} of txid : {} ", responseEntity, txid);

		return responseEntity;

	}

	/// @Async("apicall")
	public String getRequest(callback_arch cb) throws Exception {
		logger.info("Api call Start Time : {} of txid : {}", System.currentTimeMillis(), cb.getTxid());

		Map<String, String> clientHeader = headerProperties.getHeader();

		// Prepare final URL
		String finalUrlString = prepareUrlString(clientUrl, cb);
		logger.info("Get call  To : {}  ", finalUrlString);

		// Initialize connection
		URL url = new URL(finalUrlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);

		// Add headers
		for (Map.Entry<String, String> entry : clientHeader.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		logger.info("Headers: {} of txid :  {} ", clientHeader, cb.getTxid());

		try {
			// Read response
			int responseCode = connection.getResponseCode();
			logger.info("Response Code: {} of txid : {} ", responseCode, cb.getTxid());

			StringBuilder response = new StringBuilder();
			try (BufferedReader in = new BufferedReader(new InputStreamReader(
					responseCode == 200 ? connection.getInputStream() : connection.getErrorStream()))) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			}
			logger.info("Api call End Time : {} of txid : {} ", System.currentTimeMillis(), cb.getTxid());
			logger.info("Response from client api  : {} of txid : {} ", response.toString(), cb.getTxid());

			if (responseCode == 200) {
				logger.info(
						"Container Is going to Saving Success Response inside saveCallbackData " + response.toString());
				saveCallbackData(cb, response.toString());
			} else {
				logger.info(
						"Container Is going to Saving Error Response inside saveCallbackData " + response.toString());

				saveCallbackData(cb, String.valueOf(responseCode));
			}

			return response.toString();

		}

		catch (Exception e) {

			logger.info("Getting Exception During Api Call " + e.getLocalizedMessage());
			saveCallbackData(cb, e.getLocalizedMessage());
			return e.getLocalizedMessage();
		}

	}

	@Async("callbackInsert")
	public void saveCallbackData(callback_arch cb, String Response) throws JsonProcessingException {

		String cbJsonString = (new ObjectMapper()).writeValueAsString(cb);

		try {

			cb.setResponse(Response);
			cbpRepository.save(cb);
			logger.info("Insert to arch txid : {}, Response:{}, System.currentTimeMillisEnd : {} ", cb.getTxid(),
					Response, System.currentTimeMillis());

		}

		catch (Exception ex) {

			logger.error("Error During insertion into database : {}  of txid : {} of callback : {} ", ex.getMessage(),
					cb.getTxid(), cbJsonString);

			ex.printStackTrace();

		}

	}

	public String prepareUrlString(String url, callback_arch cb) throws UnsupportedEncodingException {
		Map<String, String> values = new HashMap<>();

		logger.info("url: {} cb : {} ", url, cb);

		String encodedText = URLEncoder.encode(cb.getText(), StandardCharsets.UTF_8.toString());
		String encodedCorelationid = URLEncoder.encode(cb.getCorelationid(), StandardCharsets.UTF_8.toString());
		String encodedtxid = URLEncoder.encode(cb.getTxid(), StandardCharsets.UTF_8.toString());
		String encodedto = URLEncoder.encode(cb.getTok(), StandardCharsets.UTF_8.toString());
		String encodedfrom = URLEncoder.encode(cb.getFromk(), StandardCharsets.UTF_8.toString());
		String encodedpdu = URLEncoder.encode(cb.getPdu(), StandardCharsets.UTF_8.toString());
		String encodedDlvStat = URLEncoder.encode(cb.getDeliverystatus(), StandardCharsets.UTF_8.toString());
		String encodedDlvDate = URLEncoder.encode(cb.getDeliverydt(), StandardCharsets.UTF_8.toString());
		String encodedDesc = URLEncoder.encode(cb.getDescription(), StandardCharsets.UTF_8.toString());

		// Add parameters dynamically
		values.put("corelationId", encodedCorelationid);
		values.put("txid", encodedtxid);
		values.put("to", encodedto);
		values.put("from", encodedfrom);
		values.put("pdu", encodedpdu);
		values.put("deliverystatus", encodedDlvStat);
		values.put("deliverydt", encodedDlvDate);
		values.put("description", encodedDesc);

		values.put("text", encodedText);

		// Use regex to find placeholders in {curly braces}
		Pattern pattern = Pattern.compile("\\{(\\w+)}");
		Matcher matcher = pattern.matcher(url);
		StringBuffer result = new StringBuffer();

		while (matcher.find()) {
			String key = matcher.group(1); // Extract key inside {}
			String value = values.getOrDefault(key, ""); // Get value or empty if not found
			matcher.appendReplacement(result, value != null ? value : "");
		}
		matcher.appendTail(result);

		return result.toString();
	}
	
	
	
}
