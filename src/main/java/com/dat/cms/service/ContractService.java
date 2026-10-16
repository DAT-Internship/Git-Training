package com.dat.cms.service;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractService {
	private final JdbcTemplate db;

	public ContractService(JdbcTemplate db) {
		this.db = db;
	}

	public List<Map<String, Object>> departments() {
		return db.queryForList("SELECT department_id,dept_name FROM departments WHERE is_active=1 ORDER BY dept_name");
	}

	public List<Map<String, Object>> parties() {
		return db.queryForList(
				"SELECT party_id,party_name FROM customers_suppliers WHERE is_active=1 ORDER BY party_name");
	}

	public List<Map<String, Object>> categories() {
		return db.queryForList("SELECT category_id,name FROM master_contract_category WHERE is_active=1 ORDER BY name");
	}

	public List<Map<String, Object>> types() {
		return db
				.queryForList("SELECT contract_type_id,name FROM master_contract_type WHERE is_active=1 ORDER BY name");
	}

	public List<Map<String, Object>> currencies() {
		return db.queryForList("SELECT currency_id,code,name FROM master_currency WHERE is_active=1 ORDER BY code");
	}

	@Transactional
	public long create(String no, String desc, int dept, int party, int category, int type, int currency,
			BigDecimal value, String username) {

		Integer exists = db.queryForObject("SELECT COUNT(*) FROM contracts WHERE contract_no = ?", Integer.class, no);

		if (exists != null && exists > 0) {
			throw new IllegalArgumentException("Contract number already exists.");
		}

		String cat = db.queryForObject("SELECT code FROM master_contract_category WHERE category_id = ?", String.class,
				category);

		Integer agreement = db.queryForObject("SELECT agreement_for_id FROM master_agreement_for WHERE code = ?",
				Integer.class, "REVENUE".equalsIgnoreCase(cat) ? "REVENUE" : "EXPENSE");

		int status = db.queryForObject("SELECT status_id FROM master_contract_status WHERE code = 'DRAFT'",
				Integer.class);

		int uid = db.queryForObject("SELECT user_id FROM users WHERE username = ?", Integer.class, username);

		db.update(
				"INSERT INTO contracts(" + "contract_no, agreement_for_id, category_id, contract_type_id, "
						+ "status_id, department_id, party_id, description, currency_id, "
						+ "contract_value, created_by, register_date, created_at, updated_at"
						+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
						+ "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
				no, agreement, category, type, status, dept, party, desc, currency, value, uid);

		Long id = db.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

		// Build JSON safely for audit logging
		 String json = "{\"contract_no\":\""
	                + escape(no)
	                + "\",\"status\":\"DRAFT\"}";
		db.update(
				"INSERT INTO audit_logs("
						+ "user_id, action, entity_type, entity_id, old_values, new_values, created_at"
						+ ") VALUES (?, 'CREATED', 'CONTRACT', ?, NULL, CAST(? AS JSON), CURRENT_TIMESTAMP)",
				uid, String.valueOf(id), json);

		return id;
	}
	private String escape(String s) {
	    if (s == null) {
	        return "";
	    }

	    return s.replace("\\", "\\\\")
	            .replace("\"", "\\\"")
	            .replace("\n", "\\n")
	            .replace("\r", "\\r");
	}
}