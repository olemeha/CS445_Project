package org.BuyNothingProject.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Thanks;
import org.BuyNothingProject.messages.ConstraintViolationMessage;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.ThanksRepository;
import org.BuyNothingProject.web.models.ThanksModel;
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

@Component
@RestController
@RequestMapping("/accounts/{uid}/thanks")
public class UserThanksController {

	@Autowired
	AccountRepository accountRepo;
	@Autowired
	ThanksRepository thanksRepo;

	/*
	 * Create Thanks
	 */

	@PostMapping
	public ResponseEntity createThanks(@PathVariable int uid, @RequestBody ThanksModel thanks) throws Exception {
		String uri = "/accounts/" + String.valueOf(uid) + "/thanks/";
		Thanks obj = new Thanks();
		Account userAccount = accountRepo.getAccountById(uid);
		Account thankToAccount = accountRepo.getAccountById(thanks.getThank_to());
		if (userAccount != null && thankToAccount != null) {
			obj.setUserAccount(userAccount);
			obj.setThankedAccount(thankToAccount);
			obj.setDescription(thanks.getDescription());
			try {
				thanksRepo.save(obj);
				return ResponseEntity.created(new URI(uri + String.valueOf(obj.getTid()))).body(obj);
			} catch (Exception e) {
				if (e.getCause() instanceof RollbackException) {
					ConstraintViolationMessage msg = ConstraintViolationMessage.getViolationBody(e);
					msg.setInstance(uri);
					return ResponseEntity.badRequest().body(msg);
				} else {
					throw e;
				}
			}
		}
		return null;

	}

	/*
	 * Update thanks
	 */
	@PutMapping("{tid}")
	public ResponseEntity updateThanks(@PathVariable int uid, @PathVariable int tid,
			@RequestBody ThanksModel newThanks) {
		String uri = "/accounts/" + String.valueOf(uid) + "/thanks/" + String.valueOf(tid);
		Thanks thanks = thanksRepo.getThankById(tid);
		if (newThanks.getUid() != thanks.getThankedUid()) {
			ConstraintViolationMessage ex = new ConstraintViolationMessage();
			ex.setStatus(400);
			ex.setInstance(uri);
			ex.setDetail("This account (" + String.valueOf(uid) + ") did not create thanks ("
					+ String.valueOf(thanks.getTid()) + " and is not allowed to update it)");
			return ResponseEntity.badRequest().body(ex);
		}

		thanks.setDescription(newThanks.getDescription());
		thanks.setThankedAccount(accountRepo.getAccountById(newThanks.getThank_to()));
		try {
			thanksRepo.save(thanks);
			return ResponseEntity.noContent().build();
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
	 * view my thanks
	 */
	@GetMapping
	public ResponseEntity viewMyThanks(@PathVariable int uid) {
		Account account = accountRepo.getAccountById(uid);
		if (account != null) {
			return ResponseEntity.ok().body(account.getThanks());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
