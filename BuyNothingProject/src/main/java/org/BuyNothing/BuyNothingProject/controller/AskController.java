package org.BuyNothing.BuyNothingProject.controller;

import org.BuyNothing.BuyNothingProject.repository.AskRepository;
import org.BuyNothing.BuyNothingProject.repository.NoteRepository;
import org.BuyNothing.BuyNothingProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ask")
public class AskController {
	
	@Autowired
	AskRepository ar;
	@Autowired
	UserRepository ur;
	@Autowired
	NoteRepository nr;
}
