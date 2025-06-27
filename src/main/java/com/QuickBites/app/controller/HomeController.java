package com.QuickBites.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.repositories.FoodCategoryRepository;
import com.QuickBites.app.services.AuthService;
import com.QuickBites.app.services.FoodItemService;

import jakarta.validation.Valid;

@Validated
@Controller
@RequestMapping("/public")
public class HomeController {

	@Autowired
	AuthService authService;
	
	@Autowired
	FoodCategoryRepository foodCategoryRepo;
	
	@Autowired
	FoodItemService foodItemService;
	
	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<String> Home() {
		return ResponseEntity.ok("Hello guys"); 
	}
	
	
	@GetMapping("/createMenu")
	public String getCreateMenu(Model model) {
		model.addAttribute("foodItem",new CreateFoodItemDTO());
		model.addAttribute("categories",foodCategoryRepo.findAll());
		return "index";
	}
	
	@PostMapping(path="/createMenu", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createMenu(@Valid @ModelAttribute("foodItem") CreateFoodItemDTO foodItem
			,BindingResult result
			,Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("categories",foodCategoryRepo.findAll());
			return "index";
		}	
	foodItemService.addFoodItem(foodItem);
	return "redirect:/public/createMenu";
	}

}
