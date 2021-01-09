package ro.sapientia2015.story.controller;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import ro.sapientia2015.story.CommentTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.service.CommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class CommentControllerTest {
	

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_MESSAGE = "message";

    private CommentController controller;

    private MessageSource messageSourceMock;

    private CommentService serviceMock;
    
    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new CommentController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(CommentService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddCommentForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(StoryTestUtil.ID, model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(CommentController.VIEW_ADD, view);

       CommentDTO formObject = (CommentDTO) model.asMap().get(CommentController.MODEL_ATTRIBUTE);
        
        assertNull(formObject.getId());
        assertNull(formObject.getMessage());
        assertEquals(formObject.getStoryId(), StoryTestUtil.ID);
    }

    @Test
    public void add() {
    	CommentDTO formObject = CommentTestUtil.createFormObject(null, CommentTestUtil.MESSAGE);
    	formObject.setStoryId(StoryTestUtil.ID);

    	Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
    	model.setStory(modelStory);
    	modelStory.addComment(model);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(CommentController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = StoryTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(CommentController.PARAMETER_ID)), modelStory.getId());

        assertFeedbackMessage(attributes, CommentController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyComment() {
    	CommentDTO formObject = CommentTestUtil.createFormObject(null, "");
    	formObject.setStoryId(StoryTestUtil.ID);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(CommentController.VIEW_ADD+"/"+StoryTestUtil.ID+"", view);
        assertFieldErrors(result, "message");
    }

    @Test
    public void addWithTooLongMessage() {
        String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_DESCRIPTION + 1);

        CommentDTO formObject = CommentTestUtil.createFormObject(null, message);
        formObject.setStoryId(StoryTestUtil.ID);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(CommentController.VIEW_ADD+"/"+StoryTestUtil.ID+"", view);
        assertFieldErrors(result, "message");
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
    	model.setStory(modelStory);
    	
        when(serviceMock.deleteById(CommentTestUtil.ID)).thenReturn(model);
        

        initMessageSourceForFeedbackMessage(CommentController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(CommentTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(CommentTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, CommentController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = CommentTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(CommentTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(CommentTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }


    @Test
    public void showUpdateCommentForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        Comment updated = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
        updated.setStory(modelStory);
    	modelStory.addComment(updated);
        when(serviceMock.findById(CommentTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(CommentTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(CommentTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(CommentController.VIEW_UPDATE, view);

        CommentDTO formObject = (CommentDTO) model.asMap().get(CommentController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getMessage(), formObject.getMessage());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateCommentFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(CommentTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(CommentTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(CommentTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
    	CommentDTO formObject = CommentTestUtil.createFormObject(CommentTestUtil.ID, CommentTestUtil.MESSAGE_UPDATED);

    	Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE_UPDATED);
        model.setStory(modelStory);
    	modelStory.addComment(model);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(CommentController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = StoryTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(CommentController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, CommentController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
    	CommentDTO formObject = CommentTestUtil.createFormObject(CommentTestUtil.ID, "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(CommentController.VIEW_UPDATE, view);
        assertFieldErrors(result, "message");
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_DESCRIPTION + 1);

        CommentDTO formObject = CommentTestUtil.createFormObject(CommentTestUtil.ID, message);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(CommentController.VIEW_UPDATE, view);
        assertFieldErrors(result, "message");
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	CommentDTO formObject = CommentTestUtil.createFormObject(CommentTestUtil.ID, CommentTestUtil.MESSAGE_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/comment/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, CommentController.FLASH_MESSAGE_KEY_FEEDBACK);
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
