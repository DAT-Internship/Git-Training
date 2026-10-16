package com.dat.cms.controller;

import com.dat.cms.model.Payment;
import com.dat.cms.repository.PaymentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
	private final PaymentRepository repository;

	public PaymentController(PaymentRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Payment> all() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Payment one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id));
	}

	@PostMapping
	public Payment create(@Valid @RequestBody Payment item) {
		item.setId(null);
		return repository.save(item);
	}

	@PutMapping("/{id}")
	public Payment update(@PathVariable Long id, @Valid @RequestBody Payment item) {
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
