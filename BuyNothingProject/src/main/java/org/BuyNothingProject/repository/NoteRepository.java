package org.BuyNothingProject.repository;

import java.util.List;

import org.BuyNothingProject.domain.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends CrudRepository<Note, Integer> {
	@Query("SELECT e FROM Note e")
	public List<Note> getAllNotes();

	@Query("SELECT e FROM Note e WHERE e.nid= :nid")
	public Note getNoteById(@Param(value = "nid") Integer nid);

	@Query("SELECT e FROM Note e WHERE e.to_id= :uid")
	public List<Note> getNoteByGId(@Param(value = "uid") Integer uid);
}
