package ro.sapientia2015.story;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;

public class WorkLogTestUtil {
	public static final Long STORY_ID = Long.valueOf(1);
	public static final String STORY_TITLE = "Test Story Title";
	public static final String DESCRIPTION = "Worklog Description";
	public static final String LOGGED_AT = "2021-01-12";
	public static final String STARTED_AT = "10:25";
	public static final String STARTED_AT_POST = "10:30";
	public static final String ENDED_AT = "11:40";
	public static final String ENDED_AT_POST = "11:30";
	protected static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
	public static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.worklog.deleted";
	public static final String REQUEST_MAPPING_LIST = "/worklog/list/all";
	public static final String MODEL_ATTRIBUTE = "worklog";
	public static final String VIEW_UPDATE = "worklog/update";
	
	public static WorkLogDTO createFormObject(Long story_id, String story_title, String logged_at, String started_at, String ended_at, String description) {
		WorkLogDTO dto = new WorkLogDTO();

		dto.setStory_id(story_id);
		dto.setStory_title(story_title);
		dto.setLogged_at(logged_at);
		dto.setStarted_at(started_at);
		dto.setEnded_at(ended_at);
		dto.setDescription(description);

        return dto;
    }
	
	protected static LocalDate convertStringToLocalDate(String strDate) {
    	DateTimeFormatter data_formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
		return LocalDate.parse(strDate, data_formatter);
    }
    
    protected static LocalTime convertStringToLocalTime(String strTime) {
		DateTimeFormatter time_formatter = DateTimeFormatter.ISO_LOCAL_TIME;
		return LocalTime.parse(strTime, time_formatter);
    }
	
	public static WorkLog createModel(Long story_id, String story_title, String logged_at, String started_at, String ended_at, String description) {
		WorkLog model = WorkLog.getBuilder(story_id, story_title)
				.logged_at(convertStringToLocalDate(logged_at))
				.started_at(convertStringToLocalTime(started_at))
				.ended_at(convertStringToLocalTime(ended_at))
				.description(description)
				.build();

        ReflectionTestUtils.setField(model, "id", story_id);

        return model;
    }
	
	public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }
}
