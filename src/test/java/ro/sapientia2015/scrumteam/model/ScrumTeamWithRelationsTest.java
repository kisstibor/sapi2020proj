package ro.sapientia2015.scrumteam.model;

import org.junit.Test;

import ro.sapientia2015.scrumofscrums.model.ScrumOfScrums;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.story.model.Story;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.awt.List;
import java.util.ArrayList;


public class ScrumTeamWithRelationsTest {
	
	public final Long ID = 1L;
	private final String NAME = "name";
	private final String MEMBERS = "member1, member2, member3";
	public final Integer PROGRESS_BAR_MAX = 10;
	public final Integer PROGRESS_BAR_DONE = 6;
	public final Integer PROGRESS_BAR_NOT_DONE = 4;
	
	//
	// Relation with: Story
	//
	
	@Test
    public void buildWithStoriesInformation() {
		
		ArrayList<Story> stories = new ArrayList<Story>();
		stories.add(Story.getBuilder("story1").description("desc1").progress(1).build());
		stories.add(Story.getBuilder("story2").description("desc2").progress(2).build());
		
		ScrumTeam built = ScrumTeam.getBuilder(NAME)
				.members(MEMBERS)
				.stories(stories)
				.build();

        assertNull(built.getId());
        assertEquals(MEMBERS, built.getMembers());
        assertEquals(NAME, built.getName());
        assertNull(built.getScrumOfScrums());
        assertEquals(new Integer(2), built.getStoryCount());
        assertNotNull(built.getStoriesSeparated());
        // Story 1
        assertEquals("story1", built.getStories().get(0).getTitle());
        assertEquals("desc1", built.getStories().get(0).getDescription());
        assertEquals(new Integer(1), built.getStories().get(0).getProgress());
        // Story 2
        assertEquals("story2", built.getStories().get(1).getTitle());
        assertEquals("desc2", built.getStories().get(1).getDescription());
        assertEquals(new Integer(2), built.getStories().get(1).getProgress());
    }
	
	//
	// Relation with: Story and Scrum Of Scrums
	//
	
	@Test
    public void buildWithScrumOfScrumsInformation() {
		
		ArrayList<Story> stories1 = new ArrayList<Story>();
		ArrayList<Story> stories2 = new ArrayList<Story>();
		
		Story story1 = Story.getBuilder("story1").description("desc1").progress(1).build();
		Story story2 = Story.getBuilder("story2").description("desc2").progress(2).build();
		Story story3 = Story.getBuilder("story3").description("desc3").progress(3).build();
		stories1.add(story1);
		stories1.add(story2);
		stories2.add(story3);
		
		ScrumTeam scrumTeam1 = ScrumTeam.getBuilder(NAME) // story: 1, 2
				.members(MEMBERS)
				.stories(stories1)
				.build();
		
		ScrumTeam scrumTeam2 = ScrumTeam.getBuilder(NAME) // story: 3
				.members(MEMBERS)
				.stories(stories2)
				.build();
		
		ArrayList<ScrumTeam> scrumTeams = new ArrayList<ScrumTeam>();
		scrumTeams.add(scrumTeam1);
		scrumTeams.add(scrumTeam2);

		ScrumOfScrums sos = ScrumOfScrums.getBuilder("sos").scrumTeams(scrumTeams).description("desc").build();

		
		assertNotNull(sos);
		assertNull(sos.getId());
		assertNotNull(sos.getScrumTeams());
		assertEquals("sos", 			sos.getName());
		assertEquals("desc", 			sos.getDescription());
        // Scrum Team 1 -> Story 1
        assertEquals("story1", 			sos.getScrumTeams().get(0).getStories().get(0).getTitle());
        assertEquals("desc1", 			sos.getScrumTeams().get(0).getStories().get(0).getDescription());
        assertEquals(new Integer(1), 	sos.getScrumTeams().get(0).getStories().get(0).getProgress());
        // Scrum Team 1 -> Story 2
        assertEquals("story2", 			sos.getScrumTeams().get(0).getStories().get(1).getTitle());
        assertEquals("desc2", 			sos.getScrumTeams().get(0).getStories().get(1).getDescription());
        assertEquals(new Integer(2), 	sos.getScrumTeams().get(0).getStories().get(1).getProgress());
        // Scrum Team 2 -> Story 1
        assertEquals("story3", 			sos.getScrumTeams().get(1).getStories().get(0).getTitle());
        assertEquals("desc3", 			sos.getScrumTeams().get(1).getStories().get(0).getDescription());
        assertEquals(new Integer(3), 	sos.getScrumTeams().get(1).getStories().get(0).getProgress());
    }
	

}
