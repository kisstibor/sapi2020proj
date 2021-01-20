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

import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.UserController;
import ro.sapientia2015.story.controller.UserController;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.UserService;
import ro.sapientia2015.story.service.UserService;

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
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class UserControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";
    private static final Long FIELD_EXPERIENCE = 0L;

    private UserController controller;

    private MessageSource messageSourceMock;

    private UserService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new UserController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(UserService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddUserForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(UserController.VIEW_ADD, view);

        UserDTO formObject = (UserDTO) model.asMap().get(UserController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
        assertNull(formObject.getExperience());
    }

    @Test
    public void add() {
        UserDTO formObject = UserTestUtil.createFormObject(null, UserTestUtil.DESCRIPTION, UserTestUtil.TITLE, UserTestUtil.EXPERIENCE);

        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.DESCRIPTION, UserTestUtil.TITLE, UserTestUtil.EXPERIENCE);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(UserController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(UserController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, UserController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyUser() {
    	UserDTO formObject = UserTestUtil.createFormObject(null, "", "", null);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(UserController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = UserTestUtil.createStringWithLength(User.MAX_LENGTH_DESCRIPTION + 1);
        String title = UserTestUtil.createStringWithLength(User.MAX_LENGTH_TITLE + 1);

        UserDTO formObject = UserTestUtil.createFormObject(null, description, title, null);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(UserController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.DESCRIPTION, UserTestUtil.TITLE, UserTestUtil.EXPERIENCE);
        when(serviceMock.deleteById(UserTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(UserController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(UserTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, UserController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(UserTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(UserTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<User> models = new ArrayList<User>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(UserController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(UserController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        User found = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.DESCRIPTION, UserTestUtil.TITLE, UserTestUtil.EXPERIENCE);
        when(serviceMock.findById(UserTestUtil.ID)).thenReturn(found);

        String view = controller.findById(UserTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(UserController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(UserController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(UserTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(UserTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateUserForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        User updated = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.DESCRIPTION, UserTestUtil.TITLE, UserTestUtil.EXPERIENCE);
        when(serviceMock.findById(UserTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(UserTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(UserController.VIEW_UPDATE, view);

        UserDTO formObject = (UserDTO) model.asMap().get(UserController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
        assertEquals(updated.getExperience(), formObject.getExperience());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateUserFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(UserTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(UserTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(UserTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
    	UserDTO formObject = UserTestUtil.createFormObject(UserTestUtil.ID, UserTestUtil.DESCRIPTION_UPDATED, UserTestUtil.TITLE_UPDATED, UserTestUtil.EXPERIENCE_UPDATED);

        User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.DESCRIPTION_UPDATED, UserTestUtil.TITLE_UPDATED, UserTestUtil.EXPERIENCE_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(UserController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(UserController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, UserController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        UserDTO formObject = UserTestUtil.createFormObject(UserTestUtil.ID, "", "", null);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(UserController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String description = UserTestUtil.createStringWithLength(User.MAX_LENGTH_DESCRIPTION + 1);
        String title = UserTestUtil.createStringWithLength(User.MAX_LENGTH_TITLE + 1);

        UserDTO formObject = UserTestUtil.createFormObject(UserTestUtil.ID, description, title, UserTestUtil.EXPERIENCE);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(UserController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        UserDTO formObject = UserTestUtil.createFormObject(UserTestUtil.ID, UserTestUtil.DESCRIPTION_UPDATED, UserTestUtil.TITLE_UPDATED, UserTestUtil.EXPERIENCE_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, UserController.FLASH_MESSAGE_KEY_FEEDBACK);
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
