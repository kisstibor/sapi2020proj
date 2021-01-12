package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author sapi
 *
 */
public class GoalTest {
	private String GOAL = "goal";
	private String METHOD = "method";
	private String METRICS = "metrics";
	
    @Test
    public void buildWithMandatoryInformation() {
        Goal built = Goal.getBuilder(GOAL).build();

        assertNull(built.getId());
        assertNull(built.getMethod());
        assertNull(built.getMetrics());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertEquals(GOAL, built.getGoal());
        assertEquals(0, built.getVersion());
    }
    
    @Test
    public void buildWithAllInformation() {
        Goal built = Goal.getBuilder(GOAL)
                .setMethod(METHOD)
                .setMetrics(METRICS)
                .build();

        assertNull(built.getId());
        assertEquals(METHOD, built.getMethod());
        assertEquals(METRICS, built.getMetrics());
        assertEquals(0, built.getVersion());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
    }
    
    @Test
    public void prePersist() {
        Goal goal = new Goal();
        goal.prePersist();

        assertNull(goal.getId());
        assertNull(goal.getGoal());
        assertNull(goal.getMethod());
        assertNull(goal.getMetrics());
        assertNotNull(goal.getCreationTime());
        assertNotNull(goal.getModificationTime());
        assertEquals(0, goal.getVersion());
        assertEquals(goal.getCreationTime(), goal.getModificationTime());
    }
    
    @Test
    public void preUpdate() {
        Goal goal = new Goal();
        goal.prePersist();

        pause(1000);

        goal.preUpdate();

        assertNull(goal.getId());
        assertNull(goal.getGoal());
        assertNull(goal.getMethod());
        assertNull(goal.getMetrics());
        assertNotNull(goal.getCreationTime());
        assertNotNull(goal.getModificationTime());
        assertEquals(0, goal.getVersion());
        assertTrue(goal.getModificationTime().isAfter(goal.getCreationTime()));
    }
    
    private void pause(long timeInMillis) {
        try {
            Thread.currentThread();
			Thread.sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            System.out.println("Error: " + e.toString());
        }
    }

}
