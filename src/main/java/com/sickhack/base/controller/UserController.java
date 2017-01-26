package com.sickhack.base.controller;

import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.codec.EncoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.sickhack.base.dbmodel.UserDbModel;
import com.sickhack.base.storage.UserStorage;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserStorage userStorage;

	UserController(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@GetMapping("/list")
	ModelAndView list() {
		List<UserDbModel> users = userStorage.getUsers();

		ModelAndView mav = new ModelAndView("user_list", ImmutableMap.of("users", users));
		return mav;
	}

	@GetMapping("/detail/{userId}")
	ModelAndView register(@PathVariable UUID userId) {
		UserDbModel user = userStorage.getUser(userId);

		ModelAndView mav = new ModelAndView("user_detail", ImmutableMap.of("user", user));
		return mav;
	}

	@GetMapping("/register")
	String register() {
		return "user_register";
	}

	@PostMapping("/register")
	String register(@RequestParam("name") String name, @RequestParam("snippet") String snippet)
			throws JsonProcessingException, EncoderException {
		// TODO: Add validation.
		
		UserDbModel user = userStorage.createUser();
		user.setName(name).setSnippet(snippet);
		userStorage.writeUser(user);

		logger.info("Registered a user {}.", user);

		return "redirect:/user/list";
	}
}
