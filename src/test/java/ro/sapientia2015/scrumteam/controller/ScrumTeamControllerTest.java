package ro.sapientia2015.scrumteam.controller;

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

import ro.sapientia2015.scrumteam.ScrumTeamTestUtil;
import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.scrumteam.service.ScrumTeamService;
import ro.sapientia2015.story.exception.NotFoundException;

public class ScrumTeamControllerTest {
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_MEMBERS = "members";
    private static final String FIELD_NAME = "title";

    private ScrumTeamController controller;

    private MessageSource messageSourceMock;

    private ScrumTeamService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new ScrumTeamController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(ScrumTeamService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddStoryForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddTeamForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(ScrumTeamController.VIEW_ADD, view);

        ScrumTeamDTO formObject = (ScrumTeamDTO) model.asMap().get(ScrumTeamController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getMembers());
        assertNull(formObject.getName());
    }

    @Test
    public void add() {
        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(null, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);

        ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(ScrumTeamController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(ScrumTeamController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, ScrumTeamController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyStory() {
        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(null, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(ScrumTeamController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String members = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_MEMBERS + 1);
        String name = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_NAME + 1);

        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(null, name, members);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(ScrumTeamController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_MEMBERS, FIELD_NAME);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(serviceMock.deleteById(ScrumTeamTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(ScrumTeamController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(ScrumTeamTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, ScrumTeamController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(ScrumTeamTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(ScrumTeamTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<ScrumTeam> models = new ArrayList<ScrumTeam>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(ScrumTeamController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(ScrumTeamController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        ScrumTeam found = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(serviceMock.findById(ScrumTeamTestUtil.ID)).thenReturn(found);

        String view = controller.findTeamById(ScrumTeamTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(ScrumTeamController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(ScrumTeamController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(ScrumTeamTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findTeamById(ScrumTeamTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateStoryForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        ScrumTeam updated = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(serviceMock.findById(ScrumTeamTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(ScrumTeamTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(ScrumTeamController.VIEW_UPDATE, view);

        ScrumTeamDTO formObject = (ScrumTeamDTO) model.asMap().get(ScrumTeamController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getMembers(), formObject.getMembers());
        assertEquals(updated.getName(), formObject.getName());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateStoryFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(ScrumTeamTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(ScrumTeamTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME_UPDATED, ScrumTeamTestUtil.UPDATED_MEMBERS);

        ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME_UPDATED, ScrumTeamTestUtil.UPDATED_MEMBERS);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(ScrumTeamController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(ScrumTeamController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, ScrumTeamController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }
    /*
    @Test
    public void updateEmpty() throws NotFoundException {
        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(ScrumTeamController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String members = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_MEMBERS + 1);
        String name = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_NAME + 1);

        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, name, members);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(ScrumTeamController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_MEMBERS, FIELD_NAME);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        ScrumTeamDTO formObject = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME_UPDATED, ScrumTeamTestUtil.UPDATED_MEMBERS);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/scrumteam/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
	*/

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, ScrumTeamController.FLASH_MESSAGE_KEY_FEEDBACK);
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









