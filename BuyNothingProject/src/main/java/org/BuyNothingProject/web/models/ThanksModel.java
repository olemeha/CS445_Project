package org.BuyNothingProject.web.models;

public class ThanksModel {
	private int uid;
	private int tid;
	private int thank_to;
	private String description;
	private String date_created;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getThank_to() {
		return thank_to;
	}

	public void setThank_to(int thank_to) {
		this.thank_to = thank_to;
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

}
