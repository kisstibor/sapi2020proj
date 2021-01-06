package ro.sapientia2015.story;

public class CommonTestUtil {

	public static void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
	
}
