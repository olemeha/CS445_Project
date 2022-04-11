package org.BuyNothingProject.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/reports")
public class ReportsController {
	@GetMapping
	public ResponseEntity getReports() {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
	}

	@GetMapping("{rid}")
	public ResponseEntity getReportsWithId(@PathVariable int rid) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
	}
}
