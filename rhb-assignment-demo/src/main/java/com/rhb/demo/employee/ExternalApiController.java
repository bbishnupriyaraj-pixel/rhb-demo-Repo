package com.rhb.demo.employee;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
public class ExternalApiController {
	private final ExternalApiService service;

	public ExternalApiController(ExternalApiService service) {
		this.service = service;
	}

	@GetMapping("/google-check")
	public ExternalApiService.ExternalApiResult googleCheck() {
		return service.checkGoogle();
	}
}
