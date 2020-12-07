package br.com.mashup.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

public class HttpUtilities {
	
	private static HttpUtilities httpUtilities;
	
	private HttpHeaders headers;
	
	private Map<String, String> headerParams;
	
	private HttpUtilities() {}
	
	public static HttpUtilities getInstance() {
		if(httpUtilities == null)
			httpUtilities = new HttpUtilities();
		return httpUtilities;
	}
	
	public HttpHeaders buildHeadersInstance() {
		headers = new HttpHeaders();
		for(Map.Entry<String, String> param : headerParams.entrySet()) {
			headers.set(param.getKey(), param.getValue());			
		}
		return headers;
	}
	
	public Map<String, String> getHeaderParamsInstance() {
		headerParams = new HashMap<>();
		return headerParams;
	}
}
