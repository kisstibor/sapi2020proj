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

import ro.sapientia2015.story.PriorityTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.PriorityDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.service.PriorityService;

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

/**
 * @author Kapas Krisztina
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class PriorityControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_NAME = "name";

    private PriorityController controller;

    private MessageSource messageSourceMock;

    private PriorityService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new PriorityController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(PriorityService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddPriorityForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(PriorityController.VIEW_ADD, view);

        PriorityDTO formObject = (PriorityDTO) model.asMap().get(PriorityController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getName());
    }

    @Test
    public void add() {
		PriorityDTO formObject = PriorityTestUtil.createFormObject(null, PriorityTestUtil.NAME);

        Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(PriorityController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = PriorityTestUtil.createRedirectViewPath(PriorityController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(PriorityController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, PriorityController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyPriority() {
    	PriorityDTO formObject = PriorityTestUtil.createFormObject(null, "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(PriorityController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test
    public void addWithTooLongName() {
        
        String name = PriorityTestUtil.createStringWithLength(Priority.MAX_LENGTH_NAME + 1);

        PriorityDTO formObject = PriorityTestUtil.createFormObject(null, name);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(PriorityController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(serviceMock.deleteById(PriorityTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(PriorityController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(PriorityTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, PriorityController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = PriorityTestUtil.createRedirectViewPath(PriorityController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(PriorityTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(PriorityTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Priority> models = new ArrayList<Priority>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(PriorityController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(PriorityController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Priority found = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(serviceMock.findById(PriorityTestUtil.ID)).thenReturn(found);

        String view = controller.findById(PriorityTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(PriorityController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(PriorityController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(PriorityTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(PriorityTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdatePriorityForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Priority updated = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(serviceMock.findById(PriorityTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(PriorityTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(PriorityController.VIEW_UPDATE, view);

        PriorityDTO formObject = (PriorityDTO) model.asMap().get(PriorityController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getName(), formObject.getName());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdatePriorityFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(PriorityTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(PriorityTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(PriorityTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        PriorityDTO formObject = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, PriorityTestUtil.NAME_UPDATED);

        Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(PriorityController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = PriorityTestUtil.createRedirectViewPath(PriorityController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(PriorityController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, PriorityController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        PriorityDTO formObject = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(PriorityController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test
    public void updateWhenNameTooLong() throws NotFoundException {
        String name = PriorityTestUtil.createStringWithLength(Priority.MAX_LENGTH_NAME + 1);

        PriorityDTO formObject = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, name );

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(PriorityController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_NAME);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        PriorityDTO formObject = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, PriorityTestUtil.NAME_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/priority/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, PriorityController.FLASH_MESSAGE_KEY_FEEDBACK);
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
