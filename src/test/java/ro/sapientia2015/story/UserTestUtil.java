package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

public class UserTestUtil {

	public static final Long ID = 1L;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    public static UserDTO createFormObject(Long id, String username, String password) {
    	UserDTO dto = new UserDTO();

        dto.setId(id);
        dto.setUsername(username);
        dto.setPassword(password);

        return dto;
    }
    
    public static User createModel(Long id, String username, String password) {
    	User model = User.getBuilder(username)
    			.password(password)
    			.build();
    	
    	ReflectionTestUtils.setField(model, "id", id);
    	
    	return model;
    }
    
}
