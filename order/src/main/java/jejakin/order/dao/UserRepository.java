package jejakin.order.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import jejakin.order.model.User;

public interface UserRepository extends MongoRepository<User, Integer>{

}
