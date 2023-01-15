package jejakin.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jejakin.order.dao.UserRepository;
import jejakin.order.model.User;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/adduser")
	public String saveUser (@RequestBody User user){
		userRepo.save(user);
		return "user ditambahkan dengan" + user.getId();
	}
	
	@GetMapping("/users")
	public List<User> getUser() {
		return userRepo.findAll();
	}
}
