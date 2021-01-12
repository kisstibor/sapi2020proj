package ro.sapientia2015.scrumteam.model;

import org.junit.Test;

import ro.sapientia2015.scrumteam.model.ScrumTeam;

import static junit.framework.Assert.*;


public class ScrumTeamTest {

	public final Long ID = 1L;
	private final String NAME = "name";
	private final String MEMBERS = "member1, member2, member3";
	
	@Test
    public void buildWithMandatoryInformation() {
        ScrumTeam built = ScrumTeam.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getMembers());
        assertEquals(NAME, built.getName());
        assertNull(built.getStories());
        assertNull(built.getScrumOfScrums());
        assertEquals(new Integer(0), built.getStoryCount());
        assertNull(built.getStoriesSeparated());
    }
	
	@Test
    public void buildWithALLInformation() {
		ScrumTeam built = ScrumTeam.getBuilder(NAME)
				.members(MEMBERS)
				.build();

        assertNull(built.getId());
        assertEquals(MEMBERS, built.getMembers());
        assertEquals(NAME, built.getName());
        assertNull(built.getStories());
        assertNull(built.getScrumOfScrums());
        assertEquals(new Integer(0), built.getStoryCount());
        assertNull(built.getStoriesSeparated());
    }
	
	
}
