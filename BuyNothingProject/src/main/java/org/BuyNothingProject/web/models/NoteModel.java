package org.BuyNothingProject.web.models;

public class NoteModel {
	private int uid;
	private int nid;
	private String to_type;
	private int to_user_id;
	private int to_id;
	private String description;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public String getTo_type() {
		return to_type;
	}

	public void setTo_type(String to_type) {
		this.to_type = to_type;
	}

	public int getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(int to_user_id) {
		this.to_user_id = to_user_id;
	}

	public int getTo_id() {
		return to_id;
	}

	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	private String date_created;

}
