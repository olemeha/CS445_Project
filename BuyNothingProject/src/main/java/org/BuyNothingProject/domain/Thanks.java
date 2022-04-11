package org.BuyNothingProject.domain;

import java.time.Instant;

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
@Table(name = "Thanks")
@EntityListeners(AuditingEntityListener.class)
public class Thanks {

	@Id
	@Column(name = "tid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int tid;

	@ManyToOne
	@JoinColumn(name = "user_account_uid", referencedColumnName = "uid")
	private Account userAccount;

	@ManyToOne
	@JoinColumn(name = "thanked_account_uid", referencedColumnName = "uid")
	private Account thankedAccount;

	@NotEmpty(message = "The description number may not be empty")
	private String description;

	@CreatedDate
	@Column(columnDefinition = "DATETIME(3)")
	private Instant dateCreated;

	@JsonProperty(value = "uid")
	public int getUserAccount() {
		return userAccount.getUid();
	}

	public void setUserAccount(Account userAccount) {
		this.userAccount = userAccount;
	}

	@JsonProperty(value = "thank_to")
	public int getThankedUid() {
		return thankedAccount.getUid();
	}

	public void setThankedAccount(Account thankedAccount) {
		this.thankedAccount = thankedAccount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTid() {
		return tid;
	}

	@JsonProperty(value = "date_created")
	public Instant getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Instant dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Thanks() {
		super();
	}
}
