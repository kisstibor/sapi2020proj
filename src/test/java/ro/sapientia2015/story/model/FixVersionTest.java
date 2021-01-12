package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class FixVersionTest {

	private String NAME = "name";
	
	@Test
    public void buildWithMandatoryInformation() {
        FixVersion built = FixVersion.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }
	
	@Test
    public void prePersist() {
        FixVersion fixVersion = new FixVersion();
        fixVersion.prePersist();

        assertNull(fixVersion.getId());
        assertNotNull(fixVersion.getCreationTime());
        assertNull(fixVersion.getModificationTime());
        assertNull(fixVersion.getName());
        assertEquals(0L, fixVersion.getVersion());
    }
	
	@Test
    public void preUpdate() {
		FixVersion fixVersion = new FixVersion();
        fixVersion.prePersist();

        pause(1000);

        fixVersion.preUpdate();

        assertNull(fixVersion.getId());
        assertNotNull(fixVersion.getCreationTime());
        assertNotNull(fixVersion.getModificationTime());
        assertNull(fixVersion.getName());
        assertEquals(0L, fixVersion.getVersion());
        assertTrue(fixVersion.getModificationTime().isAfter(fixVersion.getCreationTime()));
    }
	
	@Test
    public void update() {
		FixVersion fixVersion = new FixVersion();
        fixVersion.update(NAME);

        assertNull(fixVersion.getId());
        assertNull(fixVersion.getCreationTime());
        assertNull(fixVersion.getModificationTime());
        assertEquals(NAME, fixVersion.getName());
        assertEquals(0L, fixVersion.getVersion());
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
