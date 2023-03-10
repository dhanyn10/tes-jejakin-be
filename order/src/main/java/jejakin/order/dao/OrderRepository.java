package jejakin.order.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jejakin.order.model.Order;

@Repository
public interface OrderRepository extends MongoRepository <Order, String>{

	List<Order> findAllByUserId(String id);

}
