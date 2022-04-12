/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.BuyNothingProject.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.BuyNothingProject.web.models.AskModel;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@org.springframework.core.annotation.Order(2)
public class AskControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String basePath = "/accounts/" + AccountControllerTests.uid + "/asks";
	public static String aid;

	@Test
	@Order(1)
	public void createAskShouldReturn201() throws Exception {
		// ask created
		AskModel ac = Util.getAsk();
		MvcResult mvc = this.mockMvc
				.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		JSONObject jsonObject = new JSONObject(mvc.getResponse().getContentAsString());
		this.aid = String.valueOf((int) jsonObject.get("aid"));
		// bad request
		ac.setDescription("");
		this.mockMvc.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(2)
	public void getAskTest() throws Exception {
		// found
		this.mockMvc.perform(get(basePath + "/" + aid)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.aid").value(aid)).andExpect(jsonPath("$.type").value("borrow"));
		// not found
		this.mockMvc.perform(get(basePath + "/10")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@Order(3)
	public void deactivateAsk() throws Exception {
		AskModel ac = Util.getAsk();

		// not found
		this.mockMvc.perform(get(basePath + "/10/deactivate")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@Order(4)
	public void updateAskTest() throws Exception {
		// bad request activate 10 using object of 1
		AskModel ac = Util.getAsk();
		// found but constraint violation
		ac.setDescription("");
		this.mockMvc.perform(
				put(basePath + "/" + aid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		// updates
		ac.setDescription("Test Update description");
		ac.setUid(1000);
		// bad request
		this.mockMvc.perform(
				put(basePath + "/" + aid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		this.mockMvc.perform(
				put(basePath + "/" + aid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// deactivate and then update
		this.mockMvc.perform(get(basePath + "/" + aid + "/deactivate")).andDo(print()).andExpect(status().isOk());
		this.mockMvc.perform(
				put(basePath + "/" + aid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(5)
	public void searchAskTest() throws Exception {
		// with key and dates
		this.mockMvc
				.perform(get("/asks").param("key", "Test Update description").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc
				.perform(get("/asks").param("key", "Test Update description").param("start_date", "10-May-2000")
						.param("end_date", "10-May-3000").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		// all
		this.mockMvc
				.perform(get("/asks").param("key", "Test Update description").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc
				.perform(get("/asks").param("v_by", AccountControllerTests.uid).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc.perform(get("/asks").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].description").value("Test Update description"));
	}

	@Test
	@Order(6)
	public void viewMyAskTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get(basePath).param("is_active", "false").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
	}

	@Test
	@Order(7)
	public void viewAskByIdTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get("/asks/" + aid).param("is_active", "false").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.description").value("Test Update description"));
	}
}
