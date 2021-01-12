package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.project.model.Project;

import static junit.framework.Assert.*;


public class ProjectTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";

    @Test
    public void buildWithMandatoryInformation() {
        Project built = Project.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Project built = Project.getBuilder(TITLE)
                .description(DESCRIPTION)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        Project Project = new Project();
        Project.prePersist();

        assertNull(Project.getId());
        assertNotNull(Project.getCreationTime());
        assertNull(Project.getDescription());
        assertNotNull(Project.getModificationTime());
        assertNull(Project.getTitle());
        assertEquals(0L, Project.getVersion());
        assertEquals(Project.getCreationTime(), Project.getModificationTime());
    }

    @Test
    public void preUpdate() {
        Project Project = new Project();
        Project.prePersist();

        pause(1000);

        Project.preUpdate();

        assertNull(Project.getId());
        assertNotNull(Project.getCreationTime());
        assertNull(Project.getDescription());
        assertNotNull(Project.getModificationTime());
        assertNull(Project.getTitle());
        assertEquals(0L, Project.getVersion());
        assertTrue(Project.getModificationTime().isAfter(Project.getCreationTime()));
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
