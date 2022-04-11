package org.BuyNothingProject.restservice;

import java.util.ArrayList;
import java.util.List;

import org.BuyNothingProject.domain.Account;
import org.BuyNothingProject.domain.Address;
import org.BuyNothingProject.web.models.AskModel;
import org.BuyNothingProject.web.models.GiveModel;
import org.BuyNothingProject.web.models.NoteModel;
import org.BuyNothingProject.web.models.ThanksModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	public static String asJsonString(final Object obj) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final String jsonContent = mapper.writeValueAsString(obj);
		return jsonContent;

	}

	public static Account getAccount() {
		Account ac = new Account();
		ac.setName("Test");
		ac.setPhone("9988-999-888");
		ac.setUid(1);
		ac.setPicture("some_picture");
		Address ad = new Address();
		ad.setStreet("India");
		ad.setZip("11228899");
		ac.setAddress(ad);
		return ac;
	}

	public static AskModel getAsk() throws Exception {
		AskModel ac = new AskModel();
		ac.setIs_active(true);
		ac.setDescription("something");
		ac.setType("borrow");
		List<String> ziplist = new ArrayList();
		ziplist.add("11228899");
		ziplist.add("1290319");
		ac.setExtra_zip(ziplist);
		ac.setEnd_date("12-May-2022");
		ac.setStart_date("12-Jan-2022");
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		return ac;
	}

	public static GiveModel getGive() throws Exception {
		GiveModel ac = new GiveModel();
		ac.setIs_active(true);
		ac.setDescription("something");
		ac.setType("service");
		List<String> ziplist = new ArrayList();
		ziplist.add("11228899");
		ziplist.add("1290319");
		ac.setExtra_zip(ziplist);
		ac.setEnd_date("12-May-2022");
		ac.setStart_date("12-Jan-2022");
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		return ac;
	}

	public static NoteModel getNote() throws Exception {
		NoteModel ac = new NoteModel();
		ac.setDescription("Some description");
		ac.setTo_id(Integer.parseInt(AskControllerTests.aid));
		ac.setTo_type("ask");
		ac.setTo_user_id(Integer.parseInt(AccountControllerTests.uid));
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		return ac;
	}
	public static ThanksModel getThanks() throws Exception {
		ThanksModel ac = new ThanksModel();
		ac.setThank_to(Integer.parseInt(AccountControllerTests.uid));
		ac.setDescription("Thanks");
		return ac;
	}

}
