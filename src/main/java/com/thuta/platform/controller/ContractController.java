package com.thuta.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ContractController {
	@GetMapping("/")
	public String contract() {
		return "contract";
}
}