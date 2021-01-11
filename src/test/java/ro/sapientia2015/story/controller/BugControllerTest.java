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

import ro.sapientia2015.story.BugTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.service.BugService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class BugControllerTest {
	
	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";

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
        ReflectionTestUtils.setField(controller, "bugService", serviceMock);
    }

    @Test
    public void showAddBugForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(BugController.PATH_ADD, view);

        BugDTO formObject = (BugDTO) model.asMap().get(BugController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
    }

    @Test
    public void add() {
        BugDTO formObject = BugTestUtil.createFormObject(null, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);

        Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(BugController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.addBug(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = BugTestUtil.createRedirectViewPath(BugController.REDIRECT_LIST);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(BugController.ID_ATTRIBUTE)), model.getId());

        assertFeedbackMessage(attributes, BugController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyBug() {
        BugDTO formObject = BugTestUtil.createFormObject(null, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.addBug(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(BugController.PATH_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);

        BugDTO formObject = BugTestUtil.createFormObject(null, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.addBug(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(BugController.PATH_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }
    
    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Bug> models = new ArrayList<Bug>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.listBugs(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.PATH_LIST, view);
        assertEquals(models, model.asMap().get(BugController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Bug found = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(serviceMock.findById(BugTestUtil.ID)).thenReturn(found);

        String view = controller.viewBug(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.PATH_VIEW, view);
        assertEquals(found, model.asMap().get(BugController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(BugTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.viewBug(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateBugForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Bug updated = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(serviceMock.findById(BugTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(BugTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(BugTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(BugController.PATH_UPDATE, view);

        BugDTO formObject = (BugDTO) model.asMap().get(BugController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
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
    	BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.TITLE_UPDATED);

    	Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.TITLE_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(BugController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = BugTestUtil.createRedirectViewPath(BugController.REDIRECT_BUG);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(BugController.ID_ATTRIBUTE)), model.getId());

        assertFeedbackMessage(attributes, BugController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(BugController.PATH_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);

        BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/bug/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(BugController.PATH_UPDATE, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	BugDTO formObject = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.TITLE_UPDATED);

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
