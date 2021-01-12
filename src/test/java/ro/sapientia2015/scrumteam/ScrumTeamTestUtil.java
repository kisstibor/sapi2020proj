package ro.sapientia2015.scrumteam;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;

public class ScrumTeamTestUtil {
	
	public static final Long ID = 1L;
    public static final String MEMBERS = "members";
    public static final String UPDATED_MEMBERS = "updatedMembers";
    public static final String NAME = "NAME";
    public static final String NAME_UPDATED = "updatedName";

    private static final String CHARACTER = "a";

    public static ScrumTeamDTO createFormObject(Long id, String name, String members) {
    	ScrumTeamDTO dto = new ScrumTeamDTO();

        dto.setId(id);
        dto.setMembers(members);
        dto.setName(name);

        return dto;
    }

    public static ScrumTeam createModel(Long id, String name, String members) {
    	ScrumTeam model = ScrumTeam.getBuilder(name)
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
