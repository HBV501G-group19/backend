package is.hi.hbvproject.service.Implementation;

import java.util.Optional;
import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.repositories.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

  UserRepository userRepository;
	
	@Autowired
	public UserDetailsServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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