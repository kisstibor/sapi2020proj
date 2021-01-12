package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Task;
import static junit.framework.Assert.*;

public class TaskTest {
	private String TITLE = "title";
	private String DESCRIPTION = "description";

	@Test
	public void buildWithMandatoryInformation() {
	Task built = Task.getBuilder(TITLE).build();

	assertNull(built.getId());
	assertNull(built.getCreationTime());
	assertNull(built.getDescription());
	assertEquals(TITLE, built.getTitle());
	}

	
	@Test
	public void buildWithAllInformation() {
		Task built = Task.getBuilder(TITLE)
				.description(DESCRIPTION)
				.build();

		assertNull(built.getId());
		assertNull(built.getState());
	}

	@Test
	public void prePersist() {
	Task task = new Task();
	task.prePersist();

	assertNull(task.getId());
	assertNull(task.getState());
	assertNull(task.getDescription());
	}

	

	@Test
	public void preUpdate() {
		Task task = new Task();
		task.prePersist();
		pause(1000);

		assertNull(task.getId());
		assertNotNull(task.getCreationTime());
		assertNull(task.getDescription());
		assertNull(task.getTitle());
		}


	private void pause(long timeInMillis) {
		try {
			Thread.currentThread().sleep(timeInMillis);
			}
		catch (InterruptedException e) {

			}
		}
}
