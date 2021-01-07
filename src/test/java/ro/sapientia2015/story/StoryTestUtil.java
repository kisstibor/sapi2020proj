package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Story;

public class StoryTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";

    public static StoryDTO createFormObject(Long id, String description, String title) {
        StoryDTO dto = new StoryDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);

        return dto;
    }

    public static Story createModel(Long id, String description, String title) {
        Story model = Story.getBuilder(title)
                .description(description)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

}
