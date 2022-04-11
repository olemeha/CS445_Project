package org.BuyNothingProject.domain;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Note")
@EntityListeners(AuditingEntityListener.class)
public class Note {

	@Id
	@Column(name = "nid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int nid;

	@ManyToOne
	@JoinColumn(name = "account_uid", referencedColumnName = "uid")
	private Account account;

	private String to_type;

	private int to_user_id;

	private int to_id;

	@NotEmpty(message = "The description number may not be empty")
	private String description;

	@CreatedDate
	@Column(columnDefinition = "DATETIME(3)")
	private Instant dateCreated;

	private int uid;

	public void setUid(int uid) {
		this.uid = uid;
	}

	@JsonProperty(value = "uid")
	public int getUid() {
		return account.getUid();
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	@JsonProperty(value = "to_type")
	public String getToType() {
		return to_type;
	}

	public void setToType(String to_type) throws Exception {
		String[] types = { "note", "give", "ask" };
		List<String> allowedValues = java.util.Arrays.asList(types);
		if (!allowedValues.contains(to_type)) {
			throw new Exception("Type must be note, ask or give");
		}
		this.to_type = to_type;
	}

	@JsonProperty(value = "to_user_id")
	public int getToUserId() {
		return to_user_id;
	}

	public void setToUserId(int to_user_id) {
		this.to_user_id = to_user_id;
	}

	@JsonProperty(value = "to_id")
	public int getToId() {
		return to_id;
	}

	public void setToId(int to_id) {
		this.to_id = to_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Instant dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Note() {
		super();
	}

	public void setAccount(Account account) {
		this.account = account;

	}
}
