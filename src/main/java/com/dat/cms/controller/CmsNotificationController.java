package com.dat.cms.controller;

import com.dat.cms.model.CmsNotification;
import com.dat.cms.repository.CmsNotificationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class CmsNotificationController {
	private final CmsNotificationRepository repository;

	public CmsNotificationController(CmsNotificationRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<CmsNotification> all() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public CmsNotification one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found: " + id));
	}

	@PostMapping
	public CmsNotification create(@Valid @RequestBody CmsNotification item) {
		item.setId(null);
		return repository.save(item);
	}

	@PutMapping("/{id}")
	public CmsNotification update(@PathVariable Long id, @Valid @RequestBody CmsNotification item) {
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
