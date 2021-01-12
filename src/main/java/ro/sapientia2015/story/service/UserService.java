package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

public interface UserService {
	
	/**
     * Adds a new user entry.
     * @param added The information of the added user entry.
     * @return  The added user entry.
     */
    public User add(UserDTO added);
    
    /**
     * Returns a list of user entries.
     * @return The list of user entries.
     */
    public List<User> findAll();

}
