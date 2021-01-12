package ro.sapientia2015.scrum;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;

public class ScrumTestUtil {
	public static final Long ID = 1L;
    public static final String MEMBERS = "Mem1, Mem2";
    public static final String MEMBERS_UPDATED = "Mem1, Mem4";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";

    private static final String CHARACTER = "a";

    public static ScrumDTO createFormObject(Long id, String members, String title) {
    	ScrumDTO dto = new ScrumDTO();

        dto.setId(id);
        dto.setMembers(members);
        dto.setTitle(title);

        return dto;
    }

    public static Scrum createModel(Long id, String members, String title) {
    	Scrum model = Scrum.getBuilder(title)
                .members(members)
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
