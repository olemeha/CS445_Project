package org.BuyNothingProject.domain;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Ask")
@EntityListeners(AuditingEntityListener.class)
public class Ask {

	@Id
	@Column(name = "aid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int aid;

	@ManyToOne
	@JoinColumn(name = "account_uid", referencedColumnName = "uid")
	private Account account;

	@NotEmpty(message = "The type may not be empty")
	private String type;

	@NotEmpty(message = "The description number may not be empty")
	private String description;

	@NotNull(message = "Please pass the start_date parameter")
	private String startDate;

	@NotNull(message = "Please pass the end_date parameter")
	private String endDate;

	@NotNull(message = "Please pass the is_active parameter")
	private boolean isActive;

	@CreatedDate
	@Column(columnDefinition = "DATETIME(3)")
	private Instant dateCreated;
	
	@ElementCollection
	private List<String> extraZip;


	@JsonProperty(value = "extra_zip")
	public List<String> getExtraZip() {
		return extraZip;
	}

	@JsonProperty(value = "uid")
	public int getUid() {
		return account.getUid();
	}


	public void setExtraZip(List<String> extraZip) {
		this.extraZip = extraZip;
	}

	public int getAid() {
		return aid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) throws Exception {
		String[] hai = { "borrow", "gift", "help" };
		List<String> allowedValues = java.util.Arrays.asList(hai);
		if (!allowedValues.contains(type)) {
			throw new Exception("Type must be borrow, gift, help");
		}
		this.type = type;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("start_date")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@JsonProperty("end_date")
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@JsonProperty("date_created")
	public Instant getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Instant dateCreated) {
		this.dateCreated = dateCreated;
	}

	@JsonProperty("is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Ask() {
		super();
	}
}
