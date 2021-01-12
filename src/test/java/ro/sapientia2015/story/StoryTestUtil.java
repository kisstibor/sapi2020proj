package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;

public class StoryTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final Long USER_ID = 2L;
    public static final Long USER_ID_UPDATED = 3L;
    public static final String QUERY_TEXT = "query";

    public static StoryDTO createFormObject(Long id, String description, String title) {
    	return createFormObject(id, description, title, null);
    }
    
    public static StoryDTO createFormObject(Long id, String description, String title, Long userId) {
        StoryDTO dto = new StoryDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        dto.setUserId(userId);

        return dto;
    }
    
    public static Story createModel(Long id, String description, String title) {
    	return createModel(id, description, title, null);
    }

    public static Story createModel(Long id, String description, String title, User user) {
        Story model = Story.getBuilder(title)
                .description(description)
                .user(user)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

}
