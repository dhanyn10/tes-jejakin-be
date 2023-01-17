package jejakin.order.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
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
	
	@PostMapping("adduser")
	public String saveUser (@RequestBody User user){
		userRepo.save(user);
		return "user ditambahkan dengan" + user.getId();
	}
	
	@GetMapping("users")
	public List<User> getUser() {
		return userRepo.findAll();
	}
	
	@PostMapping("login")
	public String loginUser(@RequestBody User user, HttpSession session) {
		JSONObject json = new JSONObject();
		String tempUser = user.getUsername();
		
		String dataUser = userRepo.findUserByUsername(tempUser);
		if(dataUser == null) {
			json.put("message", "user not exist");
		} else {
			if(session.getAttribute("username") == null) {
				session.setAttribute("username", user.getUsername());
				json.put("message", "logged in");
			} else {
				json.put("message", "already logged in");
			}
		}
		return json.toString();
	}

	@PostMapping("logout")
	public String logoutUser(@RequestBody User user, HttpSession session) {
		JSONObject json = new JSONObject();
		if(session.getAttribute("username") != null) {
			session.removeAttribute("username");
			json.put("message", "logged out");
		} else {
			json.put("message", "already logged out");
		}
		return json.toString();		
	}
}
