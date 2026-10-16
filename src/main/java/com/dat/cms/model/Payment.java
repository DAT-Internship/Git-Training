package com.dat.cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String invoiceNo;
	@NotNull
	private Long contractId;
	private String customer;
	@NotNull
	private LocalDate dueDate;
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal amount;
	private String status = "PENDING";
	private BigDecimal outstanding;
	private LocalDate paidDate;
	private String notes;

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String v) {
		invoiceNo = v;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long v) {
		contractId = v;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String v) {
		customer = v;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate v) {
		dueDate = v;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal v) {
		amount = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		status = v;
	}

	public BigDecimal getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(BigDecimal v) {
		outstanding = v;
	}

	public LocalDate getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDate v) {
		paidDate = v;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String v) {
		notes = v;
	}
}
