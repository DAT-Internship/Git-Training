package com.dat.cms.controller;

import com.dat.cms.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
	private final ContractRepository contracts;
	private final PaymentRepository payments;
	private final ApprovalRepository approvals;
	private final CmsNotificationRepository notifications;

	public DashboardController(ContractRepository c, PaymentRepository p, ApprovalRepository a,
			CmsNotificationRepository n) {
		contracts = c;
		payments = p;
		approvals = a;
		notifications = n;
	}

	@GetMapping
	public Map<String, Object> summary() {
		return Map.of("totalContracts", contracts.count(), "totalPayments", payments.count(), "pendingApprovals",
				approvals.findAll().stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStage())).count(),
				"notifications", notifications.count());
	}
}
