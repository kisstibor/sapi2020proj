package ro.sapientia2015.story;

import org.joda.time.DateTime;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.model.Comment;

public class CommentTestUtil {
	
	public static final Long ID = 1L;
	public static final String MESSAGE = "test message";
	public static final String MESSAGE_UPDATED = "updated test message";
	
	private static final String CHARACTER = "a";


    public static CommentDTO createFormObject(Long id, String message) {
    	CommentDTO dto = new CommentDTO();

        dto.setId(id);
        dto.setMessage(message);
        
        return dto;
    }

    public static Comment createModel(Long id, String message) {
    	Comment model = Comment.getBuilder().message(message).build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
	

}
