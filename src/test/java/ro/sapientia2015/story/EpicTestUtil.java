package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.model.Epic;


public class EpicTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";

    private static final String CHARACTER = "a";
	public static final String TITLE_UPDATED = "updatedTitle";
	public static final String DESCRIPTION_UPDATED = "updatedDescription";

    public static EpicDTO createFormObject(Long id, String description, String title) {
    	EpicDTO dto = new EpicDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);

        return dto;
    }

    public static Epic createModel(Long id, String description, String title) {
    	Epic model = Epic.getBuilder(title)
                .description(description)
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
