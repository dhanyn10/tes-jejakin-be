package jejakin.order.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jejakin.order.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer>{

	String findUserByUsername(String tempUser);

}
