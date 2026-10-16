package com.dat.cms.controller;

import com.dat.cms.model.Approval;
import com.dat.cms.repository.ApprovalRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@CrossOrigin(origins = "*")
public class ApprovalController {
	private final ApprovalRepository repository;

	public ApprovalController(ApprovalRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Approval> all() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Approval one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id));
	}

	@PostMapping
	public Approval create(@Valid @RequestBody Approval item) {
		item.setId(null);
		return repository.save(item);
	}

	@PutMapping("/{id}")
	public Approval update(@PathVariable Long id, @Valid @RequestBody Approval item) {
		if (!repository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id);
		item.setId(id);
		return repository.save(item);
	}

    @PutMapping("/{id}/approve")
    public Approval approve(@PathVariable Long id) {
        Approval item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Record not found: " + id));

        if (!"PENDING".equalsIgnoreCase(item.getStage())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "This approval has already been processed");
        }

        item.setStage("APPROVED");
        item.setDecision("APPROVED");
        item.setDecidedAt(LocalDateTime.now());

        return repository.save(item);
    }

    @PutMapping("/{id}/reject")
    public Approval reject(@PathVariable Long id) {
        Approval item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Record not found: " + id));

        if (!"PENDING".equalsIgnoreCase(item.getStage())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "This approval has already been processed");
        }

        item.setStage("REJECTED");
        item.setDecision("REJECTED");
        item.setDecidedAt(LocalDateTime.now());

        return repository.save(item);
    }

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		if (!repository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id);
		repository.deleteById(id);
	}
}
