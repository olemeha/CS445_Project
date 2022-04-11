package org.BuyNothingProject.domain;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Account")
@EntityListeners(AuditingEntityListener.class)
public class Account {

	@Id
	@Column(name = "uid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uid;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@JsonIgnore
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Ask> ask;

	@JsonIgnore
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Give> give;

	@JsonIgnore
	@OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
	private List<Thanks> thanks;

	@JsonIgnore
	@OneToMany(mappedBy = "thankedAccount", cascade = CascadeType.ALL)
	private List<Thanks> thankedAccounts;

	public List<Thanks> getThankedAccounts() {
		return thankedAccounts;
	}

	public void setThankedAccounts(List<Thanks> thankedAccounts) {
		this.thankedAccounts = thankedAccounts;
	}

	@NotEmpty(message = "The name may not be empty")
	private String name;

	@NotEmpty(message = "The phone number may not be empty")
	private String phone;

	@NotNull(message = "Please pass the picture parameter")
	private String picture;

	@NotNull(message = "Please pass the is_active parameter")
	private boolean isActive;

	@CreationTimestamp
	@Column(columnDefinition = "DATETIME(3)")
	private Instant dateCreated;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Ask> getAsk() {
		return ask;
	}

	public void setAsk(List<Ask> ask) {
		this.ask = ask;
	}

	public List<Give> getGive() {
		return give;
	}

	public List<Thanks> getThanks() {
		return thanks;
	}

	public void setThanks(List<Thanks> thanks) {
		this.thanks = thanks;
	}

	public void setGive(List<Give> give) {
		this.give = give;
	}

	@JsonProperty("is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@JsonProperty("date_created")
	public Instant getDateCreated() {
		return dateCreated;
	}

	public void setdateCreated(Instant dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Account() {
		super();
	}

}
