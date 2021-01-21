package ro.sapientia2015.story.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VacationTest {
	private static final Long VACATION_ID = 1L;
    private static final String VACATION_STARTDATE= "2020-10-01";
    private static final String VACATION_ENDDATE= "2020-12-01";

    @Test
    public void buildWithMandatoryInformation() {
        Vacation built = Vacation.getBuilder(VACATION_STARTDATE, VACATION_ENDDATE).build();

        assertNull(built.getId());
        assertEquals(VACATION_STARTDATE,built.getVacationStartDate());
        assertEquals(VACATION_ENDDATE,built.getVacationEndDate());
    }
    
    public void buildWithAllInformation() {
        Vacation built = Vacation.getBuilder(VACATION_STARTDATE, VACATION_ENDDATE).build();

        assertNull(built.getId());
        assertEquals(VACATION_STARTDATE,built.getVacationStartDate());
        assertEquals(VACATION_ENDDATE,built.getVacationEndDate());
    }
    
    @Test
    public void prePersist() {
        Vacation vacation = new Vacation();
        vacation.prePersist();

        assertNull(vacation.getId());
        assertNotNull(vacation.getVacationStartDate()); 
        assertNotNull(vacation.getVacationEndDate());

    }

    @Test
    public void preUpdate() {
    	 Vacation vacation = new Vacation();
         vacation.prePersist();

        pause(1000);

        vacation.preUpdate();
        
        assertNull(vacation.getId());
        assertNotNull(vacation.getVacationStartDate()); 
        assertNotNull(vacation.getVacationEndDate());

        }  
    
    private void pause(long timeInMillis) {
        try {
            Thread.currentThread();
			Thread.sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}