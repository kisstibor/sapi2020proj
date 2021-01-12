package ro.sapientia2015.scrum.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

public class ScrumTest {
	private String TITLE = "title";
    private String MEMBERS = "Mem1, Mem2";

    @Test
    public void buildWithMandatoryInformation() {
    	Scrum built = Scrum.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertEquals(TITLE, built.getTitle());
        assertNull(MEMBERS, built.getMembers());
    }

    @Test
    public void buildWithAllInformation() {
    	Scrum built = Scrum.getBuilder(TITLE)
                .members(MEMBERS)
                .build();

    	assertNull(built.getId());
        assertEquals(TITLE, built.getTitle());
        assertEquals(MEMBERS, built.getMembers());
    }
}
