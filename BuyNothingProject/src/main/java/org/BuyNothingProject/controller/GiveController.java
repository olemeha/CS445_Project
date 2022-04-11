package org.BuyNothingProject.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Give;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.GiveRepository;
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
@RequestMapping("/gives")
public class GiveController {
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	GiveRepository giveRepo;
	/*
	 * List Gives
	 */

	@GetMapping("{gid}")
	public ResponseEntity viewGive(@PathVariable int gid) {
		Give give = giveRepo.getGiveById(gid);
		if (give != null) {
			return ResponseEntity.ok().body(give);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * view all gives
	 */
	public List<Give> vieAllGives(Integer v_by, Boolean isActive) {
		List<Give> allGives = giveRepo.getAllGives();
		Account acc = accountRepo.getAccountById(v_by);
		if (acc != null) {
			List<Give> matches = new ArrayList();
			for (Give give : allGives) {
				if (give.getExtraZip().contains(acc.getAddress().getZip()) || give.getUid() == acc.getUid()
						|| acc.getName().intern() == "CSR #1") {
					if (isActive != null) {
						if (give.getIsActive() == isActive)
							matches.add(give);
					} else {
						matches.add(give);
					}
				}
			}
			return matches;
		}
		return null;
	}

	/*
	 * Search Gives
	 */
	@GetMapping
	public List<Give> searchGives(@RequestParam(required = false) Integer v_by,
			@RequestParam(required = false, name = "is_active") Boolean isActive,
			@RequestParam(required = false) String key, @RequestParam(required = false) String start_date,
			@RequestParam(required = false) String end_date) {

		if (v_by != null) {
			return vieAllGives(v_by, isActive);
		}
		if (key == null && end_date == null && start_date == null) {
			return giveRepo.getAllGives();
		}
		List<Give> matches = new ArrayList<Give>();
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
		for (Give give : giveRepo.getAllGives()) {
			try {
				String accountJson = ow.writeValueAsString(give).toLowerCase().intern();
				if (accountJson.contains(key.toLowerCase().intern())) {
					if (start_date != null && end_date != null) {
						String expectedDatePattern = "dd-MMM-yyyy";
						Instant start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						Instant end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						if (give.getDateCreated().compareTo(start) >= 0 && give.getDateCreated().compareTo(end) <= 0) {
							matches.add(give);
						}
					} else {
						matches.add(give);
					}
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
		return matches;
	}
}
