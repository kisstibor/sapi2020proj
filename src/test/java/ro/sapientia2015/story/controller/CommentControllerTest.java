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
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.service.CommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class CommentControllerTest {
    private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private static final String FIELD_MESSAGE = "message";

    private CommentController controller;

    private CommentService serviceMock;

    private MessageSource messageSourceMock;

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
    public void showAddStoryForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(CommentTestUtil.STORY_ID, model);

        assertEquals(CommentController.VIEW_ADD, view);
        assertEquals((Long) model.asMap().get(CommentController.STORY_ID), CommentTestUtil.STORY_ID);

        CommentDTO formObject = (CommentDTO) model.asMap().get(CommentController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getMessage());
        assertEquals(CommentTestUtil.STORY_ID, formObject.getStoryId());
    }

    @Test
    public void add() throws NotFoundException {
    	CommentDTO formObject = CommentTestUtil.createFormObject(null, CommentTestUtil.MESSAGE, CommentTestUtil.STORY_ID);

    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE, CommentTestUtil.STORY);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/"+CommentTestUtil.STORY_ID+"/message/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(CommentTestUtil.STORY_ID, formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = CommentTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_STORY_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(CommentController.STORY_ID)), model.getId());
    }

    @Test
    public void addEmptyStory() throws NotFoundException {
    	CommentDTO formObject = CommentTestUtil.createFormObject(null, "",  CommentTestUtil.STORY_ID);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/"+CommentTestUtil.STORY_ID+"/message/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(CommentTestUtil.STORY_ID, formObject, result, attributes);


        assertEquals(CommentController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_MESSAGE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() throws NotFoundException {
        String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_MESSAGE + 1);

        CommentDTO formObject = CommentTestUtil.createFormObject(null, message,  CommentTestUtil.STORY_ID);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/"+CommentTestUtil.STORY_ID+"/message/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(CommentTestUtil.STORY_ID, formObject, result, attributes);

        assertEquals(CommentController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_MESSAGE);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE, CommentTestUtil.STORY);
        when(serviceMock.deleteById(CommentTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(StoryController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(CommentTestUtil.STORY_ID, CommentTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(CommentTestUtil.ID);


        String expectedView = CommentTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_STORY_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(CommentController.STORY_ID)), model.getId());
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(CommentTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(CommentTestUtil.STORY_ID, CommentTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(CommentTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(Long.valueOf((String) attributes.get(CommentController.STORY_ID)), CommentTestUtil.STORY_ID);
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
