package ro.sapientia2015.story;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.springframework.validation.BindingResult;

public class CommonTestUtil {
	
	private static final String CHARACTER = "a";

	public static void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
	
	public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }
	
	public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
	
	public static void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }

}
