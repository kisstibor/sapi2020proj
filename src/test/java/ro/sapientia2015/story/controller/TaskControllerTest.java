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

import ro.sapientia2015.story.TaskTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.TaskController;
import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Task;
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
public class TaskControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_PRIORITY = "priority";

    private TaskController controller;

    private MessageSource messageSourceMock;

    private TaskService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new TaskController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(TaskService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddTaskForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(TaskController.VIEW_ADD, view);

        TaskDTO formObject = (TaskDTO) model.asMap().get(TaskController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
        assertNull(formObject.getPriority());
    }

    @Test
    public void add() {
        TaskDTO formObject = TaskTestUtil.createFormObject(null, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);

        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(TaskController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(TaskController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, TaskController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyTask() {
        TaskDTO formObject = TaskTestUtil.createFormObject(null, "", "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(TaskController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_DESCRIPTION + 1);
        String title = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);
        String priority = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);

        TaskDTO formObject = TaskTestUtil.createFormObject(null, description, title, priority);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(TaskController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(serviceMock.deleteById(TaskTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(TaskController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(TaskTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, TaskController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(TaskTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(TaskTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Task> models = new ArrayList<Task>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(TaskController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(TaskController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Task found = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(serviceMock.findById(TaskTestUtil.ID)).thenReturn(found);

        String view = controller.findById(TaskTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(TaskController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(TaskController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(TaskTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(TaskTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateTaskForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Task updated = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(serviceMock.findById(TaskTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(TaskTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(TaskController.VIEW_UPDATE, view);

        TaskDTO formObject = (TaskDTO) model.asMap().get(TaskController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateTaskFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(TaskTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(TaskTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(TaskTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        TaskDTO formObject = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION_UPDATED, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.PRIORITY_UPDATED);

        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION_UPDATED, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.PRIORITY_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(TaskController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(TaskController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, TaskController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        TaskDTO formObject = TaskTestUtil.createFormObject(TaskTestUtil.ID, "", "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(TaskController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String description = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_DESCRIPTION + 1);
        String title = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);
        String priority = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);

        TaskDTO formObject = TaskTestUtil.createFormObject(TaskTestUtil.ID, description, title, priority);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(TaskController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        TaskDTO formObject = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION_UPDATED, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.PRIORITY_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/Task/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, TaskController.FLASH_MESSAGE_KEY_FEEDBACK);
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