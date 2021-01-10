package ro.sapientia2015.story.model;

import org.junit.Test;

import static junit.framework.Assert.*;

public class StoryNewTest {

	@Test
	public void testTitle(){
		Story story = Story.getBuilder("Hello").build();
		assertNotNull(story.getTitle());
	}
	
	@Test
	public void testBuildingStoryWithSpentTime() {
		Story story = Story.getBuilder("Story").time("4").build();
		assertEquals(story.getTitle(), "Story");
		assertEquals(story.getTime(), "4");
		assertEquals(story.getDescription(), null);
	}
	
	@Test
	public void testUpdatingWithSpentTime() {
		Story story = Story.getBuilder("MyStoryTitle").build();
		assertEquals(story.getTitle(), "MyStoryTitle");
		assertEquals(story.getTime(), null);
		assertEquals(story.getDescription(), null);
		story.update("myNewWonderfullDescription", "This too changed", "2");
		assertEquals(story.getTitle(), "This too changed");
		assertEquals(story.getTime(), "2");
		assertEquals(story.getDescription(), "myNewWonderfullDescription");
	}
}
