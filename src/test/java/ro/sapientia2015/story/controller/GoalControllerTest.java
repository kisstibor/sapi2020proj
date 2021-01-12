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

import ro.sapientia2015.story.GoalTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Goal;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.GoalService;
import ro.sapientia2015.story.service.StoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class GoalControllerTest {
	
    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_GOAL = "goal";
    private static final String FIELD_METHOD = "method";
    private static final String FIELD_METRICS = "metrics";

    private GoalController controller;

    private MessageSource messageSourceMock;

    private GoalService serviceMock;
    
    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new GoalController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(GoalService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }
    
    @Test
    public void showAddStoryForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(GoalController.VIEW_ADD, view);

        GoalDTO formObject = (GoalDTO) model.asMap().get(GoalController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getGoal());
        assertNull(formObject.getMethod());
        assertNull(formObject.getMetrics());
    }
    
    @Test
    public void add() {
        GoalDTO formObject = GoalTestUtil.createFormObject(null, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);

        Goal model = GoalTestUtil.createModel(GoalTestUtil.ID, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/goal/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(GoalController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = GoalTestUtil.createRedirectViewPath(GoalController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Integer.valueOf((String) attributes.get(GoalController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, GoalController.FEEDBACK_MESSAGE_KEY_ADDED);
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
