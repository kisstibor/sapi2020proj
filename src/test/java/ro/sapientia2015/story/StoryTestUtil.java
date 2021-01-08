package ro.sapientia2015.story;

import org.joda.time.DateTime;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryStatus;

public class StoryTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final StoryStatus STATUS = StoryStatus.TODO;
    public static final StoryStatus STATUS_UPDATED_1 = StoryStatus.INPROGRESS;
    public static final StoryStatus STATUS_UPDATED_2 = StoryStatus.UNDERTESTING;
    public static final StoryStatus STATUS_UPDATED_3 = StoryStatus.DONE;
    public static final DateTime DUEDATE = DateTime.now().plusDays(1);

    private static final String CHARACTER = "a";

    public static StoryDTO createFormObject(Long id, String description, String title, DateTime date, StoryStatus newStatus) {
        StoryDTO dto = new StoryDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        if(date == null) {
        	dto.setDueDate(null);
        }else {
        	dto.setDueDate(date.toDate());
        }
        
        dto.setStatus(newStatus);
        
        return dto;
    }

    public static Story createModel(Long id, String description, String title, DateTime date) {
        Story model = Story.getBuilder(title)
                .description(description)
                .dueDate(date)
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
