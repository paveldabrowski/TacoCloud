package tacos.data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tacos.User;


public interface UserRepository extends CrudRepository<User, Long> {

  User findByUsername(String username);
  
}
