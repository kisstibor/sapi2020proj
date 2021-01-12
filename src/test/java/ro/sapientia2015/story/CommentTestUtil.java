package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;

public class CommentTestUtil {

    public static final Long ID = 1L;
    public static final Long STORY_ID = 1L;

    public static final String MESSAGE = "description";
    public static final String MESSAGE_UPDATED = "updatedDescription";
    public static final Story STORY = getStory();

    private static final String CHARACTER = "a";

    private static Story getStory() {
    	Story result = Story.getBuilder("title").build();
        ReflectionTestUtils.setField(result, "id", STORY_ID);

        return result;
    }

    public static CommentDTO createFormObject(Long id, String message, Long storyId) {
    	CommentDTO dto = new CommentDTO();

        dto.setId(id);
        dto.setMessage(message);
        dto.setStoryId(storyId);

        return dto;
    }

    public static Comment createModel(Long id, String message, Story story) {
    	Comment model = Comment.getBuilder(message)
                .build();

    	model.setStory(story);

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
