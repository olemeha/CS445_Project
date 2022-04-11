package org.BuyNothingProject.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.BuyNothingProject.domain.Thanks;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.ThanksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@RestController
@RequestMapping("/thanks")
public class ThanksController {
	@Autowired
	AccountRepository accountRepo;

	@Autowired
	ThanksRepository thanksRepo;
	/*
	 * List Thanks
	 */

	@GetMapping("received/{uid}")
	public ResponseEntity viewThanksReceived(@PathVariable int uid) {
		List<Thanks> thanksList = accountRepo.getAccountById(uid).getThankedAccounts();
		if (thanksList != null) {
			return ResponseEntity.ok().body(thanksList);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * Search Thanks
	 */
	@GetMapping
	public List<Thanks> searchThanks(@RequestParam(required = false) String key,
			@RequestParam(required = false) String start_date, @RequestParam(required = false) String end_date) {

		if (key == null && end_date == null && start_date == null) {
			return thanksRepo.getAllThanks();
		}
		List<Thanks> matches = new ArrayList();
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
		for (Thanks thanks : thanksRepo.getAllThanks()) {
			try {
				String accountJson = ow.writeValueAsString(thanks).toLowerCase().intern();
				if (accountJson.contains(key.toLowerCase().intern())) {
					if (start_date != null && end_date != null) {
						String expectedDatePattern = "dd-MMM-yyyy";
						Instant start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						Instant end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						if (thanks.getDateCreated().compareTo(start) >= 0
								&& thanks.getDateCreated().compareTo(end) <= 0) {
							matches.add(thanks);
						}
					} else {
						matches.add(thanks);
					}
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
		return matches;
	}
}
