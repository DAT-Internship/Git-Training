package com.dat.cms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approvals")
public class Approval {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long contractId;
	private String contractNo;
	private String submitter;
	private String stage = "PENDING";
	private String priority = "NORMAL";
	private String decision;
	private String comment;
	private LocalDateTime submittedAt = LocalDateTime.now();
	private LocalDateTime decidedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long v) {
		contractId = v;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String v) {
		contractNo = v;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String v) {
		submitter = v;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String v) {
		stage = v;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String v) {
		priority = v;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String v) {
		decision = v;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String v) {
		comment = v;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime v) {
		submittedAt = v;
	}

	public LocalDateTime getDecidedAt() {
		return decidedAt;
	}

	public void setDecidedAt(LocalDateTime v) {
		decidedAt = v;
	}
}
