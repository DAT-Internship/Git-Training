package com.dat.cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true, nullable = false)
	private String contractNo;
	@NotBlank
	private String customer;
	private String category;
	private String projectId;
	private LocalDate startDate;
	private LocalDate endDate;
	@DecimalMin("0.0")
	private BigDecimal value;
	private String currency = "MMK";
	private String status = "DRAFT";
	private String owner;
	private String description;
	private Integer renewalNoticeDays = 30;

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String v) {
		contractNo = v;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String v) {
		customer = v;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String v) {
		category = v;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String v) {
		projectId = v;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate v) {
		startDate = v;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate v) {
		endDate = v;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal v) {
		value = v;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String v) {
		currency = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		status = v;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String v) {
		owner = v;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String v) {
		description = v;
	}

	public Integer getRenewalNoticeDays() {
		return renewalNoticeDays;
	}

	public void setRenewalNoticeDays(Integer v) {
		renewalNoticeDays = v;
	}
}
