package com.dat.cms.controller;

import com.dat.cms.model.*;
import com.dat.cms.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class WorkflowController {
	private final ContractRepository contracts;
	private final ApprovalRepository approvals;
	private final CmsNotificationRepository notifications;

	public WorkflowController(ContractRepository c, ApprovalRepository a, CmsNotificationRepository n) {
		contracts = c;
		approvals = a;
		notifications = n;
	}

	@PostMapping("/contracts/{id}/submit")
	public Contract submit(@PathVariable Long id) {
		Contract c = contracts.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found"));
		c.setStatus("PENDING_APPROVAL");
		contracts.save(c);
		Approval a = new Approval();
		a.setContractId(c.getId());
		a.setContractNo(c.getContractNo());
		a.setSubmitter(c.getOwner());
		a.setStage("PENDING");
		approvals.save(a);
		notify("Contract submitted", c.getContractNo() + " was submitted for approval", "APPROVAL");
		return c;
	}

	@PostMapping("/approvals/{id}/decision")
	public Approval decision(@PathVariable Long id, @RequestBody Map<String, String> body) {
		Approval a = approvals.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Approval not found"));
		String decision = body.getOrDefault("decision", "").toUpperCase();
		if (!decision.equals("APPROVE") && !decision.equals("REJECT"))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "decision must be APPROVE or REJECT");
		a.setDecision(decision);
		a.setStage(decision.equals("APPROVE") ? "APPROVED" : "REJECTED");
		a.setComment(body.get("comment"));
		a.setDecidedAt(LocalDateTime.now());
		approvals.save(a);
		contracts.findById(a.getContractId()).ifPresent(c -> {
			c.setStatus(decision.equals("APPROVE") ? "ACTIVE" : "REJECTED");
			contracts.save(c);
		});
		notify("Approval " + a.getStage(), a.getContractNo() + " was " + decision.toLowerCase(), "APPROVAL");
		return a;
	}

	private void notify(String title, String message, String type) {
		CmsNotification n = new CmsNotification();
		n.setTitle(title);
		n.setMessage(message);
		n.setType(type);
		notifications.save(n);
	}
}
