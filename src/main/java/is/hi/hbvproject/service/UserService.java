package is.hi.hbvproject.service;

import java.util.List;
import java.util.Optional;

import is.hi.hbvproject.persistence.entities.User;

public interface UserService {
	void delete(User user);
	void deleteAll();
	long count();
	void deleteById(Long id);
	boolean existsById(Long id);
	boolean existsByUsername(String username);
	List<User> findAll();
	List<User> findAllById(Iterable<Long> ids);
	Optional<User> findById(Long id);
	User save(User user);
	void deleteAll(Iterable<? extends User> users);
}
