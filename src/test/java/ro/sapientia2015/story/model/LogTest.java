package ro.sapientia2015.story.model;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import ro.sapientia2015.story.model.Log;
import static junit.framework.Assert.*;

public class LogTest {
	  private String TITLE = "title";
	    private String DESCRIPTION = "description";
	    private String ASSIGNTO = "Lorand";
	    private String STATUS = "DONE";
	    private String DOC = "doc.txt";

	    @Test
	    public void buildWithMandatoryInformation() {
	        Log built = Log.getBuilder(TITLE).assignTo(ASSIGNTO, STATUS).build();

	        assertNull(built.getId());
	        assertNull(built.getCreationTime());
	        assertNull(built.getDescription());
	        assertNull(built.getModificationTime());
	        assertEquals(TITLE, built.getTitle());
	        assertEquals(ASSIGNTO, built.getAssignTo());
	        assertEquals(STATUS, built.getStatus());
	        assertEquals(0L, built.getVersion());
	    }

	    @Test
	    public void buildWithAllInformation() {
	        Log built = Log.getBuilder(TITLE)
	                .description(DESCRIPTION)
	                .assignTo(ASSIGNTO, STATUS)
	                .doc(DOC)
	                .build();

	        assertNull(built.getId());
	        assertNull(built.getCreationTime());
	        assertEquals(DESCRIPTION, built.getDescription());
	        assertEquals(ASSIGNTO, built.getAssignTo());
	        assertNull(built.getModificationTime());
	        assertEquals(TITLE, built.getTitle());
	        assertEquals(STATUS, built.getStatus());
	        assertEquals(DOC, built.getDoc());
	        assertEquals(0L, built.getVersion());
	    }

	    @Test
	    public void prePersist() {
	        Log log = new Log();
	        log.prePersist();

	        assertNull(log.getId());
	        assertNotNull(log.getCreationTime());
	        assertNull(log.getDescription());
	        assertNotNull(log.getModificationTime());
	        assertNull(log.getTitle());
	        assertEquals(0L, log.getVersion());
	        assertEquals(log.getCreationTime(), log.getModificationTime());
	    }

	    @Test
	    public void preUpdate() {
	        Log log = new Log();
	        log.prePersist();

	        pause(1000);

	        log.preUpdate();

	        assertNull(log.getId());
	        assertNotNull(log.getCreationTime());
	        assertNull(log.getDescription());
	        assertNotNull(log.getModificationTime());
	        assertNull(log.getTitle());
	        assertNull(log.getAssignTo());
	        assertNull(log.getStatus());
	        assertEquals(0L, log.getVersion());
	        assertTrue(log.getModificationTime().isAfter(log.getCreationTime()));
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
