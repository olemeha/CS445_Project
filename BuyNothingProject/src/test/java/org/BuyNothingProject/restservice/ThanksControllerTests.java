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

import org.BuyNothingProject.web.models.ThanksModel;
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
@org.springframework.core.annotation.Order(5)
public class ThanksControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String basePath = "/accounts/" + AccountControllerTests.uid + "/thanks";
	public static String tid;

	@Test
	@Order(1)
	public void createThanksShouldReturn201() throws Exception {
		// thanks created
		ThanksModel ac = Util.getThanks();
		MvcResult mvc = this.mockMvc
				.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		JSONObject jsonObject = new JSONObject(mvc.getResponse().getContentAsString());
		this.tid = String.valueOf((int) jsonObject.get("tid"));
		// bad request
		ac.setDescription("");
		this.mockMvc.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Order(2)
	public void getThanksTest() throws Exception {
		// found
		this.mockMvc.perform(get(basePath)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].tid").value(tid)).andExpect(jsonPath("$[0].description").value("Thanks"));
	}

	@Test
	@Order(5)
	public void updateThanksTest() throws Exception {
		// bad request activate 10 using object of 1
		ThanksModel ac = Util.getThanks();
		// found but constraint violation
		ac.setDescription("");
		this.mockMvc.perform(
				put(basePath + "/" + tid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		// updates
		ac.setDescription("Test Update description");
		ac.setUid(1000);
		// bad request
		this.mockMvc.perform(
				put(basePath + "/" + tid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		this.mockMvc.perform(
				put(basePath + "/" + tid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	@Order(5)
	public void searchThanksTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(get("/thanks").param("key", "Thanks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Thanks"));
		this.mockMvc
				.perform(get("/thanks").param("key", "Thanks").param("start_date", "10-May-2000")
						.param("end_date", "10-May-3000").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Thanks"));

	}

	@Test
	@Order(6)
	public void viewReceviedThanksTest() throws Exception {
		// with key and dates
		this.mockMvc
				.perform(get("/thanks/received/" + AccountControllerTests.uid).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].description").value("Test Update description"));
	}

}
