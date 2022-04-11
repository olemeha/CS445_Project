package org.BuyNothingProject.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.RollbackException;

import org.BuyNothingProject.domain.Note;
import org.BuyNothingProject.messages.ConstraintViolationMessage;
import org.BuyNothingProject.repository.AccountRepository;
import org.BuyNothingProject.repository.AskRepository;
import org.BuyNothingProject.repository.GiveRepository;
import org.BuyNothingProject.repository.NoteRepository;
import org.BuyNothingProject.web.models.Conversation;
import org.BuyNothingProject.web.models.NoteModel;
import org.BuyNothingProject.web.models.NotesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	GiveRepository giveRepo;

	@Autowired
	AskRepository askRepo;

	@Autowired
	NoteRepository noteRepo;

	/*
	 * Create Note
	 */

	@PostMapping
	public ResponseEntity createNote(@RequestBody NoteModel note) throws Exception {
		try {
			if (accountRepo.getAccountById(note.getTo_user_id()) != null
					&& accountRepo.getAccountById(note.getUid()) != null) {
				if ((note.getTo_type().intern() == "give" && giveRepo.getGiveById(note.getTo_id()) != null)
						|| (note.getTo_type().intern() == "ask" && askRepo.getAskById(note.getTo_id()) != null)
						|| (note.getTo_type().intern() == "note" && noteRepo.getNoteById(note.getTo_id()) != null)) {
					Note obj = new Note();
					obj.setDescription(note.getDescription());
					obj.setToId(note.getTo_id());
					obj.setToType(note.getTo_type());
					obj.setToUserId(note.getTo_user_id());
					obj.setUid(note.getUid());
					obj.setAccount(accountRepo.getAccountById(note.getUid()));
					noteRepo.save(obj);
					return ResponseEntity.created(new URI("/notes/" + String.valueOf(obj.getNid()))).body(obj);
				}
			}

		} catch (Exception e) {
			if (e.getCause() instanceof RollbackException) {
				ConstraintViolationMessage msg = ConstraintViolationMessage.getViolationBody(e);
				msg.setInstance("/notes");
				return ResponseEntity.badRequest().body(msg);
			} else {
				throw e;
			}
		}
		return ResponseEntity.notFound().build();
	}

	/*
	 * Update note
	 */
	@PutMapping("{nid}")
	public ResponseEntity updateNote(@PathVariable int nid, @RequestBody NoteModel updatedNote) throws Exception {
		String uri = "/notes/" + String.valueOf(nid);
		Note note = noteRepo.getNoteById(nid);
		if (note != null) {
			if (accountRepo.getAccountById(updatedNote.getTo_user_id()) != null
					&& accountRepo.getAccountById(updatedNote.getUid()) != null) {
				if ((updatedNote.getTo_type().intern() == "give"
						&& giveRepo.getGiveById(updatedNote.getTo_id()) != null)
						|| (updatedNote.getTo_type().intern() == "ask"
								&& askRepo.getAskById(updatedNote.getTo_id()) != null)
						|| (updatedNote.getTo_type().intern() == "note"
								&& noteRepo.getNoteById(updatedNote.getTo_id()) != null)) {
					try {
						note.setDescription(updatedNote.getDescription());
						note.setToId(updatedNote.getTo_id());
						note.setToType(updatedNote.getTo_type());
						note.setToUserId(updatedNote.getTo_user_id());
						note.setAccount(accountRepo.getAccountById(note.getUid()));
						noteRepo.save(note);
						return ResponseEntity.noContent().build();
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
			}
		}
		return ResponseEntity.notFound().build();
	}

	/*
	 * Delete Note
	 */
	@DeleteMapping("{nid}")
	public ResponseEntity deleteNote(@PathVariable int nid) {
		Note note = noteRepo.getNoteById(nid);
		if (note != null) {
			noteRepo.delete(note);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private List getNotes() {
		List<NotesResult> results = new ArrayList();
		List<Integer> giveIds = giveRepo.getAllGiveIds();
		List<Note> allNotes = noteRepo.getAllNotes();
		Map<String, NotesResult> map = new HashMap();
		for (Note note : allNotes) {
			String to_user_id = String.valueOf(note.getToUserId());
			String uid = String.valueOf(note.getUid());
			String key1 = to_user_id.intern() + "__" + uid.intern();
			String key2 = uid.intern() + "__" + to_user_id.intern();
			if (map.get(key1) == null && map.get(key2) == null) {
				NotesResult nr = new NotesResult();
				nr.source_id = String.valueOf(note.getToId());
				nr.uid = to_user_id;
				Conversation c = new Conversation();
				c.with_uid = uid;
				c.notes = new ArrayList();
				c.notes.add(note);
				nr.conversation = new ArrayList();
				nr.conversation.add(c);
				map.put(key1, nr);
			} else {
				NotesResult nr = map.get(key1) == null ? map.get(key2) : map.get(key1);
				((Conversation) nr.conversation.get(0)).notes.add(note);
			}
		}
		for (NotesResult nr : map.values()) {
			results.add(nr);
		}
		return results;
	}

	/*
	 * Search Notes
	 */
	@GetMapping
	public ResponseEntity getNotes(@RequestParam(required = false) String c_by,
			@RequestParam(required = false) String v_by, @RequestParam(required = false) String type,
			@RequestParam(required = false) Integer agid, @RequestParam(required = false) String key) {
		if (key != null) {
			List<Note> matches = new ArrayList();
			ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
			for (Note account : noteRepo.getAllNotes()) {
				try {
					String accountJson = ow.writeValueAsString(account).toLowerCase().intern();
					if (accountJson.contains(key.toLowerCase().intern())) {
						matches.add(account);
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}
			return ResponseEntity.ok().body(matches);
		} else if (c_by == null && v_by == null) {
			return ResponseEntity.ok().body(getNotes());
		} else {
			List<NotesResult> allNotes = getNotes();
			List<NotesResult> someNotes = new ArrayList<NotesResult>();
			for (NotesResult nr : allNotes) {
				if (nr.uid.intern() == c_by.intern() || nr.uid.intern() == v_by.intern()) {
					someNotes.add(nr);
					return ResponseEntity.ok().body(someNotes);
				}
			}

		}
		return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).build();

	}

	public void deleteNotesById(Integer uid, Integer gid, Integer aid) {
		List<Note> notesToDelete = new ArrayList();
		int match_id;
		match_id = uid != null ? uid : (gid != null ? gid : (aid != null ? aid : 0));
		for (Note n : noteRepo.getAllNotes()) {
			if (n.getToId() == match_id) {
				notesToDelete.add(n);
			}
		}
		for (Note n : notesToDelete) {
			noteRepo.delete(n);
		}
	}
}
