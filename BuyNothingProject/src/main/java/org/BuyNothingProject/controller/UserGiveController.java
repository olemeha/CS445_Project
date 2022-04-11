package org.BuyNothingProject.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Give;
import org.BuyNothingProject.messages.ConstraintViolationMessage;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.GiveRepository;
import org.BuyNothingProject.web.models.GiveModel;
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
@RequestMapping("/accounts/{uid}/gives")
public class UserGiveController {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	GiveRepository giveRepo;

	@Autowired
	NoteController noteController;
	/*
	 * Create Give
	 */

	@PostMapping
	public ResponseEntity createGive(@PathVariable int uid, @RequestBody Give give) throws Exception {
		String uri = "/accounts/" + String.valueOf(uid) + "/gives/";
		try {
			Account account = accountRepo.getAccountById(uid);

			if (account.getIsActive()) {
				give.setAccount(account);
				giveRepo.save(give);
				give = giveRepo.getGiveById(give.getGid());
				return ResponseEntity.created(new URI(uri + String.valueOf(give.getGid()))).body(give);
			} else {
				ConstraintViolationMessage ex = new ConstraintViolationMessage();
				ex.setStatus(400);
				ex.setInstance("/accounts/" + String.valueOf(account.getUid()));
				ex.setDetail("This account " + String.valueOf(uid) + " is not active an may not create a give.");
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
	 * deactivate give
	 */

	@GetMapping("{gid}/deactivate")
	public ResponseEntity deactivateGive(@PathVariable int uid, @PathVariable int gid) {
		Account account = accountRepo.getAccountById(uid);
		Give give = giveRepo.getGiveById(gid);
		if (give != null && account != null && give.getUid() == account.getUid()) {
			give.setIsActive(false);
			giveRepo.save(give);
			return ResponseEntity.ok().body(give);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * Update give
	 */
	@PutMapping("{gid}")
	public ResponseEntity updateGive(@PathVariable int uid, @PathVariable int gid, @RequestBody GiveModel newGive) {
		String uri = "/accounts/" + String.valueOf(uid) + "/gives/" + String.valueOf(gid);
		Give give = giveRepo.getGiveById(gid);
		if (newGive.getUid() != give.getUid()) {
			ConstraintViolationMessage ex = new ConstraintViolationMessage();
			ex.setStatus(400);
			ex.setInstance(uri);
			ex.setDetail("This account (" + String.valueOf(uid) + ") did not create give ("
					+ String.valueOf(newGive.getGid()) + " and is not allowed to update it)");
			return ResponseEntity.badRequest().body(ex);
		}
		if (give.getIsActive()) {
			try {
				give.setType(newGive.getType());
			} catch (Exception e1) {
				ConstraintViolationMessage ex = new ConstraintViolationMessage();
				ex.setStatus(400);
				ex.setInstance(uri);
				ex.setDetail(e1.getMessage());
				return ResponseEntity.badRequest().body(ex);
			}
			give.setDescription(newGive.getDescription());
			give.setStartDate(newGive.getStart_date());
			give.setEndDate(newGive.getEnd_date());
			give.setExtraZip(newGive.getExtra_zip());
			try {
				giveRepo.save(give);
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
			ex.setDetail("This account give id (" + String.valueOf(newGive.getGid()) + " is not active)");
			return ResponseEntity.badRequest().body(ex);
		}

	}

	/*
	 * Delete give
	 */
	@DeleteMapping("{gid}")
	public ResponseEntity deleteGive(@PathVariable int uid, @PathVariable int gid) {
		Give give = giveRepo.getGiveById(gid);
		if (give != null && give.getUid() == uid) {
			noteController.deleteNotesById(null, gid, null);
			giveRepo.delete(give);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("{gid}")
	public ResponseEntity getGive(@PathVariable int uid, @PathVariable int gid) {
		Give give = giveRepo.getGiveById(gid);
		Account acc = accountRepo.getAccountById(uid);
		if (give != null && acc != null && give.getUid() == acc.getUid()) {
			return ResponseEntity.ok().body(give);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * view my gives
	 */
	@GetMapping
	public ResponseEntity viewMyGives(@PathVariable int uid,
			@RequestParam(required = false, name = "is_active") Boolean isActive) {
		Account account = accountRepo.getAccountById(uid);
		if (account != null) {
			if (isActive == null) {
				return ResponseEntity.ok().body(account.getGive());
			}
			List<Give> matches = new ArrayList();
			for (Give give : account.getGive()) {
				if (give.getIsActive() == isActive) {
					matches.add(give);
				}
			}
			return ResponseEntity.ok().body(matches);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
