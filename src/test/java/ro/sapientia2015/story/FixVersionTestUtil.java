package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.model.FixVersion;

public class FixVersionTestUtil {

	public static final Long ID = 1L;
    public static final String NAME = "name";
    public static final String NAME_UPDATED = "updatedName";

    private static final String CHARACTER = "a";

    public static FixVersionDTO createFormObject(Long id, String name) {
    	FixVersionDTO dto = new FixVersionDTO();

        dto.setId(id);
        dto.setName(name);

        return dto;
    }

    public static FixVersion createModel(Long id, String name) {
        FixVersion model = FixVersion.getBuilder(name)
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
