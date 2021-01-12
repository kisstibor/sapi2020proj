package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import ro.sapientia2015.story.TaskTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Task;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.TaskService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class BoardControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";

    private BoardController controller;

    private MessageSource messageSourceMock;

    private TaskService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new BoardController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(TaskService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showBoard() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showBoard(model);
        assertEquals(BoardController.VIEW_BOARD, view);
    }
    
    @Test
    public void showAddGETTest() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);
    }

    @Test
    public void showAddFormTest() {
        TaskDTO formObject = TaskTestUtil.createFormObject(null, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.DESCRIPTION);

        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.DESCRIPTION);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/board/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(BoardController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = TaskTestUtil.createRedirectViewPath("/board");
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(BoardController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, BoardController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void add() {
        TaskDTO formObject = TaskTestUtil.createFormObject(null, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.DESCRIPTION);

        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.DESCRIPTION);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/board/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = TaskTestUtil.createRedirectViewPath("/board");
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(StoryController.PARAMETER_ID)), model.getId());

    }

    	
    @Test
    public void constructFormObjectForUpdateFormTest()
    {
    	Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.STATE);
    	
    	TaskDTO formObject  = controller.constructFormObjectForUpdateForm(model);
    	assertEquals(model.getDescription(), formObject.getDescription());
    	assertEquals(model.getState(), formObject.getState());
    	assertEquals(model.getTitle(), formObject.getTitle());
    	
    }

//    @Test
//    public void deleteById() throws NotFoundException {
//        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
//
//        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.STATE);
//        when(serviceMock.deleteById(TaskTestUtil.ID)).thenReturn(model);
//
//        initMessageSourceForFeedbackMessage(BoardController.FEEDBACK_MESSAGE_KEY_DELETED);
//
//        String view = controller.deleteTask(TaskTestUtil.ID, attributes);
//
//        verify(serviceMock, times(1)).deleteById(TaskTestUtil.ID);
//        verifyNoMoreInteractions(serviceMock);
//        assertFeedbackMessage(attributes, BoardController.FEEDBACK_MESSAGE_KEY_DELETED);
//
//        String expectedView = TaskTestUtil.createRedirectViewPath("/board");
//        assertEquals(expectedView, view);
//    }

//    @Test(expected = NotFoundException.class)
//    public void deleteByIdWhenIsNotFound() throws NotFoundException {
//        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
//
//        when(serviceMock.deleteById(TaskTestUtil.ID)).thenThrow(new NotFoundException(""));
//
//        controller.deleteTask(TaskTestUtil.ID, attributes);
//
//        verify(serviceMock, times(1)).deleteById(TaskTestUtil.ID);
//    }

   

    @Test(expected = NotFoundException.class)
    public void showUpdateStoryFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(StoryTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

   

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, StoryController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
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
}
