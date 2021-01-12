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

import ro.sapientia2015.story.FixVersionTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.FixVersionService;
import ro.sapientia2015.story.service.StoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class FixVersionControllerTest {

	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_NAME = "name";
    
    private FixVersionController controller;

    private MessageSource messageSourceMock;
    
    private FixVersionService serviceMock;
    
    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new FixVersionController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(FixVersionService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }
    
    @Test
    public void showAddFixVersionForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(FixVersionController.VIEW_ADD, view);

        FixVersionDTO formObject = (FixVersionDTO) model.asMap().get(FixVersionController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getName());
    }
    
    @Test
    public void add() {
        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(null, FixVersionTestUtil.NAME);

        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(FixVersionController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(FixVersionController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, FixVersionController.FEEDBACK_MESSAGE_KEY_ADDED);
    }
    
    @Test
    public void addEmptyFixVersion() {
        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(null, "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(FixVersionController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_NAME);
    }
    
    @Test
    public void addWithTooLongName() {
        String name = FixVersionTestUtil.createStringWithLength(FixVersion.MAX_LENGTH_NAME + 1);

        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(null, name);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(FixVersionController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_NAME);
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID,  FixVersionTestUtil.NAME);
        when(serviceMock.deleteById(StoryTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(FixVersionController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(FixVersionTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, FixVersionController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }
    
    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(StoryTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(FixVersionTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
    
    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<FixVersion> models = new ArrayList<FixVersion>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(FixVersionController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(FixVersionController.MODEL_ATTRIBUTE_LIST));
    }
    
    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        FixVersion found = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(serviceMock.findById(FixVersionTestUtil.ID)).thenReturn(found);

        String view = controller.findById(FixVersionTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(FixVersionController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(FixVersionController.MODEL_ATTRIBUTE));
    }
    
    @Test(expected = NotFoundException.class)
    public void findByIdWhenNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(FixVersionTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(FixVersionTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
    
    @Test
    public void showUpdateFixVersionForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        FixVersion updated = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(serviceMock.findById(FixVersionTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(FixVersionTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(FixVersionController.VIEW_UPDATE, view);

        FixVersionDTO formObject = (FixVersionDTO) model.asMap().get(FixVersionController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getName(), formObject.getName());
    }
    
    @Test(expected = NotFoundException.class)
    public void showUpdateFixVersionFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(FixVersionTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(FixVersionTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
    
    @Test
    public void update() throws NotFoundException {
        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);

        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(FixVersionController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(FixVersionController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, FixVersionController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }
    
    @Test
    public void updateEmpty() throws NotFoundException {
        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(FixVersionController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_NAME);
    }
    
    @Test
    public void updateWhenTitleTooLong() throws NotFoundException {
        String name = FixVersionTestUtil.createStringWithLength(FixVersion.MAX_LENGTH_NAME + 1);

        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, name);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(FixVersionController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_NAME);
    }
    
    @Test(expected = NotFoundException.class)
    public void updateWhenNotFound() throws NotFoundException {
        FixVersionDTO formObject = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/fixVersion/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }
    
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }
    
    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, FixVersionController.FLASH_MESSAGE_KEY_FEEDBACK);
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
    
    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
    
    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }
}
