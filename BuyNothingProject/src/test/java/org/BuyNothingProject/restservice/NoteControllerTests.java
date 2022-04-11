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

import org.BuyNothingProject.web.models.NoteModel;
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
@org.springframework.core.annotation.Order(4)
public class NoteControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String basePath = "/notes";
	public static String nid;

	@Test
	@Order(1)
	public void createNoteShouldReturn201() throws Exception {
		// note created
		NoteModel ac = Util.getNote();
		MvcResult mvc = this.mockMvc
				.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		JSONObject jsonObject = new JSONObject(mvc.getResponse().getContentAsString());
		this.nid = String.valueOf((int) jsonObject.get("nid"));
		// bad request
		ac.setDescription("");
		this.mockMvc.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
		// not found
		ac.setTo_id(1000);
		this.mockMvc.perform(post(basePath).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	@Order(2)
	public void getNoteTest() throws Exception {
		// found
		this.mockMvc.perform(get(basePath)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].source_id").value(AskControllerTests.aid))
				.andExpect(jsonPath("$[0].conversation[0].notes[0].description").value("Some description"));
		this.mockMvc.perform(get(basePath).param("c_by", AccountControllerTests.uid)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].source_id").value(AskControllerTests.aid))
				.andExpect(jsonPath("$[0].conversation[0].notes[0].description").value("Some description"));
		this.mockMvc.perform(get(basePath).param("key", "Some description")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].to_id").value(AskControllerTests.aid))
				.andExpect(jsonPath("$[0].description").value("Some description"));
	}

	@Test
	@Order(3)
	public void updateNoteTest() throws Exception {
		// bad request activate 10 using object of 1
		NoteModel ac = Util.getNote();
		// found but constraint violation
		ac.setDescription("");
		this.mockMvc.perform(
				put(basePath + "/" + nid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		// updates
		ac.setDescription("Test Update description");
		ac.setUid(1000);
		// bad request
		this.mockMvc.perform(
				put(basePath + "/" + nid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		ac.setUid(Integer.parseInt(AccountControllerTests.uid));
		this.mockMvc.perform(
				put(basePath + "/" + nid).content(Util.asJsonString(ac)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	@Order(4)
	public void deleteNoteByTest() throws Exception {
		// with key and dates
		this.mockMvc.perform(delete(basePath + "/" + nid).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
