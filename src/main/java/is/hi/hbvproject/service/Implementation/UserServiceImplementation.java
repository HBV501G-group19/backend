package is.hi.hbvproject.service.Implementation;
import is.hi.hbvproject.service.UserService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
	
	UserRepository userRepository;
	
	@Autowired
	public UserServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public void	delete(User user) {
		userRepository.delete(user);
	};
	
	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	};
	
	@Override
	public long count() {
		return userRepository.count();
	};
	
	@Override
	public void	deleteById(Long id) {
		userRepository.deleteById(id);
	};
	
	@Override
	public boolean existsById(Long id) {
		return userRepository.existsById(id);
	};
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	};
	
	@Override
	public List<User> findAllById(Iterable<Long> ids) {
		return userRepository.findAllById(ids);
	};
	
	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	};
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	};
	
	@Override
	public void deleteAll(Iterable<? extends User> users) {
		userRepository.deleteAll(users);
	};
}
