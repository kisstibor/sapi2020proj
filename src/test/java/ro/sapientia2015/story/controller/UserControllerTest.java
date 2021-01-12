package ro.sapientia2015.story.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.story.service.UserService;
import static org.mockito.Mockito.*;

import ro.sapientia2015.story.CommonTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class UserControllerTest extends ControllerTestBase {
	
	private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
	
	private UserController controller;
	
	private UserService serviceMock;

	@Before
    public void setUp() {
		super.setUp();
		
		controller = new UserController();
		
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
		assertNull(formObject.getUsername());
		assertNull(formObject.getPassword());		
	}
	
	@Test
	public void add() {
		UserDTO formObject = UserTestUtil.createFormObject(null, UserTestUtil.USERNAME, UserTestUtil.PASSWORD);
		
		User model = UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.USERNAME, UserTestUtil.PASSWORD);
		
		when(serviceMock.add(formObject)).thenReturn(model);
		
		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);
		
		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
		
		initMessageSourceForFeedbackMessage(UserController.FEEDBACK_MESSAGE_KEY_ADDED);
		
		String view = controller.add(formObject, result, attributes);
		
		verify(serviceMock, times(1)).add(formObject);
		verifyNoMoreInteractions(serviceMock);
		
		String expectedView = CommonTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertFeedbackMessage(attributes, UserController.FEEDBACK_MESSAGE_KEY_ADDED, UserController.FLASH_MESSAGE_KEY_FEEDBACK);
	}
	
	@Test
	public void addWithEmptyUsername() {
		UserDTO formObject = UserTestUtil.createFormObject(null, null, UserTestUtil.PASSWORD);
		
		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);
		
		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
		
		String view = controller.add(formObject, result, attributes);
		
		verifyNoMoreInteractions(serviceMock, messageSourceMock);
		
		assertEquals(UserController.VIEW_ADD, view);
		
		CommonTestUtil.assertFieldErrors(result, FIELD_USERNAME);
	}
	
	@Test
	public void addWithEmptyPassword() {
		UserDTO formObject = UserTestUtil.createFormObject(null, UserTestUtil.USERNAME, null);
		
		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);
		
		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
		
		String view = controller.add(formObject, result, attributes);
		
		verifyNoMoreInteractions(serviceMock, messageSourceMock);
		
		assertEquals(UserController.VIEW_ADD, view);
		
		CommonTestUtil.assertFieldErrors(result, FIELD_PASSWORD);
	}
	
	@Test
    public void addWithTooLongUsernameAndPassword() {
        String username = CommonTestUtil.createStringWithLength(User.MAX_LENGTH_USERNAME + 1);
        String password = CommonTestUtil.createStringWithLength(User.MAX_LENGTH_PASSWORD + 1);

        UserDTO formObject = UserTestUtil.createFormObject(null, username, password);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/user/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(UserController.VIEW_ADD, view);
        
        CommonTestUtil.assertFieldErrors(result, FIELD_USERNAME, FIELD_PASSWORD);
    }
}
