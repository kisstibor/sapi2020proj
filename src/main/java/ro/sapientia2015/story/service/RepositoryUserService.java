package ro.sapientia2015.story.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

@Service
public class RepositoryUserService implements UserService {

	@Resource
    private UserRepository repository;

	@Override
	public User add(UserDTO added) {
		User model = User.getBuilder(added.getUsername())
				.password(added.getPassword())
				.build();
		
		return this.repository.save(model);
	}
	
}
