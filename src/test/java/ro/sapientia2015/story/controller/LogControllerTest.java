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

import ro.sapientia2015.story.LogTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Log;
import ro.sapientia2015.story.service.LogService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class LogControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_ASSIGNTO = "title";
    private static final String FIELD_STATUS= "title";
    private static final String FIELD_DOC = "title";

    private LogController controller;

    private MessageSource messageSourceMock;

    private LogService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new LogController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(LogService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddLogForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(LogController.VIEW_ADD, view);

        LogDTO formObject = (LogDTO) model.asMap().get(LogController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
    }

    @Test
    public void add() {
    	LogDTO formObject = LogTestUtil.createFormObject(null, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);

    	Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE, LogTestUtil.ASSIGNTO, LogTestUtil.STATUS,LogTestUtil.DOC);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(LogController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(LogController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, LogController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyLog() {
    	LogDTO formObject = LogTestUtil.createFormObject(null, "", "","","","");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(LogController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE,FIELD_ASSIGNTO,FIELD_STATUS);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_DESCRIPTION + 1);
        String title = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        String assignto = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        String status = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        String doc = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);

        LogDTO formObject = LogTestUtil.createFormObject(null, description, title,assignto,status,doc);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(LogController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE,FIELD_ASSIGNTO,FIELD_STATUS);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);
        when(serviceMock.deleteById(LogTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(LogController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(LogTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, LogController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(LogTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(LogTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Log> models = new ArrayList<Log>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(LogController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(LogController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Log found = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);
        when(serviceMock.findById(LogTestUtil.ID)).thenReturn(found);

        String view = controller.findById(LogTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(LogController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(LogController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(LogTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(LogTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateLogForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Log updated = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);
        when(serviceMock.findById(LogTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(LogTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(LogController.VIEW_UPDATE, view);

        LogDTO formObject = (LogDTO) model.asMap().get(LogController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
        assertEquals(updated.getAssignTo(), formObject.getAssignTo());
        assertEquals(updated.getStatus(), formObject.getStatus());
        assertEquals(updated.getDoc(), formObject.getDoc());
        
        
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateLogFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(LogTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(LogTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(LogTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        LogDTO formObject = LogTestUtil.createFormObject(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);

        Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION_UPDATED, LogTestUtil.TITLE_UPDATED,LogTestUtil.ASSINGTO_UPDATED,LogTestUtil.STATUS_UPDATED,LogTestUtil.DOC_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(LogController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(LogController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, LogController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        LogDTO formObject = LogTestUtil.createFormObject(LogTestUtil.ID, "", "","","","");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(LogController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE,FIELD_ASSIGNTO,FIELD_STATUS);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
    	   String description = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_DESCRIPTION + 1);
           String title = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
           String assignto = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
           String status = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
           String doc = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        

        LogDTO formObject = LogTestUtil.createFormObject(LogTestUtil.ID, description, title,assignto,status,doc);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(LogController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE,FIELD_ASSIGNTO,FIELD_STATUS);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        LogDTO formObject = LogTestUtil.createFormObject(LogTestUtil.ID, LogTestUtil.DESCRIPTION_UPDATED, LogTestUtil.TITLE_UPDATED,LogTestUtil.ASSINGTO_UPDATED,LogTestUtil.STATUS_UPDATED,LogTestUtil.DOC_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/log/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, LogController.FLASH_MESSAGE_KEY_FEEDBACK);
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
