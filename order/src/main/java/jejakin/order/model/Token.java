package jejakin.order.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Document(collection="token")
public class Token {
	
	@MongoId(FieldType.STRING)
	private String id;
	private String username;
	private String token;

}
