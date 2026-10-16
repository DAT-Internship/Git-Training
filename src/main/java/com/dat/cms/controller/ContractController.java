package com.dat.cms.controller;

import com.dat.cms.model.Contract;
import com.dat.cms.repository.ContractRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractController {
	private final ContractRepository repository;

	public ContractController(ContractRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Contract> all() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Contract one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id));
	}

	@PostMapping
	public Contract create(@Valid @RequestBody Contract item) {
		item.setId(null);
		return repository.save(item);
	}

	@PutMapping("/{id}")
	public Contract update(@PathVariable Long id, @Valid @RequestBody Contract item) {
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
