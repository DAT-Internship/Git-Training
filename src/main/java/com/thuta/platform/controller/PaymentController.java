package com.thuta.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

	
	@GetMapping("/payment")
	public String payment() {
		return "payment";
	}
	
	
	@GetMapping("/status")
	public String status() {
		return "payment";
	}
	
	
	
}
