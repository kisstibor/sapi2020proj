package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;

public class UserTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final Long EXPERIENCE = 0L;
    public static final Long EXPERIENCE_UPDATED = 1L;
    		

    private static final String CHARACTER = "a";

    public static UserDTO createFormObject(Long id, String description, String title, Long experience) {
        UserDTO dto = new UserDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        dto.setExperience(experience);

        return dto;
    }

    public static User createModel(Long id, String description, String title, Long experience) {
        User model = User.getBuilder(title)
                .description(description)
                .experience(experience)
                .build();

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
