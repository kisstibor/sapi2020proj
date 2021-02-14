package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Task;
import static junit.framework.Assert.*;

public class TaskTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";
    private String PRIORITY = "priority";

    @Test
    public void buildWithMandatoryInformation() {
        Task built = Task.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getPriority());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Task built = Task.getBuilder(TITLE)
                .description(DESCRIPTION)
                .priority(PRIORITY)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(PRIORITY, built.getPriority());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        Task Task = new Task();
        Task.prePersist();

        assertNull(Task.getId());
        assertNotNull(Task.getCreationTime());
        assertNull(Task.getDescription());
        assertNull(Task.getPriority());
        assertNotNull(Task.getModificationTime());
        assertNull(Task.getTitle());
        assertEquals(0L, Task.getVersion());
        assertEquals(Task.getCreationTime(), Task.getModificationTime());
    }

    @Test
    public void preUpdate() {
        Task Task = new Task();
        Task.prePersist();

        pause(1000);

        Task.preUpdate();

        assertNull(Task.getId());
        assertNotNull(Task.getCreationTime());
        assertNull(Task.getDescription());
        assertNull(Task.getPriority());
        assertNotNull(Task.getModificationTime());
        assertNull(Task.getTitle());
        assertEquals(0L, Task.getVersion());
        assertTrue(Task.getModificationTime().isAfter(Task.getCreationTime()));
    }

    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}