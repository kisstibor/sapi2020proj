package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.model.Log;



public class LogTestUtil {
	 public static final Long ID = 1L;
	    public static final String DESCRIPTION = "description";
	    public static final String DESCRIPTION_UPDATED = "updatedDescription";
	    public static final String TITLE = "title";
	    public static final String STATUS = "status";
	    public static final String ASSIGNTO = "assingto";
	    public static final String DOC = "doc";
	    public static final String TITLE_UPDATED = "updatedTitle";
	    public static final String STATUS_UPDATED = "status1";
	    public static final String ASSINGTO_UPDATED = "assingto";
	    public static final String DOC_UPDATED = "doc1";
	    

	    private static final String CHARACTER = "a";

	    public static LogDTO createFormObject(Long id, String description, String title,String assignTo,String status,String doc) {
	        LogDTO dto = new LogDTO();

	        dto.setId(id);
	        dto.setDescription(description);
	        dto.setTitle(title);
	        dto.setAssignTo(assignTo);
	        dto.setStatus(status);
	        dto.setDoc(doc);

	        return dto;
	    }

	    public static Log createModel(Long id, String description, String title,String assignTo,String status,String doc) {
	        Log model = Log.getBuilder(title)
	                .description(description)
	                .assignTo(assignTo, status)
	                .doc(doc)
	                .build();

	        ReflectionTestUtils.setField(model, "id", id);

	        return model;
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
	}
