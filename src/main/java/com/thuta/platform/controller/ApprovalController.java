package com.thuta.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ApprovalController {
	@GetMapping("/about")
	public String approval() {
		return "about";
	}
	
}