package ImageHoster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ImageHoster.model.User;
import ImageHoster.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public void registerUser(User newUser) {
		userRepository.registerUser(newUser);
	}

	// This method returned user object if the username and password combination
	// exists in database else null.
	public User login(User user) {
		// Complete the method
		return userRepository.checkUser(user.getUsername(), user.getPassword());
	}
}
