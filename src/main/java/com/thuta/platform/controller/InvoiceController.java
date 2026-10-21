package com.thuta.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class InvoiceController {

	@GetMapping("/status")
	public String status() {
		return "payment";
	}
}
