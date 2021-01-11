package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

public class BugTest {
	
	private String TITLE = "title";
    private String DESCRIPTION = "description";

    @Test
    public void buildWithMandatoryInformation() {
        Bug built = Bug.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getDescription());
        assertEquals(TITLE, built.getTitle());
    }

    @Test
    public void buildWithAllInformation() {
    	Bug built = Bug.getBuilder(TITLE)
                .description(DESCRIPTION)
                .build();

        assertNull(built.getId());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(TITLE, built.getTitle());
    }

}
