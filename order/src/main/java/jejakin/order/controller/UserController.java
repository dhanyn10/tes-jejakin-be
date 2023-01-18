package jejakin.order.controller;

import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import jejakin.order.dao.TokenRepository;
import jejakin.order.dao.UserRepository;
import jejakin.order.model.User;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private TokenRepository tokenRepo;
	
	@PostMapping("adduser")
	public String saveUser (@RequestBody ObjectNode objectNode){
		JSONObject report = new JSONObject();
		User saveThis = new User();
		String jsonToken = objectNode.get("token").asText();
		saveThis.setUsername(objectNode.get("username").asText());
		saveThis.setFirstname(objectNode.get("firstname").asText());
		saveThis.setLastname(objectNode.get("lastname").asText());
		saveThis.setEmail(objectNode.get("email").asText());
		saveThis.setRole(objectNode.get("role").asText());
		
		// token untuk memastikan user mana yang menambahkan user baru
		String dataToken = tokenRepo.findByToken(jsonToken);
		if(dataToken == null) {
			report.put("message", "token invalid");
			return report.toString();
		}
		
		// konversi ke jsonobject
		JSONObject mytoken = new JSONObject(dataToken);
		// ambil data username dari json
		String username = mytoken.getString("username");
		
		// validasi role user dengan mengambil data dari collection: user
		String dataUser = userRepo.findByUsername(username);
		// pastikan data user masih ada sesuai dengan token
		if(dataUser == null) {
			report.put("message", "user with token: "+ dataToken+ "not found");
			return report.toString();
		}
		// data token terbukti masih sesuai, konversi ke jsonobject
		JSONObject myUser = new JSONObject(dataUser);
		String getRole = myUser.getString("role");
		if(!"admin".equals(getRole)) {
			report.put("message", "role not valid");
			return report.toString();
		}
		
		// cek user yang akan ditambahkan
		String tmpUser = userRepo.findByUsername(saveThis.getUsername());
		String tmpEmail = userRepo.findByEmail(saveThis.getEmail());
		if(tmpUser != null || tmpEmail != null) {
			report.put("message", "user already exist");
		} else {
			report.put("message", "user added");
			userRepo.save(saveThis);	
		}
		return report.toString();
	}
	
	@GetMapping("all")
	public List<User> getUser() {
		return userRepo.findAll();
	}
}
