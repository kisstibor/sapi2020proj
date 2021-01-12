package ro.sapientia2015.project;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;

public class ProjectTestUtil {

	public static final Long ID = 1L;

	public static final String NAME = "name";
	public static final String NAME_UPDATED = "updatedName";

	public static final String PRODUCTOWNER = "productOwner";
	public static final String PRODUCTOWNER_UPDATED = "updatedProductOwner";

	public static final String SCRUMMASTER = "scrumMaster";
	public static final String SCRUMMASTER_UPDATED = "updatedScrumMaster";

	public static final String MEMBERS = "members";
	public static final String MEMBERS_UPDATED = "updatedMembers";

	private static final String CHARACTER = "a";

	public static ProjectDTO createFormObject(Long id, String name, String productOwner, String scrumMaster,
			String members) {
		ProjectDTO dto = new ProjectDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setProductOwner(productOwner);
		dto.setScrumMaster(scrumMaster);
		dto.setMembers(members);
		return dto;
	}

	public static Project createModel(Long id, String name, String productOwner, String scrumMaster, String members) {
		Project model = Project.getBuilder(name).productOwner(productOwner).scrumMaster(scrumMaster)
				.members(members).build();
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
