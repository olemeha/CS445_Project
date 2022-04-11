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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.BuyNothingProject.domain.Account;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@org.springframework.core.annotation.Order(1)
public class AccountControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String basePath = "/accounts";
	public static String uid;

	public static String asJsonString(final Object obj) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final String jsonContent = mapper.writeValueAsString(obj);
		return jsonContent;

	}

	@Test
	@Order(1)
	public void createAccountShouldReturn201() throws Exception {
		// account created
		Account ac = Util.getAccount();
		MvcResult mvc = this.mockMvc
				.perform(post(basePath).content(asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		JSONObject jsonObject = new JSONObject(mvc.getResponse().getContentAsString());
		this.uid = String.valueOf((int) jsonObject.get("uid"));
		// bad request
		ac.setName("");
		this.mockMvc.perform(post(basePath).content(asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(2)
	public void getAccountTest() throws Exception {
		// found
		this.mockMvc.perform(get(basePath + "/" + uid)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.uid").value(uid)).andExpect(jsonPath("$.name").value("Test"));
		// not found
		this.mockMvc.perform(get(basePath + "/10")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@Order(3)
	public void activateAccount() throws Exception {
		Account ac = Util.getAccount();

		// not found
		this.mockMvc.perform(get(basePath + "/10/activate")).andDo(print()).andExpect(status().isNotFound());
		// activate
		this.mockMvc.perform(get(basePath + "/" + uid + "/activate")).andDo(print()).andExpect(status().isOk());

	}

	@Test
	@Order(4)
	public void updateAccountTest() throws Exception {
		// bad request activate 10 using object of 1
		Account ac = Util.getAccount();
		this.mockMvc.perform(put(basePath + "/10").contentType(MediaType.APPLICATION_JSON).content(asJsonString(ac)))
				.andExpect(status().isBadRequest());

		// found but constraint violation
		ac.setName("");
		this.mockMvc
				.perform(put(basePath + "/" + uid).content(asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		// updates
		ac.setName("Test Update Name");
		ac.setUid(Integer.parseInt(uid));
		// bad request
		this.mockMvc
				.perform(put(basePath + "/" + uid).content(asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		ac.setIsActive(true);
		this.mockMvc
				.perform(put(basePath + "/" + uid).content(asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(5)
	public void searchAccountTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get(basePath).param("key", "name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Test Update Name"));
		this.mockMvc
				.perform(get(basePath).param("key", "name").param("start_date", "10-May-2000")
						.param("end_date", "10-May-3000").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Test Update Name"));
		// all
		this.mockMvc.perform(get(basePath).param("key", "name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Test Update Name"));
	}
}
