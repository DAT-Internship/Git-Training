package com.thuta.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Notification {
	@GetMapping("/")
	public String noti() {
		return "noti";
}
}
