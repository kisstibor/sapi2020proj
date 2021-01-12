package ro.sapientia2015.scrum.controller;

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
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.scrum.ScrumTestUtil;
import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.scrum.service.ScrumService;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;

public class ScrumControllerTest {
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_MEMBERS = "members";
    private static final String FIELD_TITLE = "title";
    
    private ScrumController controller;
    
    private MessageSource messageSourceMock;
    
    private ScrumService serviceMock;
    
    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new ScrumController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(ScrumService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }
    
    @Test
    public void showAddScrumForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddTeamForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(ScrumController.VIEW_ADD, view);

        ScrumDTO formObject = (ScrumDTO) model.asMap().get(ScrumController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getMembers());
        assertNull(formObject.getTitle());
    }
    
    @Test
    public void add() {
    	ScrumDTO formObject = ScrumTestUtil.createFormObject(null, ScrumTestUtil.MEMBERS, ScrumTestUtil.TITLE);

    	Scrum model = ScrumTestUtil.createModel(ScrumTestUtil.ID, ScrumTestUtil.MEMBERS, ScrumTestUtil.TITLE);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrum/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(ScrumController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = ScrumTestUtil.createRedirectViewPath(ScrumController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(ScrumController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, ScrumController.FEEDBACK_MESSAGE_KEY_ADDED);
    }
    
    @Test
    public void addEmptyScrum() {
    	ScrumDTO formObject = ScrumTestUtil.createFormObject(null, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrum/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(ScrumController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }
    
    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String members = ScrumTestUtil.createStringWithLength(Story.MAX_LENGTH_DESCRIPTION + 1);
        String title = ScrumTestUtil.createStringWithLength(Story.MAX_LENGTH_TITLE + 1);

        ScrumDTO formObject = ScrumTestUtil.createFormObject(null, members, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrum/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(ScrumController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_MEMBERS, FIELD_TITLE);
    }
    
    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Scrum> models = new ArrayList<Scrum>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(ScrumController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(ScrumController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Scrum found = ScrumTestUtil.createModel(ScrumTestUtil.ID, ScrumTestUtil.MEMBERS, ScrumTestUtil.TITLE);
        when(serviceMock.findById(ScrumTestUtil.ID)).thenReturn(found);

        String view = controller.findTeamById(ScrumTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(ScrumController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(ScrumController.MODEL_ATTRIBUTE));
    }
    
    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(ScrumTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findTeamById(ScrumTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
    
    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
    
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }
    
    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, ScrumController.FLASH_MESSAGE_KEY_FEEDBACK);
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
    
    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }
}
