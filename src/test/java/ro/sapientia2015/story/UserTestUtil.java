package ro.sapientia2015.story;

import ro.sapientia2015.story.dto.UserDTO;

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
    
}
