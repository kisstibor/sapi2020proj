package ro.sapientia2015.story.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import ro.sapientia2015.story.BugTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.service.BugService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class BugControllerTest {
	
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_STATUS = "status";
	
	private BugController controller;
	
	private MessageSource messageSourceMock;
	
	private BugService serviceMock;
	
	@Resource
	private Validator validator;
	
	@Before
	public void setUp() {
		controller = new BugController();
		
		messageSourceMock = mock(MessageSource.class);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);
		
		serviceMock = mock(BugService.class);
		ReflectionTestUtils.setField(controller, "service", serviceMock);
	}
	
	@Test
    public void showAddBugForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(BugController.VIEW_ADD, view);

        BugDTO formObject = (BugDTO) model.asMap().get(BugController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getTitle());
        assertNull(formObject.getDescription());
        assertNull(formObject.getStatus());
        
    }

    @Test
    public void add() {
        BugDTO formObject = BugTestUtil.createFormObject(null, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);

        Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(BugController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(BugController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, BugController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyBug() {
        BugDTO formObject = BugTestUtil.createFormObject(null, "", "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(BugController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
    	String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String status = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_STATUS + 1);

        BugDTO formObject = BugTestUtil.createFormObject(null, title, description, status);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(BugController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE, FIELD_DESCRIPTION, FIELD_STATUS);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
        when(serviceMock.deleteById(BugTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(BugController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(BugTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, BugController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(BugTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(BugTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Bug> models = new ArrayList<Bug>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(BugController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Bug found = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
        when(serviceMock.findById(BugTestUtil.ID)).thenReturn(found);

        String view = controller.findById(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(BugController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(BugTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateBugForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Bug updated = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
        when(serviceMock.findById(BugTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.VIEW_UPDATE, view);

        BugDTO formObject = (BugDTO) model.asMap().get(BugController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getTitle(), formObject.getTitle());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getStatus(), formObject.getStatus());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateBugFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(BugTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.TITLE_UPDATED, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.STATUS_UPDATED);

        Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE_UPDATED, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.STATUS_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(BugController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(BugController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, BugController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, "", "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(BugController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
    	String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String status = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_STATUS + 1);

        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID,  title, description, status);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(BugController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE, FIELD_DESCRIPTION, FIELD_STATUS);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.TITLE_UPDATED, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.STATUS_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, BugController.FLASH_MESSAGE_KEY_FEEDBACK);
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
