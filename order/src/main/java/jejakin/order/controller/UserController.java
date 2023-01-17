package jejakin.order.controller;

import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jejakin.order.dao.TokenRepository;
import jejakin.order.dao.UserRepository;
import jejakin.order.model.Token;
import jejakin.order.model.User;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TokenRepository tokenRepo;
	
	@PostMapping("adduser")
	public String saveUser (@RequestBody User user){
		JSONObject report = new JSONObject();
		
		String dataUser = userRepo.findByUsername(user.getUsername());
		String dataEmail = userRepo.findByEmail(user.getEmail());
		if(dataUser != null || dataEmail != null) {
			report.put("message", "user already exist");
		} else {
			report.put("message", "user added");
			userRepo.save(user);	
		}
		return report.toString();
	}
	
	@GetMapping("users")
	public List<User> getUser() {
		return userRepo.findAll();
	}
	
	public static String GenerateToken(int targetStringLength) {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    return generatedString;
	}
	
	@PostMapping("login")
	public String loginUser(@RequestBody User user) {
		JSONObject json = new JSONObject();
		String tempUser = user.getUsername();
		
		String dataUser = userRepo.findByUsername(tempUser);
		if(dataUser == null) {
			json.put("message", "user not exist");
		} else {
			// cek user apakah sudah memiliki token
			String dataToken = tokenRepo.findByUsername(tempUser);
			if(dataToken != null) { // jika data token sudah ada
				JSONObject jsonToken = new JSONObject(dataToken);
				String token = jsonToken.getString("token");
				json.put("username", tempUser);
				json.put("token", token);
			} else { // jika data token belum ada
				String tempToken = GenerateToken(10);
				Token token = new Token();
				token.setUsername(tempUser);
				token.setToken(tempToken);
				tokenRepo.save(token);
				json.put("username", tempUser);
				json.put("token", tempToken);
			}
		}
		return json.toString();
	}

	@PostMapping("logout")
	public String logoutUser(@RequestBody Token token) {
		JSONObject json = new JSONObject();
		String userToken = token.getUsername();
		String dataUser = tokenRepo.findByUsername(userToken);
		if(dataUser != null) {
			JSONObject tmpToken = new JSONObject(dataUser);
			String usertoken = tmpToken.getString("_id");
			tokenRepo.deleteById(usertoken);
			json.put("message", "delete token success");
		} else {
			json.put("message", "token not found");
		}
		return json.toString();
	}
}
