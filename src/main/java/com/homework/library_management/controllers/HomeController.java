package com.homework.library_management.controllers;

import com.homework.library_management.services.HomeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
	HomeService homeService;

	@GetMapping("/home")
	public String home(HttpServletRequest request) {
		homeService.prepareStatistic(request);
		return "home";
	}

}