package jejakin.order.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Document(collection="order")
public class Order {

	@MongoId(FieldType.STRING)
	private String id;
	private String userId;
	private String productId;
	private int amount;
	private String status;
}
