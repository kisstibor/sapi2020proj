package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.model.Task;

public class TaskTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";
    public static final String PRIORITY = "priority";
    public static final String PRIORITY_UPDATED = "priorityTitle";

    private static final String CHARACTER = "a";

    public static TaskDTO createFormObject(Long id, String description, String title, String priority) {
        TaskDTO dto = new TaskDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);
        dto.setPriority(priority);

        return dto;
    }

    public static Task createModel(Long id, String description, String title, String priority) {
        Task model = Task.getBuilder(title)
                .description(description)
                .priority(priority)
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