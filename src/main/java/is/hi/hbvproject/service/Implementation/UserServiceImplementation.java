package is.hi.hbvproject.service.Implementation;

import is.hi.hbvproject.service.UserService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.repositories.UserRepository;

import static java.util.Collections.emptyList;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

	UserRepository userRepository;

	@Autowired
	public UserServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void delete(User user) {
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
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	};

	@Override
	public boolean existsById(Long id) {
		return userRepository.existsById(id);
	};

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

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

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
				emptyList());
	}
}
