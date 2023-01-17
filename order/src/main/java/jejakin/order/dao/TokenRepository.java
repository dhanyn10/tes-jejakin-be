package jejakin.order.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jejakin.order.model.Token;

@Repository
public interface TokenRepository extends MongoRepository <Token, String> {

	String findByUsername(String tempUser);

	String findByToken(String token);

}
