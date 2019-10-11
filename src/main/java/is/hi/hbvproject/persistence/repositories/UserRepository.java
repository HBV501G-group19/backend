package is.hi.hbvproject.persistence.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import is.hi.hbvproject.persistence.entities.User;

@Repository
public interface UserRepository {
	public User save(User user);
	public Optional<User> findById(long id);
	public User deleteById(long id);
	public boolean existsById(long id);
	public List<User> findAll();
	public List<User> findAllById(Iterable<Long> ids);
	public List<User> deleteUsers(Iterable<Long> ids);
	public List<User> deleteAll(); 
}
