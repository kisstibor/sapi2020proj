package ro.sapientia2015.story.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.WorkLogTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.WorkLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class WorkLogControllerTest {
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private WorkLogController controller;

    private MessageSource messageSourceMock;

    private WorkLogService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
    	StoryService storyService = Mockito.mock(StoryService.class);
    	//Mockito.spy(storyService);
        controller = new WorkLogController(storyService);

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(WorkLogService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }
    
    @Test
    public void showAddWorkLogForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(WorkLogController.VIEW_ADD, view);

        WorkLogDTO formObject = (WorkLogDTO) model.asMap().get(WorkLogController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getStory_id());
        assertNull(formObject.getStory_title());
        assertNull(formObject.getStarted_at());
        assertNull(formObject.getEnded_at());
        assertNull(formObject.getLogged_at());
        assertNull(formObject.getDescription());
        assertNull(formObject.getStarted_at_date());
        assertNull(formObject.getLogged_at_date());
    }
    
    @Test
    public void add() {
    	WorkLogDTO formObject = WorkLogTestUtil.createFormObject(WorkLogTestUtil.STORY_ID, 
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
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/worklog/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(WorkLogController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        //verify(serviceMock, times(1)).add(formObject);
        //verifyNoMoreInteractions(serviceMock);

        String expectedView = WorkLogTestUtil.createRedirectViewPath(WorkLogController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(WorkLogController.PARAMETER_ID)), model.getStory_id());

        //assertFeedbackMessage(attributes, WorkLogController.PARAMETER_ID);
    }
    
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }
    
    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
    
    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, WorkLogController.FLASH_MESSAGE_KEY_FEEDBACK);
    }
    
    private void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = attributes.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);

        assertNotNull(message);
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());

        verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSourceMock);
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        WorkLog model = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(serviceMock.deleteById(WorkLogTestUtil.STORY_ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(WorkLogTestUtil.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(WorkLogTestUtil.STORY_ID, attributes);

        verify(serviceMock, times(1)).deleteById(WorkLogTestUtil.STORY_ID);
        verifyNoMoreInteractions(serviceMock);

        //assertFeedbackMessage(attributes, WorkLogController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = WorkLogTestUtil.createRedirectViewPath(WorkLogTestUtil.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }
    
    @Test
    public void showUpdateWorklogForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        WorkLog updated = WorkLogTestUtil.createModel(WorkLogTestUtil.STORY_ID, 
    			WorkLogTestUtil.STORY_TITLE,
    			WorkLogTestUtil.LOGGED_AT,
    			WorkLogTestUtil.STARTED_AT,
    			WorkLogTestUtil.ENDED_AT,
    			WorkLogTestUtil.DESCRIPTION);
        when(serviceMock.findById(WorkLogTestUtil.STORY_ID)).thenReturn(updated);

        String view = controller.showUpdateForm(WorkLogTestUtil.STORY_ID, model);

        verify(serviceMock, times(1)).findById(WorkLogTestUtil.STORY_ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(WorkLogTestUtil.VIEW_UPDATE, view);

        WorkLogDTO formObject = (WorkLogDTO) model.asMap().get(WorkLogTestUtil.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getStory_title(), formObject.getStory_title());
    }

}

