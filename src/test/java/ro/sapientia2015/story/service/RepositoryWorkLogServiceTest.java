package ro.sapientia2015.story.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.WorkLogTestUtil;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.repository.WorkLogRepository;

public class RepositoryWorkLogServiceTest {
	
	private RepositoryWorkLogService service;

    private WorkLogRepository repositoryMock;
    protected static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    @Before
    public void setUp() {
        service = new RepositoryWorkLogService();

        repositoryMock = mock(WorkLogRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }
    
    @Test
    public void add() {
    	WorkLogDTO dto = WorkLogTestUtil.createFormObject(WorkLogTestUtil.STORY_ID, 
						    			WorkLogTestUtil.STORY_TITLE,
						    			WorkLogTestUtil.LOGGED_AT,
						    			WorkLogTestUtil.STARTED_AT,
						    			WorkLogTestUtil.ENDED_AT,
						    			WorkLogTestUtil.DESCRIPTION);

        service.add(dto);

        ArgumentCaptor<WorkLog> workLogArgument = ArgumentCaptor.forClass(WorkLog.class);
        verify(repositoryMock, times(1)).save(workLogArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        WorkLog model = workLogArgument.getValue();
        
        assertNull(model.getId());
        assertNull(model.getCreatedAt());
        assertNull(model.getModifiedAt());
        assertNull(model.getLogged_at());
        assertNull(model.getStarted_at());
        assertNull(model.getEnded_at());
        assertNotNull(model.getDescription());
        assertNotNull(model.getStory_title());
        assertNotNull(model.getStory_id());
    }
    
    @Test
    public void deleteById() throws NotFoundException {
    	WorkLog model = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(repositoryMock.findOne(WorkLogTestUtil.STORY_ID)).thenReturn(model);

        WorkLog actual = service.deleteById(WorkLogTestUtil.STORY_ID);

        verify(repositoryMock, times(1)).findOne(WorkLogTestUtil.STORY_ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test
    public void findByLoggedDate() throws NotFoundException {
    	WorkLog model = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(model);
        List<WorkLog> workLogs = service.findByLoggedDate(WorkLogTestUtil.LOGGED_AT);

        assertNotNull(workLogs);
    }
    
    @Test
    public void checkIfLoggedEarlier() throws NotFoundException {
    	WorkLog model = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(repositoryMock.findOne(WorkLogTestUtil.STORY_ID)).thenReturn(model);
        boolean result = service.checkIfLoggedEarlier(WorkLogTestUtil.LOGGED_AT, 
        									convertStringToLocalTime(WorkLogTestUtil.STARTED_AT_POST),
        									convertStringToLocalTime(WorkLogTestUtil.ENDED_AT_POST));

        assertEquals(result, true);
    }
    
    @Test
    public void update() throws NotFoundException {
    	WorkLogDTO dto = WorkLogTestUtil.createFormObject(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
    	WorkLog model = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        WorkLog actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getStory_id(), actual.getStory_id());
        assertEquals(dto.getStory_title(), actual.getStory_title());
    }
    
    protected LocalDate convertStringToLocalDate(String strDate) {
    	DateTimeFormatter data_formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
		return LocalDate.parse(strDate, data_formatter);
    }
    
    protected LocalTime convertStringToLocalTime(String strTime) {
		DateTimeFormatter time_formatter = DateTimeFormatter.ISO_LOCAL_TIME;
		return LocalTime.parse(strTime, time_formatter);
    }
}
