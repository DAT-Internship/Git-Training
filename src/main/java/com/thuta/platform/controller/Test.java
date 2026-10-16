package com.thuta.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Test {

	@GetMapping("/")
	public String home() {
		return "home";
	}
}
