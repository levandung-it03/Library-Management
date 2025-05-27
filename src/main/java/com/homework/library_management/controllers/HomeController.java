package com.homework.library_management.controllers;

import com.homework.library_management.config.GlobalLogger;
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
	GlobalLogger logger;
	HomeService homeService;

	@GetMapping("/home")
	public String renderHome(HttpServletRequest request) {
		logger.handling(request, "HomeController.renderHome");
		homeService.prepareStatistic(request);
		logger.success(request, "`renderHome` successfully");
		return "home";
	}

}