package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNotNull;


import org.junit.Test;

public class DailyTest {
	private String TITLE = "title";
    private String DESCRIPTION = "description";
    private String DURATION = "duration";
    private String DATEE = "datee";
    
    @Test
    public void buildWithTitle() {
    	Daily d = Daily.getBuilder(TITLE).build();
    	
    	assertNotNull(d.getTitle());
    	assertNull(d.getId());
        assertNull(d.getCreationTime());
        assertNull(d.getDescription());
        assertNull(d.getDuration());
        assertNull(d.getDatee());
        assertNull(d.getModificationTime());
        assertEquals(TITLE, d.getTitle());
        assertEquals(0L, d.getVersion());
    }
    
    @Test
    public void buildWithTitleAndDateeAndDuration() {
    	Daily d = Daily.getBuilder(TITLE)
    			.datee(DATEE)
    			.duration(DURATION)
    			.build();
    	
    	assertNotNull(d.getTitle());
    	assertNull(d.getId());
        assertNull(d.getCreationTime());
        assertNull(d.getDescription());
        assertNotNull(d.getDuration());
        assertNotNull(d.getDatee());
        assertNull(d.getModificationTime());
        assertEquals(TITLE, d.getTitle());
        assertEquals(DATEE, d.getDatee());
        assertEquals(DURATION, d.getDuration());
        assertEquals(0L, d.getVersion());
    }
    
    @Test
    public void update() {
    	Daily d1 = Daily.getBuilder(TITLE)
    			.datee(DATEE)
    			.description(DESCRIPTION)
    			.duration(DURATION)
    			.build();
    	
    	assertNotNull(d1.getTitle());
    	assertNull(d1.getId());
        assertNull(d1.getCreationTime());
        assertNotNull(d1.getDescription());
        assertNotNull(d1.getDuration());
        assertNotNull(d1.getDatee());
        assertNull(d1.getModificationTime());
        assertEquals(TITLE, d1.getTitle());
        assertEquals(DATEE, d1.getDatee());
        assertEquals(DURATION, d1.getDuration());
        assertEquals(DESCRIPTION, d1.getDescription());
        assertEquals(0L, d1.getVersion());
        
    	d1.update("description", "title", "datee", "duration");
    	
    	assertNotNull(d1.getTitle());
    	assertNull(d1.getId());
        assertNull(d1.getCreationTime());
        assertNotNull(d1.getDescription());
        assertNotNull(d1.getDuration());
        assertNotNull(d1.getDatee());
        assertNull(d1.getModificationTime());
        assertEquals("title", d1.getTitle());
        assertEquals("datee", d1.getDatee());
        assertEquals("duration", d1.getDuration());
        assertEquals("description", d1.getDescription());
        assertEquals(0L, d1.getVersion());
    }
    
    @Test
    public void preUpdate() {
        Daily d = new Daily();
        d.prePersist();

        try {
			Thread.currentThread();
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        d.preUpdate();
        assertNull(d.getId());
        assertNotNull(d.getCreationTime());
        assertNull(d.getDescription());
        assertNull(d.getDatee());
        assertNull(d.getDuration());
        assertNotNull(d.getModificationTime());
        assertNull(d.getTitle());
        assertEquals(0L, d.getVersion());
        assertTrue(d.getModificationTime().isAfter(d.getCreationTime()));
    }
    
}
