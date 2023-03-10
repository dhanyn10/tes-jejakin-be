package jejakin.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("orders")
public class OrderController {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private TokenRepository tokenRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String myOrder (@PathVariable(value="id")String id) {
		List<Order> order = orderRepo.findAllByUserId(id);
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		for(int i = 0; i < order.size(); i++) {
			String productId = order.get(i).getProductId();
			Optional<Product> productData = productRepo.findById(productId);
			String productName = productData.get().getName().toString();
			JSONObject myorder = new JSONObject();
			myorder.put("id", order.get(i).getId());
			myorder.put("userId", order.get(i).getUserId());
			myorder.put("productId", order.get(i).getProductId());
			myorder.put("productName", productName);
			myorder.put("amount", order.get(i).getAmount());
			myorder.put("status", order.get(i).getStatus());
			arr.add(myorder);
		}
		return arr.toString();
	}
	
	@DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deleteOrder (@PathVariable(value="id")String id) {
		JSONObject report = new JSONObject();
		Optional<Order> orderId = orderRepo.findById(id);
		if(orderId.isPresent()) {
			report.put("message", "success delete order");
			report.put("deletedOrder", id);
			orderRepo.deleteById(id);
		} else {
			report.put("message", "order not found");
		}
		return report.toString();
	}
}
