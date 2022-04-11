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

import org.BuyNothingProject.web.models.GiveModel;
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
@org.springframework.core.annotation.Order(3)
public class GiveControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String basePath = "/accounts/" + AccountControllerTests.uid + "/gives";
	public static String gid;

	@Test
	@Order(1)
	public void createGiveShouldReturn201() throws Exception {
		// give created
		GiveModel ac = Util.getGive();
		MvcResult mvc = this.mockMvc
				.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		JSONObject jsonObject = new JSONObject(mvc.getResponse().getContentAsString());
		this.gid = String.valueOf((int) jsonObject.get("gid"));
		// bad request
		ac.setDescription("");
		this.mockMvc.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(2)
	public void getGiveTest() throws Exception {
		// found
		this.mockMvc.perform(get(basePath + "/" + gid)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.gid").value(gid)).andExpect(jsonPath("$.type").value("service"));
		// not found
		this.mockMvc.perform(get(basePath + "/10")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@Order(3)
	public void deactivateGive() throws Exception {
		GiveModel ac = Util.getGive();

		// not found
		this.mockMvc.perform(get(basePath + "/10/deactivate")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@Order(4)
	public void updateGiveTest() throws Exception {
		// bad request activate 10 using object of 1
		GiveModel ac = Util.getGive();
		// found but constraint violation
		ac.setDescription("");
		this.mockMvc.perform(
				put(basePath + "/" + gid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		// updates
		ac.setDescription("Test Update description");
		ac.setUid(1000);
		// bad request
		this.mockMvc.perform(
				put(basePath + "/" + gid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		this.mockMvc.perform(
				put(basePath + "/" + gid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// deactivate and then update
		this.mockMvc.perform(get(basePath + "/" + gid + "/deactivate")).andDo(print()).andExpect(status().isOk());
		this.mockMvc.perform(
				put(basePath + "/" + gid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(5)
	public void searchGiveTest() throws Exception {
		// with key and dates
		this.mockMvc
				.perform(get("/gives").param("key", "Test Update description").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc
				.perform(get("/gives").param("key", "Test Update description").param("start_date", "10-May-2000")
						.param("end_date", "10-May-3000").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		// all
		this.mockMvc
				.perform(get("/gives").param("key", "Test Update description").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc
				.perform(get("/gives").param("v_by", AccountControllerTests.uid).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
		this.mockMvc.perform(get("/gives").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].description").value("Test Update description"));
	}

	@Test
	@Order(6)
	public void viewMyGiveTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get(basePath).param("is_active", "false").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
	}

	@Test
	@Order(7)
	public void viewGiveByIdTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get("/gives/" + gid).param("is_active", "false").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.description").value("Test Update description"));
	}

	@Test
	@Order(8)
	public void deleteGiveByTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(delete(basePath + "/"+gid).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
