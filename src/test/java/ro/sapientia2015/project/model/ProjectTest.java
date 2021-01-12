package ro.sapientia2015.project.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

public class ProjectTest {

	private String NAME = "name";
	private String PRODUCTOWNER = "productOwner";

	@Test
	public void buildWithMandatoryInformation() {
		Project built = Project.getBuilder(NAME).build();

		assertNull(built.getId());
		assertNull(built.getProductOwner());
		assertNull(built.getScrumMaster());
		assertNull(built.getMembers());
		assertEquals(0L, built.getVersion());
	}

	@Test
	public void buildWithAllInformation() {
		Project built = Project.getBuilder(NAME).productOwner(PRODUCTOWNER).build();

		assertNull(built.getId());
		assertNull(built.getScrumMaster());
		assertEquals(NAME, built.getName());
		assertNull(built.getMembers());
		assertEquals(PRODUCTOWNER, built.getProductOwner());
		assertEquals(0L, built.getVersion());
	}
}
