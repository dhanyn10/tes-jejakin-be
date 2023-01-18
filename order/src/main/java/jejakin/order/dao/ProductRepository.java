package jejakin.order.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jejakin.order.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

}
