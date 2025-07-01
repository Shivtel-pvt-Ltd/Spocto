package com.SmsCallback.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumberExtractor {

	public static void main(String[] args) {
		
		 String input = "error Code [false], Error Text [641]  ";
		
//		String input ="Success";
		 
		 if (input.toLowerCase().contains("error".toLowerCase())) {
			 String regex = "\\d+";  
		        
		        Map<String, String> errorDescriptionMap = new HashMap<>();
		        errorDescriptionMap.put("642","Failed due to network congestion" );
		        errorDescriptionMap.put("897","Failed due to bandwidth congestion" );
		        errorDescriptionMap.put("876","Failed due to signal congestion" );
		        
		        String customErrorDescriptionWithCodeString="";
		        
		        
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher(input);
		        
		        List<Integer> numbers = new ArrayList<>();
		        
		        while (matcher.find()) {
		        	
		        	String errorCodeString = matcher.group();
		        	System.out.println("matcher.group() : "+ errorCodeString);
		        	
		        	String errorDescription =  errorDescriptionMap.get(matcher.group());
		        	
		        	if(errorDescription!=null)
		        	{
			        	customErrorDescriptionWithCodeString+=errorDescription;
		        	}
		        	
		        	
		            numbers.add(Integer.parseInt(matcher.group()));
		        }
		        
		        
		        if (!numbers.isEmpty()&&customErrorDescriptionWithCodeString.isEmpty()) {
		        	customErrorDescriptionWithCodeString=input;
					System.out.println(customErrorDescriptionWithCodeString.length());
				}
		        
		        System.out.println(customErrorDescriptionWithCodeString.length());
		        System.out.println("Extracted Numbers: " + numbers);
		        System.out.println("input.toLowerCase().contains(\"error\".toLowerCase()) " + input.toLowerCase().contains("error".toLowerCase()));
		        System.out.println("Custom Error: "+ customErrorDescriptionWithCodeString);
		        
		        

		}
	       
	}

}
