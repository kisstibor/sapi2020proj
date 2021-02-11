package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import ro.sapientia2015.task.model.Task;

public class StoryWithTask {
	
	private String TITLE = "title";
    private String DESCRIPTION = "description";
    
    @Test
    public void buildWithTaskInformation() {
    	ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.add(Task.getBuilder("Task1").description(DESCRIPTION).build());
        tasks.add(Task.getBuilder("Task2").description(DESCRIPTION).build());
    	
    	Story built = Story.getBuilder(TITLE)
    			.description(DESCRIPTION)
    			.tasks(tasks)
    			.build();

        assertNull(built.getId());
        assertEquals(TITLE, built.getTitle());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(tasks,built.getTasks());
        
        assertEquals("Task1", built.getTasks().get(0).getTitle());
        
        assertEquals("Task2", built.getTasks().get(1).getTitle());
    }
}
