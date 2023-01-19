package jejakin.order.controller;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jejakin.order.dao.ProductRepository;
import jejakin.order.model.Product;
import net.datafaker.Faker;

@RestController
@RequestMapping("products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping("generate/{value}")
	public String generateProduct(@PathVariable(value="value")int value) {
		Faker faker = new Faker(new Locale("en-US"));
		JSONObject report = new JSONObject();
		for(int x = 0; x < value; x++) {
			String fakeBook = faker.book().title();
			Product product = new Product();
			product.setName(fakeBook);
			productRepo.save(product);
		}
		report.put("message", "success generate data product");
		return report.toString();
	}
	
	@GetMapping("all")
	public List<Product> getProduct() {
		return productRepo.findAll();
	}
}
