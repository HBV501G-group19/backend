package hi.is.hbvproject.hbvproject.persistence.repositories;
import hi.is.hbvproject.hbvproject.persistence.enties.User;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

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
