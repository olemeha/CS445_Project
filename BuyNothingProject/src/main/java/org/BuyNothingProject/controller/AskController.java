package org.BuyNothingProject.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Ask;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.AskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/asks")
public class AskController {
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	AskRepository askRepo;
	/*
	 * List Asks
	 */

	@GetMapping("{aid}")
	public ResponseEntity viewAsk(@PathVariable int aid) {
		Ask ask = askRepo.getAskById(aid);
		if (ask != null) {
			return ResponseEntity.ok().body(ask);
		} else {
			return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).build();
		}
	}

	/*
	 * view all asks
	 */
	public List<Ask> vieAllAsks(Integer v_by, Boolean isActive) {
		List<Ask> allAsks = askRepo.getAllAsks();
		Account acc = accountRepo.getAccountById(v_by);
		if (acc != null) {

			List<Ask> matches = new ArrayList();
			for (Ask ask : allAsks) {
				if (ask.getExtraZip().contains(acc.getAddress().getZip()) || ask.getUid() == acc.getUid()
						|| acc.getName().intern() == "CSR #1") {
					if (isActive != null) {
						if (ask.getIsActive() == isActive)
							matches.add(ask);
					} else {
						matches.add(ask);
					}
				}

			}
			return matches;

		}
		return null;
	}

	/*
	 * Search Accounts
	 */
	@GetMapping
	public List<Ask> searchAsks(@RequestParam(required = false) Integer v_by,
			@RequestParam(required = false, name = "is_active") Boolean isActive,
			@RequestParam(required = false) String key, @RequestParam(required = false) String start_date,
			@RequestParam(required = false) String end_date) {

		if (v_by != null) {
			return vieAllAsks(v_by, isActive);
		}
		if (key == null && end_date == null && start_date == null) {
			return askRepo.getAllAsks();
		}
		List<Ask> matches = new ArrayList<Ask>();
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
		for (Ask ask : askRepo.getAllAsks()) {
			try {
				String accountJson = ow.writeValueAsString(ask).toLowerCase().intern();
				if (accountJson.contains(key.toLowerCase().intern())) {
					if (start_date != null && end_date != null) {
						String expectedDatePattern = "dd-MMM-yyyy";
						Instant start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						Instant end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						if (ask.getDateCreated().compareTo(start) >= 0 && ask.getDateCreated().compareTo(end) <= 0) {
							matches.add(ask);
						}
					} else {
						matches.add(ask);
					}
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
		return matches;
	}
}
