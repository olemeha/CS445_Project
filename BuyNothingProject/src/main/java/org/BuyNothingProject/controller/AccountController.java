package org.BuyNothingProject.controller;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Address;
import org.BuyNothingProject.messages.ConstraintViolationMessage;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.GiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	NoteController noteController;

	/*
	 * Create Account
	 */

	@PostMapping
	public ResponseEntity createAccount(@RequestBody Account account) throws Exception {
		try {
			account = accountRepo.save(account);
			account = accountRepo.getAccountById(account.getUid());
			return ResponseEntity.created(new URI("/accounts/" + String.valueOf(account.getUid()))).body(account);
		} catch (Exception e) {
			if (e.getCause() instanceof RollbackException) {
				ConstraintViolationMessage msg = ConstraintViolationMessage.getViolationBody(e);
				msg.setInstance("/accounts");
				return ResponseEntity.badRequest().body(msg);
			} else {
				throw e;
			}
		}
	}
	/*
	 * Activate Account
	 */

	@GetMapping("{uid}/activate")
	public ResponseEntity activateAccount(@PathVariable int uid) {
		Account ac = accountRepo.getAccountById(uid);
		if (ac == null) {
			return ResponseEntity.notFound().build();
		}
		ac.setIsActive(true);
		accountRepo.save(ac);
		return ResponseEntity.ok().body(ac);
	}

	/*
	 * Update account
	 */
	@PutMapping("{uid}")
	public ResponseEntity updateAccount(@PathVariable int uid, @RequestBody Account updatedAcc) {
		String uri = "/accounts/" + String.valueOf(uid);
		if (updatedAcc.getUid() != uid) {
			ConstraintViolationMessage ex = new ConstraintViolationMessage();
			ex.setStatus(400);
			ex.setDetail("This account (" + String.valueOf(uid) + ") is not allowed to modify account ("
					+ String.valueOf(updatedAcc.getUid()) + ")");
			return ResponseEntity.badRequest().body(ex);
		}
		Account acc = accountRepo.getAccountById(uid);
		if (acc.getUid() == updatedAcc.getUid()) {
			if (acc.getIsActive() != updatedAcc.getIsActive()) {
				ConstraintViolationMessage msg = new ConstraintViolationMessage();
				msg.setInstance(uri);
				msg.setStatus(400);
				msg.setTitle("Your request data didn't pass validation");
				msg.setDetail("You may not use PUT to activate an account, use GET /accounts/" + String.valueOf(uid)
						+ "/activate instead");
				return ResponseEntity.badRequest().body(msg);
			}
			acc.setPicture(updatedAcc.getPicture());
			acc.setPhone(updatedAcc.getPhone());
			acc.setName(updatedAcc.getName());
			Address addr = acc.getAddress();
			Address newAddr = updatedAcc.getAddress();
			addr.setStreet(newAddr.getStreet());
			addr.setZip(newAddr.getZip());
			try {
				accountRepo.save(acc);
			} catch (Exception e) {
				if (e.getCause() instanceof RollbackException) {
					ConstraintViolationMessage msg = ConstraintViolationMessage.getViolationBody(e);
					msg.setInstance(uri);
					return ResponseEntity.badRequest().body(msg);
				} else {
					throw e;
				}
			}
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * Delete Account
	 */
	@DeleteMapping("{uid}")
	public ResponseEntity deleteAccount(@PathVariable int uid) {
		Account acc = accountRepo.getAccountById(uid);
		if (acc != null) {
			noteController.deleteNotesById(uid, null, null);
			accountRepo.delete(acc);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * List Accounts
	 */

	@GetMapping("{uid}")
	public ResponseEntity getAccount(@PathVariable int uid) {
		Account acc = accountRepo.getAccountById(uid);
		if (acc != null) {
			return ResponseEntity.ok().body(acc);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * Search Accounts
	 */
	@GetMapping
	public List<Account> searchAccounts(@RequestParam(required = false) String key,
			@RequestParam(required = false) String start_date, @RequestParam(required = false) String end_date) {
		if (key == null && end_date == null && start_date == null) {
			return accountRepo.getAllAccounts();
		}
		List<Account> matches = new ArrayList<Account>();
		ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
		for (Account account : accountRepo.getAllAccounts()) {
			try {
				String accountJson = ow.writeValueAsString(account).toLowerCase();
				if (accountJson.contains(key)) {
					if (start_date != null && end_date != null) {
						String expectedDatePattern = "dd-MMM-yyyy";
						Instant start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						Instant end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern(expectedDatePattern))
								.atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant();
						if (account.getDateCreated().compareTo(start) >= 0
								&& account.getDateCreated().compareTo(end) <= 0) {
							matches.add(account);
						}
					} else {
						matches.add(account);
					}
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
		return matches;
	}
}
