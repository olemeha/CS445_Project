package org.BuyNothingProject.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Ask;
import org.BuyNothingProject.domain.Note;
import org.BuyNothingProject.messages.ConstraintViolationMessage;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.AskRepository;
import org.BuyNothingProject.repository.NoteRepository;
import org.BuyNothingProject.web.models.AskModel;
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
@RequestMapping("/accounts/{uid}/asks")
public class UserAskController {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	AskRepository askRepo;

	@Autowired
	NoteRepository noteRepo;

	@Autowired
	NoteController noteController;
	/*
	 * Create Ask
	 */

	@PostMapping
	public ResponseEntity createAsk(@PathVariable int uid, @RequestBody Ask ask) throws Exception {
		String uri = "/accounts/" + String.valueOf(uid) + "/asks/";
		try {
			Account account = accountRepo.getAccountById(uid);
			if (account.getIsActive()) {
				ask.setAccount(account);
				askRepo.save(ask);
				ask = askRepo.getAskById(ask.getAid());
				return ResponseEntity.created(new URI(uri + ask.getAid())).body(ask);
			} else {
				ConstraintViolationMessage ex = new ConstraintViolationMessage();
				ex.setStatus(400);
				ex.setInstance("/accounts/" + String.valueOf(account.getUid()));
				ex.setDetail("This account " + String.valueOf(uid) + " is not active an may not create an ask.");
				return ResponseEntity.badRequest().body(ex);
			}
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
	/*
	 * deactivate ask
	 */

	@GetMapping("{aid}/deactivate")
	public ResponseEntity deactivateAsk(@PathVariable int uid, @PathVariable int aid) {
		Account account = accountRepo.getAccountById(uid);
		Ask ask = askRepo.getAskById(aid);
		if (ask != null && account != null && ask.getUid() == account.getUid()) {
			ask.setIsActive(false);
			askRepo.save(ask);
			return ResponseEntity.ok().body(ask);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * Update ask
	 */
	@PutMapping("{aid}")
	public ResponseEntity updateAsk(@PathVariable int uid, @PathVariable int aid, @RequestBody AskModel newAsk) {
		String uri = "/accounts/" + String.valueOf(uid) + "/asks/" + String.valueOf(aid);
		Ask ask = askRepo.getAskById(aid);
		if (newAsk.getUid() != ask.getUid()) {
			ConstraintViolationMessage ex = new ConstraintViolationMessage();
			ex.setStatus(400);
			ex.setInstance(uri);
			ex.setDetail("This account (" + String.valueOf(uid) + ") did not create ask ("
					+ String.valueOf(newAsk.getAid()) + " and is not allowed to update it)");
			return ResponseEntity.badRequest().body(ex);
		}
		if (ask.getIsActive()) {
			try {
				ask.setType(newAsk.getType());
			} catch (Exception e1) {
				ConstraintViolationMessage ex = new ConstraintViolationMessage();
				ex.setStatus(400);
				ex.setInstance(uri);
				ex.setDetail(e1.getMessage());
				return ResponseEntity.badRequest().body(ex);
			}
			ask.setDescription(newAsk.getDescription());
			ask.setStartDate(newAsk.getStart_date());
			ask.setEndDate(newAsk.getEnd_date());
			ask.setExtraZip(newAsk.getExtra_zip());
			try {
				askRepo.save(ask);
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
		} else {
			ConstraintViolationMessage ex = new ConstraintViolationMessage();
			ex.setStatus(400);
			ex.setInstance(uri);
			ex.setTitle("Your request data didn't pass validation");
			ex.setDetail("This account ask id (" + String.valueOf(newAsk.getAid()) + " and is not active)");
			return ResponseEntity.badRequest().body(ex);
		}

	}

	/*
	 * Delete ask
	 */
	@DeleteMapping("{aid}")
	public ResponseEntity deleteAsk(@PathVariable int uid, @PathVariable int aid) {
		Ask ask = askRepo.getAskById(aid);
		noteController.deleteNotesById(null, null, aid);
		if (ask != null && ask.getUid() == uid) {
			askRepo.delete(ask);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("{aid}")
	public ResponseEntity getAsk(@PathVariable int uid, @PathVariable int aid) {
		Ask ask = askRepo.getAskById(aid);
		Account acc = accountRepo.getAccountById(uid);
		if (ask != null && acc != null && (ask.getUid() == acc.getUid())) {
			return ResponseEntity.ok().body(ask);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * view my asks
	 */
	@GetMapping
	public ResponseEntity viewMyAsks(@PathVariable int uid,
			@RequestParam(required = false, name = "is_active") Boolean isActive) {
		Account account = accountRepo.getAccountById(uid);
		if (account != null) {
			if (isActive == null) {
				return ResponseEntity.ok().body(account.getAsk());
			}
			List<Ask> matches = new ArrayList();
			for (Ask ask : account.getAsk()) {
				if (ask.getIsActive() == isActive) {
					matches.add(ask);
				}
			}
			return ResponseEntity.ok().body(matches);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
