package com.dat.cms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_records")
public class EmailRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String sender;
	private String subject;
	private String projectId;
	private String classification = "UNCLASSIFIED";
	private String status = "NEW";
	private LocalDateTime receivedAt = LocalDateTime.now();
	@Column(length = 3000)
	private String body;

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String v) {
		sender = v;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String v) {
		subject = v;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String v) {
		projectId = v;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String v) {
		classification = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		status = v;
	}

	public LocalDateTime getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(LocalDateTime v) {
		receivedAt = v;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String v) {
		body = v;
	}
}
