package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.model.Label;

public class LabelTestUtil {

    public static final Long ID = 1L;
    public static final String TITLE = "title";

    private static final String CHARACTER = "a";

    public static LabelDTO createFormObject(Long id, String title) {
        LabelDTO dto = new LabelDTO();

        dto.setId(id);
        dto.setTitle(title);

        return dto;
    }

    public static Label createModel(Long id, String title) {
        Label model = Label.getBuilder(title)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
