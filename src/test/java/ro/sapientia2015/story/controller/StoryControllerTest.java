package ro.sapientia2015.story.controller;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import ro.sapientia2015.story.CommonTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryListDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.UserService;

/**
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class StoryControllerTest extends ControllerTestBase {

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";

    private StoryController controller;

    private StoryService serviceMock;

    @Before
    public void setUp() {
    	super.setUp();
    	
        controller = new StoryController();

        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(StoryService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddStoryForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();
        List<User> expectedUsers = new ArrayList<User>();

        UserService userServiceMock = mock(UserService.class);
        ReflectionTestUtils.setField(controller, "userService", userServiceMock);
        
        when(userServiceMock.findAll()).thenReturn(expectedUsers);
        String view = controller.showAddForm(model);

        verify(userServiceMock, times(1)).findAll();
        verifyZeroInteractions(messageSourceMock, serviceMock, userServiceMock);
        assertEquals(StoryController.VIEW_ADD, view);

        Map<String, Object> modelMap = model.asMap();
		StoryDTO formObject = (StoryDTO) modelMap.get(StoryController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
        assertEquals(expectedUsers, formObject.getUsers());
    }

    @Test
    public void add() {
        StoryDTO formObject = StoryTestUtil.createFormObject(null, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);

        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(StoryController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = CommonTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(StoryController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, StoryController.FEEDBACK_MESSAGE_KEY_ADDED, ControllerBase.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    @Test
    public void addEmptyStory() {
        StoryDTO formObject = StoryTestUtil.createFormObject(null, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(StoryController.VIEW_ADD, view);
        CommonTestUtil.assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = CommonTestUtil.createStringWithLength(Story.MAX_LENGTH_DESCRIPTION + 1);
        String title = CommonTestUtil.createStringWithLength(Story.MAX_LENGTH_TITLE + 1);

        StoryDTO formObject = StoryTestUtil.createFormObject(null, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(StoryController.VIEW_ADD, view);
        CommonTestUtil.assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);
        when(serviceMock.deleteById(StoryTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(StoryController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(StoryTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, StoryController.FEEDBACK_MESSAGE_KEY_DELETED, StoryController.FLASH_MESSAGE_KEY_FEEDBACK);

        String expectedView = CommonTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(StoryTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(StoryTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Story> models = new ArrayList<Story>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryController.VIEW_LIST, view);
        StoryListDTO dto = (StoryListDTO)model.asMap().get(StoryController.MODEL_ATTRIBUTE_STORY_LIST);
        assertEquals(models, dto.getStories());
    }
    
    @Test
    public void findByTitle() throws NotFoundException {
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
    	
    	List<Story> models = new ArrayList<Story>();
    	when(serviceMock.findByTitle(StoryTestUtil.QUERY_TEXT)).thenReturn(models);
    	
    	String view = controller.findByTitle(StoryTestUtil.QUERY_TEXT, model, attributes);
    	
    	verify(serviceMock, times(1)).findByTitle(StoryTestUtil.QUERY_TEXT);
    	verifyNoMoreInteractions(serviceMock);
    	verifyZeroInteractions(messageSourceMock);
    	
    	assertEquals(StoryController.VIEW_LIST, view);
    	StoryListDTO dto = (StoryListDTO)model.asMap().get(StoryController.MODEL_ATTRIBUTE_STORY_LIST);
    	assertEquals(models, dto.getStories());
    	assertEquals(StoryTestUtil.QUERY_TEXT, dto.getQuery());
    }
    
    @Test
    public void findByTitleWhenIsNotFound() throws NotFoundException {
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
    	
    	when(serviceMock.findByTitle(StoryTestUtil.QUERY_TEXT)).thenThrow(new NotFoundException(""));
    	initMessageSourceForFeedbackMessage(StoryController.FEEDBACK_MESSAGE_KEY_NO_RESULTS);
    	
    	String view = controller.findByTitle(StoryTestUtil.QUERY_TEXT, model, attributes);
    	
    	verify(serviceMock, times(1)).findByTitle(StoryTestUtil.QUERY_TEXT);
    	verifyNoMoreInteractions(serviceMock);
    	
    	assertFeedbackMessage(attributes, StoryController.FEEDBACK_MESSAGE_KEY_NO_RESULTS, StoryController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Story found = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);
        when(serviceMock.findById(StoryTestUtil.ID)).thenReturn(found);

        String view = controller.findById(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(StoryController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(StoryTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateStoryForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Story updated = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, UserTestUtil.createModel(UserTestUtil.ID, UserTestUtil.USERNAME, UserTestUtil.PASSWORD));
        when(serviceMock.findById(StoryTestUtil.ID)).thenReturn(updated);
        
        List<User> expectedUsersList = new ArrayList<User>();
        UserService userServiceMock = mock(UserService.class);
        ReflectionTestUtils.setField(controller, "userService", userServiceMock);
        when(userServiceMock.findAll()).thenReturn(expectedUsersList);

        String view = controller.showUpdateForm(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verify(userServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock, userServiceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryController.VIEW_UPDATE, view);

        StoryDTO formObject = (StoryDTO) model.asMap().get(StoryController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
        assertEquals(updated.getUser().getId(), formObject.getUserId());
        assertEquals(expectedUsersList, formObject.getUsers());
    }
    
    @Test
    public void showUpdateStoryFormWhenUserNull() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        Story updated = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, null);
        when(serviceMock.findById(StoryTestUtil.ID)).thenReturn(updated);
        
        List<User> expectedUsersList = new ArrayList<User>();
        UserService userServiceMock = mock(UserService.class);
        ReflectionTestUtils.setField(controller, "userService", userServiceMock);
        when(userServiceMock.findAll()).thenReturn(expectedUsersList);

        String view = controller.showUpdateForm(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verify(userServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock, userServiceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryController.VIEW_UPDATE, view);

        StoryDTO formObject = (StoryDTO) model.asMap().get(StoryController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
        assertEquals(null, formObject.getUserId());
        assertEquals(expectedUsersList, formObject.getUsers());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateStoryFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(StoryTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(StoryTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryDTO formObject = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED);

        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(StoryController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = CommonTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(StoryController.PARAMETER_ID)), model.getId());
        
        assertFeedbackMessage(attributes, StoryController.FEEDBACK_MESSAGE_KEY_UPDATED, StoryController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        StoryDTO formObject = StoryTestUtil.createFormObject(StoryTestUtil.ID, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(StoryController.VIEW_UPDATE, view);
        CommonTestUtil.assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String description = CommonTestUtil.createStringWithLength(Story.MAX_LENGTH_DESCRIPTION + 1);
        String title = CommonTestUtil.createStringWithLength(Story.MAX_LENGTH_TITLE + 1);

        StoryDTO formObject = StoryTestUtil.createFormObject(StoryTestUtil.ID, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(StoryController.VIEW_UPDATE, view);
        CommonTestUtil.assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryDTO formObject = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/story/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    
    
}
