package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

@Service
public class RepositoryUserService implements UserService {

	@Resource
    private UserRepository repository;

	@Transactional
	@Override
	public User add(UserDTO added) {
		User model = User.getBuilder(added.getUsername())
				.password(added.getPassword())
				.build();
		
		return this.repository.save(model);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return this.repository.findAll();
	}
	
}
