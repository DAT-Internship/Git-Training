package com.dat.cms.controller;

import com.dat.cms.model.EmailRecord;
import com.dat.cms.repository.EmailRecordRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*")
public class EmailRecordController {
	private final EmailRecordRepository repository;

	public EmailRecordController(EmailRecordRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<EmailRecord> all() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public EmailRecord one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id));
	}

	@PostMapping
	public EmailRecord create(@Valid @RequestBody EmailRecord item) {
		item.setId(null);
		return repository.save(item);
	}

	@PutMapping("/{id}")
	public EmailRecord update(@PathVariable Long id, @Valid @RequestBody EmailRecord item) {
		if (!repository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id);
		item.setId(id);
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
