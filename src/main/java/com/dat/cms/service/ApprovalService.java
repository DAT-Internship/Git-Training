package com.dat.cms.service;

import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApprovalService {
	private final JdbcTemplate db;

	public ApprovalService(JdbcTemplate db) {
		this.db = db;
	}

	public List<Map<String, Object>> all() {
		return db.queryForList(
				"SELECT c.contract_id,c.contract_no,c.description,c.contract_value,c.register_date,st.name status_name,s.stage_name,d.dept_name,p.party_name FROM contracts c JOIN master_contract_status st ON st.status_id=c.status_id LEFT JOIN workflow_stages s ON s.stage_id=c.current_stage_id JOIN departments d ON d.department_id=c.department_id JOIN customers_suppliers p ON p.party_id=c.party_id ORDER BY c.contract_id DESC");
	}

	public List<Map<String, Object>> pending(String role) {
		return db.queryForList(
				"SELECT c.contract_id,c.contract_no,c.description,c.contract_value,c.register_date,s.stage_name,d.dept_name,p.party_name FROM contracts c JOIN master_contract_status st ON st.status_id=c.status_id JOIN workflow_stages s ON s.stage_id=c.current_stage_id JOIN roles r ON r.role_id=s.required_role_id JOIN departments d ON d.department_id=c.department_id JOIN customers_suppliers p ON p.party_id=c.party_id WHERE st.code='UNDER_REVIEW' AND r.role_code=? ORDER BY c.register_date",
				role);
	}

	@Transactional
	public void submit(long id, String username) {
		Map<String, Object> c = lock(id);
		if (!"DRAFT".equals(status(((Number) c.get("status_id")).intValue())))
			throw new IllegalStateException("Only Draft contracts can be submitted.");
		int stage = db.queryForObject("SELECT stage_id FROM workflow_stages WHERE stage_order=1 AND is_active=1",
				Integer.class);
		db.update("UPDATE contracts SET status_id=?,current_stage_id=? WHERE contract_id=?", statusId("UNDER_REVIEW"),
				stage, id);
		db.update(
				"INSERT INTO approval_steps(contract_id,stage_id,decision,created_at) VALUES(?,?,'PENDING',CURRENT_TIMESTAMP)",
				id, stage);
		audit(username, "SUBMITTED", id, null, "{\"status\":\"UNDER_REVIEW\",\"stage_order\":1}");
	}

	@Transactional
	public void approve(long id, String username, String comments) {
		Map<String, Object> c = lock(id);
		if (!"UNDER_REVIEW".equals(status(((Number) c.get("status_id")).intValue())))
			throw new IllegalStateException("Contract is not awaiting approval.");
		Integer stageId = (Integer) c.get("current_stage_id");
		if (stageId == null)
			throw new IllegalStateException("No current stage.");
		Map<String, Object> s = db.queryForMap(
				"SELECT s.stage_order,r.role_code FROM workflow_stages s JOIN roles r ON r.role_id=s.required_role_id WHERE s.stage_id=? AND s.is_active=1",
				stageId);
		requireRole(username, (String) s.get("role_code"));
		int changed = db.update(
				"UPDATE approval_steps SET decision='APPROVED',comments=?,action_at=CURRENT_TIMESTAMP WHERE contract_id=? AND stage_id=? AND decision='PENDING'",
				comments, id, stageId);
		if (changed != 1)
			throw new IllegalStateException("No pending approval for this stage.");
		int order = ((Number) s.get("stage_order")).intValue();
		List<Map<String, Object>> next = db.queryForList(
				"SELECT stage_id FROM workflow_stages WHERE stage_order>? AND is_active=1 AND is_required=1 ORDER BY stage_order LIMIT 1",
				order);
		if (next.isEmpty()) {
			db.update("UPDATE contracts SET status_id=?,current_stage_id=NULL WHERE contract_id=?",
					statusId("APPROVED"), id);
			audit(username, "APPROVED", id, "{\"status\":\"UNDER_REVIEW\"}", "{\"status\":\"APPROVED\"}");
		} else {
			int nextId = ((Number) next.get(0).get("stage_id")).intValue();
			db.update("UPDATE contracts SET current_stage_id=? WHERE contract_id=?", nextId, id);
			db.update(
					"INSERT INTO approval_steps(contract_id,stage_id,decision,created_at) VALUES(?,?,'PENDING',CURRENT_TIMESTAMP)",
					id, nextId);
			audit(username, "STAGE_APPROVED", id, "{\"stage_id\":" + stageId + "}",
					"{\"next_stage_id\":" + nextId + "}");
		}
	}

	@Transactional
	public void reject(long id, String username, String reason) {
		if (reason == null || reason.isBlank())
			throw new IllegalArgumentException("Rejection reason is required.");
		Map<String, Object> c = lock(id);
		if (!"UNDER_REVIEW".equals(status(((Number) c.get("status_id")).intValue())))
			throw new IllegalStateException("Contract is not awaiting approval.");
		Integer stageId = (Integer) c.get("current_stage_id");
		if (stageId == null)
			throw new IllegalStateException("No current stage.");
		String role = db.queryForObject(
				"SELECT r.role_code FROM workflow_stages s JOIN roles r ON r.role_id=s.required_role_id WHERE s.stage_id=?",
				String.class, stageId);
		requireRole(username, role);
		int changed = db.update(
				"UPDATE approval_steps SET decision='REJECTED',comments=?,rejection_reason=?,action_at=CURRENT_TIMESTAMP WHERE contract_id=? AND stage_id=? AND decision='PENDING'",
				reason, reason, id, stageId);
		if (changed != 1)
			throw new IllegalStateException("No pending approval for this stage.");
		int uid = db.queryForObject("SELECT user_id FROM users WHERE username=?", Integer.class, username);
		db.update(
				"UPDATE contracts SET status_id=?,current_stage_id=NULL,rejected_by=?,rejected_at=CURRENT_TIMESTAMP,rejection_reason=? WHERE contract_id=?",
				statusId("REJECTED"), uid, reason, id);
		audit(username, "REJECTED", id, "{\"status\":\"UNDER_REVIEW\",\"stage_id\":" + stageId + "}",
				"{\"status\":\"REJECTED\",\"reason\":\"" + escape(reason) + "\"}");
	}

	private Map<String, Object> lock(long id) {
		return db.queryForMap(
				"SELECT contract_id,status_id,current_stage_id FROM contracts WHERE contract_id=? FOR UPDATE", id);
	}

	private void requireRole(String username, String role) {
		Integer n = db.queryForObject(
				"SELECT COUNT(*) FROM users u JOIN user_roles ur ON ur.user_id=u.user_id JOIN roles r ON r.role_id=ur.role_id WHERE u.username=? AND r.role_code=? AND u.is_active=1",
				Integer.class, username, role);
		if (n == null || n == 0)
			throw new SecurityException("Your account cannot approve this stage.");
	}

	private int statusId(String code) {
		return db.queryForObject("SELECT status_id FROM master_contract_status WHERE code=?", Integer.class, code);
	}

	private String status(int id) {
		return db.queryForObject("SELECT code FROM master_contract_status WHERE status_id=?", String.class, id);
	}

	private void audit(String username, String action, long id, String oldJson, String newJson) {
		Integer uid = db.queryForObject("SELECT user_id FROM users WHERE username=?", Integer.class, username);
		db.update(
				"INSERT INTO audit_logs(user_id,action,entity_type,entity_id,old_values,new_values,created_at) VALUES(?,?,'CONTRACT',?,CAST(? AS JSON),CAST(? AS JSON),CURRENT_TIMESTAMP)",
				uid, action, String.valueOf(id), oldJson, newJson);
	}

	private String escape(String s) {
		if (s == null) {
			return "";
		}

		return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
	}

}