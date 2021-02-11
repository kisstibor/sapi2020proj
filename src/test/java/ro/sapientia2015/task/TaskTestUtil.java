package ro.sapientia2015.task;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.task.dto.TaskDTO;
import ro.sapientia2015.task.model.Task;

public class TaskTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final Story story = Story.getBuilder(TITLE).description(DESCRIPTION).build();
    public static final Story story_updated = Story.getBuilder(TITLE_UPDATED).description(DESCRIPTION_UPDATED).build();

    private static final String CHARACTER = "a";

    public static TaskDTO createFormObject(Long id, String title, Story story) {
        TaskDTO dto = new TaskDTO();

        dto.setId(id);
        dto.setStory(story);
        dto.setTitle(title);

        return dto;
    }

    public static Task createModel(Long id, String title, Story story) {
    	Task model = Task.getBuilder(title)
                .story(story)
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
