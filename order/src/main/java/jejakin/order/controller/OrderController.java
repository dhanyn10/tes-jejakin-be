package jejakin.order.controller;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import jejakin.order.dao.OrderRepository;
import jejakin.order.dao.ProductRepository;
import jejakin.order.dao.TokenRepository;
import jejakin.order.dao.UserRepository;
import jejakin.order.model.Order;
import jejakin.order.model.Product;
import jejakin.order.model.User;

@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private TokenRepository tokenRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@PostMapping("add")
	public String addOrder (@RequestBody ObjectNode objectNode) {
		JSONObject report = new JSONObject();
		
		String token = objectNode.get("token").asText();
		String userId = objectNode.get("userId").asText();
		String productId = objectNode.get("productId").asText();
		int amount = objectNode.get("amount").asInt();
		String status = objectNode.get("status").asText();
		
		// memastikan user telah login menggunakan token
		String dataToken = tokenRepo.findByToken(token);
		if(dataToken == null) {
			report.put("message", "token not found, you need to login first");
			return report.toString();
		}
		
		// cek userId tersedia di database
		Optional<User> dataUser = userRepo.findById(userId);
		if(dataUser.isEmpty()) {
			report.put("message", "user with id [" + userId + "] not found");
			return report.toString();
		}
		// ambil data username dari database
		String username = dataUser.get().getUsername().toString();

		// cek token sesuai dengan userId, untuk memastikan user terkait sudah login
		JSONObject cekUser = new JSONObject(dataToken);
		// data username dari token
		String usernameToken = cekUser.getString("username");
	
		// data token tidak sesuai dengan username
		if(!usernameToken.equals(username)) {
			report.put("message", "wrong token");
			return report.toString();
		}

		// cek productId tersedia di database
		Optional<Product> dataProduct = productRepo.findById(productId);
		if(dataProduct.isEmpty()) {
			report.put("message", "product with id [" + productId + "] not found");
			return report.toString();
		}
		
		Order order = new Order();
		order.setUserId(userId);
		order.setProductId(productId);
		order.setAmount(amount);
		order.setStatus(status);
		
		orderRepo.save(order);
		report.put("message", "order created");
		return report.toString();
	}
}
