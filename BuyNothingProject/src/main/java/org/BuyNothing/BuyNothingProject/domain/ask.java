package org.BuyNothing.BuyNothingProject.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Ask")
public class ask {

	@Id
	@Column(name="askId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int askId;
	private String askName;
	private String askType;
	private Boolean askActive;
	private int visible;
	private Boolean complete;
	private LocalDateTime created;
	private int ownerId;
	private ArrayList<Integer> AllowedZip ;
	
	
}
