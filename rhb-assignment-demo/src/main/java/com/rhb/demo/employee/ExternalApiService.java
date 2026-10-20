package com.rhb.demo.employee;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalApiService {
	private final RestClient client;

	public ExternalApiService(RestClient.Builder builder) {
		client = builder.baseUrl("https://www.google.com").build();
	}

	public ExternalApiResult checkGoogle() {
		String body = client.get().uri("/").retrieve().body(String.class);
		return new ExternalApiResult("https://www.google.com", body == null ? 0 : body.length());
	}

	public record ExternalApiResult(String url, int responseBodyLength) {
	}
}
