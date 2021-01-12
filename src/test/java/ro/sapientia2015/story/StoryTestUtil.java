package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;
import static org.mockito.Mockito.*;

public class StoryTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final Long FIXVERSIONID = 1L;
    public static FixVersion FIXVERSION= mock(FixVersion.class);

    private static final String CHARACTER = "a";

    public static StoryDTO createFormObject(Long id, String description, String title, Long fixVersion) {
        StoryDTO dto = new StoryDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        dto.setFixVersion(fixVersion);

        return dto;
    }

    public static Story createModel(Long id, String description, String title, FixVersion fixVersion) {
        Story model = Story.getBuilder(title)
                .description(description)
                .fixVersion(fixVersion)
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
