package com.dat.cms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class CmsNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@Column(length = 2000)
	private String message;
	private String type = "INFO";
	private boolean readFlag = false;
	private LocalDateTime createdAt = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String v) {
		title = v;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String v) {
		message = v;
	}

	public String getType() {
		return type;
	}

	public void setType(String v) {
		type = v;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setReadFlag(boolean v) {
		readFlag = v;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime v) {
		createdAt = v;
	}
}
